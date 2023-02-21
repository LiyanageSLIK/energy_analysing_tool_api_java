package com.greenbill.greenbill.entity;

import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean verified;

    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TokenEntity token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();


    @Transient
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean checkPassword(String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public UserEntity(UserRegisterDto userRegisterDto) {
        this.firstName= userRegisterDto.getFirstName();
        this.lastName= userRegisterDto.getLastName();
        this.email= userRegisterDto.getEmail();
        this.setPassword(userRegisterDto.getPassword());
        this.setRole(Role.USER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return getEmail();
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
}
