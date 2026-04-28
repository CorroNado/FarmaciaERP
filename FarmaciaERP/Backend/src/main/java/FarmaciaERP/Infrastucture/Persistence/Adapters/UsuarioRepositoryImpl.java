package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IUsuarioJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryImpl implements IUsuarioRepository {
    @Autowired
    private IUsuarioJPARepository jpaRepository;

    @Override
    public Usuario save(Usuario usuario) {
        String estadoString = (usuario.getEstado() != null) ? usuario.getEstado().name() : null;

        UsuarioJPA usuarioJPA = new UsuarioJPA(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                estadoString,
                usuario.getRegistro()
        );

        UsuarioJPA guardadoJPA = jpaRepository.save(usuarioJPA);
        return mapToDomain(guardadoJPA);
    }



    @Override
    public Optional<Usuario> findById(int id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Usuario> findByStatus(UsuarioEstados estado) {

        return jpaRepository.findByEstado(estado.name()).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }



    private Usuario mapToDomain(UsuarioJPA jpa) {
        Usuario usuario = new Usuario();
        usuario.setId(jpa.getId());
        usuario.setNombre(jpa.getNombre());
        usuario.setEmail(jpa.getEmail());
        usuario.setPassword(jpa.getPassword());

        if (jpa.getEstado() != null) {
            usuario.setEstado(UsuarioEstados.valueOf(jpa.getEstado()));
        }

        usuario.setRegistro(jpa.getRegistro());
        return usuario;
    }
}
