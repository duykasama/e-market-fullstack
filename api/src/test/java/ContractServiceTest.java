import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.services.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadNonExistentFiles() throws IOException {
        // Tạo một mock MultipartFile với tên tệp không tồn tại
        MultipartFile mockFile = createMockMultipartFile("non_existent_file.csv");

        // Định nghĩa hành vi giả lập cho các repository
        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        // Thử gọi phương thức loadContracts
        contractService.loadContracts(mockFile);

        // Kiểm tra xem hợp đồng có được lưu hay không
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testLoadExistentFiles() throws IOException {
        // Tạo một mock MultipartFile với tên tệp tồn tại
        MultipartFile mockFile = createMockMultipartFile("students.csv");

        // Định nghĩa hành vi giả lập cho các repository
        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        // Gọi phương thức loadContracts
        contractService.loadContracts(mockFile);

        // Verify that the contractRepository.saveAll method was called
        verify(contractRepository, atLeastOnce()).saveAll(anyIterable());
    }

    @Test
    public void testLoadExistentFilesWithMissedValues() throws IOException {
        // Tạo một mock MultipartFile với tên tệp tồn tại có giá trị bị thiếu
        MultipartFile mockFile = createMockMultipartFile("students-missing.csv");

        // Định nghĩa hành vi giả lập cho các repository
        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        // Gọi phương thức loadContracts
        contractService.loadContracts(mockFile);

        // Verify that the contractRepository.save method was not called due to missing values
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testSaveNullModelList() {
        // Thử lưu danh sách hợp đồng null
        assertThrows(IllegalArgumentException.class, () -> contractService.saveContracts(null));
    }

    @Test
    public void testSaveEmptyModelList() {
        // Tạo một danh sách hợp đồng rỗng
        List<Contract> emptyList = new ArrayList<>();

        // Thử lưu danh sách hợp đồng rỗng
        assertThrows(IllegalArgumentException.class, () -> contractService.saveContracts(emptyList));
    }

    @Test
    public void testSaveNonEmptyModelList() {
        // Tạo một danh sách hợp đồng không rỗng
        List<Contract> contracts = new ArrayList<>();

        // Tạo đối tượng Apartment
        Apartment apartment1 = new Apartment();
        apartment1.setId("apartment1");
        // Thiết lập các thuộc tính khác của apartment1
        // Ví dụ:
        apartment1.setAddress("123 Main St");

        // Tạo đối tượng Customer
        Customer customer1 = new Customer();
        customer1.setId("customer1");
        // Thiết lập các thuộc tính khác của customer1
        // Ví dụ:
        customer1.setFirstName("John Doe");

        // Tạo đối tượng Contract đầu tiên và thiết lập các thuộc tính của nó
        Contract contract1 = new Contract();
        contract1.setId("contract1");
        contract1.setStartDate(LocalDate.of(2023, 9, 20));
        contract1.setEndDate(LocalDate.of(2023, 9, 23));
        contract1.setApartment(apartment1); // Liên kết đối tượng Apartment với Contract
        contract1.setCustomer(customer1); // Liên kết đối tượng Customer với Contract

        // Thêm contract1 vào danh sách
        contracts.add(contract1);

        Apartment apartment2 = new Apartment();
        apartment2.setId("apartment2");
        apartment2.setAddress("456 Elm St");

        Customer customer2 = new Customer();
        customer2.setId("customer2");
        customer2.setFirstName("Jane Smith");

// Tạo đối tượng Contract thứ hai và thiết lập các thuộc tính của nó
        Contract contract2 = new Contract();
        contract2.setId("contract2");
        contract2.setStartDate(LocalDate.of(2023, 9, 21));
        contract2.setEndDate(LocalDate.of(2023, 9, 24));
        contract2.setApartment(apartment2);
        contract2.setCustomer(customer2);

// Thêm contract2 vào danh sách
        contracts.add(contract2);
        // Định nghĩa hành vi giả lập cho contractRepository.saveAll
        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        // Thử lưu danh sách hợp đồng không rỗng
        List<Contract> savedContracts = contractService.saveContracts(contracts);

        // Kiểm tra xem danh sách đã được lưu thành công hay không
        assertEquals(2, savedContracts.size());
        assertEquals("contract1", savedContracts.get(0).getId());
        assertEquals("contract2", savedContracts.get(1).getId());
    }

    @Test
    public void testGetAllModelsFromDatabase() {
        // Tạo danh sách các hợp đồng giả lập
        List<Contract> contracts = new ArrayList<>();
        contracts.add(createMockContract("contract1"));
        contracts.add(createMockContract("contract2"));

        // Định nghĩa hành vi giả lập cho contractRepository.findAll
        when(contractRepository.findAll()).thenReturn(contracts);

        // Gọi phương thức getAllContracts
        List<Contract> result = contractService.getAllContracts();

        // Kiểm tra kết quả
        assertEquals(2, result.size());
        assertEquals("contract1", result.get(0).getId());
        assertEquals("contract2", result.get(1).getId());
    }

    @Test
    public void testSaveDuplicatedModels() {
        // Tạo một danh sách hợp đồng với hai hợp đồng có cùng ID
        List<Contract> contracts = new ArrayList<>();

            // Tạo đối tượng Apartment
            Apartment apartment1 = new Apartment();
            apartment1.setId("apartment1");
            // Thiết lập các thuộc tính khác của apartment1
            // Ví dụ:
            apartment1.setAddress("123 Main St");

            // Tạo đối tượng Customer
            Customer customer1 = new Customer();
            customer1.setId("customer1");
            // Thiết lập các thuộc tính khác của customer1
            // Ví dụ:
            customer1.setFirstName("John Doe");

            // Tạo đối tượng Contract đầu tiên và thiết lập các thuộc tính của nó
            Contract contract1 = new Contract();
            contract1.setId("contract1");
            contract1.setStartDate(LocalDate.of(2023, 9, 20));
            contract1.setEndDate(LocalDate.of(2023, 9, 23));
            contract1.setApartment(apartment1); // Liên kết đối tượng Apartment với Contract
            contract1.setCustomer(customer1); // Liên kết đối tượng Customer với Contract

            // Thêm contract1 vào danh sách
            contracts.add(contract1);

        // Tạo đối tượng Apartment
        Apartment apartment2 = new Apartment();
        apartment2.setId("apartment1");
        // Thiết lập các thuộc tính khác của apartment1
        // Ví dụ:
        apartment2.setAddress("123 Main St");

        // Tạo đối tượng Customer
        Customer customer2 = new Customer();
        customer2.setId("customer1");
        // Thiết lập các thuộc tính khác của customer1
        // Ví dụ:
        customer2.setFirstName("John Doe");

        // Tạo đối tượng Contract đầu tiên và thiết lập các thuộc tính của nó
        Contract contract2 = new Contract();
        contract2.setId("contract1");
        contract2.setStartDate(LocalDate.of(2023, 9, 20));
        contract2.setEndDate(LocalDate.of(2023, 9, 23));
        contract2.setApartment(apartment1); // Liên kết đối tượng Apartment với Contract
        contract2.setCustomer(customer1); // Liên kết đối tượng Customer với Contract

        // Thêm contract2 vào danh sách
        contracts.add(contract2);

        // Định nghĩa hành vi giả lập cho contractRepository.saveAll
        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        // Thử lưu danh sách hợp đồng với các hợp đồng trùng lặp
        List<Contract> savedContracts = contractService.saveContracts(contracts);

        // Kiểm tra xem danh sách đã được lưu thành công hay không
        assertEquals(2, savedContracts.size()); // Phải lưu được cả hai hợp đồng
    }

    @Test
    public void testSaveContractsWithMissingCustomersOrApartments() {
        // Tạo một danh sách hợp đồng với khách hàng và căn hộ bị thiếu
        List<Contract> contracts = new ArrayList<>();
        Contract contract1 = createMockContract("contract1");
        contract1.setCustomer(null); // Khách hàng bị thiếu
        contract1.setApartment(null); // Căn hộ bị thiếu
        contracts.add(contract1);

        // Định nghĩa hành vi giả lập cho contractRepository.saveAll
        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        // Thử lưu danh sách hợp đồng với khách hàng và căn hộ bị thiếu
        List<Contract> savedContracts = contractService.saveContracts(contracts);

        // Kiểm tra xem danh sách đã được lưu thành công hay không
        assertEquals(0, savedContracts.size()); // Không có hợp đồng nào được lưu
    }

    // Thêm các testcase khác tương tự ở đây...

    private Contract createMockContract(String id) {
        // Tạo một đối tượng Contract giả lập
        Contract contract = new Contract();
        contract.setId(id);
        // Các thiết lập khác cho đối tượng giả lập này
        return contract;
    }

    private Apartment createMockApartment() {
        // Tạo một đối tượng Apartment giả lập
        Apartment apartment = new Apartment();
        apartment.setId("apartment1");
        // Các thiết lập khác cho đối tượng giả lập này
        return apartment;
    }

    private Customer createMockCustomer() {
        // Tạo một đối tượng Customer giả lập
        Customer customer = new Customer();
        customer.setId("customer1");
        // Các thiết lập khác cho đối tượng giả lập này
        return customer;
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
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                String csvContent = "start_date,end_date,apartmentId,customerId\n" +
                        "2023-09-20,2023-09-23,400d7792-d833-49ed-b5bb-ce603cb2b733,dd7da36a-33a7-4315-9fd5-6242bb1eda96\n";
                return csvContent.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                // Trả về một InputStream từ nội dung tệp CSV
                String csvContent = "start_date,end_date,apartmentId,customerId\n" +
                        "2023-09-20,2023-09-23,400d7792-d833-49ed-b5bb-ce603cb2b733,dd7da36a-33a7-4315-9fd5-6242bb1eda96\n";
                return new ByteArrayInputStream(csvContent.getBytes());
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {

            }
        };
    }
}
