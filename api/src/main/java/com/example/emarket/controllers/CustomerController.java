package com.example.emarket.controllers;

import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://lavender-company-fe.vercel.app", "http://localhost:10001", "http://e-market-frontend:80", "http://localhost:80"}, allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @GetMapping
    public ResponseEntity<ResponseObject> getAllCustomers() {
        logger.info("Getting all customers");

        try {
            List<Customer> customers = customerService.getAllCustomers();
            logger.info("Get customers from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(customers)
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

    @CrossOrigin
    @GetMapping("/pagination")
    public ResponseEntity<ResponseObject> getCustomersByPage(Integer pageSize, Integer offset) {
        logger.info("Getting customers by page");

        try {
            if (pageSize <= 0 || offset <= 0) {
                throw new BadRequestException("Page size and offset must be greater than 0");
            }

            Page<Customer> customers = customerService.getCustomersByPage(pageSize, offset);
            logger.info("Get customers from database successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Success")
                            .data(customers)
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
    public ResponseEntity<ResponseObject> addNewCustomer(@RequestParam String firstName,
                                                        @RequestParam String lastName,
                                                        @RequestParam String address,
                                                        @RequestParam Integer age,
                                                        @RequestParam String status
    ) {
        logger.info("Start to add new customer");

        try {
            Customer customer = Customer.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .address(address)
                    .age(age.shortValue())
                    .status(status)
                    .build();

            customerService.save(customer);
            logger.info("Add new customer successfully");

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(200)
                            .message("Add new customer successfully")
                            .build()
            );
        }
        catch (Exception e) {
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

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        logger.info("Start deleting customer");

        try {
            customerService.deleteCustomerById(id);
            logger.info("Delete customer successfully");
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
