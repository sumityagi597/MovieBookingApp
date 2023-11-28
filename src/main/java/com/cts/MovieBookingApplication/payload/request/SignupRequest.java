package com.cts.MovieBookingApplication.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
	
    @NotBlank
    @Size(min = 3, max = 20)
    private String loginId;

    @NotBlank
    private String firstName;


    @NotBlank
    private String lastName;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Long contactNumber;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;



}
