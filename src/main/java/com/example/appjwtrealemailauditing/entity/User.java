package com.example.appjwtrealemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false,length = 50)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    @Column(unique = true,nullable = false)
    private String email;//userning emaili username sifatida ishlatilgan

    @Column
    @CreationTimestamp
    private Timestamp createdAt;//qachon ro'yhatda o'tganligi
    @UpdateTimestamp
    private Timestamp updatedAt;//oxirgi marta qachon tahrirlanganligi
    @ManyToMany
    private List<Role> roleList;
    @NotNull
    private String password;

    private boolean AccountNonExpired=true;
    private boolean AccountNonLocked=true;
    private  boolean CredentialsNonExpired=true;
    private boolean enabled=true;
    private String emailCode;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.AccountNonExpired;
    }


    //Accountning blocklangan yoki blocklanmaganligini qaytaradi
    @Override
    public boolean isAccountNonLocked() {
        return this.AccountNonLocked;
    }


    //accountning  ishonchlilik muddati tugagan yoki tugamaganligini qaytaradi
    @Override
    public boolean isCredentialsNonExpired() {
        return this.CredentialsNonExpired;
    }

    //accountning o'chiq yoki yoniqligini qaytaradi
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
