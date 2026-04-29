package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.FullnameMapper;
import FarmaciaERP.Infrastucture.Persistence.Mappers.UsuarioMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IUsuarioJPARepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private IUsuarioJPARepository jpaRepository;

    public UsuarioRepositoryImpl(IUsuarioJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {

        UsuarioJPA usuarioJPA = UsuarioMapper.ToEntity(usuario);

        UsuarioJPA guardadoJPA = jpaRepository.save(usuarioJPA);
        return UsuarioMapper.ToDomain(guardadoJPA);
    }
    @Override
    public Optional<Usuario> findById(int id) {
        return jpaRepository.findById(id).map(UsuarioMapper::ToDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(UsuarioMapper::ToDomain)
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
                .map(UsuarioMapper::ToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Usuario> findByName(FullName Nombre) {

        return jpaRepository.findByNombresContainingIgnoreCase(FullnameMapper.toEmbeddable(Nombre)).stream()
                .map(UsuarioMapper::ToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Usuario> findByEmail(Email email) {
        return jpaRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(UsuarioMapper::ToDomain).findFirst();
    }
}
