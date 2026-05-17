package org.example.capston2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capston2.Api.ApiException;
import org.example.capston2.Model.User;
import org.example.capston2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void addUser(User user){
        user.setBalance(0.0);
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
    }

    public void updateUser(Integer id, User user){
        User oldUser = userRepository.findUserById(id);
        if(oldUser == null){
            throw new ApiException("User not found");
        }
        oldUser.setUsername(user.getUsername());
        oldUser.setPhoneNumber(user.getPhoneNumber());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setGender(user.getGender());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id){
        User oldUser = userRepository.findUserById(id);
        if(oldUser == null){
            throw new ApiException("User not found");
        }
        userRepository.delete(oldUser);
    }

    public void addToBalance(Integer id, Double amount){
        if(amount <= 0){
            throw new ApiException("Amount should be more than zero");
        }
        User user = userRepository.findUserById(id);
        if(user == null){
            throw new ApiException("User not found");
        }
        user.setBalance(user.getBalance()+amount);
        userRepository.save(user);
    }
}
