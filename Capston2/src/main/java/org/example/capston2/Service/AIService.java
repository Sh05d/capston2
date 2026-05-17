package org.example.capston2.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.*;
import org.example.capston2.Repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AIService {
    private final WebClient webClient = WebClient.create();

    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;
    private final ToolKitRepository toolKitRepository;
    private final StudioRepository studioRepository;
    private final ToolRentalRepository toolRentalRepository;
    private final WorkshopBookingRepository workshopBookingRepository;

    @Value("${openai.api.key}")
    private String apiKey;


    public Map<String, Object> recommendWorkshops(Integer userId){

        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new ApiException("User not found");
        }

        List<WorkshopBooking> bookings = workshopBookingRepository.findWorkshopBookingByUserId(userId);
        String history = "";
        for (WorkshopBooking booking : bookings) {
            Workshop workshop = workshopRepository.findWorkshopById(booking.getWorkshopId());
            if (workshop != null) {
                history += workshop.getCategory() + ", ";
            }
        }

        List<Workshop> workshops = workshopRepository.findWorkshopByStatus("SCHEDULE");
        String workshopData = "";
        for (Workshop w : workshops) {
            Studio studio = studioRepository.findStudioById(w.getStudioId());

            workshopData += String.format("""
                ID: %d
                Title: %s
                Category: %s
                Price: %.2f
                Studio Name: %s
                City: %s
                Rate: %.1f
                Reviews: %d

                """,
                    w.getId(),
                    w.getTitle(),
                    w.getCategory(),
                    w.getPrice(),
                    studio != null ? studio.getName() : "Unknown",
                    studio != null ? studio.getCity() : "Unknown",
                    studio != null ? studio.getRate() : 0.0,
                    studio != null ? studio.getNumberOfRating() : 0
            );
        }
        try {

            String prompt = """
                    You are a smart workshop recommendation system.
                    
                    IMPORTANT DATA RULES:
                    - "rate" ALWAYS belongs to the STUDIO (not workshop)
                    - "price" belongs to the workshop only
                    - Do NOT mix studio rate with workshop price
                    
                    User City:
                    %s
                    
                    User Booking History:
                    %s
                    
                    RULES:
                    - Recommend ONLY from given workshops
                    - Prefer same city
                    - Prefer similar categories to history
                    - Prefer higher rated studios
                    - Do NOT invent data
                    - Return ONLY valid JSON. No markdown. No backticks.
                    
                    FORMAT:
                    {
                      "workshops": [
                        {
                          "id": number,
                          "title": string,
                          "category": string,
                          "studioName": string,
                          "city": string,
                          "studioRate": number,
                          "price": number,
                          "reason": string
                        }
                      ]
                    }
                    - Sort by best match first
                    - Output must be valid JSON only
                   
                    WORKSHOPS:
                    %s
                    """.formatted(
                    oldUser.getCity(),
                    history,
                    workshopData
            );

            String aiResponse = askAI(prompt);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiResponse, Map.class);
        } catch (Exception e) {
            throw new ApiException("Error: " + e.toString());
        }
    }

    public Map<String, Object> recommendToolkits(Integer userId) {

        User oldUser = userRepository.findUserById(userId);
        if (oldUser == null) {
            throw new ApiException("User not found");
        }

        List<ToolRental> rentals = toolRentalRepository.findToolRentalByUserId(userId);
        String history ="";
        for (ToolRental rental : rentals) {
            ToolKit toolKit = toolKitRepository.findToolKitsById(rental.getToolKitId());
            history += toolKit.getCategory()+ ", ";
        }

        List<ToolKit> toolkits = toolKitRepository.findAll();
        String toolkitData = "";
        for (ToolKit t : toolkits) {
            Studio studio = studioRepository.findStudioById(t.getStudioId());

            toolkitData += String.format("""
                    ID: %d
                    Name: %s
                    Category: %s
                    Price: %.2f
                    Studio Name: %s
                    City: %s
                    Rate: %.1f
                    Reviews: %d

                """,
                    t.getId(),
                    t.getName(),
                    t.getCategory(),
                    t.getPricePerDay(),
                    studio != null ? studio.getName() : "Unknown",
                    studio != null ? studio.getCity() : "Unknown",
                    studio != null ? studio.getRate() : 0.0,
                    studio != null ? studio.getNumberOfRating() : 0
            );
        }
        try {
            String prompt = """
            You are a smart toolkit recommendation system.
            IMPORTANT DATA RULES:
            - "rate" ALWAYS belongs to the STUDIO only (NOT toolkit)
            - "pricePerDay" belongs to the toolkit rental price
            - Do NOT mix studio rating with toolkit price
                    
            User City:
            %s
            
            User Rental History:
            %s
            
            RULES:
            - Recommend ONLY from given toolkits
            - Prefer same city
            - Prefer similar categories to history
            - Prefer higher rated studios
            - Do NOT invent any data
            - Return ONLY valid JSON. No markdown. No backticks.
            
            FORMAT:
            {
              "toolkits": [
                {
                  "id": number,
                  "name": string,
                  "category": string,
                  "studioName": string,
                  "city": string,
                  "studioRate": number,
                  "pricePerDay": number,
                  "reason": string
                }
              ]
            }
            - Sort results from best match to lowest
            - Output must be valid JSON only
            TOOLKITS:
            %s
            """.formatted(
                    oldUser.getCity(),
                    history,
                    toolkitData
            );

            String aiResponse = askAI(prompt);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiResponse, Map.class);
        }
        catch (Exception e) {
            throw new ApiException("Failed to generate toolkit recommendations");
        }
    }

    private String askAI(String prompt) {

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String response = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(res -> res.bodyToMono(String.class))
                .block();

        System.out.println(response);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);

            if (node.has("error")) {
                throw new ApiException(
                        node.get("error").get("message").asText()
                );
            }

            return node.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {
            throw new ApiException("Invalid AI response: " + e.getMessage());
        }
    }
}
