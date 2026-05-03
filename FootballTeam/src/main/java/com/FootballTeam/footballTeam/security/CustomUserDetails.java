package com.FootballTeam.footballTeam.security;

import com.FootballTeam.footballTeam.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final User user;

    // Costruttore con Attributi
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Metodo per definire i ruoli
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    // Metodo per ottenere la password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Metodo per ottenere l'username
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Metodi per la gestione dell Account Status: Expired, Locked, Expired Credentials, Enabled
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
