package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.*;
import org.example.capston2.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopService {
    private final WorkshopRepository workshopRepository;
    private final StudioRepository studioRepository;
    private final WorkshopBookingRepository workshopBookingRepository;
    private final UserRepository userRepository;
    private final StudioFollowRepository studioFollowRepository;
    private final WhatsappService whatsappService;
    private final EmailService emailService;


    public List<Workshop> getWorkshops(){
        return workshopRepository.findAll();
    }

    public void addWorkshop(Workshop workshop){
        Studio studio = studioRepository.findStudioById(workshop.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not found");
        }
        workshop.setStatus("Schedule");
        workshopRepository.save(workshop);
        // Send email about new workshop to studio followers
        List<StudioFollow> studioFollows = studioFollowRepository.studioFollowersWithNotification(studio.getId());
        for(StudioFollow follow: studioFollows){
            User user = userRepository.findUserById(follow.getUserId());
            emailService.sendNewWorkshopEmail(user.getEmail(), user.getUsername(),studio.getName(),workshop.getTitle());
        }
    }

    public void updateWorkshop(Integer id, Workshop workshop){
        Workshop oldWorkshop = workshopRepository.findWorkshopById(id);
        if(oldWorkshop == null){
            throw new ApiException("Workshop not found");
        }
        Studio studio = studioRepository.findStudioById(workshop.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not found");
        }
        oldWorkshop.setTitle(workshop.getTitle());
        oldWorkshop.setDescription(workshop.getDescription());
        oldWorkshop.setCategory(workshop.getCategory());
        oldWorkshop.setStartDate(workshop.getStartDate());
        oldWorkshop.setEndDate(workshop.getEndDate());
        oldWorkshop.setAudienceGender(workshop.getAudienceGender());
        oldWorkshop.setCapacity(workshop.getCapacity());
        oldWorkshop.setPrice(workshop.getPrice());
        oldWorkshop.setStudioId(workshop.getStudioId());
        workshopRepository.save(oldWorkshop);
    }

    public void deleteWorkshop(Integer id){
        Workshop oldWorkshop = workshopRepository.findWorkshopById(id);
        if(oldWorkshop == null){
            throw new ApiException("Workshop not found");
        }
        List<WorkshopBooking> booking = workshopBookingRepository.findWorkshopBookingByWorkshopId(id);
        if(!booking.isEmpty()) {
            throw new ApiException("Can't delete workshop with existing bookings");
        }
        workshopRepository.delete(oldWorkshop);
    }

    public void cancelWorkshop(Integer id){
        Workshop workshop = workshopRepository.findWorkshopById(id);
        if(workshop == null){
            throw new ApiException("Workshop not found");
        }
        // Ensure workshop is not completed or canceled
        if(!workshop.getStatus().equalsIgnoreCase("schedule")){
            throw new ApiException("Can't cancel a completed or already canceled workshop");
        }
        // Get all workshop bookings
        List<WorkshopBooking> bookings = workshopBookingRepository.findWorkshopBookingByWorkshopId(id);
        // Change status of all bookings to cancel, refund total price and notify users through whatsapp
        if(!bookings.isEmpty()){
           for(WorkshopBooking booking: bookings){
               User user = userRepository.findUserById(booking.getUserId());
               if(user == null){
                   continue;
               }
               user.setBalance(user.getBalance() + booking.getTotalPrice());
               userRepository.save(user);
               // Send WhatsApp message to user that work shop canceled
               whatsappService.sendWorkshopCancellationMessage(user.getPhoneNumber(),booking.getTotalPrice(), workshop.getTitle());
               booking.setStatus("Cancel");
               workshopBookingRepository.save(booking);
           }
        }
        // Update workshop status
        workshop.setStatus("Cancel");
        workshopRepository.save(workshop);
    }

    public void completeWorkshop(Integer id){
        Workshop workshop = workshopRepository.findWorkshopById(id);
        if(workshop == null){
            throw new ApiException("Workshop not found");
        }
        // Ensure not to complete canceled workshop
        if(workshop.getStatus().equalsIgnoreCase("cancel")){
            throw new ApiException("can't complete cancelled workshop");
        }
        // Ensure workshop has ended
        if(workshop.getEndDate().isAfter(LocalDateTime.now())){
            throw new ApiException("can't complete workshop that has not ended yet");
        }
        workshop.setStatus("Complete");
        workshopRepository.save(workshop);
    }

    public List<Workshop> upcomingWorkshopForStudio(Integer studioId){
        return workshopRepository.upcomingWorkshopForStudio(studioId);
    }

    public List<Workshop> workshopByCategoryAndPriceRange(String category, Integer maxPrice){
        return workshopRepository.availableWorkshopByCategoryAndPriceRange(category, maxPrice);
    }

    public List<Workshop> workshopByCategoryAndAudience(String category, String audienceGender){
        return workshopRepository.availableWorkshopByCategoryAndAudience(category, audienceGender);
    }

    public List<Workshop> workshopByCategoryAndDate(String category, LocalDate date){
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return workshopRepository.availableWorkshopByCategoryAndDate(category, start, end);
    }

}
