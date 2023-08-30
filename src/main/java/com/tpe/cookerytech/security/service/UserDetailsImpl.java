package com.tpe.cookerytech.security.service;

import com.tpe.cookerytech.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities; //role bilgisini almak icin yazdik grantedAuthority security nin anladigi role classi

    //user --> UserDetails
    public static UserDetailsImpl build(User user){
        List<SimpleGrantedAuthority> authorities = user.getRoles().
                stream().
                map(role -> new SimpleGrantedAuthority(role.getType().name())). // gelen rolleri simplegrant. yapisina ceviriyoruz
                        collect(Collectors.toList());
        return new UserDetailsImpl(user.getEmail(), user.getPasswordHash(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
