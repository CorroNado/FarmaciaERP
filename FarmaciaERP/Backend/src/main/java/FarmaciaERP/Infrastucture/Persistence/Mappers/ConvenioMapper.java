package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvenioMapper {

    /**
     * proveedorRef y medicamentosRef (por id) deben ser referencias YA GESTIONADAS por JPA.
     */
    public static ConvenioJPA ToEntity(Convenio convenio, ProveedorJPA proveedorRef,
                                        Map<Integer, MedicamentoJPA> medicamentosRef) {
        ConvenioJPA entity = new ConvenioJPA();
        entity.setId(convenio.getId());
        entity.setNumero(convenio.getNumero());
        entity.setProveedor(proveedorRef);
        entity.setFechaInicio(convenio.getFechaInicio());
        entity.setFechaFin(convenio.getFechaFin());
        entity.setEstado(convenio.getEstado());

        List<FarmaciaERP.Infrastucture.Persistence.Entities.ItemConvenioJPA> items = convenio.getItemsPactados()
                .stream()
                .map(item -> ItemConvenioMapper.ToEntity(item, entity, medicamentosRef.get(item.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setItemsPactados(items);
        return entity;
    }

    public static Convenio ToDomain(ConvenioJPA entity) {
        Convenio convenio = new Convenio();
        convenio.setId(entity.getId());
        convenio.setNumero(entity.getNumero());
        convenio.setProveedor(ProveedorMapper.ToDomain(entity.getProveedor()));
        convenio.setFechaInicio(entity.getFechaInicio());
        convenio.setFechaFin(entity.getFechaFin());
        convenio.setEstado(entity.getEstado());
        convenio.setItemsPactados(entity.getItemsPactados().stream()
                .map(ItemConvenioMapper::ToDomain)
                .collect(Collectors.toList()));
        return convenio;
    }
}
