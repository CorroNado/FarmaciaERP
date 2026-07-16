package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.InspeccionCalidad;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IInspeccionCalidadRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.InspeccionCalidadJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.InspeccionCalidadMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IEntradaMercanciaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IInspeccionCalidadJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InspeccionCalidadRepositoryImpl implements IInspeccionCalidadRepository {

    private final IInspeccionCalidadJPARepository jpaRepository;
    private final IEntradaMercanciaJPARepository entradaMercanciaJPARepository;

    public InspeccionCalidadRepositoryImpl(IInspeccionCalidadJPARepository jpaRepository,
                                            IEntradaMercanciaJPARepository entradaMercanciaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.entradaMercanciaJPARepository = entradaMercanciaJPARepository;
    }

    @Override
    public InspeccionCalidad save(InspeccionCalidad inspeccionCalidad) {
        EntradaMercanciaJPA entradaMercanciaRef = entradaMercanciaJPARepository
                .findById(inspeccionCalidad.getEntradaMercancia().getId())
                .orElseThrow(() -> new BadRequestException("Entrada de mercancía (MIGO) no encontrada"));

        InspeccionCalidadJPA entity = InspeccionCalidadMapper.ToEntity(inspeccionCalidad, entradaMercanciaRef);
        InspeccionCalidadJPA saved = jpaRepository.save(entity);
        return InspeccionCalidadMapper.ToDomain(saved);
    }

    @Override
    public Optional<InspeccionCalidad> findById(Long id) {
        return jpaRepository.findById(id).map(InspeccionCalidadMapper::ToDomain);
    }

    @Override
    public List<InspeccionCalidad> findAll() {
        return jpaRepository.findAll().stream().map(InspeccionCalidadMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<InspeccionCalidad> findByEntradaMercanciaId(Long entradaMercanciaId) {
        return jpaRepository.findByEntradaMercancia_Id(entradaMercanciaId).stream()
                .map(InspeccionCalidadMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByEntradaMercanciaId(Long entradaMercanciaId) {
        return jpaRepository.existsByEntradaMercancia_Id(entradaMercanciaId);
    }
}
