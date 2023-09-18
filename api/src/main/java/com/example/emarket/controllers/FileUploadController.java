package com.example.emarket.controllers;

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

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://lavender-company-fe.vercel.app", "http://localhost:10001", "http://e-market-frontend:80", "http://localhost:80"}, allowCredentials = "true", allowedHeaders = "*")
@AllArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    private final CustomerService customerService;
    private final ApartmentService apartmentService;
    private final ContractService contractService;

    private final Logger logger = LogManager.getLogger(FileUploadController.class);

    @PostMapping
    public ResponseEntity<ResponseObject> uploadFiles(MultipartFile[] files) {
        logger.info("Start to save data from files");

        try {

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
    @Autowired
    public LoadFileCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/upload-customer")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Call the service method to process the uploaded file (e.g., save data to the database)
            InputStream inputStream = file.getInputStream();
            customerService.loadCustomer(inputStream);
            return ResponseEntity.status(HttpStatus.OK).body("Data uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading and processing the file: " + e.getMessage());
        }
    }
}
