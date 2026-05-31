package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.Studio;
import org.example.capston2.Model.ToolKit;
import org.example.capston2.Model.ToolRental;
import org.example.capston2.Repository.StudioRepository;
import org.example.capston2.Repository.ToolKitRepository;
import org.example.capston2.Repository.ToolRentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolKitService {
    private final ToolKitRepository toolKitRepository;
    private final StudioRepository studioRepository;
    private final ToolRentalRepository toolRentalRepository;

    public List<ToolKit> getToolKits(){
        return toolKitRepository.findAll();
    }

    public void addToolKit(Integer studioId, ToolKit toolKit){
        Studio studio = studioRepository.findStudioById(studioId);
        if(studio == null){
            throw new ApiException("Studio not exist");
        }
        toolKit.setStudioId(studioId);
        toolKitRepository.save(toolKit);
    }

    public void updateToolKit(Integer id, ToolKit toolKit){
        Studio studio = studioRepository.findStudioById(toolKit.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not exist");
        }
        ToolKit oldToolKit = toolKitRepository.findToolKitsById(id);
        if(oldToolKit == null){
            throw new ApiException("ToolKit not found");
        }
        oldToolKit.setName(toolKit.getName());
        oldToolKit.setDescription(toolKit.getDescription());
        oldToolKit.setCategory(toolKit.getCategory());
        oldToolKit.setPricePerDay(toolKit.getPricePerDay());
        oldToolKit.setSecurityDepositPerItem(toolKit.getSecurityDepositPerItem());
        oldToolKit.setQuantity(oldToolKit.getQuantity());
        oldToolKit.setPickupMethod(toolKit.getPickupMethod());
        oldToolKit.setStudioId(toolKit.getStudioId());
        toolKitRepository.save(oldToolKit);
    }

    public void deleteToolkit(Integer id){
        ToolKit oldToolKit = toolKitRepository.findToolKitsById(id);
        if(oldToolKit == null){
            throw new ApiException("ToolKit not found");
        }
        toolKitRepository.delete(oldToolKit);
    }

    public List<ToolKit> toolKitByCategoryAndPriceRange(String category, Integer maxPrice){
        return toolKitRepository.toolKitByCategoryAndPriceRange(category,maxPrice);
    }

    public List<ToolKit> toolKitByCategoryAndQuantity(String category, Integer minQuantity){
        return toolKitRepository.toolKitByCategoryAndQuantity(category,minQuantity);
    }

    public List<ToolKit> toolKitByCategoryAndPickupMethod(String category, String pickupMethod){
        return toolKitRepository.toolKitByCategoryAndPickupMethod(category, pickupMethod);
    }

    public Integer availableQuantityInDate(Integer id, LocalDate firstDate, LocalDate secondDate){
        ToolKit toolKit = toolKitRepository.findToolKitsById(id);
        if(toolKit == null){
            throw new ApiException("ToolKit not found");
        }
        // Check overlapping rentals for same toolkit and date range
        List<ToolRental> toolRentalList = toolRentalRepository.toolRentsAtDate(toolKit.getId(), firstDate,secondDate);
        Integer rentedQuantity = 0;
        for(ToolRental rent : toolRentalList){
            rentedQuantity += rent.getQuantity();
        }

        // Calculate available quantity to rent in the date
        Integer availableQuantity = toolKit.getQuantity() - rentedQuantity;

        return availableQuantity;
    }
}
