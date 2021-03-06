package com.reloadly.paypro.combined.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.reloadly.paypro.combined.constant.EventTopicConstant;
import com.reloadly.paypro.combined.exceptions.BadRequestException;
import com.reloadly.paypro.combined.exceptions.UnauthorisedAccessException;
import com.reloadly.paypro.combined.payload.request.LoginRequest;
import com.reloadly.paypro.combined.payload.request.SignupRequest;
import com.reloadly.paypro.combined.payload.response.LoginResponse;
import com.reloadly.paypro.combined.persistence.model.User;
import com.reloadly.paypro.combined.persistence.repository.UserRepository;
import com.reloadly.paypro.combined.security.JwtUtils;
import com.reloadly.paypro.combined.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Repository
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${mailgun-api-key}")
    private String apiKey;

    @Value("${mailgun-domain-name}")
    private String domainName;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public String processSignup(SignupRequest signupRequest) {
        if (ObjectUtils.isEmpty(signupRequest.getEmail()) || ObjectUtils.isEmpty(signupRequest.getPassword()) ||
                ObjectUtils.isEmpty(signupRequest.getPhoneNumber()) || ObjectUtils.isEmpty(signupRequest.getUsername())) {
            throw new BadRequestException("Missing required details");
        }

        if (userRepository.existsByUsername(signupRequest.getUsername()))
            throw new BadRequestException("Error: Sorry ... username has been taken ... Choose another one");
        if (userRepository.existsByEmail(signupRequest.getEmail()))
            throw new BadRequestException("Error: Sorry ... email is already in use. Login with this email if it's yours");
        if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber()))
            throw new BadRequestException("Error: Sorry ... this phone number has already been registered here");

        String accountNumber = signupRequest.getPhoneNumber().substring(1);

        User user = new User(signupRequest.getEmail(), signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getPhoneNumber(), accountNumber);
        userRepository.save(user);
        String subject = "PayPro account created successfully";
        String message = "Congratulations, you have successfully created an account with PayPro. Your account number is " + accountNumber + ". <br> " +
                "As a gesture of goodwill from us, your account has been credited with <b>$10,000.00.</b> Thanks for joining us.";
        try {
            String response = sendMessageToGivenEmail(subject, message, user.getEmail());
            log.info("Mailgun: " + response);
        } catch(Exception e){
            e.printStackTrace();
        }
        return "Congratulations, your account number is " + accountNumber;
    }

    @Override
    public LoginResponse processLogin(LoginRequest loginRequest) {
        if (ObjectUtils.isEmpty(loginRequest.getUsername()) || ObjectUtils.isEmpty(loginRequest.getPassword())) {
            throw new BadRequestException("Missing required details");
        }
        LoginResponse response = new LoginResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            response = new LoginResponse(jwt);
        } catch (Exception e) {
            throw new UnauthorisedAccessException("Invalid username or password");
        }
        return response;
    }


    private String sendMessageToGivenEmail(String subject, String message, String emailAddress) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", "Doctor Vee from PayPro <doctorvee@paypro.com>")
                .queryString("to", emailAddress)
                .queryString("subject", subject != null ? subject : "Notification from PayPro")
                .queryString("html", "<p style='font-family:arial; font-size:1.5em; color:blue;text-align:center;'>"
                        + message +
                        "</h3> <footer style='background-color:skyblue; text-align:center'>✔ PayPro</footer>")
                .asJson();
        log.info(String.valueOf(response.getBody()));
        String responseString;
        if (response.getBody().getObject().has("id")) {
            responseString = "Email Successfully Sent 👍";
        } else {
            responseString = "An error occurred 👎";
        }
        return responseString;
    }

}
