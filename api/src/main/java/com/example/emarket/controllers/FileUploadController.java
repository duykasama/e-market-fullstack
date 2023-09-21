package com.example.emarket.controllers;

import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



import java.lang.reflect.Array;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://lavender-company-fe.vercel.app", "http://localhost:10001", "http://e-market-frontend:80", "http://localhost:80"}, allowCredentials = "true", allowedHeaders = "*")
@AllArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    private final CustomerService customerService;
    private final ApartmentService apartmentService;
    private final ContractService contractService;
    private final CustomerRepository customerRepository;
    private final ApartmentRepository apartmentRepository;
    private final ContractRepository contractRepository;

    private final Logger logger = LogManager.getLogger(FileUploadController.class);

    @PostMapping
    public ResponseEntity<ResponseObject> uploadFiles(MultipartFile[] files) {
        logger.info("Start to save data from files");

        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    if (fileName != null) {
                        if (fileName.startsWith("Customer")) {
                            customerService.saveByFile(file);
                        } else if (fileName.startsWith("Apartment")) {
                            apartmentService.saveByFile(file);
                        } else if (fileName.startsWith("Contract")) {
                            contractService.saveByFile(file);
                        }
                    }
                }
            }
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Saved data from files successfully")
                            .data("20 objects saved")
                            .build()
            );
        }catch (Exception e) {
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
