package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.User;
import org.example.capston2.Model.Workshop;
import org.example.capston2.Model.WorkshopBooking;
import org.example.capston2.Repository.UserRepository;
import org.example.capston2.Repository.WorkshopBookingRepository;
import org.example.capston2.Repository.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopBookingService {
    private final WorkshopBookingRepository workshopBookingRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;
    private final WhatsappService whatsappService;
    private final EmailService emailService;

    public List<WorkshopBooking> getWorkshopBookings(){
        return workshopBookingRepository.findAll();
    }

    public void BookWorkshop(WorkshopBooking workshopBooking){
        User user = userRepository.findUserById(workshopBooking.getUserId());
        if(user == null){
            throw new ApiException("User not found");
        }
        Workshop workshop = workshopRepository.findWorkshopById(workshopBooking.getWorkshopId());
        if(workshop == null){
            throw new ApiException("Workshop not found");
        }

        // Ensure workshop is available for booking
        if(!workshop.getStatus().equalsIgnoreCase("Schedule")){
            throw new ApiException("Can't book a completed or canceled workshop");
        }

        // Check audience restriction (male,female or mix)
        boolean isMixed = workshop.getAudienceGender().equalsIgnoreCase("mix");
        if(!isMixed && !workshop.getAudienceGender().equalsIgnoreCase(user.getGender())){
            throw new ApiException("User gender is not allowed for this workshop");
        }

        // Check seat availability
        Integer numberOfSeat = workshopBooking.getNumberOfSeats();
        if(workshop.getCapacity()< numberOfSeat){
            throw new ApiException("Number of seat not available");
        }

        Double totalPrice = workshop.getPrice()*numberOfSeat;
        // Check user balance
        if(user.getBalance()< totalPrice){
            throw new ApiException("Your balance lower than total price");
        }

        // Deduct balance from user
        user.setBalance(user.getBalance()-totalPrice);

        // Reduce workshop capacity
        workshop.setCapacity(workshop.getCapacity()-numberOfSeat);

        userRepository.save(user);
        workshopRepository.save(workshop);

        // Set book total price and status to confirm
        workshopBooking.setStatus("Confirm");
        workshopBooking.setTotalPrice(totalPrice);
        workshopBookingRepository.save(workshopBooking);
        // Send WhatsApp notification
        whatsappService.sendBookingConfirmationMessage(user.getPhoneNumber(), workshop.getTitle(),workshopBooking.getTotalPrice());
    }

    public void updateWorkshopBooking(Integer id, WorkshopBooking workshopBooking){
        WorkshopBooking oldWorkshopBooking = workshopBookingRepository.findWorkshopBookingById(id);
        if(oldWorkshopBooking == null){
            throw new ApiException("Booking not found");
        }
        // Ensure booking status not cancel
        if(oldWorkshopBooking.getStatus().equalsIgnoreCase("Cancel")){
            throw new ApiException("This booking already canceled");
        }
        // Ensure workshop is Schedule not Completed or Canceled
        Workshop workshop = workshopRepository.getActiveWorkshopById(oldWorkshopBooking.getWorkshopId());
        if(workshop == null){
            throw new ApiException("Can't modify booking for a completed or canceled workshop");
        }
        User user = userRepository.findUserById(oldWorkshopBooking.getUserId());

        if (user == null) {
            throw new ApiException("User not found");
        }

        // To know if number of booking seats increase or decrease
        Integer oldSeats = oldWorkshopBooking.getNumberOfSeats();
        Integer newSeats = workshopBooking.getNumberOfSeats();

        // Prevent removing all seats
        if (newSeats == 0) {
            throw new ApiException("At least one seat must remain");
        }

        Integer difference = newSeats - oldSeats;

        if (difference > 0) {

            // Check capacity
            if (workshop.getCapacity() < difference) {
                throw new ApiException("Not enough available seats");
            }
            Double extraPrice = workshop.getPrice() * difference;

            // Check balance
            if (user.getBalance() < extraPrice) {
                throw new ApiException("Insufficient balance");
            }
            // Deduct user balance
            user.setBalance(user.getBalance() - extraPrice);

            // Reduce workshop capacity
            workshop.setCapacity(workshop.getCapacity() - difference);

            // Update total price
            oldWorkshopBooking.setTotalPrice(oldWorkshopBooking.getTotalPrice() + extraPrice);
        }
        else if (difference < 0) {

            Integer canceledSeats = Math.abs(difference);
            Double refund = workshop.getPrice() * canceledSeats;

            // Refund user
            user.setBalance(user.getBalance() + refund);

            // Return seats to workshop
            workshop.setCapacity(workshop.getCapacity() + canceledSeats);

            // Update total price
            oldWorkshopBooking.setTotalPrice(oldWorkshopBooking.getTotalPrice() - refund);
        }
        oldWorkshopBooking.setNumberOfSeats(newSeats);

        userRepository.save(user);
        workshopRepository.save(workshop);
        workshopBookingRepository.save(oldWorkshopBooking);
        // Send message
        emailService.sendBookingUpdateEmail(user.getEmail(), user.getUsername(), workshop.getTitle());
    }

    public void deleteWorkshopBooking(Integer id){
        WorkshopBooking oldWorkshopBooking = workshopBookingRepository.findWorkshopBookingById(id);
        if(oldWorkshopBooking == null){
            throw new ApiException("Booking not found");
        }
        workshopBookingRepository.delete(oldWorkshopBooking);
    }

    public void cancelBooking(Integer id){
        WorkshopBooking booking = workshopBookingRepository.findWorkshopBookingById(id);
        if(booking == null){
            throw new ApiException("Booking not found");
        }
        if(booking.getStatus().equalsIgnoreCase("Cancel")){
            throw new ApiException("This booking already canceled");
        }
        // Ensure workshop is Schedule not Completed or Canceled
        // If completed can't cancel and take refund
        // If canceled already receive the refund
        Workshop workshop = workshopRepository.getActiveWorkshopById(booking.getWorkshopId());
        if(workshop == null){
            throw new ApiException("Can't cancel booking for a completed or canceled workshop");
        }

        // Get user to refund to balance
        User user = userRepository.findUserById(booking.getUserId());
        if(user == null){
            throw new ApiException("User not found");
        }

        // Restore workshop capacity
        workshop.setCapacity(workshop.getCapacity()+booking.getNumberOfSeats());
        // Refund user balance
        user.setBalance(user.getBalance()+booking.getTotalPrice());

        workshopRepository.save(workshop);
        userRepository.save(user);

        booking.setStatus("Cancel");
        workshopBookingRepository.save(booking);
        // Send to user
        whatsappService.sendCancelBookingMessage(user.getPhoneNumber(), workshop.getTitle(), booking.getTotalPrice());
        emailService.sendBookingStatusChangeEmail(user.getEmail(), user.getUsername(), booking.getId(), booking.getStatus());
    }
}
