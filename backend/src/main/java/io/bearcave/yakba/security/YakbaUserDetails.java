package io.bearcave.yakba.security;

import io.bearcave.yakba.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class YakbaUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    private YakbaUserDetails(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
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

    public static UserDetails from(User dbUser) {
        return new YakbaUserDetails(
                dbUser.getUsername(),
                dbUser.getPassword(),
                dbUser.isEnabled());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
