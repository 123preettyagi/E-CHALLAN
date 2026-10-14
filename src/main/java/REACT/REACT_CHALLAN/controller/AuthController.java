package REACT.REACT_CHALLAN.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import REACT.REACT_CHALLAN.config.JwtService;
import REACT.REACT_CHALLAN.dto.ApiResponse;
import REACT.REACT_CHALLAN.dto.AuthResponse;
import REACT.REACT_CHALLAN.dto.LoginRequest;
import REACT.REACT_CHALLAN.dto.RegisterRequest;
import REACT.REACT_CHALLAN.entity.User;
import REACT.REACT_CHALLAN.service.UserService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, 
                         UserService userService, 
                         JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            
            AuthResponse response = new AuthResponse(
                null,
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getRole(),
                "User registered successfully"
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate user
        	Authentication authentication =
        	        authenticationManager.authenticate(
        	                new UsernamePasswordAuthenticationToken(
        	                        request.getUsernameOrEmail(),
        	                        request.getPassword()
        	                ));
        	
            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            
            // Get user details
            User user = userService.findByUsernameOrEmail(request.getUsernameOrEmail());

            AuthResponse response = new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
            );

            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid username or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT is stateless, so we just return success
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
}