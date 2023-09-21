import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyIterable;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testLoadNonExistentFiles() throws IOException {
        // Create a mock MultipartFile with a non-existent file name
        MultipartFile mockFile = new MockMultipartFile("non_existent_file.csv", "non_existent_file.csv", "text/csv", "your,csv,data".getBytes());

        // Define the behavior of apartmentRepository.findById to return a mock apartment
        Mockito.when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));

        // Define the behavior of customerRepository.findById to return an empty optional
        Mockito.when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the loadCustomer method
        customerService.loadCustomer(mockFile.getInputStream());

        // Verify that customerRepository.save method was never called
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any(Customer.class));
    }


    @Test
    public void testLoadExistentFile() throws IOException {
        // Create a mock MultipartFile with an existing file containing customer data
        MultipartFile mockFile = new MockMultipartFile("customers.csv", "customers.csv", "text/csv", "your,csv,data".getBytes());

        // Define the behavior of customerRepository.saveAll to return an empty list
        when(customerRepository.saveAll(anyIterable())).thenReturn(Collections.emptyList());

        // Call the loadCustomer method
        customerService.loadCustomer(mockFile.getInputStream());

        // Verify that the customerRepository.saveAll method was called exactly once with any iterable
        verify(customerRepository, times(1)).saveAll(anyIterable());
    }

    @Test
    public void testLoadExistentFilesWithMissedValues() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "customers-missing.csv", // Tên tệp
                getClass().getResourceAsStream("/customers-missing.csv") // Đường dẫn tới tệp dữ liệu thử nghiệm của bạn
        );

        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        customerService.loadCustomer(mockFile.getInputStream());

        verify(customerRepository, never()).save(any(Customer.class));
    }



    @Test
    public void testSaveNullModelList() {
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomers(null));
    }

    @Test
    public void testSaveEmptyModelList() {
        List<Customer> emptyList = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomers(emptyList));
    }

    @Test
    public void testSaveNonEmptyModelList() {
        // Create a list of non-empty customers
        List<Customer> customerList = new ArrayList<>();
        customerList.add(createMockCustomer("customer_id"));

        // Define the behavior of customerRepository.saveAll to return the input list
        Mockito.when(customerRepository.saveAll(anyIterable())).thenReturn(customerList);

        // Call the saveCustomers method
        List<Customer> savedCustomers = customerService.saveCustomers(customerList);

        // Verify that the savedCustomers list is not null and not empty
        assertNotNull(savedCustomers);
        assertFalse(savedCustomers.isEmpty());

        // Verify that each customer in the savedCustomers list is not null and has a non-null ID
        for (Customer customer : savedCustomers) {
            if (customer != null) {
                assertNotNull(customer.getId());
            }
        }
    }

    @Test
    public void testGetAllModelsFromDatabase() {
        List<Customer> customers = new ArrayList<>();
        customers.add(createMockCustomer("customer1"));
        customers.add(createMockCustomer("customer2"));

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("customer1", result.get(0).getId());
        assertEquals("customer2", result.get(1).getId());
    }

    @Test
    public void testSaveDuplicatedModels() {
        List<Customer> customers = new ArrayList<>();

        Customer customer1 = new Customer();
        customer1.setId("customer1");
        customer1.setFirstName("John Doe");

        customers.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId("customer1");
        customer2.setFirstName("Jane Smith");

        customers.add(customer2);

        when(customerRepository.saveAll(anyIterable())).thenReturn(customers);

        List<Customer> savedCustomers = customerService.saveCustomers(customers);

        assertEquals(2, savedCustomers.size());
    }


    private Customer createMockCustomer(String customerId) {
        // Create and return a mock Customer object
        Customer customer = new Customer();
        customer.setId(customerId);
        // Set other properties if needed
        return customer;
    }

    private Apartment createMockApartment() {
        Apartment apartment = new Apartment();
        apartment.setId("apartment1");
        return apartment;
    }

    private MultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "text/csv";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                String csvContent = "id,firstName,apartmentId\n" +
                        "customer1,John Doe,apartment1\n";
                return csvContent.getBytes().length;
            }


            @Override
            public byte[] getBytes() throws IOException {
                String csvContent = "id,firstName,apartmentId\n" +
                        "customer1,John Doe,apartment1\n";
                return csvContent.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                String csvContent = "id,firstName,apartmentId\n" +
                        "customer1,John Doe,apartment1\n";
                return new ByteArrayInputStream(csvContent.getBytes());
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {

            }
        };
    }
}