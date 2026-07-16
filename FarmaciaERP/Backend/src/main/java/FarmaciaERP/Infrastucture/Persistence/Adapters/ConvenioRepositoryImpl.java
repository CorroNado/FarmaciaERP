package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Domain.Enums.EstadoConvenio;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConvenioRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.ConvenioMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IConvenioJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IMedicamentoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IProveedorJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ConvenioRepositoryImpl implements IConvenioRepository {

    private final IConvenioJPARepository jpaRepository;
    private final IProveedorJPARepository proveedorJPARepository;
    private final IMedicamentoJPARepository medicamentoJPARepository;

    public ConvenioRepositoryImpl(IConvenioJPARepository jpaRepository,
                                   IProveedorJPARepository proveedorJPARepository,
                                   IMedicamentoJPARepository medicamentoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.proveedorJPARepository = proveedorJPARepository;
        this.medicamentoJPARepository = medicamentoJPARepository;
    }

    @Override
    public Convenio save(Convenio convenio) {
        ProveedorJPA proveedorRef = proveedorJPARepository.findById(convenio.getProveedor().getId())
                .orElseThrow(() -> new BadRequestException("Proveedor no encontrado: " + convenio.getProveedor().getId()));

        Map<Integer, MedicamentoJPA> medicamentosRef = convenio.getItemsPactados().stream()
                .map(item -> item.getMedicamento().getId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> medicamentoJPARepository.findById(id)
                                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + id))
                ));

        ConvenioJPA entity = ConvenioMapper.ToEntity(convenio, proveedorRef, medicamentosRef);
        ConvenioJPA saved = jpaRepository.save(entity);
        return ConvenioMapper.ToDomain(saved);
    }

    @Override
    public Optional<Convenio> findById(Long id) {
        return jpaRepository.findById(id).map(ConvenioMapper::ToDomain);
    }

    @Override
    public List<Convenio> findAll() {
        return jpaRepository.findAll().stream().map(ConvenioMapper::ToDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Convenio> findByNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(ConvenioMapper::ToDomain);
    }

    @Override
    public List<Convenio> findByProveedorId(Long proveedorId) {
        return jpaRepository.findByProveedor_Id(proveedorId).stream().map(ConvenioMapper::ToDomain).toList();
    }

    @Override
    public List<Convenio> findByEstado(EstadoConvenio estado) {
        return jpaRepository.findByEstado(estado).stream().map(ConvenioMapper::ToDomain).toList();
    }
}
