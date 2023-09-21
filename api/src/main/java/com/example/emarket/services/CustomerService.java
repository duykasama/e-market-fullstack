        package com.example.emarket.services;

        import com.example.emarket.exceptions.CustomerNotFoundException;
        import com.example.emarket.models.entities.Customer;
        import com.example.emarket.repositories.CustomerRepository;
        import com.example.emarket.repositories.UserRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.List;
        import java.util.Optional;

        @Service
        public class CustomerService {

            private final CustomerRepository customerRepository;
            public CustomerService(CustomerRepository customerRepository) {
                this.customerRepository = customerRepository;
            }
            public List<Customer> getAllCustomers() {
                return customerRepository.findAll();
            }

            public void save(Customer customer) {
                customerRepository.save(customer);
            }

            public Customer getCustomerById(String customerId) {
                return customerRepository.findById(customerId).orElse(null);
            }

            public void deleteCustomerById(String id) throws CustomerNotFoundException {
                Optional<Customer> customerOptional = customerRepository.findById(id);
                if (customerOptional.isPresent()) {
                    customerRepository.delete(customerOptional.get());
                } else {
                    throw new CustomerNotFoundException();
                }
            }

            public Page<Customer> getCustomersByPage(Integer pageSize, Integer offset) {
                return customerRepository.findAll(PageRequest.of(offset-1, pageSize));
            }

            public final String saveByFile (MultipartFile file){
                if (file.isEmpty()) {
                    throw new IllegalArgumentException("File is empty.");
                }

                try (InputStream is = file.getInputStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(",");
                        if(data[0].trim().equalsIgnoreCase("first_name")){
                            continue;
                        }


                        if (data.length == 5) {
        //                    String id = data[0].trim();
                            String firstName = data[0].trim();
                            String lastName = data[1].trim();
                            String address = data[2].trim();
                            int age = Integer.parseInt(data[3].trim());
                            String status = data[4].trim();

                            Customer customer = Customer.builder()
        //                            .id(id)
                                    .firstName(firstName)
                                    .lastName(lastName)
                                    .address(address)
                                    .age((short)age)
                                    .status(status)
                                    .build();

                            customerRepository.save(customer);
                        }
                    }            return "Data uploaded successfully.";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public List<Customer> searchCustomersByKeyword(String keyword) {
                return customerRepository.findByFirstNameContainingOrLastNameContaining(keyword, keyword);
            }

        }
