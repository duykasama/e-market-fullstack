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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        // Khởi tạo Mockito
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadContracts() throws IOException {
        // Tạo một mock MultipartFile
        MultipartFile mockFile = createMockMultipartFile("sample.csv");

        // Định nghĩa hành vi giả lập cho các repository
        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        // Gọi phương thức loadContracts
        contractService.loadContracts(mockFile);

        // Verify that the contractRepository.save method was called
        verify(contractRepository, times(1)).save(any(Contract.class));
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
                String csvContent = "contract1,apartment1,customer1,2023-09-20,2023-09-25\n";
                return csvContent.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                // Trả về một InputStream từ nội dung tệp CSV
                String csvContent = "contract1,apartment1,customer1,2023-09-20,2023-09-25\n";
                return new ByteArrayInputStream(csvContent.getBytes());
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {

            }
        };
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


        @Test
        public void testLoadContractsWhenCsvFileNameIsContractCsv() throws IOException {
            // Tạo một mock MultipartFile với tên là "contract.csv"
            MultipartFile mockFile = createMockMultipartFile("contract.csv");

            // Định nghĩa hành vi giả lập cho các repository
            when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
            when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
            when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

            // Gọi phương thức loadContracts
            contractService.loadContracts(mockFile);

            // Verify that the contractRepository.save method was called
            verify(contractRepository, times(1)).save(any(Contract.class));
        }
}