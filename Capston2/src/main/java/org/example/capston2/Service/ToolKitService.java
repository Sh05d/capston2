package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.Studio;
import org.example.capston2.Model.ToolKit;
import org.example.capston2.Repository.StudioRepository;
import org.example.capston2.Repository.ToolKitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolKitService {
    private final ToolKitRepository toolKitRepository;
    private final StudioRepository studioRepository;

    public List<ToolKit> getToolKits(){
        return toolKitRepository.findAll();
    }

    public void addToolKit(ToolKit toolKit){
        Studio studio = studioRepository.findStudioById(toolKit.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not exist");
        }
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
        oldToolKit.setInsuranceFeePrtItem(toolKit.getInsuranceFeePrtItem());
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
}
