package FarmaciaERP.Infrastucture.Security;

import FarmaciaERP.Application.Security.CustomUserDetails;
import FarmaciaERP.Application.Services.UserDetailsServiceCustom;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IUsuarioJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsServiceCustom, UserDetailsService {

    private final IUsuarioJPARepository usuarioRepository;

    @Override
    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(
                (long) usuario.getId(),
                usuario.getEmail().getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
    public UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByEmail_Email(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new CustomUserDetails(
                usuario.getId(),
                usuario.getEmail().getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
