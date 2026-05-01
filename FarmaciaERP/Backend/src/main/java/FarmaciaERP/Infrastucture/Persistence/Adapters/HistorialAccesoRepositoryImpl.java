package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.HistorialAcceso;
import FarmaciaERP.Domain.Repositories.IHistorialAccesoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.HistorialAccesoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.HistorialAccesoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IHistorialAccesoJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistorialAccesoRepositoryImpl implements IHistorialAccesoRepository {
    private final IHistorialAccesoJPARepository historialAccesoJPARepository;

    public HistorialAccesoRepositoryImpl(IHistorialAccesoJPARepository historialAccesoJPARepository) {
        this.historialAccesoJPARepository = historialAccesoJPARepository;
    }

    @Override
    public HistorialAcceso save(HistorialAcceso historial) {
        HistorialAccesoJPA saved = HistorialAccesoMapper.toEntity(historial);
        historialAccesoJPARepository.save(saved);
        return HistorialAccesoMapper.toDomain(saved);
    }

    @Override
    public List<HistorialAcceso> findAll() {
        return historialAccesoJPARepository.findAll()
                .stream()
                .map(HistorialAccesoMapper::toDomain)
                .toList();
    }

    @Override
    public List<HistorialAcceso> findByUsuarioId(Long usuarioId) {
        return historialAccesoJPARepository.findByUsuario_Id(usuarioId)
                .stream()
                .map(HistorialAccesoMapper::toDomain)
                .toList();
    }
}
