package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.infrastucture.persistence.entities.UsuarioJPA;
import FarmaciaERP.infrastucture.persistence.mappers.UsuarioMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IUserJPARepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private IUserJPARepository jpaRepository;

    public UsuarioRepositoryImpl(IUserJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {

        UsuarioJPA usuarioJPA = UsuarioMapper.ToEntity(user);

        UsuarioJPA guardadoJPA = jpaRepository.save(usuarioJPA);
        return UsuarioMapper.ToDomain(guardadoJPA);
    }
    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UsuarioMapper::ToDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(UsuarioMapper::ToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<User> findByStatus(UserStatus estado) {

        return jpaRepository.findByEstado(estado.name()).stream()
                .map(UsuarioMapper::ToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByName(FullName fullName) {

        return jpaRepository.findByNombres_Value(fullName.getValue()).stream()
                .map(UsuarioMapper::ToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<User> findByEmail(EmailContact emailContact) {
        System.out.printf("UsuarioRepositoryImpl.findByEmail(%s): ", emailContact);
        return jpaRepository.findByEmail_Email(emailContact.getDireccion()).stream()
                .map(UsuarioMapper::ToDomain).findFirst();
    }
}
