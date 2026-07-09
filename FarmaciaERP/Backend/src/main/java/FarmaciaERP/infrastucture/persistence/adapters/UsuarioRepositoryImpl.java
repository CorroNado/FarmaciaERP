package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.domain.valueObjects.Telephone;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.PasswordEmb;
import FarmaciaERP.infrastucture.persistence.entities.UserJPA;
import FarmaciaERP.infrastucture.persistence.mappers.UserMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IUserJPARepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private final IUserJPARepository jpaRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(User saver) {
        UserJPA jpa = userMapper.toEntity(saver);
        if (saver.getId() != null) {
            jpaRepository.findById(saver.getId()).ifPresent(entityPersistente -> {
                entityPersistente.setUsername(jpa.getUsername());
                entityPersistente.setUserPassword(jpa.getUserPassword());
                entityPersistente.setNombreCompleto(jpa.getNombreCompleto());
                entityPersistente.setPerfil(jpa.getPerfil());
                entityPersistente.setLoginSeguro(jpa.getLoginSeguro());
                jpaRepository.save(entityPersistente);
            });
        } else {
            String encrypt = passwordEncoder.encode(jpa.getUserPassword().getValor());
            jpa.setUserPassword(new PasswordEmb(encrypt));
            jpaRepository.save(jpa);
        }
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
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(userMapper::toDomain);
    }
    @Override
    public List<User> findByStatus(UserStatus estado) {

        return jpaRepository.findByEstado(estado.name()).stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByFullName(FullName fullName) {
        return jpaRepository.findByNombres(fullName.getValor())
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByProfile(Long perfilId){
        return jpaRepository.findByProfile(perfilId)
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<User> findByUsername(Username username){
        return jpaRepository.findByUsername(username.getValor())
                .map(userMapper::toDomain);
    }
    @Override
    public List<User> findByTelephone(Telephone telefono){
        return jpaRepository.findByTelephone(telefono.getNumeroCompleto())
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByDepartment(Long departamentoId){
        return jpaRepository.findByDepartment(departamentoId)
                .stream().map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByProvince(Long provinciaId){
        return jpaRepository.findByProvince(provinciaId)
                .stream().map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> findByDistrict(Long distritoId){
        return jpaRepository.findByDistrict(distritoId)
                .stream().map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<User> findByEmail(EmailAddress emailAddress) {
        return jpaRepository.findByEmail(emailAddress.getDireccion()).stream()
                .map(userMapper::toDomain).findFirst();
    }
}
