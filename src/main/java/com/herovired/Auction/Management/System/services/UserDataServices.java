package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.models.User;
import com.herovired.Auction.Management.System.models.UserData;
import com.herovired.Auction.Management.System.repositories.UserDataRepository;
import com.herovired.Auction.Management.System.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;

@Service
public class UserDataServices {
    private UserDataRepository userDataRepository;

    public HashMap<String, String> validateRequestBody(UserData userData, BindingResult resultSet) {
        HashMap<String, String> errorMap = new HashMap<>();
        if (resultSet.hasErrors()) {
            for (FieldError fieldError : resultSet.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

        }

        return errorMap;
    }
}
