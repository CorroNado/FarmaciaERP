package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.MedicamentoCategoria;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Medicamento {
    private int id;
    private String nombre;
    private String presentacion;
    private double precio;
    private int stock;
    private MedicamentoCategoria categoria;
    private LocalDate fechaVencimiento;

    public Medicamento() {}

    public Medicamento(int id, String nombre, String presentacion, double precio,
                       int stock, MedicamentoCategoria categoria, LocalDate fechaVencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }
    public boolean requiereReceta(){
        return  categoria.requiereReceta();
    }
    public boolean estaDisponible(){
        return stock > 0 && !estaVencido();
    }

}

