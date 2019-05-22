package io.bearcave.yakba.security;

import io.bearcave.yakba.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class YakbaUserDetails implements UserDetails {

    private String id;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private YakbaUserDetails(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public static UserDetails from(User dbUser) {
        return new YakbaUserDetails(
                dbUser.getId(),
                dbUser.getPassword());
    }
}
