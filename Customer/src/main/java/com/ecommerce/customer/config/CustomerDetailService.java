package com.ecommerce.customer.config;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    Customer customer;


    @Autowired
    public CustomerDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName;
        Customer customer = customerRepository.findByEmail(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.getRole()));
        userName=username;

        if(customer==null){
            throw new UsernameNotFoundException("Could not find username");
        }
        if(customer.isBlocked()){
            throw new LockedException("User is blocked");
        }

        return new CustomerDetails(
                customer.getEmail(),
                customer.getPassword(),
                authorities,
                customer.getName(),
                customer.getCustomer_id(),
                customer.getMobile(),
                customer.isActivated());
    }
}
