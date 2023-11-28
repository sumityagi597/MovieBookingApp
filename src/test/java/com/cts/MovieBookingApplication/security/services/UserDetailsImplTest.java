package com.cts.MovieBookingApplication.security.services;

import com.cts.MovieBookingApplication.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
class UserDetailsImplTest {

    UserDetailsImpl u;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMethod(){
        u = new UserDetailsImpl(new ObjectId("64932c5d6b1fb619351db4ea"),"sumityagi","sumit","tyagi",
                "sumityagi@gmail.com",1233L,"123",new ArrayList<>());
        u.equals(u);
        u.equals(new Object());
        u.setLoginId("sumity");
        u.get_id();
        u.set_id(new ObjectId("64932c5d6b1fb619351db4ea"));
        u.getEmail();
        u.getPassword();
        u.getAuthorities();
        u.getUsername();
        u.isAccountNonExpired();
        u.isAccountNonLocked();
        u.isCredentialsNonExpired();
        u.isEnabled();
        UserDetailsImpl.build(new User());
        Assertions.assertEquals("sumityagi@gmail.com",u.getEmail());
    }
}
