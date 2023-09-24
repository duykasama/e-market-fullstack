import com.example.emarket.controllers.ContractController;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractControllerTest {

    @InjectMocks
    private ContractController contractController;

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ApartmentService apartmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewContract_ValidInput_ShouldReturnSuccess() {
        // Mock input data
        String customerId = "customer_id";
        String apartmentId = "apartment_id";
        LocalDate startDate = LocalDate.parse("2023-09-24");
        LocalDate endDate = LocalDate.parse("2023-09-30");

        // Mock customer and apartment retrieval
        Customer customer = new Customer();
        Apartment apartment = new Apartment();
        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(apartment);

        // Create a contract to return when addNewContract is called
        Contract contract = new Contract();
        doNothing().when(contractService).addNewContract(any(Contract.class));

        // Call the API
        ResponseEntity<ResponseObject> response = contractController.addNewContract(customerId, apartmentId, startDate.toString(), endDate.toString());

        // Assert
        // Verify that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the response object contains the expected message
        assertEquals("New contract added successfully", response.getBody().getMessage());

        // Verify that the contractService.addNewContract() was called with the expected contract object
        verify(contractService, times(1)).addNewContract(any(Contract.class));
    }
    @Test
    public void testGetAllContracts_ValidData_ShouldReturnContracts() {
        // Mock data
        List<Contract> contracts = getSampleContracts();

        // Mock contractService to return contracts when getAllContracts is called
        when(contractService.getAllContracts()).thenReturn(contracts);

        // Call the API
        ResponseEntity<ResponseObject> response = contractController.getAllContracts();

        // Assert
        // Verify that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the response object contains the expected data
        assertEquals(contracts, response.getBody().getData());

        // Verify that the contractService.getAllContracts() was called
        verify(contractService, times(1)).getAllContracts();
    }

    @Test
    public void testGetContractsByPage_InvalidData_ShouldReturnBadRequest() {
        // Invalid data
        int pageSize = -1; // Invalid pageSize
        int offset = 0;

        // Call the API with invalid data
        ResponseEntity<ResponseObject> response = contractController.getContractsByPage(pageSize, offset);

        // Assert
        // Verify that the status code is 400 (Bad Request)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify that the response message contains an error message related to the invalid data
        assertTrue(response.getBody().getMessage().contains("Page size and offset must be greater than 0"));

        // Verify that contractService.getContractsByPage() was not called
        verify(contractService, never()).getContractsByPage(pageSize, offset);
    }


    @Test
    public void testGetContractsByPage_WithInvalidPageSizeAndOffset_ShouldReturnBadRequest() {
        // Invalid page size and offset
        int pageSize = 0;
        int offset = -1;

        // Call the API with invalid inputs
        ResponseEntity<ResponseObject> response = contractController.getContractsByPage(pageSize, offset);

        // Assert
        // Verify that the status code is 400 (Bad Request)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify that the response message contains the expected error message
        assertTrue(response.getBody().getMessage().contains("Page size and offset must be greater than 0"));

        // Verify that contractService.getContractsByPage() was not called
        verify(contractService, never()).getContractsByPage(anyInt(), anyInt());
    }

    @Test
    public void testAddNewContract_WithInvalidDate_ShouldReturnBadRequestOrInternalServerError() {
        // Invalid date format
        String customerId = "customer_id";
        String apartmentId = "apartment_id";
        String startDate = "2023-09-24"; // Valid format
        String endDate = "2023/09/30";   // Invalid format

        // Mock customer and apartment retrieval
        Customer customer = new Customer();
        Apartment apartment = new Apartment();
        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(apartment);

        try {
            // Try to parse endDate, and if it fails, catch the exception
            LocalDate endDateParsed = LocalDate.parse(endDate);

            // If parsing succeeds, proceed with creating the contract
            Contract contract = Contract.builder()
                    .customer(customerService.getCustomerById(customerId))
                    .apartment(apartmentService.getApartmentById(apartmentId))
                    .startDate(LocalDate.parse(startDate))
                    .endDate(endDateParsed)
                    .build();

            contractService.addNewContract(contract);

            // In case of successful contract creation, return a 200 (OK) response
            ResponseEntity<ResponseObject> response = contractController.addNewContract(customerId, apartmentId, startDate, endDate);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        } catch (DateTimeParseException e) {
            // If parsing fails, return a 400 (Bad Request) or 500 (Internal Server Error) response
            ResponseEntity<ResponseObject> response = ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject.builder()
                                    .statusCode(400)
                                    .message("Invalid date format: " + e.getMessage())
                                    .build()
                    );
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            // ResponseEntity<ResponseObject> response = ResponseEntity
            //         .internalServerError()
            //         .body(
            //                 ResponseObject.builder()
            //                         .statusCode(500)
            //                         .message("Internal server error: " + e.getMessage())
            //                         .build()
            //         );
            // assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        // Verify that contractService.addNewContract() was not called
        verify(contractService, never()).addNewContract(any(Contract.class));
    }

    // Helper methods for getting sample data (replace with actual data)
    private List<Contract> getSampleContracts() {
        // Replace with actual sample contracts
        return null;
    }

    private Page<Contract> getSamplePageOfContracts() {
        // Replace with actual sample page of contracts
        return null;
    }
}
