<#
.SYNOPSIS
    Generador Sprint 2 — Módulo Contable FarmaciaERP (Asientos Contables)
.DESCRIPTION
    Crea las entidades, repositorios, adaptadores, casos de uso y controladores
    necesarios para gestionar el Libro Diario (Asientos Contables y Líneas),
    incluyendo la validación de partida doble.

    USO: Ejecutar desde la carpeta Backend/ del proyecto (donde está pom.xml)
         .\generar_asientos_contables.ps1
#>

$ErrorActionPreference = "Stop"
$OutputEncoding = [System.Text.Encoding]::UTF8

$BASE = "src\main\java\FarmaciaERP"

if (-not (Test-Path "pom.xml")) {
    Write-Host "ERROR: no se encontró pom.xml en el directorio actual." -ForegroundColor Red
    Write-Host "Ejecuta este script desde la carpeta Backend/ del proyecto." -ForegroundColor Red
    exit 1
}

$script:Created = 0
$script:Skipped = 0

function Write-File {
    param (
        [Parameter(Mandatory=$true)] [string]$Path,
        [Parameter(Mandatory=$true)] [string]$Content
    )

    if (Test-Path $Path) {
        Write-Host "  [SKIP] ya existe: $Path" -ForegroundColor Yellow
        $script:Skipped++
    } else {
        $parentDir = Split-Path -Parent $Path
        if (-not (Test-Path $parentDir)) {
            New-Item -ItemType Directory -Path $parentDir -Force | Out-Null
        }
        # Escribir en UTF-8 sin BOM para compatibilidad con Java
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($Path, $Content.Trim(), $utf8NoBom)
        Write-Host "  [OK]   $Path" -ForegroundColor Green
        $script:Created++
    }
}

Write-Host "=== Generando Motor de Asientos Contables (Libro Diario) ===" -ForegroundColor Cyan

# ---------------------------------------------------------------------------
# ENUMS
# ---------------------------------------------------------------------------

Write-File "$BASE\Domain\Enums\TipoAsiento.java" @"
package FarmaciaERP.Domain.Enums;

/**
 * Clasificación del asiento o voucher contable.
 */
public enum TipoAsiento {
    INGRESO,
    EGRESO,
    DIARIO,
    APERTURA,
    CIERRE
}
"@

Write-File "$BASE\Domain\Enums\EstadoAsiento.java" @"
package FarmaciaERP.Domain.Enums;

/**
 * Ciclo de vida del asiento contable en el Libro Diario.
 */
public enum EstadoAsiento {
    BORRADOR,
    CONTABILIZADO,
    ANULADO
}
"@

# ---------------------------------------------------------------------------
# DOMAIN ENTITIES
# ---------------------------------------------------------------------------

Write-File "$BASE\Domain\Entities\LineaAsiento.java" @"
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Detalle o partida individual dentro de un Asiento Contable.
 * Vincula una subcuenta divisionaria con un monto al Debe o al Haber.
 */
@Getter
@Setter
public class LineaAsiento {

    private Long id;
    private SubcuentaDivisionaria subcuenta;
    private CentroCosto centroCosto;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;

    private LineaAsiento() {
    }

    public LineaAsiento(SubcuentaDivisionaria subcuenta, CentroCosto centroCosto,
                        BigDecimal debe, BigDecimal haber, String glosaDetalle) {
        if (subcuenta == null) {
            throw new BadRequestException("La línea del asiento debe tener una subcuenta asociada");
        }
        if (debe == null) debe = BigDecimal.ZERO;
        if (haber == null) haber = BigDecimal.ZERO;

        if (debe.compareTo(BigDecimal.ZERO) < 0 || haber.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Los montos al Debe o Haber no pueden ser negativos");
        }
        if (debe.compareTo(BigDecimal.ZERO) > 0 && haber.compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Una misma línea no puede tener montos en el Debe y en el Haber simultáneamente");
        }
        if (debe.compareTo(BigDecimal.ZERO) == 0 && haber.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("La línea debe tener un monto mayor a cero en el Debe o en el Haber");
        }

        this.subcuenta = subcuenta;
        this.centroCosto = centroCosto;
        this.debe = debe;
        this.haber = haber;
        this.glosaDetalle = glosaDetalle;
    }

