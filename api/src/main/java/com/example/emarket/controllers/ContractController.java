package com.example.emarket.controllers;

import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://lavender-company-fe.vercel.app", "http://localhost:10001", "http://e-market-frontend:80", "http://localhost:80"}, allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;
    private final CustomerService customerService;
    private final ApartmentService apartmentService;

    private final Logger logger = LogManager.getLogger(ContractController.class);

    @GetMapping
    public ResponseEntity<ResponseObject> getAllContracts() {
        logger.info("Getting contracts from database");

        try {
            List<Contract> contracts = contractService.getAllContracts();
            logger.info("Get contracts from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Get contracts successfully")
                            .data(contracts)
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
    public ResponseEntity<ResponseObject> getContractsByPage(Integer pageSize, Integer offset) {
        logger.info("Getting contracts by page");

        try {
            if (pageSize <= 0 || offset <= 0) {
                throw new BadRequestException("Page size and offset must be greater than 0");
            }

            Page<Contract> contracts = contractService.getContractsByPage(pageSize, offset);
            logger.info("Get contracts from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(contracts)
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
    public ResponseEntity<ResponseObject> addNewContract(
            String customerId,
            String apartmentId,
            String startDate,
            String endDate
    ) {
        logger.info("Start to add new contract");

        try {
            Contract contract = Contract.builder()
                    .customer(customerService.getCustomerById(customerId))
                    .apartment(apartmentService.getApartmentById(apartmentId))
                    .startDate(LocalDate.parse(startDate))
                    .endDate(LocalDate.parse(endDate))
                    .build();

            contractService.addNewContract(contract);

            return ResponseEntity.ok(
              ResponseObject.builder()
                      .statusCode(200)
                      .message("New contract added successfully")
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
}
