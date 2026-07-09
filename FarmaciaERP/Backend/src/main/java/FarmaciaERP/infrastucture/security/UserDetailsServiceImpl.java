package FarmaciaERP.infrastucture.security;

import FarmaciaERP.domain.enums.PermissionStatus;
import FarmaciaERP.infrastucture.persistence.repositories.IUserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserJPARepository usuarioRepository;

    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado"));
        List<SimpleGrantedAuthority> authorities = usuario.getPerfil()
                .getPermisos()
                .stream()
                .filter(p -> p.getEstado() == PermissionStatus.ACTIVO) // solo permisos activos
                .map(p -> new SimpleGrantedAuthority(p.getCodigo()))
                .toList();
        return new CustomUserDetails(
                (long) usuario.getUserId(),
                usuario.getUsername().getValor(),
                usuario.getUserPassword().getValor(),
                authorities
        );
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado"));
        List<SimpleGrantedAuthority> authorities = usuario.getPerfil()
                .getPermisos()
                .stream()
                .filter(p -> p.getEstado() == PermissionStatus.ACTIVO) // solo permisos activos
                .map(p -> new SimpleGrantedAuthority(p.getCodigo()))
                .toList();
        return new CustomUserDetails(
                usuario.getUserId(),
                usuario.getUsername().getValor(),
                usuario.getUserPassword().getValor(),
                authorities
        );
    }
}
