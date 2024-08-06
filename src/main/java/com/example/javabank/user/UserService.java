package com.example.javabank.user;

import com.example.javabank.account.Account;

import java.util.List;
import java.util.Optional;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsernameAndPassword(String username, String password){
        Optional<User>userOptional=userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            return null;
        }
        User user=userOptional.get();
        if(user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public User findByUserId(Integer userId){
        Optional<User>userOptional=userRepository.findByUserId(userId);
        return userOptional.orElse(null);
    }
    public User findByUsername(String username){
        Optional<User>userOptional=userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }

    public List<User> findAllUsers(){
        return userRepository.findAllUsers();
    }


    public boolean createUser(User user, String accountType){
        Optional<User> searchedUser=userRepository.findByUsername(user.getUsername());
        if(searchedUser.isPresent()){
            return false;
        }
        return userRepository.createUser(user,accountType);
    }

    public List<Account> getAccountsByUserId(Integer userId){
        return userRepository.getAccountsByUserId(userId);
    }


}
