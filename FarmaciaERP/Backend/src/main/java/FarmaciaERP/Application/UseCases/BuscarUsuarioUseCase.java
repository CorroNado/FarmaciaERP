package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BuscarUsuarioUseCase {

    private final IUsuarioRepository usuarioRepository;

    public BuscarUsuarioUseCase (IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }


    public List<Usuario> porNombre(FullName nombre) {
        return usuarioRepository.findByName(nombre);
    }

    public List<Usuario> porEstado (UsuarioEstados estado) {
        return usuarioRepository.findByStatus(estado);
    }

    public Optional<Usuario> porEmail(Email email) {
        return usuarioRepository.findByEmail(email);
    }
}
