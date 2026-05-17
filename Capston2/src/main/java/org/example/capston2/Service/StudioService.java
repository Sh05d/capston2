package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.Studio;
import org.example.capston2.Repository.StudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;

    public List<Studio> getStudios(){
        return studioRepository.findAll();
    }

    public void addStudio(Studio studio){
        studio.setRate(0.0);
        studio.setNumberOfRating(0);
        studioRepository.save(studio);
    }

    public void updateStudio(Integer id, Studio studio){
        Studio oldStudio = studioRepository.findStudioById(id);
        if(oldStudio == null){
            throw new ApiException("Studio not found");
        }
        oldStudio.setName(studio.getName());
        oldStudio.setEmail(studio.getEmail());
        oldStudio.setCity(studio.getCity());
        oldStudio.setLocation(studio.getLocation());
        studioRepository.save(oldStudio);
    }

    public void deleteStudio(Integer id){
        Studio oldStudio = studioRepository.findStudioById(id);
        if(oldStudio == null){
            throw new ApiException("Studio not found");
        }
        studioRepository.delete(oldStudio);
    }

    public List<Studio> bestStudioInCity(String city){
        return studioRepository.topStudioInCity(city);
    }
}
