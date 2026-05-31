package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.ToolRental;
import org.example.capston2.Model.ToolKit;
import org.example.capston2.Model.User;
import org.example.capston2.Repository.ToolRentalRepository;
import org.example.capston2.Repository.ToolKitRepository;
import org.example.capston2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolRentalService {
    private final ToolRentalRepository toolRentalRepository;
    private final ToolKitRepository toolKitRepository;
    private final UserRepository userRepository;
    private final WhatsappService whatsappService;
    private final EmailService emailService;

    public List<ToolRental> getRentToolKits(){
        return toolRentalRepository.findAll();
    }

    //Add
    public void rentToolkit(Integer userId, Integer toolKitId, ToolRental toolRental){
        LocalDate rentStart = toolRental.getStartDate();
        Integer days = toolRental.getRentDays();
        LocalDate rentEnd = rentStart.plusDays(days);
        Integer rentQuantity = toolRental.getQuantity();
        User user = userRepository.findUserById(userId);
        if(user == null){
            throw new ApiException("User not exist");
        }
        ToolKit toolKit = toolKitRepository.findToolKitsById(toolKitId);
        if(toolKit == null){
            throw new ApiException("Toolkit not exist");
        }
        Integer toolkitQuantity = toolKit.getQuantity();
        if(toolkitQuantity < rentQuantity){
            throw new ApiException("Number you entered large than the quantity");
        }
        // Check overlapping rentals for same toolkit and date range
        List<ToolRental> toolRentalList = toolRentalRepository.toolRentsAtDate(toolKit.getId(), rentStart,rentEnd);
        Integer rentedQuantity = 0;
        for(ToolRental rent : toolRentalList){
            rentedQuantity += rent.getQuantity();
        }

        // Calculate available quantity to rent in the date
        Integer availableQuantity = toolkitQuantity - rentedQuantity;
        if(availableQuantity < rentQuantity){
            throw new ApiException("This quantity not available at this date");
        }

        Double rentalPrice = toolKit.getPricePerDay()*days*rentQuantity;
        Double depositAmount = toolKit.getSecurityDepositPerItem() * rentQuantity;
        Double totalPrice =  + rentalPrice + depositAmount;
        // Check user balance if less than total price
        if(user.getBalance() < totalPrice){
            throw new ApiException("Balance lower than total price");
        }
        // Set rental details including end date, prices, status, deposit, and damage information
        toolRental.setUserId(userId);
        toolRental.setToolKitId(toolKitId);
        toolRental.setEndDate(rentEnd);
        toolRental.setRentalPrice(rentalPrice);
        toolRental.setDepositAmount(depositAmount);
        toolRental.setTotalPrice(totalPrice);
        toolRental.setStatus("Confirm");
        toolRental.setIsDepositRefunded(false);
        toolRental.setHasDamage(false);
        toolRental.setDamageCost(0.0);

        user.setBalance(user.getBalance() - totalPrice);
        userRepository.save(user);
        toolRentalRepository.save(toolRental);
        // send message
        whatsappService.sendRentToolkitMessage(user.getPhoneNumber(), toolKit.getName(),toolRental.getTotalPrice(),toolRental.getEndDate());

    }

    public void updateToolRental(Integer id, ToolRental toolRental){
        ToolRental oldToolRental = toolRentalRepository.findRentToolKitById(id);
        if(oldToolRental == null){
            throw new ApiException("Rent not found");
        }
        ToolKit toolKit = toolKitRepository.findToolKitsById(oldToolRental.getToolKitId());
        if (toolKit == null) {
            throw new ApiException("Toolkit not found");
        }

        User user = userRepository.findUserById(oldToolRental.getUserId());
        if (user == null) {
            throw new ApiException("User not found");
        }

        LocalDate newStart = toolRental.getStartDate();
        Integer newDays = toolRental.getRentDays();
        Integer newQuantity = toolRental.getQuantity();

        LocalDate newEnd = newStart.plusDays(newDays);

        if (newQuantity > toolKit.getQuantity()) {
            throw new ApiException("Requested quantity exceeds toolkit stock");
        }

        List<ToolRental> rentals = toolRentalRepository.toolRentsAtDate(toolKit.getId(), newStart, newEnd);

        int rentedQuantity = 0;
        for (ToolRental r : rentals) {
            if (!r.getId().equals(oldToolRental.getId())) {
                rentedQuantity += r.getQuantity();
            }
        }

        int available = toolKit.getQuantity() - rentedQuantity;

        if (available < newQuantity) {
            throw new ApiException("Not enough quantity available for selected dates");
        }

        double oldTotal = oldToolRental.getTotalPrice();
        Double newRentalPrice = toolKit.getPricePerDay() * newDays * newQuantity;
        Double newDepositAmount = toolKit.getSecurityDepositPerItem() * newQuantity;
        double newTotal = newRentalPrice + newDepositAmount;

        double difference = newTotal - oldTotal;

        if (difference > 0) {
            // user must pay more
            if (user.getBalance() < difference) {
                throw new ApiException("Insufficient balance for update");
            }
            user.setBalance(user.getBalance() - difference);
        } else {
            // refund
            user.setBalance(user.getBalance() + Math.abs(difference));
        }
        userRepository.save(user);

        oldToolRental.setStartDate(newStart);
        oldToolRental.setEndDate(newEnd);
        oldToolRental.setRentDays(newDays);
        oldToolRental.setQuantity(newQuantity);
        oldToolRental.setRentalPrice(newRentalPrice);
        oldToolRental.setDepositAmount(newDepositAmount);
        oldToolRental.setTotalPrice(newTotal);

        toolRentalRepository.save(oldToolRental);
    }

    public void deleteToolRental(Integer id){
        ToolRental oldToolRental = toolRentalRepository.findRentToolKitById(id);
        if(oldToolRental == null){
            throw new ApiException("Rent not found");
        }
        toolRentalRepository.delete(oldToolRental);
    }

    public void cancelRent(Integer id){
        ToolRental toolRental = toolRentalRepository.findRentToolKitById(id);
        if(toolRental == null){
            throw new ApiException("Rent not found");
        }
        // Ensure status not Handover, Returned or cancel already
        if(!toolRental.getStatus().equalsIgnoreCase("Confirm")){
            throw new ApiException("Only confirmed rents can be canceled");
        }
        User user = userRepository.findUserById(toolRental.getUserId());
        if(user ==  null){
            throw new ApiException("User not found");
        }
        // Refund total price to user and change rent status to cancel
        user.setBalance(user.getBalance()+ toolRental.getTotalPrice());
        toolRental.setStatus("Cancel");

        userRepository.save(user);
        toolRentalRepository.save(toolRental);
        // send message
        whatsappService.sendCancelRentMessage(user.getPhoneNumber(),toolRental.getTotalPrice());
        emailService.sendRentStatusChangeEmail(user.getEmail(), user.getUsername(), toolRental.getId(), toolRental.getStatus());
    }

    public void handoverTool(Integer id){
        ToolRental rental = toolRentalRepository.findRentToolKitById(id);
        if(rental == null){
            throw new ApiException("Rent not found");
        }
        if (!rental.getStatus().equalsIgnoreCase("Confirm")) {
            throw new ApiException("Only confirmed rentals can be handed over");
        }
        rental.setStatus("Handover");

        toolRentalRepository.save(rental);
        User user = userRepository.findUserById(rental.getUserId());
        if (user == null) {
            throw new ApiException("User not found");
        }
        emailService.sendRentStatusChangeEmail(user.getEmail(), user.getUsername(), rental.getId(), "Handover");
    }

    public void returnedTool(Integer id){
        ToolRental rental = toolRentalRepository.findRentToolKitById(id);
        if(rental == null){
            throw new ApiException("Rent not found");
        }
        if (!rental.getStatus().equalsIgnoreCase("Handover")) {
            throw new ApiException("Rental must be in handover status before return");
        }
        rental.setStatus("Returned");

        toolRentalRepository.save(rental);
        User user = userRepository.findUserById(rental.getUserId());
        if (user == null) {
            throw new ApiException("User not found");
        }
        emailService.sendRentStatusChangeEmail(user.getEmail(), user.getUsername(), rental.getId(), "Returned");
    }

    public void markDamage(Integer id, Double damageCost){
        if(damageCost < 0){
            throw new ApiException("Damage cost should be positive");
        }
        ToolRental toolRental = toolRentalRepository.findRentToolKitById(id);
        if (toolRental == null) {
            throw new ApiException("Rent not found");
        }
        if(toolRental.getDepositAmount() < damageCost){
            throw new ApiException("Damage cost exceed deposit amount");
        }
        if(!toolRental.getStatus().equalsIgnoreCase("Returned")){
            throw new ApiException("Can't mark damage unless tool returned");
        }
        toolRental.setHasDamage(true);
        toolRental.setDamageCost(damageCost);
        toolRentalRepository.save(toolRental);
    }

    public void refundDeposit(Integer id){
        ToolRental toolRental = toolRentalRepository.findRentToolKitById(id);
        if (toolRental == null) {
            throw new ApiException("Rent not found");
        }
        // Ensure Deposit not already refunded
        if (toolRental.getIsDepositRefunded()) {
            throw new ApiException("Security deposit already refunded");
        }
        // Allow refund after return
        if (!toolRental.getStatus().equalsIgnoreCase("Returned")) {
            throw new ApiException("Insurance refund allowed after return");
        }
        User user = userRepository.findUserById(toolRental.getUserId());
        if (user == null) {
            throw new ApiException("User not found");
        }
        Double refundAmount = toolRental.getDepositAmount();

        // Reduce damage cost if there is damage
        if(toolRental.getHasDamage()){
            refundAmount -= toolRental.getDamageCost();
        }

        // Refund insurance fee to user
        user.setBalance(user.getBalance() + refundAmount);
        userRepository.save(user);

        // Set true in insuranceRefunded
        toolRental.setIsDepositRefunded(true);
        toolRentalRepository.save(toolRental);

        //Email
        emailService.sendInsuranceRefundEmail(user.getEmail(), user.getUsername(), refundAmount);
    }

    public List<ToolRental> userUpcomingRentals(Integer userId){
        return toolRentalRepository.userUpcomingRentals(userId);
    }

}
