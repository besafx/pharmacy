package com.besafx.app.auditing;


import com.besafx.app.entity.Person;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PersonAwareUserDetails implements UserDetails {

    @Getter
    private final Person person;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public PersonAwareUserDetails(Person person) {
        this(person, new ArrayList<>());
    }

    public PersonAwareUserDetails(Person person, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.person = person;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getEmail();
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
        return this.person.getEnabled();
    }
}
