package com.example.userservice.services;

import com.example.userservice.dtos.SendEmailMessageDto;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository,
                TokenRepository tokenRepository,
                KafkaTemplate<String, String> kafkaTemplate,
                ObjectMapper objectMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signUp(String email,
                       String name,
                       String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);
        //save the user object to the DB.
        User savedUser = userRepository.save(user);

        SendEmailMessageDto emailMessageDto = new SendEmailMessageDto();
        emailMessageDto.setTo(savedUser.getEmail());
        emailMessageDto.setFrom("akhtarvahid543@gmail.com");
        emailMessageDto.setSubject("Welcome to the akhtarvahid platform - we're thrilled to have you on board!");
        emailMessageDto.setBody("You’ve successfully joined our community, and we’re excited for you to start exploring all the features we have to offer. Whether you’re here to [mention the core value or feature of your product/service], we’re here to support you every step of the way");


        try {
            kafkaTemplate.send(
                    "sendEmail",
                    objectMapper.writeValueAsString(emailMessageDto)
            );
        } catch (JsonProcessingException e) {
            System.out.println("Encountered error::" +e.getMessage());
            throw new RuntimeException(e);
        }


        return savedUser;
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " doesn't exist");
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new UserNotFoundException("User password " + password + " doesn't match");
        }

        //Login successful, generate a Token.
        Token token = generateToken(user);

        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        //128 character alphanumeric string.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    // If token is active, make deleted:false which means user logged out
    public void logout(String tokenValue) {

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);

        if (optionalToken.isEmpty()) {
            throw new UserNotFoundException("Token is not provided!");
        }


        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    // If token is active returns users details else 200
    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());

        //Throw new Exception
        return optionalToken.map(Token::getUser).orElse(null);

    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}