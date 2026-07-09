package FarmaciaERP.application.dto.Request;

public record CrearUsuarioRequest(
        Long perfilId,
        String username,
        String password,
        String nombre,
        String apellido,
        String email
) {}
