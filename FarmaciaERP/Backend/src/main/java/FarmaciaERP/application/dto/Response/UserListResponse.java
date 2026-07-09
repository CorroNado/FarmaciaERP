package FarmaciaERP.application.dto.Response;

import java.time.LocalDateTime;

public record UserListResponse(
        Long id,
        String username,
        String fullName,
        Long perfil,
        String estado,
        LocalDateTime createdAt
) {}
