package com.example.emarket.controllers;

import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://lavender-company-fe.vercel.app", "http://localhost:10001", "http://e-market-frontend:80", "http://localhost:80"}, allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final Logger logger = LogManager.getLogger(ApartmentController.class);

    @GetMapping("/uri")
    public String getUri() {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toString();
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllApartments() {
        logger.info("Getting apartments from database");

        try {
            List<Apartment> apartments = apartmentService.getAllApartments();
            logger.info("Get apartments from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Get apartments successfully")
                            .data(apartments)
                            .build()
            );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(
                      ResponseObject.builder()
                              .statusCode(500)
                              .message(e.getMessage())
                              .build()
                    );
        }
    }

    @GetMapping("/pagination")
    public ResponseEntity<ResponseObject> getApartmentsByPage(Integer pageSize, Integer offset) {
        logger.info("Getting apartments by page");

        try {
            if (pageSize <= 0 || offset <= 0) {
                throw new BadRequestException("Page size and offset must be greater than 0");
            }

            Page<Apartment> apartments = apartmentService.getApartmentsByPage(pageSize, offset);
            logger.info("Get apartments from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(apartments)
                            .build()
            );
        } catch (BadRequestException e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject.builder()
                                    .statusCode(400)
                                    .message(e.getMessage())
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .internalServerError()
                    .body(
                            ResponseObject.builder()
                                    .statusCode(500)
                                    .message(e.getMessage())
                                    .build()
                    );
        }
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addApartment(String address,
                             String rentalPrice,
                             Integer numberOfRooms
    ) {
        logger.info("Start to add new apartment");

        try {
            Apartment apartment = Apartment.builder()
                    .address(address)
                    .rentalPrice(rentalPrice)
                    .numberOfRooms(numberOfRooms.shortValue())
                    .build();

            apartmentService.addApartment(apartment);
            logger.info("Added new apartment successfully");

            return ResponseEntity.ok(
                        ResponseObject.builder()
                                .statusCode(200)
                                .message("New apartment created successfully")
                                .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(
                            ResponseObject.builder()
                                    .statusCode(500)
                                    .message(e.getMessage())
                                    .build()
            );
        }
    }
}
