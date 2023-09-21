package com.example.emarket.controllers;

import com.example.emarket.enums.FileType;
import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.exceptions.FileFormatNotSupported;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import com.example.emarket.util.FileTypeDistributor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
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
            logger.info("Start to read files");

            if (files == null || files.length == 0) {
                throw new BadRequestException("There is no files to read");
            }

            AtomicInteger recordCount = new AtomicInteger();

            Arrays
                    .stream(files)
                    .distinct()
                    .forEach(file -> {
                        try {
                            String headers = new String(file.getBytes()).split("\n")[0].toLowerCase().trim().replaceAll("^[^a-zA-Z0-9,]*$", "");
                            FileType fileType = FileTypeDistributor.getFileTypeFromHeaders(headers);

                            switch (fileType) {
                                case CUSTOMER:
                                    int customerCount = customerService.loadCustomer(file.getInputStream());
                                    recordCount.addAndGet(customerCount);

                                    logger.info("Save " + customerCount + " customers from 1 file");
                                    break;
                                case APARTMENT:
                                    int apartmentCount = apartmentService.saveByFile(file);
                                    recordCount.addAndGet(apartmentCount);

                                    logger.info("Save " + apartmentCount + " apartments from 1 file");
                                    break;
                                case CONTRACT:
                                    int contractCount = contractService.loadContracts(file);
                                    recordCount.addAndGet(contractCount);

                                    logger.info("Save " + contractCount + " contracts from 1 file");
                                    break;
                                default:
                                    throw new BadRequestException("File is not in the correct format");
                            }
                        } catch (IOException | BadRequestException e) {
                            logger.error(e.getMessage());
                        } catch (FileFormatNotSupported e) {
                            logger.error(e.getMessage());
                            throw new BadRequestException(e.getMessage());
                        }
                    });
            logger.info("Saved data from files successfully, " + recordCount + " records inserted");

            return ResponseEntity
                    .ok(
                            ResponseObject.builder()
                                    .statusCode(200)
                                    .message("Saved data from files successfully, " + recordCount + " records inserted")
                                    .build()
                    );
        } catch (BadRequestException e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .status(400)
                    .body(
                            ResponseObject.builder()
                                    .statusCode(400)
                                    .message(e.getMessage())
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
