package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepo;
import com.example.userservice.repositories.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private TokenRepo tokenRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(
            UserRepo userRepo,
            TokenRepo tokenRepo,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Token login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            //TODO: Throw exception
            return null;
        }

        User user = userOptional.get();
        if (bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            Token token = new Token();
            token.setUser(user);
            token.setValue(RandomStringUtils.randomAlphabetic(128));
            LocalDate today = LocalDate.now();
            LocalDate onedayLater = today.plusDays(1);
            Date expiryAt = Date.from(onedayLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
            token.setExpiryAt(expiryAt);
            return tokenRepo.save(token);
        }


        return null;
    }

    public void logout(String token) {
        Optional<Token> optionalToken = tokenRepo.findByValueAndDeletedEquals(token, false);
        if (optionalToken.isEmpty()) {
            //TODO: throw exception

        }
        Token toDelete = optionalToken.get();
        toDelete.setDeleted(true);
        tokenRepo.save(toDelete);

    }


    public User signup(String name, String email, String password) {
        // TODO: Validation

        User u = new User();
        u.setEmail(email);
        u.setName(name);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));

        User saveduser = userRepo.save(u);
        return saveduser;
    }
}
