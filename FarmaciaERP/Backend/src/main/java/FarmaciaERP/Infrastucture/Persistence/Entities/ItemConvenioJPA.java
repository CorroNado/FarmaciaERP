package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ItemConvenio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemConvenioJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "convenio_id", nullable = false)
    private ConvenioJPA convenio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private MedicamentoJPA medicamento;

    @Column(name = "precio_pactado", nullable = false)
    private double precioPactado;
}
