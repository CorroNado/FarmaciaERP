package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

import java.util.List;
import java.util.Optional;

public class BuscarUsuarioUseCase {

    private final IUsuarioRepository usuarioRepository;

    public BuscarUsuarioUseCase (IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> porId(int id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }


    public List<Usuario> porNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    public List<Usuario> porEstado (UsuarioEstados estado) {
        return usuarioRepository.findByStatus(estado);
    }
}
