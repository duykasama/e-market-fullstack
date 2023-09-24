import com.example.emarket.controllers.CustomerController;
import com.example.emarket.exceptions.CustomerNotFoundException;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerService);
    }

    @Test
    public void testGetAllCustomers() {
        List<Customer> mockCustomers = Arrays.asList(
                new Customer("1", "John", "Doe", "Address 1", (short) 30, "Active"),
                new Customer("2", "Alice", "Smith", "Address 2", (short) 25, "Inactive")
        );

        when(customerService.getAllCustomers()).thenReturn(mockCustomers);

        ResponseEntity<ResponseObject> response = customerController.getAllCustomers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(mockCustomers, (List<Customer>) response.getBody().getData());

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void testGetCustomersByPage() {
        int pageSize = 10;
        int offset = 1;

        List<Customer> mockCustomers = Arrays.asList(
                new Customer("1", "John", "Doe", "Address 1", (short) 30, "Active"),
                new Customer("2", "Alice", "Smith", "Address 2", (short) 25, "Inactive")
        );

        Page<Customer> page = new PageImpl<>(mockCustomers);

        when(customerService.getCustomersByPage(pageSize, offset)).thenReturn(page);

        ResponseEntity<ResponseObject> response = customerController.getCustomersByPage(pageSize, offset);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(mockCustomers, page.getContent());

        verify(customerService, times(1)).getCustomersByPage(pageSize, offset);
    }

    @Test
    public void testGetCustomersByPageWithInvalidParameters() {
        int pageSize = 0;
        int offset = 0;

        ResponseEntity<ResponseObject> response = customerController.getCustomersByPage(pageSize, offset);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Page size and offset must be greater than 0", response.getBody().getMessage());

        verify(customerService, never()).getCustomersByPage(pageSize, offset);
    }

    @Test
    public void testAddNewCustomer() {
        String firstName = "John";
        String lastName = "Doe";
        String address = "Address 1";
        Integer age = 30;
        String status = "Active";

        ResponseEntity<ResponseObject> response = customerController.addNewCustomer(firstName, lastName, address, age, status);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Add new customer successfully", response.getBody().getMessage());

        verify(customerService, times(1)).save(any(Customer.class));
    }





    @Test
    public void testSearchCustomer() {
        String keyword = "John";

        List<Customer> mockCustomers = Arrays.asList(
                new Customer("1", "John", "Doe", "Address 1", (short) 30, "Active"),
                new Customer("2", "Johnny", "Doe", "Address 2", (short) 25, "Inactive")
        );

        when(customerService.searchCustomersByKeyword(keyword)).thenReturn(mockCustomers);

        ResponseEntity<ResponseObject> response = customerController.searchCustomer(keyword);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(mockCustomers, (List<Customer>) response.getBody().getData());

        verify(customerService, times(1)).searchCustomersByKeyword(keyword);
    }
}
