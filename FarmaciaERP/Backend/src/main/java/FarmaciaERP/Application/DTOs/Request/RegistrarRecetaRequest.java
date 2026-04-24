package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Application.DTOs.shared.ItemRecetaDTO;
import lombok.Data;

import java.util.List;

@Data
public class RegistrarRecetaRequest {
    private int IdMedico;
    private int IdPaciente;
    private List<ItemRecetaDTO> items;

}