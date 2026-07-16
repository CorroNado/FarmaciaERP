package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Enums.MedicamentoCategoria;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarMedicamentoUseCase {

    private final IMedicamentoRepository medicamentoRepository;

    public BuscarMedicamentoUseCase(IMedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public Optional<Medicamento> porId(int id) {
        return medicamentoRepository.findById(id);
    }

    public List<Medicamento> todos() {
        return medicamentoRepository.findAll();
    }

    public List<Medicamento> porNombre(String nombre) {
        return medicamentoRepository.findByNombre(nombre);
    }

    public List<Medicamento> porCategoria(MedicamentoCategoria categoria) {
        return medicamentoRepository.findByCategoria(categoria);
    }
}
