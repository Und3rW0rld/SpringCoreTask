package com.uw.controller;

import com.uw.config.jwt.AuthTokenFilter;
import com.uw.config.jwt.JwtUtils;
import com.uw.config.jwt.RefreshTokenService;
import com.uw.config.jwt.TokenManager;
import com.uw.dto.LoginDTO;
import com.uw.dto.RefreshTokenRequest;
import com.uw.dto.RefreshTokenResponse;
import com.uw.dto.UserInfoDTO;
import com.uw.model.RefreshToken;
import com.uw.model.User;
import com.uw.repository.UserRepository;
import com.uw.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("api/v1")
public class LoginController {

    private final UserService userService;
    private final TokenManager tokenManager;
    private final AuthTokenFilter authTokenFilter;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public LoginController(TokenManager tokenManager, AuthTokenFilter authTokenFilter,
                           RefreshTokenService refreshTokenService,
                           UserService userService, UserRepository userRepository,
                           JwtUtils jwtUtils) {
        this.tokenManager = tokenManager;
        this.authTokenFilter = authTokenFilter;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/login")
    @Operation(summary = "User Login", description = "This method is used to Log In")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws AuthenticationException {
        return userService.authenticate(loginDTO);
    }

    @PutMapping("/login")
    @Operation(summary = "Change Current User Password", description = "This method changes User's password and returns new password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal String username,
                                            @RequestBody UserInfoDTO userInfoDTO){
        return userService.changePassword(username,userInfoDTO);
    }

    @PostMapping(value = "/refresh")
    @Operation(summary = "Refresh Token", description = "This method is used to refresh JWT token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request,
                                          HttpServletRequest httpRequest) {
        String refreshToken = request.getRefreshToken();
        String oldActiveToken = authTokenFilter.parseJwt(httpRequest);
        tokenManager.invalidateToken(oldActiveToken);
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtToken(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, refreshToken));
                }).orElseThrow(RuntimeException::new);
    }

    @PostMapping("/logout")
    @Operation(summary = "Log Out", description = "This method is used to Log Out current User")
    public ResponseEntity<?> logoutUser(HttpServletRequest httpRequest) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .orElseThrow(EntityNotFoundException::new);
        String oldActiveToken = authTokenFilter.parseJwt(httpRequest);
        tokenManager.invalidateToken(oldActiveToken);
        Long userId = user.getId();
        refreshTokenService.deleteByUserId(userId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Log out successful");
    }
}
