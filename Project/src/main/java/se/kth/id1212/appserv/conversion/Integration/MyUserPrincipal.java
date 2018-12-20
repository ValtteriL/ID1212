package se.kth.id1212.appserv.conversion.Integration;

import se.kth.id1212.appserv.conversion.Model.User;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;

public class MyUserPrincipal implements UserDetails {
    private User user;
 
    public MyUserPrincipal(User user) {
        this.user = user;
    }
    //...
    public String getUsername() {
        return user.getUsername();
    }
    public String getPassword() {
        return user.getPassword();
    }

    // for simplicitys sake, dont expire or lock accounts
    public boolean isEnabled() {
        return true;
    }
    public boolean isAccountNonLocked() {
        return true;
    }
    public boolean isAccountNonExpired() {
        return true;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // only one type of users
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.asList(new DummyGrantedAuthority("USER"));
    }
}

// dummy class for implementing grantedauthority
class DummyGrantedAuthority implements GrantedAuthority {
    
    private String authority;
    
    public DummyGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }
}