package com.uw.service;

import com.uw.config.ApplicationUser;
import com.uw.config.BruteForceProtector;
import com.uw.config.PasswordConfig;
import com.uw.config.jwt.JwtUtils;
import com.uw.config.jwt.RefreshTokenService;
import com.uw.dto.JwtResponse;
import com.uw.dto.LoginDTO;
import com.uw.dto.UserInfoDTO;
import com.uw.exceptions.AuthenticationErrorException;
import com.uw.exceptions.ExceptionResponse;
import com.uw.exceptions.WrongPasswordException;
import com.uw.model.RefreshToken;
import com.uw.model.User;
import com.uw.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final BruteForceProtector bruteForceProtector;

    public UserService(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenService refreshTokenService, BruteForceProtector bruteForceProtector) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.bruteForceProtector = bruteForceProtector;
    }

    public ResponseEntity<?> authenticate(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(EntityNotFoundException::new);
        if (!user.isActive()) {
            return ResponseEntity.badRequest().body(new ExceptionResponse("User set to inactive, please contact support", LocalDateTime.now()));
        }
        if (bruteForceProtector.isUserBlocked(loginDTO.getUsername())){
            throw new RuntimeException("The user is blocked");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));
        } catch (Exception e) {
            bruteForceProtector.recordFailedLoginAttempt(loginDTO.getUsername());
            throw new AuthenticationErrorException();
        }
        bruteForceProtector.unblockUser(loginDTO.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(applicationUser.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                applicationUser.getId(),
                applicationUser.getUsername(),
                applicationUser.getAuthorities().stream().findFirst()
                        .toString()
        ));
    }

    public ResponseEntity<?> changePassword(@AuthenticationPrincipal String username,
                                            @NotNull UserInfoDTO userInfoDTO){
        if (userInfoDTO.getNewPassword() == null ) {
            throw new WrongPasswordException();
        }
        if (userInfoDTO.getNewPassword().equals(userInfoDTO.getOldPassword())) {
            throw new RuntimeException();
        }
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        String encryptedNewPass = PasswordConfig.passwordEncoder().encode(userInfoDTO.getNewPassword());
        userRepository.changePassword(encryptedNewPass, user.getId());
        return ResponseEntity.status(200).body("Password changed successfully");
    }

    public ResponseEntity<?> activateDeactivate(@NotNull String username){
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        user.setActive(!user.isActive());
        userRepository.activateDeactivate(user.isActive(), user.getId());
        return ResponseEntity.ok("User activated/deactivated successfully");
    }

}