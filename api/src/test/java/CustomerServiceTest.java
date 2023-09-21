import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.services.ContractService;
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
    private ContractRepository contractRepository;
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
        // Tạo một mock MockMultipartFile với tên tệp tồn tại có giá trị bị thiếu
        MockMultipartFile mockFile = new MockMultipartFile(
                "customers-missing.csv", // Tên tệp
                getClass().getResourceAsStream("/customers-missing.csv") // Đường dẫn tới tệp dữ liệu thử nghiệm của bạn
        );

        // Định nghĩa hành vi giả lập cho customerRepository (chỉ cần giả lập cho customerRepository)
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        // Gọi phương thức loadCustomer
        customerService.loadCustomer(mockFile.getInputStream());

        // Verify rằng phương thức customerRepository.save không được gọi vì giá trị bị thiếu
        verify(customerRepository, never()).save(any(Customer.class));
    }



    @Test
    public void testSaveNullModelList() {
        // Thử lưu danh sách khách hàng null
        assertThrows(IllegalArgumentException.class, () -> customerService.saveCustomers(null));
    }

    @Test
    public void testSaveEmptyModelList() {
        // Tạo một danh sách khách hàng rỗng
        List<Customer> emptyList = new ArrayList<>();

        // Thử lưu danh sách khách hàng rỗng
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
        // Tạo danh sách các khách hàng giả lập
        List<Customer> customers = new ArrayList<>();
        customers.add(createMockCustomer("customer1"));
        customers.add(createMockCustomer("customer2"));

        // Định nghĩa hành vi giả lập cho customerRepository.findAll
        when(customerRepository.findAll()).thenReturn(customers);

        // Gọi phương thức getAllCustomers
        List<Customer> result = customerService.getAllCustomers();

        // Kiểm tra kết quả
        assertEquals(2, result.size());
        assertEquals("customer1", result.get(0).getId());
        assertEquals("customer2", result.get(1).getId());
    }

    @Test
    public void testSaveDuplicatedModels() {
        // Tạo một danh sách khách hàng với hai khách hàng có cùng ID
        List<Customer> customers = new ArrayList<>();

        // Tạo đối tượng Customer đầu tiên và thiết lập các thuộc tính của nó
        Customer customer1 = new Customer();
        customer1.setId("customer1");
        customer1.setFirstName("John Doe");

        // Thêm customer1 vào danh sách
        customers.add(customer1);

        // Tạo đối tượng Customer thứ hai và thiết lập các thuộc tính của nó
        Customer customer2 = new Customer();
        customer2.setId("customer1");
        customer2.setFirstName("Jane Smith");

        // Thêm customer2 vào danh sách
        customers.add(customer2);

        // Định nghĩa hành vi giả lập cho customerRepository.saveAll
        when(customerRepository.saveAll(anyIterable())).thenReturn(customers);

        // Thử lưu danh sách khách hàng với các khách hàng trùng lặp
        List<Customer> savedCustomers = customerService.saveCustomers(customers);

        // Kiểm tra xem danh sách đã được lưu thành công hay không
        assertEquals(2, savedCustomers.size()); // Phải lưu được cả hai khách hàng
    }


    private Customer createMockCustomer(String customerId) {
        // Create and return a mock Customer object
        Customer customer = new Customer();
        customer.setId(customerId);
        // Set other properties if needed
        return customer;
    }

    private Apartment createMockApartment() {
        // Tạo một đối tượng Apartment giả lập
        Apartment apartment = new Apartment();
        apartment.setId("apartment1");
        // Các thiết lập khác cho đối tượng giả lập này
        return apartment;
    }

    private MultipartFile createMockMultipartFile(String fileName) throws IOException {
        // Tạo một mock MultipartFile với nội dung của tệp CSV mẫu
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
                // Trả về một InputStream từ nội dung tệp CSV
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
