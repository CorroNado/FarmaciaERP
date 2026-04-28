package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

import java.util.List;
import java.util.Optional;

public class BuscarUsuarioUseCase {

    private final IUsuarioRepository usuarioRepository;

    public BuscarUsuarioUseCase (IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> porId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    public List<Usuario> todos() {
        return usuarioRepository.listarTodos();
    }

    public Optional<Usuario> porDocumento(String documento) {
        return usuarioRepository.buscarPorDocumentoIdentidad(documento);
    }

    public List<Usuario> porNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    public List<Usuario> porTipoSeguro(TipoSeguro tipoSeguro) {
        return usuarioRepository.buscarPorTipoSeguro(tipoSeguro);
    }
}
