package com.cts.MovieBookingApplication.payload.response;

import java.util.List;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class JwtResponse {
	
	private String token;
    private String type = "Bearer";
    private ObjectId _id;
    private String loginId;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, ObjectId  _id, String loginId, String email, List<String> roles) {
        this.token = accessToken;
        this._id = _id;
        this.loginId = loginId;
        this.email = email;
        this.roles = roles;
    }

}
