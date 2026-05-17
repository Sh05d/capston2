package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.*;
import org.example.capston2.Repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioReviewService {
    private final StudioReviewRepository studioReviewRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final WorkshopBookingRepository workshopBookingRepository;
    private final WorkshopRepository workshopRepository;
    private final ToolRentalRepository toolRentalRepository;
    private final ToolKitRepository toolKitRepository;

    public List<StudioReview> getStudioReviews(){
        return studioReviewRepository.findAll();
    }

    public void addStudioReview(StudioReview studioReview){
        User user = userRepository.findUserById(studioReview.getUserId());
        if(user == null){
            throw new ApiException("User not exist");
        }
        Studio studio = studioRepository.findStudioById(studioReview.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not found");
        }
        Boolean allowToReview = false;

        // Allow to review if rent from studio and there any rent tool returned
        List<ToolRental> userRents = toolRentalRepository.userReturnedRentals(user.getId());
        for(ToolRental rental: userRents){
            ToolKit toolKit = toolKitRepository.findToolKitsByIdAndStudioId(rental.getToolKitId(),studio.getId());
            if(toolKit != null){
                allowToReview = true;
                break;
            }
        }
        // Allow to review if booking workshop from studio is confirmed and workshop completed only
        List<WorkshopBooking> userBooking = workshopBookingRepository.userConfirmBookings(user.getId());
        for(WorkshopBooking booking: userBooking){
            Workshop workshop = workshopRepository.completeWorkshopByStudioId(booking.getWorkshopId(),studio.getId());
            if(workshop != null){
                allowToReview = true;
                break;
            }
        }

        if(!allowToReview){
            throw new ApiException("Can't add review");
        }

        studioReviewRepository.save(studioReview);

        double totalRating = studio.getRate() * studio.getNumberOfRating() + studioReview.getRate();
        int newCount = studio.getNumberOfRating() + 1;
        Double newRate = totalRating / newCount;

        studio.setRate(newRate);
        studio.setNumberOfRating(newCount);
        studioRepository.save(studio);
    }

    public void updateStudioReview(Integer id, StudioReview studioReview){
        StudioReview oldReview = studioReviewRepository.findReviewById(id);
        if(oldReview == null){
            throw new ApiException("Review not found");
        }
        Studio studio = studioRepository.findStudioById(oldReview.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not found");
        }
        double oldTotalRating = studio.getRate() * studio.getNumberOfRating();
        double newTotalRating = oldTotalRating - oldReview.getRate() + studioReview.getRate();
        Double newRate = newTotalRating / studio.getNumberOfRating();

        oldReview.setContent(studioReview.getContent());
        oldReview.setRate(studioReview.getRate());
        studioReviewRepository.save(oldReview);

        studio.setRate(newRate);
        studioRepository.save(studio);
    }

    public void deleteStudioReview(Integer id){
        StudioReview oldReview = studioReviewRepository.findReviewById(id);
        if(oldReview == null){
            throw new ApiException("Review not found");
        }
        studioReviewRepository.delete(oldReview);
    }

    public List<StudioReview> studioReviews(Integer studioId){
        return studioReviewRepository.studioReviews(studioId);
    }

    public List<StudioReview> studioReviewsByRate(Integer studioId ,Double rate){
        return studioReviewRepository.studioReviewsByRate(studioId, rate);
    }
}
