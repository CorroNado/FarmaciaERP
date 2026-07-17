package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;

public class EmpleadoMapper {

    public static EmpleadoJPA ToEntity(Empleado empleado) {
        EmpleadoJPA entity = new EmpleadoJPA();
        if (empleado.getId() != null) {
            entity.setId(empleado.getId());
        }
        entity.setCodigo(empleado.getCodigo());
        entity.setApellidoPaterno(empleado.getApellidoPaterno());
        entity.setApellidoMaterno(empleado.getApellidoMaterno());
        entity.setNombres(empleado.getNombres());
        entity.setDni(empleado.getDni());
        entity.setRol(empleado.getRol());
        entity.setArea(empleado.getArea());
        entity.setFechaIngreso(empleado.getFechaIngreso());
        entity.setSalario(empleado.getSalario());
        entity.setContrato(empleado.getContrato());
        entity.setCorreo(empleado.getCorreo());
        entity.setTelefono(empleado.getTelefono());
        entity.setEstado(empleado.getEstado());
        entity.setBajaProgramadaFechaEfectiva(empleado.getBajaProgramadaFechaEfectiva());
        entity.setBajaProgramadaObservacion(empleado.getBajaProgramadaObservacion());
        entity.setBajaProgramadaTurnoInfo(empleado.getBajaProgramadaTurnoInfo());
        return entity;
    }

    public static Empleado ToDomain(EmpleadoJPA entity) {
        Empleado empleado = new Empleado();
        empleado.setId(entity.getId());
        empleado.setCodigo(entity.getCodigo());
        empleado.setApellidoPaterno(entity.getApellidoPaterno());
        empleado.setApellidoMaterno(entity.getApellidoMaterno());
        empleado.setNombres(entity.getNombres());
        empleado.setDni(entity.getDni());
        empleado.setRol(entity.getRol());
        empleado.setArea(entity.getArea());
        empleado.setFechaIngreso(entity.getFechaIngreso());
        empleado.setSalario(entity.getSalario());
        empleado.setContrato(entity.getContrato());
        empleado.setCorreo(entity.getCorreo());
        empleado.setTelefono(entity.getTelefono());
        empleado.setEstado(entity.getEstado());
        empleado.setBajaProgramadaFechaEfectiva(entity.getBajaProgramadaFechaEfectiva());
        empleado.setBajaProgramadaObservacion(entity.getBajaProgramadaObservacion());
        empleado.setBajaProgramadaTurnoInfo(entity.getBajaProgramadaTurnoInfo());
        return empleado;
    }
}
