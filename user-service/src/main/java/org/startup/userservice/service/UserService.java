package org.startup.userservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.startup.userservice.entity.User;
import org.startup.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final String USERS_CACHE = "users";
    private static final String USER_BY_ID_CACHE = "userById";


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(USERS_CACHE)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = USER_BY_ID_CACHE, key = "#id")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Caching(
            put = @CachePut(value = USER_BY_ID_CACHE, key = "#id", unless = "#result == null"),
            evict = @CacheEvict(value = USERS_CACHE, allEntries = true)
    )
    public User updateUser(Long id, User user) {
        User newUser=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        if(user.getUsername()!=null){
            newUser.setUsername(user.getUsername());
        }
        if (user.getEmail()!=null){
            newUser.setEmail(user.getEmail());
        }
        if (user.getPassword()!=null){
            newUser.setPassword(user.getPassword());
        }
        return userRepository.save(newUser);

    }

    @Caching(evict = {
            @CacheEvict(value = USER_BY_ID_CACHE, key = "#id"),
            @CacheEvict(value = USERS_CACHE, allEntries = true)
    })
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}

