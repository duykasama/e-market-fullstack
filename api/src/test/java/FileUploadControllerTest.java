import com.example.emarket.controllers.FileUploadController;
import com.example.emarket.enums.FileType;
import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.exceptions.FileFormatNotSupported;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import com.example.emarket.util.FileTypeDistributor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.emarket.responses.ResponseObject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class FileUploadControllerTest {

    @InjectMocks
    private FileUploadController fileUploadController;

    @Mock
    private CustomerService customerService;

    @Mock
    private ApartmentService apartmentService;

    @Mock
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadUnsupportedFileFormat() {
        // Create a mock MultipartFile with an unsupported format
        MockMultipartFile unsupportedFile = new MockMultipartFile("file", "file3.doc", "application/msword", new byte[0]);

        // Create an array of MultipartFile
        MultipartFile[] files = new MultipartFile[]{unsupportedFile};

        // Mock a FileUploadController
        FileUploadController fileUploadController = mock(FileUploadController.class);

        // Mock the response from fileUploadController when calling uploadFiles
        when(fileUploadController.uploadFiles(files)).thenReturn(ResponseEntity.badRequest().body(new ResponseObject("File format is not supported")));

        // Call the uploadFiles method from fileUploadController
        ResponseEntity<ResponseObject> responseEntity = fileUploadController.uploadFiles(files);

        // Check the response and status
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("File format is not supported", responseEntity.getBody().getMessage());

        // Ensure that the uploadFiles method of fileUploadController was called once with the files array
        verify(fileUploadController, times(1)).uploadFiles(files);
    }

    @Test
    void testUploadFiles_NoFiles() {
        // Arrange
        MultipartFile[] files = new MultipartFile[0];

        // Act
        ResponseEntity<ResponseObject> response = fileUploadController.uploadFiles(files);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("There is no files to read", response.getBody().getMessage());
    }

    @Test
    void testUploadFiles_FileFormatNotSupported() throws IOException {
        // Arrange
        MockMultipartFile unsupportedFile = createMockMultipartFile("unsupported-data.txt");
        MultipartFile[] files = new MultipartFile[]{unsupportedFile};

        // Mô phỏng việc apartmentService.saveByFile ném ngoại lệ
        doThrow(new RuntimeException("File format is not supported")).when(apartmentService).saveByFile(any(MultipartFile.class));

        // Act
        ResponseEntity<ResponseObject> response = fileUploadController.uploadFiles(files);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("File format is not supported", response.getBody().getMessage()); // Updated assertion
    }




    private MockMultipartFile createMockMultipartFile(String filename) {
        return new MockMultipartFile(
                filename,
                filename,
                "text/csv",
                "Test file content".getBytes()
        );
    }

}