    public static LineaAsiento reconstruir(Long id, SubcuentaDivisionaria subcuenta, CentroCosto centroCosto,
                                           BigDecimal debe, BigDecimal haber, String glosaDetalle) {
        LineaAsiento linea = new LineaAsiento();
        linea.id = id;
        linea.subcuenta = subcuenta;
        linea.centroCosto = centroCosto;
        linea.debe = debe;
        linea.haber = haber;
        linea.glosaDetalle = glosaDetalle;
        return linea;
    }
}
"@

Write-File "$BASE\Domain\Entities\AsientoContable.java" @"
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera del Asiento Contable (Libro Diario).
 * Contiene la lógica para validar que la transacción esté cuadrada (Partida Doble).
 */
@Getter
@Setter
public class AsientoContable {

    private Long id;
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private EstadoAsiento estado;
    private List<LineaAsiento> lineas = new ArrayList<>();

    private AsientoContable() {
    }

    public AsientoContable(String numero, LocalDate fechaContable, String glosa,
                           TipoAsiento tipoAsiento, List<LineaAsiento> lineas) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de asiento es obligatorio");
        }
        if (fechaContable == null) {
            throw new BadRequestException("La fecha contable es obligatoria");
        }
        if (glosa == null || glosa.isBlank()) {
            throw new BadRequestException("La glosa o explicación del asiento es obligatoria");
        }
        if (tipoAsiento == null) {
            throw new BadRequestException("El tipo de asiento es obligatorio");
        }
        if (lineas == null || lineas.size() < 2) {
            throw new BadRequestException("Un asiento contable debe tener al menos dos líneas para cumplir la partida doble");
        }

        this.numero = numero;
        this.fechaContable = fechaContable;
        this.glosa = glosa;
        this.tipoAsiento = tipoAsiento;
        this.estado = EstadoAsiento.BORRADOR;
        this.lineas = lineas;

        validarPartidaDoble();
    }

    public static AsientoContable reconstruir(Long id, String numero, LocalDate fechaContable, String glosa,
                                              TipoAsiento tipoAsiento, EstadoAsiento estado, List<LineaAsiento> lineas) {
        AsientoContable asiento = new AsientoContable();
        asiento.id = id;
        asiento.numero = numero;
        asiento.fechaContable = fechaContable;
        asiento.glosa = glosa;
        asiento.tipoAsiento = tipoAsiento;
        asiento.estado = estado;
        asiento.lineas = lineas;
        return asiento;
    }

    /**
     * Valida la ecuación contable fundamental: Sumatoria del Debe == Sumatoria del Haber.
     */
    public void validarPartidaDoble() {
        BigDecimal totalDebe = calcularTotalDebe();
        BigDecimal totalHaber = calcularTotalHaber();

        if (totalDebe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto total del asiento debe ser mayor a cero");
        }
        if (totalDebe.compareTo(totalHaber) != 0) {
            throw new BadRequestException(String.format(
                "Asiento descuadrado (Partida Doble): Total Debe (S/ %s) no coincide con Total Haber (S/ %s)",
                totalDebe, totalHaber));
        }
    }

    public BigDecimal calcularTotalDebe() {
        return lineas.stream()
                .map(LineaAsiento::getDebe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalHaber() {
        return lineas.stream()
                .map(LineaAsiento::getHaber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void contabilizar() {
        if (this.estado != EstadoAsiento.BORRADOR) {
            throw new BadRequestException("Solo se pueden contabilizar asientos en estado BORRADOR");
        }
        validarPartidaDoble();
        this.estado = EstadoAsiento.CONTABILIZADO;
    }

    public void anular() {
        if (this.estado == EstadoAsiento.ANULADO) {
            throw new BadRequestException("El asiento ya se encuentra anulado");
        }
        this.estado = EstadoAsiento.ANULADO;
    }
}
"@

# ---------------------------------------------------------------------------
# DOMAIN REPOSITORY INTERFACES
# ---------------------------------------------------------------------------

Write-File "$BASE\Domain\Repositories\IAsientoContableRepository.java" @"
package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Enums.EstadoAsiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAsientoContableRepository {

    AsientoContable save(AsientoContable asientoContable);

    Optional<AsientoContable> findById(Long id);

    Optional<AsientoContable> findByNumero(String numero);

    List<AsientoContable> findAll();

    List<AsientoContable> findByEstado(EstadoAsiento estado);

    List<AsientoContable> findByFechaContableBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumero(String numero);
}
"@

# ---------------------------------------------------------------------------
# JPA ENTITIES
# ---------------------------------------------------------------------------

Write-File "$BASE\Infrastucture\Persistence\Entities\LineaAsientoJPA.java" @"
package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "LineaAsiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaAsientoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asiento_contable_id", nullable = false)
    private AsientoContableJPA asientoContable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcuenta_id", nullable = false)
    private SubcuentaDivisionariaJPA subcuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_costo_id")
    private CentroCostoJPA centroCosto;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal debe;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal haber;

    @Column(length = 255)
    private String glosaDetalle;
}
"@

Write-File "$BASE\Infrastucture\Persistence\Entities\AsientoContableJPA.java" @"
package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AsientoContable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsientoContableJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numero;

    @Column(nullable = false)
    private LocalDate fechaContable;

    @Column(nullable = false, length = 500)
    private String glosa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAsiento tipoAsiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsiento estado;

    @OneToMany(mappedBy = "asientoContable", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LineaAsientoJPA> lineas = new ArrayList<>();
}
"@

# ---------------------------------------------------------------------------
# MAPPERS
# ---------------------------------------------------------------------------

Write-File "$BASE\Infrastucture\Persistence\Mappers\LineaAsientoMapper.java" @"
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LineaAsientoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

public class LineaAsientoMapper {

    public static LineaAsientoJPA ToEntity(LineaAsiento domain, AsientoContableJPA asientoRef,
                                           SubcuentaDivisionariaJPA subcuentaRef, CentroCostoJPA centroCostoRef) {
        LineaAsientoJPA entity = new LineaAsientoJPA();
        entity.setId(domain.getId());
        entity.setAsientoContable(asientoRef);
        entity.setSubcuenta(subcuentaRef);
        entity.setCentroCosto(centroCostoRef);
        entity.setDebe(domain.getDebe());
        entity.setHaber(domain.getHaber());
        entity.setGlosaDetalle(domain.getGlosaDetalle());
        return entity;
    }

    public static LineaAsiento ToDomain(LineaAsientoJPA entity) {
        return LineaAsiento.reconstruir(
                entity.getId(),
                SubcuentaDivisionariaMapper.ToDomain(entity.getSubcuenta()),
                entity.getCentroCosto() == null ? null : CentroCostoMapper.ToDomain(entity.getCentroCosto()),
                entity.getDebe(),
                entity.getHaber(),
                entity.getGlosaDetalle()
        );
    }
}
"@

Write-File "$BASE\Infrastucture\Persistence\Mappers\AsientoContableMapper.java" @"
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LineaAsientoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

import java.util.List;
import java.util.Map;

public class AsientoContableMapper {

    public static AsientoContableJPA ToEntity(AsientoContable domain,
                                              Map<Long, SubcuentaDivisionariaJPA> subcuentasMap,
                                              Map<Long, CentroCostoJPA> centrosCostoMap) {
        AsientoContableJPA entity = new AsientoContableJPA();
        entity.setId(domain.getId());
        entity.setNumero(domain.getNumero());
        entity.setFechaContable(domain.getFechaContable());
        entity.setGlosa(domain.getGlosa());
        entity.setTipoAsiento(domain.getTipoAsiento());
        entity.setEstado(domain.getEstado());

        List<LineaAsientoJPA> lineasJPA = domain.getLineas().stream().map(linea -> {
            SubcuentaDivisionariaJPA subcuentaRef = subcuentasMap.get(linea.getSubcuenta().getId());
            CentroCostoJPA centroCostoRef = linea.getCentroCosto() == null ? null : centrosCostoMap.get(linea.getCentroCosto().getId());
            return LineaAsientoMapper.ToEntity(linea, entity, subcuentaRef, centroCostoRef);
        }).toList();

        entity.getLineas().clear();
        entity.getLineas().addAll(lineasJPA);
        return entity;
    }

    public static AsientoContable ToDomain(AsientoContableJPA entity) {
        List<LineaAsiento> lineasDomain = entity.getLineas().stream()
                .map(LineaAsientoMapper::ToDomain)
                .toList();

        return AsientoContable.reconstruir(
                entity.getId(),
                entity.getNumero(),
                entity.getFechaContable(),
                entity.getGlosa(),
                entity.getTipoAsiento(),
                entity.getEstado(),
                lineasDomain
        );
    }
}
"@

# ---------------------------------------------------------------------------
# SPRING DATA JPA REPOSITORIES
# ---------------------------------------------------------------------------

Write-File "$BASE\Infrastucture\Persistence\Repositories\IAsientoContableJPARepository.java" @"
package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAsientoContableJPARepository extends JpaRepository<AsientoContableJPA, Long> {

    Optional<AsientoContableJPA> findByNumero(String numero);

    List<AsientoContableJPA> findByEstado(EstadoAsiento estado);

    List<AsientoContableJPA> findByFechaContableBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumero(String numero);
}
"@

# ---------------------------------------------------------------------------
# ADAPTERS
# ---------------------------------------------------------------------------

Write-File "$BASE\Infrastucture\Persistence\Adapters\AsientoContableRepositoryImpl.java" @"
package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.AsientoContableMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IAsientoContableJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISubcuentaDivisionariaJPARepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AsientoContableRepositoryImpl implements IAsientoContableRepository {

    private final IAsientoContableJPARepository jpaRepository;
    private final ISubcuentaDivisionariaJPARepository subcuentaJPARepository;
    private final ICentroCostoJPARepository centroCostoJPARepository;

    public AsientoContableRepositoryImpl(IAsientoContableJPARepository jpaRepository,
                                         ISubcuentaDivisionariaJPARepository subcuentaJPARepository,
                                         ICentroCostoJPARepository centroCostoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.subcuentaJPARepository = subcuentaJPARepository;
        this.centroCostoJPARepository = centroCostoJPARepository;
    }

    @Override
    public AsientoContable save(AsientoContable asientoContable) {
        Map<Long, SubcuentaDivisionariaJPA> subcuentasMap = new HashMap<>();
        Map<Long, CentroCostoJPA> centrosCostoMap = new HashMap<>();

        for (LineaAsiento linea : asientoContable.getLineas()) {
            Long subcuentaId = linea.getSubcuenta().getId();
            if (!subcuentasMap.containsKey(subcuentaId)) {
                SubcuentaDivisionariaJPA subRef = subcuentaJPARepository.findById(subcuentaId)
                        .orElseThrow(() -> new BadRequestException("Subcuenta no encontrada: " + subcuentaId));
                subcuentasMap.put(subcuentaId, subRef);
            }

            if (linea.getCentroCosto() != null) {
                Long ccId = linea.getCentroCosto().getId();
                if (!centrosCostoMap.containsKey(ccId)) {
                    CentroCostoJPA ccRef = centroCostoJPARepository.findById(ccId)
                            .orElseThrow(() -> new BadRequestException("Centro de costo no encontrado: " + ccId));
                    centrosCostoMap.put(ccId, ccRef);
                }
            }
        }

        AsientoContableJPA entity = AsientoContableMapper.ToEntity(asientoContable, subcuentasMap, centrosCostoMap);
        AsientoContableJPA saved = jpaRepository.save(entity);
        return AsientoContableMapper.ToDomain(saved);
    }

    @Override
    public Optional<AsientoContable> findById(Long id) {
        return jpaRepository.findById(id).map(AsientoContableMapper::ToDomain);
    }

    @Override
    public Optional<AsientoContable> findByNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(AsientoContableMapper::ToDomain);
    }

    @Override
    public List<AsientoContable> findAll() {
        return jpaRepository.findAll().stream().map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public List<AsientoContable> findByEstado(EstadoAsiento estado) {
        return jpaRepository.findByEstado(estado).stream().map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public List<AsientoContable> findByFechaContableBetween(LocalDate inicio, LocalDate fin) {
        return jpaRepository.findByFechaContableBetween(inicio, fin).stream()
                .map(AsientoContableMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByNumero(String numero) {
        return jpaRepository.existsByNumero(numero);
    }
}
"@

# ---------------------------------------------------------------------------
# DTOs — Request
# ---------------------------------------------------------------------------

Write-File "$BASE\Application\DTOs\Request\CrearLineaAsientoRequest.java" @"
package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearLineaAsientoRequest {
    private Long subcuentaId;
    private Long centroCostoId;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;
}
"@

Write-File "$BASE\Application\DTOs\Request\CrearAsientoContableRequest.java" @"
package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoAsiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearAsientoContableRequest {
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private List<CrearLineaAsientoRequest> lineas;
}
"@

# ---------------------------------------------------------------------------
# DTOs — Response
# ---------------------------------------------------------------------------

Write-File "$BASE\Application\DTOs\Response\LineaAsientoResponse.java" @"
package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LineaAsientoResponse {
    private Long id;
    private Long subcuentaId;
    private String subcuentaCodigo;
    private String subcuentaNombre;
    private Long centroCostoId;
    private String centroCostoCodigo;
    private String centroCostoNombre;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;
}
"@

Write-File "$BASE\Application\DTOs\Response\AsientoContableResponse.java" @"
package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsientoContableResponse {
    private Long id;
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private EstadoAsiento estado;
    private BigDecimal totalDebe;
    private BigDecimal totalHaber;
    private List<LineaAsientoResponse> lineas;
}
"@

# ---------------------------------------------------------------------------
# USE CASES + ASSEMBLERS
# ---------------------------------------------------------------------------

Write-File "$BASE\Application\UseCases\AsientoContableResponseAssembler.java" @"
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Application.DTOs.Response.LineaAsientoResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;

import java.util.List;

public class AsientoContableResponseAssembler {

    public static AsientoContableResponse toResponse(AsientoContable asiento) {
        List<LineaAsientoResponse> lineasResponse = asiento.getLineas().stream()
                .map(AsientoContableResponseAssembler::toLineaResponse)
                .toList();

        return new AsientoContableResponse(
                asiento.getId(),
                asiento.getNumero(),
                asiento.getFechaContable(),
                asiento.getGlosa(),
                asiento.getTipoAsiento(),
                asiento.getEstado(),
                asiento.calcularTotalDebe(),
                asiento.calcularTotalHaber(),
                lineasResponse
        );
    }

    private static LineaAsientoResponse toLineaResponse(LineaAsiento linea) {
        return new LineaAsientoResponse(
                linea.getId(),
                linea.getSubcuenta().getId(),
                linea.getSubcuenta().getCodigo(),
                linea.getSubcuenta().getNombre(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getId(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getCodigo(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getNombre(),
                linea.getDebe(),
                linea.getHaber(),
                linea.getGlosaDetalle()
        );
    }
}
"@

Write-File "$BASE\Application\UseCases\CrearAsientoContableUseCase.java" @"
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearAsientoContableRequest;
import FarmaciaERP.Application.DTOs.Request.CrearLineaAsientoRequest;
import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrearAsientoContableUseCase {

    private final IAsientoContableRepository asientoRepository;
    private final ISubcuentaDivisionariaRepository subcuentaRepository;
    private final ICentroCostoRepository centroCostoRepository;

    public CrearAsientoContableUseCase(IAsientoContableRepository asientoRepository,
                                       ISubcuentaDivisionariaRepository subcuentaRepository,
                                       ICentroCostoRepository centroCostoRepository) {
        this.asientoRepository = asientoRepository;
        this.subcuentaRepository = subcuentaRepository;
        this.centroCostoRepository = centroCostoRepository;
    }

    @Transactional
    public AsientoContableResponse ejecutar(CrearAsientoContableRequest request) {
        if (asientoRepository.existsByNumero(request.getNumero())) {
            throw new BadRequestException("Ya existe un asiento contable registrado con el número " + request.getNumero());
        }

        List<LineaAsiento> lineasDomain = new ArrayList<>();
        for (CrearLineaAsientoRequest lReq : request.getLineas()) {
            SubcuentaDivisionaria subcuenta = subcuentaRepository.findById(lReq.getSubcuentaId())
                    .orElseThrow(() -> new BadRequestException("Subcuenta no encontrada: " + lReq.getSubcuentaId()));

            CentroCosto centroCosto = null;
            if (lReq.getCentroCostoId() != null) {
                centroCosto = centroCostoRepository.findById(lReq.getCentroCostoId())
                        .orElseThrow(() -> new BadRequestException("Centro de costo no encontrado: " + lReq.getCentroCostoId()));
            }

            LineaAsiento linea = new LineaAsiento(subcuenta, centroCosto, lReq.getDebe(), lReq.getHaber(), lReq.getGlosaDetalle());
            lineasDomain.add(linea);
        }

        AsientoContable asiento = new AsientoContable(
                request.getNumero(),
                request.getFechaContable(),
                request.getGlosa(),
                request.getTipoAsiento(),
                lineasDomain
        );

        AsientoContable guardado = asientoRepository.save(asiento);
        return AsientoContableResponseAssembler.toResponse(guardado);
    }
}
"@

Write-File "$BASE\Application\UseCases\BuscarAsientoContableUseCase.java" @"
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuscarAsientoContableUseCase {

    private final IAsientoContableRepository asientoRepository;

    public BuscarAsientoContableUseCase(IAsientoContableRepository asientoRepository) {
        this.asientoRepository = asientoRepository;
    }

    public Optional<AsientoContableResponse> porId(Long id) {
        return asientoRepository.findById(id).map(AsientoContableResponseAssembler::toResponse);
    }

    public Optional<AsientoContableResponse> porNumero(String numero) {
        return asientoRepository.findByNumero(numero).map(AsientoContableResponseAssembler::toResponse);
    }

    public List<AsientoContableResponse> todos() {
        return asientoRepository.findAll().stream().map(AsientoContableResponseAssembler::toResponse).toList();
    }

    public List<AsientoContableResponse> porEstado(EstadoAsiento estado) {
        return asientoRepository.findByEstado(estado).stream().map(AsientoContableResponseAssembler::toResponse).toList();
    }

    public List<AsientoContableResponse> porRangoFechas(LocalDate inicio, LocalDate fin) {
        return asientoRepository.findByFechaContableBetween(inicio, fin).stream()
                .map(AsientoContableResponseAssembler::toResponse).toList();
    }
}
"@

Write-File "$BASE\Application\UseCases\ContabilizarAsientoUseCase.java" @"
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContabilizarAsientoUseCase {

    private final IAsientoContableRepository asientoRepository;

    public ContabilizarAsientoUseCase(IAsientoContableRepository asientoRepository) {
        this.asientoRepository = asientoRepository;
    }

    @Transactional
    public AsientoContableResponse ejecutar(Long id) {
        AsientoContable asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Asiento contable no encontrado: " + id));

        asiento.contabilizar();
        AsientoContable actualizado = asientoRepository.save(asiento);
        return AsientoContableResponseAssembler.toResponse(actualizado);
    }
}
"@

# ---------------------------------------------------------------------------
# CONTROLLERS
# ---------------------------------------------------------------------------

Write-File "$BASE\Presentation\Controllers\AsientoContableController.java" @"
package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearAsientoContableRequest;
import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Application.UseCases.BuscarAsientoContableUseCase;
import FarmaciaERP.Application.UseCases.ContabilizarAsientoUseCase;
import FarmaciaERP.Application.UseCases.CrearAsientoContableUseCase;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/asientos")
public class AsientoContableController {

    private final CrearAsientoContableUseCase crearAsientoUseCase;
    private final BuscarAsientoContableUseCase buscarAsientoUseCase;
    private final ContabilizarAsientoUseCase contabilizarAsientoUseCase;

    public AsientoContableController(CrearAsientoContableUseCase crearAsientoUseCase,
                                     BuscarAsientoContableUseCase buscarAsientoUseCase,
                                     ContabilizarAsientoUseCase contabilizarAsientoUseCase) {
        this.crearAsientoUseCase = crearAsientoUseCase;
        this.buscarAsientoUseCase = buscarAsientoUseCase;
        this.contabilizarAsientoUseCase = contabilizarAsientoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearAsientoContableRequest request) {
        try {
            AsientoContableResponse creado = crearAsientoUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsientoContableResponse> obtenerPorId(@PathVariable Long id) {
        return buscarAsientoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AsientoContableResponse>> obtenerTodos(
            @RequestParam(required = false) EstadoAsiento estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return ResponseEntity.ok(buscarAsientoUseCase.porRangoFechas(fechaInicio, fechaFin));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarAsientoUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarAsientoUseCase.todos());
    }

    @PostMapping("/{id}/contabilizar")
    public ResponseEntity<?> contabilizar(@PathVariable Long id) {
        try {
            AsientoContableResponse contabilizado = contabilizarAsientoUseCase.ejecutar(id);
            return ResponseEntity.ok(contabilizado);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
"@

Write-Host ""
Write-Host "=== Generación Completada ===" -ForegroundColor Cyan
Write-Host "Archivos creados: $script:Created" -ForegroundColor Green
Write-Host "Archivos saltados (ya existían): $script:Skipped" -ForegroundColor Yellow
Write-Host ""
Write-Host "Endpoints REST disponibles:" -ForegroundColor Cyan
Write-Host "  POST /api/contabilidad/asientos                     (Crear asiento en BORRADOR)"
Write-Host "  GET  /api/contabilidad/asientos                     (Listar todos o filtrar por ?estado= o ?fechaInicio=&fechaFin=)"
Write-Host "  GET  /api/contabilidad/asientos/{id}                (Ver detalle por ID)"
Write-Host "  POST /api/contabilidad/asientos/{id}/contabilizar   (Validar partida doble y cambiar a CONTABILIZADO)"
Write-Host ""
Write-Host "Compila con: .\mvnw compile" -ForegroundColor White