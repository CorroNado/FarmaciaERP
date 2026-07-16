package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Pago;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPagoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PagoJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.PagoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IConciliacionTresViasJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IFacturaMIROJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPagoJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PagoRepositoryImpl implements IPagoRepository {

    private final IPagoJPARepository jpaRepository;
    private final IFacturaMIROJPARepository facturaMIROJPARepository;
    private final IConciliacionTresViasJPARepository conciliacionTresViasJPARepository;

    public PagoRepositoryImpl(IPagoJPARepository jpaRepository,
                               IFacturaMIROJPARepository facturaMIROJPARepository,
                               IConciliacionTresViasJPARepository conciliacionTresViasJPARepository) {
        this.jpaRepository = jpaRepository;
        this.facturaMIROJPARepository = facturaMIROJPARepository;
        this.conciliacionTresViasJPARepository = conciliacionTresViasJPARepository;
    }

    @Override
    public Pago save(Pago pago) {
        FacturaMIROJPA facturaMIRORef = facturaMIROJPARepository.findById(pago.getFacturaMIRO().getId())
                .orElseThrow(() -> new BadRequestException("Factura (MIRO) no encontrada"));
        ConciliacionTresViasJPA conciliacionRef = conciliacionTresViasJPARepository
                .findById(pago.getConciliacionTresVias().getId())
                .orElseThrow(() -> new BadRequestException("Conciliación de 3 vías (MRBR) no encontrada"));

        PagoJPA entity = PagoMapper.ToEntity(pago, facturaMIRORef, conciliacionRef);
        PagoJPA saved = jpaRepository.save(entity);
        return PagoMapper.ToDomain(saved);
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return jpaRepository.findById(id).map(PagoMapper::ToDomain);
    }

    @Override
    public List<Pago> findAll() {
        return jpaRepository.findAll().stream().map(PagoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Pago> findByFacturaMIROId(Long facturaMIROId) {
        return jpaRepository.findByFacturaMIRO_Id(facturaMIROId).stream().map(PagoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByFacturaMIROId(Long facturaMIROId) {
        return jpaRepository.existsByFacturaMIRO_Id(facturaMIROId);
    }
}
