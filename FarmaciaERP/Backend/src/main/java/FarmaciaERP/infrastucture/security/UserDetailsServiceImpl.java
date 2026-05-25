package FarmaciaERP.infrastucture.security;

import FarmaciaERP.infrastucture.persistence.repositories.IUserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserJPARepository usuarioRepository;

    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado"));

        return new CustomUserDetails(
                (long) usuario.getId(),
                usuario.getEmail().getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
    @Override
    public UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByEmail_Email(username)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado"));
        return new CustomUserDetails(
                usuario.getId(),
                usuario.getEmail().getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
