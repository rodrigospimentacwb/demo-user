package br.com.pepper.demouser.domains.users.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements UserDetails {
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO PEPPER Implement role logic here
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO PEPPER Implement logic later
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO PEPPER Implement logic later
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO PEPPER Implement logic later
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO PEPPER Implement logic later
        return true;
    }
}
