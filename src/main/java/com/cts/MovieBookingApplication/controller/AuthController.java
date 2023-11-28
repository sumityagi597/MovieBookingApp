package com.cts.MovieBookingApplication.controller;

import com.cts.MovieBookingApplication.model.ERole;
import com.cts.MovieBookingApplication.model.Role;
import com.cts.MovieBookingApplication.model.User;
import com.cts.MovieBookingApplication.payload.request.LoginRequest;
import com.cts.MovieBookingApplication.payload.request.SignupRequest;
import com.cts.MovieBookingApplication.payload.response.JwtResponse;
import com.cts.MovieBookingApplication.payload.response.MessageResponse;
import com.cts.MovieBookingApplication.repository.RoleRepo;
import com.cts.MovieBookingApplication.repository.UserRepo;
import com.cts.MovieBookingApplication.security.jwt.JwtUtils;
import com.cts.MovieBookingApplication.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1.0/moviebooking")
@Slf4j
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepo userRepository;

	@Autowired
	RoleRepo roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		try{
			log.info("Inside /login -> LoginRequest :  "+loginRequest.toString());
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			ResponseEntity<?> response = ResponseEntity.ok(
					new JwtResponse(jwt, userDetails.get_id(), userDetails.getUsername(), userDetails.getEmail(), roles));
			log.info("Outside /login -> Response : "+response.toString());
			return  response;
		}catch(Exception e){
			return new ResponseEntity<>("Bad credentials",HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByLoginId(signUpRequest.getLoginId())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: LoginId is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getLoginId(), signUpRequest.getFirstName(), signUpRequest.getLastName(),
				signUpRequest.getEmail(), signUpRequest.getContactNumber(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		String errorMessegae = "Error: Role is not found.";

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(errorMessegae));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(errorMessegae));
					roles.add(adminRole);

					break;
				case "guest":
					Role modRole = roleRepository.findByName(ERole.ROLE_GUEST)
							.orElseThrow(() -> new RuntimeException(errorMessegae));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(errorMessegae));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/{loginId}/forgot")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<String> changePassword(@RequestBody LoginRequest loginRequest, @PathVariable String loginId){
		log.info("forgot password endopoint accessed by "+loginRequest.getLoginId());
		Optional<User> user1 = userRepository.findByLoginId(loginId);
		User availableUser = user1.get();
		User updatedUser = new User(
				loginId,
				availableUser.getFirstName(),
				availableUser.getLastName(),
				availableUser.getEmail(),
				availableUser.getContactNumber(),
				passwordEncoder.encode(loginRequest.getPassword())
		);
		updatedUser.set_id(availableUser.get_id());
		updatedUser.setRoles(availableUser.getRoles());
		userRepository.save(updatedUser);
		log.info(loginRequest.getLoginId()+" has password changed successfully");
		return new ResponseEntity<>("Users password changed successfully", HttpStatus.OK);
	}
}
