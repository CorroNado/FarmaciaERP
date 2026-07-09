package FarmaciaERP.application.dto.Request;

import java.util.List;

public record ActualizarUsuarioRequest(
        String username,
        String nombre,
        String apellido,
        Long perfilId,
        List<EmailDto> emails,
        List<AddressDto> direcciones,
        List<TelephoneDto> telefonos
) {
    public record EmailDto(Long id, String email, String etiqueta, String estado) {}
    public record AddressDto(Long id, String descripcion, String etiqueta, Long distritoId, String estado) {}
    public record TelephoneDto(String numeroCompleto, String tipo, String descripcion) {}
}


