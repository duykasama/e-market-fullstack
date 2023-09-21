import com.example.emarket.exceptions.CustomerNotFoundException;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("1", "John", "Doe", "123 Main St", (short) 30, "Active"));
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        Customer customer = new Customer("1", "John", "Doe", "123 Main St", (short) 30, "Active");
        // Act
        customerService.save(customer);
        // Assert
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testGetCustomerById() {
        // Arrange
        String customerId = "1";
        Customer customer = new Customer(customerId, "John", "Doe", "123 Main St", (short) 30, "Active");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        // Act
        Customer result = customerService.getCustomerById(customerId);
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testDeleteCustomerById() {
        // Arrange
        String customerId = "1";
        Customer customer = new Customer(customerId, "John", "Doe", "123 Main St", (short) 30, "Active");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        // Act
        assertDoesNotThrow(() -> customerService.deleteCustomerById(customerId));
        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void testDeleteCustomerById_CustomerNotFoundException() {
        // Arrange
        String customerId = "1";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(customerId));
    }

    @Test
    void testGetCustomersByPage() {
        // Arrange
        int pageSize = 10;
        int offset = 1;
        PageRequest pageRequest = PageRequest.of(offset - 1, pageSize);
        when(customerRepository.findAll(pageRequest)).thenReturn(mock(Page.class));
        // Act
        Page<Customer> result = customerService.getCustomersByPage(pageSize, offset);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveByFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "customers.csv",
                "customer-data.csv",
                "text/csv",
                "John,Doe,123 Main St,30,Active\nJane,Smith,456 Elm St,25,Inactive".getBytes()
        );
        // Act
        String message = customerService.saveByFile(file);
        // Assert
        assertEquals("Data uploaded successfully.", message);
        verify(customerRepository, times(2)).save(any(Customer.class));
    }
}
