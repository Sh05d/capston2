package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.Studio;
import org.example.capston2.Model.StudioFollow;
import org.example.capston2.Model.User;
import org.example.capston2.Repository.StudioFollowRepository;
import org.example.capston2.Repository.StudioRepository;
import org.example.capston2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioFollowService {
    private final StudioFollowRepository studioFollowRepository;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;

    public List<StudioFollow> getStudioFollows(){
        return studioFollowRepository.findAll();
    }

    public void addStudioFollow(StudioFollow studioFollow){
        User user = userRepository.findUserById(studioFollow.getUserId());
        if(user == null){
            throw new ApiException("User not exist");
        }
        Studio studio = studioRepository.findStudioById(studioFollow.getStudioId());
        if(studio == null){
            throw new ApiException("Studio not found");
        }
        studioFollow.setNotify(true);
        studioFollowRepository.save(studioFollow);
    }

    public void updateStudioFollow(Integer id, StudioFollow studioFollow){
        StudioFollow oldStudioFollow = studioFollowRepository.findStudioFollowById(id);
        if(oldStudioFollow == null){
            throw new ApiException("Studio follow not found");
        }
        oldStudioFollow.setNotify(studioFollow.getNotify());
        studioFollowRepository.save(oldStudioFollow);
    }

    public void deleteStudioFollow(Integer id){
        StudioFollow oldStudioFollow = studioFollowRepository.findStudioFollowById(id);
        if(oldStudioFollow == null){
            throw new ApiException("Studio follow not found");
        }
        studioFollowRepository.delete(oldStudioFollow);
    }

    public List<StudioFollow> userFollowing(Integer userId){
        return studioFollowRepository.getUserFollows(userId);
    }

    public void disableAllNotification(Integer userId){
        List<StudioFollow> userFollows = studioFollowRepository.getUserFollows(userId);
        for(StudioFollow studioFollow: userFollows){
            studioFollow.setNotify(false);
            studioFollowRepository.save(studioFollow);
        }
    }


}
