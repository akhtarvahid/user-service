package com.example.userservice.services;

import com.example.userservice.models.User;
import com.example.userservice.repositories.UserRepo;
import com.example.userservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        Optional<User> user = userRepo.findByEmail(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found for " +  username);
        }

        return new CustomUserDetails(user.get());
    }
}