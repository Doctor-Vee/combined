package com.reloadly.paypro.combined.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reloadly.paypro.combined.persistence.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
public class AuthenticatedUserDetails implements UserDetails {
//    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    private String accountNumber;

    @JsonIgnore
    private String password;


    public static AuthenticatedUserDetails build(User user) {
        return new AuthenticatedUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getAccountNumber(), user.getPassword());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatedUserDetails user = (AuthenticatedUserDetails) o;
        return Objects.equals(id, user.id);
    }
}
