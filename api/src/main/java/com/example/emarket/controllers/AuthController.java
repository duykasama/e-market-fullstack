package com.example.emarket.controllers;

import com.example.emarket.exceptions.InvalidCredentialsException;
import com.example.emarket.exceptions.UserAlreadyExistsException;
import com.example.emarket.exceptions.UserDoesNotExistException;
import com.example.emarket.models.entities.User;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.JwtService;
import com.example.emarket.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final Logger logger = LogManager.getLogger(AuthController.class);

    @GetMapping("/refresh")
    public ResponseEntity<ResponseObject> refreshToken(@CookieValue String refreshToken) {
        logger.info("Start to refresh token");
        ResponseObject responseObject = new ResponseObject();

        try {
            if (refreshToken != null && !refreshToken.isBlank()) {
                logger.info("Returning new token");

                responseObject.setStatusCode(200);
                responseObject.setMessage("Token has been refreshed");
                responseObject.setData(jwtService.generateToken(refreshToken));

                logger.info("Token has been refreshed");
                return ResponseEntity.ok(responseObject);
            }

            responseObject.setStatusCode(401);
            responseObject.setMessage("Unauthorized");
            return ResponseEntity.status(401)
                    .body(responseObject);

        } catch (Exception e) {
            responseObject.setStatusCode(401);
            responseObject.setMessage(e.getMessage());
            responseObject.setData(null);

            return ResponseEntity.status(401)
                    .body(responseObject);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(String email, String password) {
        logger.info("Start to register user");

        try {
            User registeredUser = userService.registerUser(email, password);
            logger.info("Register use successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Registered successfully")
                            .data(jwtService.generateToken(registeredUser))
                            .build()
            );
        } catch (UserAlreadyExistsException e) {
            logger.error(e.getMessage());

            return ResponseEntity.status(401).body(
                    ResponseObject.builder()
                            .statusCode(401)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .statusCode(500)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> signIn(String email, String password, HttpServletResponse response, HttpServletRequest request) {
        logger.info("Start to authenticate user");

        try {
            User user = userService.login(email, password);
            logger.info("User is authenticated");

            String refreshToken = jwtService.generateRefreshToken(user);

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setMaxAge(1000 * 60 * 60 * 24);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setDomain("localhost");
            cookie.setPath("/");

            response.addCookie(cookie);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Log in successfully")
                            .data(jwtService.generateToken(user))
                            .build()
            );
        } catch (UserDoesNotExistException e) {
            logger.error(e.getMessage());

            return ResponseEntity.status(401).body(
                    ResponseObject.builder()
                            .statusCode(401)
                            .message(e.getMessage())
                            .build()
            );
        } catch (InvalidCredentialsException e) {
            logger.error(e.getMessage());

            return ResponseEntity.status(401).body(
                    ResponseObject.builder()
                            .statusCode(401)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.internalServerError().body(
                    ResponseObject.builder()
                            .statusCode(500)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<ResponseObject> signOut(HttpServletResponse response) {
        logger.info("Start to sign out");

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
        logger.info("Sign out successfully");

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(200)
                        .message("Log out successfully")
                        .build()
        );
    }
}
