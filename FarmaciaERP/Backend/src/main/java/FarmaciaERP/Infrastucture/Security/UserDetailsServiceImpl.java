package FarmaciaERP.Infrastucture.Security;

import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Infrastucture.Persistence.Mappers.EmailMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IUsuarioJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUsuarioJPARepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailContainingIgnoreCase(username)
                .stream().map(usuario -> User.builder()
                        .username(EmailMapper.toDomain(usuario.getEmail()).getEmail())
                        .password(usuario.getPassword())
                        .roles(usuario.getRol().name())
                        .build()
                ).findAny().orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username ));
    }
}
