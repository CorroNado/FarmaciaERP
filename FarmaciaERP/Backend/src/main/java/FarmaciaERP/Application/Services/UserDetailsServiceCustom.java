package FarmaciaERP.Application.Services;

import FarmaciaERP.Application.Security.CustomUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsServiceCustom {
    CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException;
}
