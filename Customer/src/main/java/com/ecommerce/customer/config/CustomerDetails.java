package com.ecommerce.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class CustomerDetails extends org.springframework.security.core.userdetails.User implements Serializable {

    private String name;
    private long customer_id;

    private Long mobile;

    private boolean is_activated;

    public CustomerDetails(String email, String password, Collection<? extends GrantedAuthority> authorities,
                           String name,long customer_id, Long mobile,boolean is_activated) {
        super(email, password, authorities);
        this.name=name;
        this.customer_id=customer_id;
        this.mobile = mobile;
        this.is_activated= is_activated;
    }
}
