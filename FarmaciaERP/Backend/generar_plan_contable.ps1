# ============================================================================
# Generador Sprint 1 — Módulo Contable FarmaciaERP (Versión Windows PowerShell)
# CORREGIDO: Reemplaza archivos existentes y fuerza codificación UTF-8 sin BOM (\ufeff)
#
# USO: Ejecutar desde PowerShell en la carpeta Backend/ del proyecto:
#   .\generar_plan_contable.ps1
# ============================================================================

$ErrorActionPreference = "Stop"
$BASE = "src/main/java/FarmaciaERP"

if (-not (Test-Path "pom.xml")) {
    Write-Error "ERROR: no se encontró pom.xml en el directorio actual. Ejecuta este script desde la carpeta Backend/ del proyecto."
    exit 1
}

# Crear árbol de directorios de la arquitectura limpia
$directorios = @(
    "$BASE/Domain/Enums", "$BASE/Domain/Entities", "$BASE/Domain/Repositories",
    "$BASE/Infrastucture/Persistence/Entities", "$BASE/Infrastucture/Persistence/Mappers",
    "$BASE/Infrastucture/Persistence/Repositories", "$BASE/Infrastucture/Persistence/Adapters",
    "$BASE/Application/DTOs/Request", "$BASE/Application/DTOs/Response",
    "$BASE/Application/UseCases", "$BASE/Presentation/Controllers"
)

foreach ($dir in $directorios) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
}

$script:UPDATED = 0
$script:CREATED = 0

# Función modificada: Ahora sobrescribe SIEMPRE para limpiar los archivos corruptos
function Write-File ($path, $content) {
    $parent = Split-Path -Parent $path
    if (-not (Test-Path $parent)) {
        New-Item -ItemType Directory -Path $parent -Force | Out-Null
    }

    # Detectamos si estamos creando desde cero o actualizando para darte feedback visual
    $existe = Test-Path $path

    # Configuración explícita para evitar \ufeff (UTF-8 sin BOM)
    $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($path, $content.Trim(), $utf8NoBom)

    if ($existe) {
        Write-Host "  [UPDATED] $path (sobrescrito y corregido)" -ForegroundColor Cyan
        $script:UPDATED++
    } else {
        Write-Host "  [CREATED] $path" -ForegroundColor Green
        $script:CREATED++
    }
}

Write-Host "=== Generando y actualizando Plan de Cuentas y Plan de Costos ===" -ForegroundColor Cyan

# ---------------------------------------------------------------------------
# ENUMS
# ---------------------------------------------------------------------------

Write-File "$BASE/Domain/Enums/TipoCuenta.java" @'
package FarmaciaERP.Domain.Enums;

/**
 * Clasificación de la cuenta contable según el elemento del balance /
 * estado de resultados al que pertenece (PCGE).
 */
public enum TipoCuenta {
    ACTIVO,
    PASIVO,
    PATRIMONIO,
    INGRESO,
    GASTO,
    COSTO
}
'@

Write-File "$BASE/Domain/Enums/NaturalezaCuenta.java" @'
package FarmaciaERP.Domain.Enums;

/**
 * Naturaleza contable: define si la cuenta aumenta con el DEBE (deudora)
 * o con el HABER (acreedora). Se usa para validar la cuadratura de los
 * asientos del Libro Diario.
 */
public enum NaturalezaCuenta {
    DEUDORA,
    ACREEDORA
}
'@

# ---------------------------------------------------------------------------
# DOMAIN ENTITIES
# ---------------------------------------------------------------------------

Write-File "$BASE/Domain/Entities/Cuenta.java" @'
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Cuentas · nivel Cuenta (PCGE). Ej: "60 Compras", "70 Ventas".
 * Las SubcuentaDivisionaria cuelgan de una Cuenta.
 */
@Getter
@Setter
public class Cuenta {

    private Long id;
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
    private boolean activa;

    private Cuenta() {
    }

    public Cuenta(String codigo, String nombre, TipoCuenta tipoCuenta, NaturalezaCuenta naturaleza) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de la cuenta es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la cuenta es obligatorio");
        }
        if (tipoCuenta == null) {
            throw new BadRequestException("El tipo de cuenta es obligatorio");
        }
        if (naturaleza == null) {
            throw new BadRequestException("La naturaleza de la cuenta es obligatoria");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoCuenta = tipoCuenta;
        this.naturaleza = naturaleza;
        this.activa = true;
    }

    public static Cuenta reconstruir(Long id, String codigo, String nombre, TipoCuenta tipoCuenta,
                                      NaturalezaCuenta naturaleza, boolean activa) {
        Cuenta cuenta = new Cuenta();
        cuenta.id = id;
        cuenta.codigo = codigo;
        cuenta.nombre = nombre;
        cuenta.tipoCuenta = tipoCuenta;
        cuenta.naturaleza = naturaleza;
        cuenta.activa = activa;
        return cuenta;
    }

    public void desactivar() {
        this.activa = false;
    }
}
'@

Write-File "$BASE/Domain/Entities/SubcuentaDivisionaria.java" @'
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Cuentas · nivel Subcuenta Divisionaria (PCGE). Ej: "601
 * Mercaderías" cuelga de la Cuenta "60 Compras". RN: el código debe
 * iniciar con el código de la cuenta padre (jerarquía PCGE).
 */
@Getter
@Setter
public class SubcuentaDivisionaria {

    private Long id;
    private String codigo;
    private String nombre;
    private Cuenta cuenta;
    private boolean activa;

    private SubcuentaDivisionaria() {
    }

    public SubcuentaDivisionaria(String codigo, String nombre, Cuenta cuenta) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de la subcuenta divisionaria es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la subcuenta divisionaria es obligatorio");
        }
        if (cuenta == null) {
            throw new BadRequestException("La subcuenta divisionaria debe pertenecer a una cuenta");
        }
        if (!codigo.startsWith(cuenta.getCodigo())) {
            throw new BadRequestException(
                    "El código " + codigo + " debe iniciar con el código de la cuenta padre (" + cuenta.getCodigo() + ")");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuenta = cuenta;
        this.activa = true;
    }

    public static SubcuentaDivisionaria reconstruir(Long id, String codigo, String nombre, Cuenta cuenta,
                                                      boolean activa) {
        SubcuentaDivisionaria subcuenta = new SubcuentaDivisionaria();
        subcuenta.id = id;
        subcuenta.codigo = codigo;
        subcuenta.nombre = nombre;
        subcuenta.cuenta = cuenta;
        subcuenta.activa = activa;
        return subcuenta;
    }

    public void desactivar() {
        this.activa = false;
    }
}
'@

Write-File "$BASE/Domain/Entities/CentroCosto.java" @'
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Costos · Centro de Costo. Agrupa el gasto/costo por área o
 * sucursal. La sucursal es opcional (hay centros de costo transversales,
 * ej. "Administración Central").
 */
@Getter
@Setter
public class CentroCosto {

    private Long id;
    private String codigo;
    private String nombre;
    private Sucursal sucursal;
    private boolean activo;

    private CentroCosto() {
    }

    public CentroCosto(String codigo, String nombre, Sucursal sucursal) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código del centro de costo es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre del centro de costo es obligatorio");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.sucursal = sucursal;
        this.activo = true;
    }

    public static CentroCosto reconstruir(Long id, String codigo, String nombre, Sucursal sucursal,
                                           boolean activo) {
        CentroCosto centroCosto = new CentroCosto();
        centroCosto.id = id;
        centroCosto.codigo = codigo;
        centroCosto.nombre = nombre;
        centroCosto.sucursal = sucursal;
        centroCosto.activo = activo;
        return centroCosto;
    }

    public void desactivar() {
        this.activo = false;
    }
}
'@

Write-File "$BASE/Domain/Entities/PartidaPresupuestal.java" @'
package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Partida presupuestal asociada a un centro de costo, usada para comparar
 * presupuesto vs. ejecución real.
 */
@Getter
@Setter
public class PartidaPresupuestal {

    private Long id;
    private String codigo;
    private String nombre;
    private CentroCosto centroCosto;
    private BigDecimal montoPresupuestado;
    private boolean activa;

    private PartidaPresupuestal() {
    }

    public PartidaPresupuestal(String codigo, String nombre, CentroCosto centroCosto,
                                BigDecimal montoPresupuestado) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de la partida presupuestal es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la partida presupuestal es obligatorio");
        }
        if (centroCosto == null) {
            throw new BadRequestException("La partida presupuestal debe estar asociada a un centro de costo");
        }
        if (montoPresupuestado == null || montoPresupuestado.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("El monto presupuestado no puede ser negativo");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.centroCosto = centroCosto;
        this.montoPresupuestado = montoPresupuestado;
        this.activa = true;
    }

    public static PartidaPresupuestal reconstruir(Long id, String codigo, String nombre, CentroCosto centroCosto,
                                                    BigDecimal montoPresupuestado, boolean activa) {
        PartidaPresupuestal partida = new PartidaPresupuestal();
        partida.id = id;
        partida.codigo = codigo;
        partida.nombre = nombre;
        partida.centroCosto = centroCosto;
        partida.montoPresupuestado = montoPresupuestado;
        partida.activa = activa;
        return partida;
    }

    public void desactivar() {
        this.activa = false;
    }
}
'@

# ---------------------------------------------------------------------------
# DOMAIN REPOSITORY INTERFACES
# ---------------------------------------------------------------------------

Write-File "$BASE/Domain/Repositories/ICuentaRepository.java" @'
package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cuenta;

import java.util.List;
import java.util.Optional;

public interface ICuentaRepository {

    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> findByCodigo(String codigo);

    List<Cuenta> findAll();

    List<Cuenta> findAllActivas();

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Domain/Repositories/ISubcuentaDivisionariaRepository.java" @'
package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;

import java.util.List;
import java.util.Optional;

public interface ISubcuentaDivisionariaRepository {

    SubcuentaDivisionaria save(SubcuentaDivisionaria subcuenta);

    Optional<SubcuentaDivisionaria> findById(Long id);

    Optional<SubcuentaDivisionaria> findByCodigo(String codigo);

    List<SubcuentaDivisionaria> findAll();

    List<SubcuentaDivisionaria> findByCuentaId(Long cuentaId);

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Domain/Repositories/ICentroCostoRepository.java" @'
package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.CentroCosto;

import java.util.List;
import java.util.Optional;

public interface ICentroCostoRepository {

    CentroCosto save(CentroCosto centroCosto);

    Optional<CentroCosto> findById(Long id);

    Optional<CentroCosto> findByCodigo(String codigo);

    List<CentroCosto> findAll();

    List<CentroCosto> findAllActivos();

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Domain/Repositories/IPartidaPresupuestalRepository.java" @'
package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;

import java.util.List;
import java.util.Optional;

public interface IPartidaPresupuestalRepository {

    PartidaPresupuestal save(PartidaPresupuestal partida);

    Optional<PartidaPresupuestal> findById(Long id);

    List<PartidaPresupuestal> findAll();

    List<PartidaPresupuestal> findByCentroCostoId(Long centroCostoId);

    boolean existsByCodigo(String codigo);
}
'@

# ---------------------------------------------------------------------------
# JPA ENTITIES
# ---------------------------------------------------------------------------

Write-File "$BASE/Infrastucture/Persistence/Entities/CuentaJPA.java" @'
package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cuenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NaturalezaCuenta naturaleza;

    @Column(nullable = false)
    private boolean activa;
}
'@

Write-File "$BASE/Infrastucture/Persistence/Entities/SubcuentaDivisionariaJPA.java" @'
package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SubcuentaDivisionaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcuentaDivisionariaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CuentaJPA cuenta;

    @Column(nullable = false)
    private boolean activa;
}
'@

Write-File "$BASE/Infrastucture/Persistence/Entities/CentroCostoJPA.java" @'
package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CentroCosto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CentroCostoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    private SucursalJPA sucursal;

    @Column(nullable = false)
    private boolean activo;
}
'@

Write-File "$BASE/Infrastucture/Persistence/Entities/PartidaPresupuestalJPA.java" @'
package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PartidaPresupuestal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaPresupuestalJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_costo_id", nullable = false)
    private CentroCostoJPA centroCosto;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montoPresupuestado;

    @Column(nullable = false)
    private boolean activa;
}
'@

# ---------------------------------------------------------------------------
# MAPPERS
# ---------------------------------------------------------------------------

Write-File "$BASE/Infrastucture/Persistence/Mappers/CuentaMapper.java" @'
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;

public class CuentaMapper {

    public static CuentaJPA ToEntity(Cuenta cuenta) {
        CuentaJPA entity = new CuentaJPA();
        entity.setId(cuenta.getId());
        entity.setCodigo(cuenta.getCodigo());
        entity.setNombre(cuenta.getNombre());
        entity.setTipoCuenta(cuenta.getTipoCuenta());
        entity.setNaturaleza(cuenta.getNaturaleza());
        entity.setActiva(cuenta.isActiva());
        return entity;
    }

    public static Cuenta ToDomain(CuentaJPA entity) {
        return Cuenta.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getTipoCuenta(),
                entity.getNaturaleza(),
                entity.isActiva()
        );
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Mappers/SubcuentaDivisionariaMapper.java" @'
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;

public class SubcuentaDivisionariaMapper {

    public static SubcuentaDivisionariaJPA ToEntity(SubcuentaDivisionaria subcuenta, CuentaJPA cuentaRef) {
        SubcuentaDivisionariaJPA entity = new SubcuentaDivisionariaJPA();
        entity.setId(subcuenta.getId());
        entity.setCodigo(subcuenta.getCodigo());
        entity.setNombre(subcuenta.getNombre());
        entity.setCuenta(cuentaRef);
        entity.setActiva(subcuenta.isActiva());
        return entity;
    }

    public static SubcuentaDivisionaria ToDomain(SubcuentaDivisionariaJPA entity) {
        return SubcuentaDivisionaria.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                CuentaMapper.ToDomain(entity.getCuenta()),
                entity.isActiva()
        );
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Mappers/CentroCostoMapper.java" @'
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;

public class CentroCostoMapper {

    public static CentroCostoJPA ToEntity(CentroCosto centroCosto, SucursalJPA sucursalRef) {
        CentroCostoJPA entity = new CentroCostoJPA();
        entity.setId(centroCosto.getId());
        entity.setCodigo(centroCosto.getCodigo());
        entity.setNombre(centroCosto.getNombre());
        entity.setSucursal(sucursalRef);
        entity.setActivo(centroCosto.isActivo());
        return entity;
    }

    public static CentroCosto ToDomain(CentroCostoJPA entity) {
        return CentroCosto.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getSucursal() == null ? null : SucursalMapper.ToDomain(entity.getSucursal()),
                entity.isActivo()
        );
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Mappers/PartidaPresupuestalMapper.java" @'
package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;

public class PartidaPresupuestalMapper {

    public static PartidaPresupuestalJPA ToEntity(PartidaPresupuestal partida, CentroCostoJPA centroCostoRef) {
        PartidaPresupuestalJPA entity = new PartidaPresupuestalJPA();
        entity.setId(partida.getId());
        entity.setCodigo(partida.getCodigo());
        entity.setNombre(partida.getNombre());
        entity.setCentroCosto(centroCostoRef);
        entity.setMontoPresupuestado(partida.getMontoPresupuestado());
        entity.setActiva(partida.isActiva());
        return entity;
    }

    public static PartidaPresupuestal ToDomain(PartidaPresupuestalJPA entity) {
        return PartidaPresupuestal.reconstruir(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                CentroCostoMapper.ToDomain(entity.getCentroCosto()),
                entity.getMontoPresupuestado(),
                entity.isActiva()
        );
    }
}
'@

# ---------------------------------------------------------------------------
# SPRING DATA JPA REPOSITORIES
# ---------------------------------------------------------------------------

Write-File "$BASE/Infrastucture/Persistence/Repositories/ICuentaJPARepository.java" @'
package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICuentaJPARepository extends JpaRepository<CuentaJPA, Long> {

    Optional<CuentaJPA> findByCodigo(String codigo);

    List<CuentaJPA> findByActivaTrue();

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Infrastucture/Persistence/Repositories/ISubcuentaDivisionariaJPARepository.java" @'
package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISubcuentaDivisionariaJPARepository extends JpaRepository<SubcuentaDivisionariaJPA, Long> {

    Optional<SubcuentaDivisionariaJPA> findByCodigo(String codigo);

    List<SubcuentaDivisionariaJPA> findByCuenta_Id(Long cuentaId);

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Infrastucture/Persistence/Repositories/ICentroCostoJPARepository.java" @'
package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICentroCostoJPARepository extends JpaRepository<CentroCostoJPA, Long> {

    Optional<CentroCostoJPA> findByCodigo(String codigo);

    List<CentroCostoJPA> findByActivoTrue();

    boolean existsByCodigo(String codigo);
}
'@

Write-File "$BASE/Infrastucture/Persistence/Repositories/IPartidaPresupuestalJPARepository.java" @'
package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPartidaPresupuestalJPARepository extends JpaRepository<PartidaPresupuestalJPA, Long> {

    List<PartidaPresupuestalJPA> findByCentroCosto_Id(Long centroCostoId);

    boolean existsByCodigo(String codigo);
}
'@

# ---------------------------------------------------------------------------
# ADAPTERS
# ---------------------------------------------------------------------------

Write-File "$BASE/Infrastucture/Persistence/Adapters/CuentaRepositoryImpl.java" @'
package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CuentaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICuentaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CuentaRepositoryImpl implements ICuentaRepository {

    private final ICuentaJPARepository jpaRepository;

    public CuentaRepositoryImpl(ICuentaJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        CuentaJPA entity = CuentaMapper.ToEntity(cuenta);
        CuentaJPA saved = jpaRepository.save(entity);
        return CuentaMapper.ToDomain(saved);
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return jpaRepository.findById(id).map(CuentaMapper::ToDomain);
    }

    @Override
    public Optional<Cuenta> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(CuentaMapper::ToDomain);
    }

    @Override
    public List<Cuenta> findAll() {
        return jpaRepository.findAll().stream().map(CuentaMapper::ToDomain).toList();
    }

    @Override
    public List<Cuenta> findAllActivas() {
        return jpaRepository.findByActivaTrue().stream().map(CuentaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Adapters/SubcuentaDivisionariaRepositoryImpl.java" @'
package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CuentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SubcuentaDivisionariaJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.SubcuentaDivisionariaMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICuentaJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISubcuentaDivisionariaJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubcuentaDivisionariaRepositoryImpl implements ISubcuentaDivisionariaRepository {

    private final ISubcuentaDivisionariaJPARepository jpaRepository;
    private final ICuentaJPARepository cuentaJPARepository;

    public SubcuentaDivisionariaRepositoryImpl(ISubcuentaDivisionariaJPARepository jpaRepository,
                                                ICuentaJPARepository cuentaJPARepository) {
        this.jpaRepository = jpaRepository;
        this.cuentaJPARepository = cuentaJPARepository;
    }

    @Override
    public SubcuentaDivisionaria save(SubcuentaDivisionaria subcuenta) {
        CuentaJPA cuentaRef = cuentaJPARepository.findById(subcuenta.getCuenta().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Cuenta no encontrada: " + subcuenta.getCuenta().getId()));

        SubcuentaDivisionariaJPA entity = SubcuentaDivisionariaMapper.ToEntity(subcuenta, cuentaRef);
        SubcuentaDivisionariaJPA saved = jpaRepository.save(entity);
        return SubcuentaDivisionariaMapper.ToDomain(saved);
    }

    @Override
    public Optional<SubcuentaDivisionaria> findById(Long id) {
        return jpaRepository.findById(id).map(SubcuentaDivisionariaMapper::ToDomain);
    }

    @Override
    public Optional<SubcuentaDivisionaria> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(SubcuentaDivisionariaMapper::ToDomain);
    }

    @Override
    public List<SubcuentaDivisionaria> findAll() {
        return jpaRepository.findAll().stream().map(SubcuentaDivisionariaMapper::ToDomain).toList();
    }

    @Override
    public List<SubcuentaDivisionaria> findByCuentaId(Long cuentaId) {
        return jpaRepository.findByCuenta_Id(cuentaId).stream().map(SubcuentaDivisionariaMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Adapters/CentroCostoRepositoryImpl.java" @'
package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.CentroCostoMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ISucursalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CentroCostoRepositoryImpl implements ICentroCostoRepository {

    private final ICentroCostoJPARepository jpaRepository;
    private final ISucursalJPARepository sucursalJPARepository;

    public CentroCostoRepositoryImpl(ICentroCostoJPARepository jpaRepository,
                                      ISucursalJPARepository sucursalJPARepository) {
        this.jpaRepository = jpaRepository;
        this.sucursalJPARepository = sucursalJPARepository;
    }

    @Override
    public CentroCosto save(CentroCosto centroCosto) {
        SucursalJPA sucursalRef = null;
        if (centroCosto.getSucursal() != null) {
            sucursalRef = sucursalJPARepository.findById(centroCosto.getSucursal().getId())
                    .orElseThrow(() -> new BadRequestException(
                            "Sucursal no encontrada: " + centroCosto.getSucursal().getId()));
        }

        CentroCostoJPA entity = CentroCostoMapper.ToEntity(centroCosto, sucursalRef);
        CentroCostoJPA saved = jpaRepository.save(entity);
        return CentroCostoMapper.ToDomain(saved);
    }

    @Override
    public Optional<CentroCosto> findById(Long id) {
        return jpaRepository.findById(id).map(CentroCostoMapper::ToDomain);
    }

    @Override
    public Optional<CentroCosto> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(CentroCostoMapper::ToDomain);
    }

    @Override
    public List<CentroCosto> findAll() {
        return jpaRepository.findAll().stream().map(CentroCostoMapper::ToDomain).toList();
    }

    @Override
    public List<CentroCosto> findAllActivos() {
        return jpaRepository.findByActivoTrue().stream().map(CentroCostoMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}
'@

Write-File "$BASE/Infrastucture/Persistence/Adapters/PartidaPresupuestalRepositoryImpl.java" @'
package FarmaciaERP.Infrastucture.Persistence.Adapters;

import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import FarmaciaERP.Infrastucture.Persistence.Entities.CentroCostoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PartidaPresupuestalJPA;
import FarmaciaERP.Infrastucture.Persistence.Mappers.PartidaPresupuestalMapper;
import FarmaciaERP.Infrastucture.Persistence.Repositories.ICentroCostoJPARepository;
import FarmaciaERP.Infrastucture.Persistence.Repositories.IPartidaPresupuestalJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PartidaPresupuestalRepositoryImpl implements IPartidaPresupuestalRepository {

    private final IPartidaPresupuestalJPARepository jpaRepository;
    private final ICentroCostoJPARepository centroCostoJPARepository;

    public PartidaPresupuestalRepositoryImpl(IPartidaPresupuestalJPARepository jpaRepository,
                                              ICentroCostoJPARepository centroCostoJPARepository) {
        this.jpaRepository = jpaRepository;
        this.centroCostoJPARepository = centroCostoJPARepository;
    }

    @Override
    public PartidaPresupuestal save(PartidaPresupuestal partida) {
        CentroCostoJPA centroCostoRef = centroCostoJPARepository.findById(partida.getCentroCosto().getId())
                .orElseThrow(() -> new BadRequestException(
                        "Centro de costo no encontrado: " + partida.getCentroCosto().getId()));

        PartidaPresupuestalJPA entity = PartidaPresupuestalMapper.ToEntity(partida, centroCostoRef);
        PartidaPresupuestalJPA saved = jpaRepository.save(entity);
        return PartidaPresupuestalMapper.ToDomain(saved);
    }

    @Override
    public Optional<PartidaPresupuestal> findById(Long id) {
        return jpaRepository.findById(id).map(PartidaPresupuestalMapper::ToDomain);
    }

    @Override
    public List<PartidaPresupuestal> findAll() {
        return jpaRepository.findAll().stream().map(PartidaPresupuestalMapper::ToDomain).toList();
    }

    @Override
    public List<PartidaPresupuestal> findByCentroCostoId(Long centroCostoId) {
        return jpaRepository.findByCentroCosto_Id(centroCostoId).stream()
                .map(PartidaPresupuestalMapper::ToDomain).toList();
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }
}
'@

# ---------------------------------------------------------------------------
# DTOs — Request
# ---------------------------------------------------------------------------

Write-File "$BASE/Application/DTOs/Request/CrearCuentaRequest.java" @'
package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearCuentaRequest {
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
}
'@

Write-File "$BASE/Application/DTOs/Request/CrearSubcuentaDivisionariaRequest.java" @'
package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearSubcuentaDivisionariaRequest {
    private String codigo;
    private String nombre;
    private Long cuentaId;
}
'@

Write-File "$BASE/Application/DTOs/Request/CrearCentroCostoRequest.java" @'
package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearCentroCostoRequest {
    private String codigo;
    private String nombre;
    private Long sucursalId;
}
'@

Write-File "$BASE/Application/DTOs/Request/CrearPartidaPresupuestalRequest.java" @'
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
public class CrearPartidaPresupuestalRequest {
    private String codigo;
    private String nombre;
    private Long centroCostoId;
    private BigDecimal montoPresupuestado;
}
'@

# ---------------------------------------------------------------------------
# DTOs — Response
# ---------------------------------------------------------------------------

Write-File "$BASE/Application/DTOs/Response/CuentaResponse.java" @'
package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
    private boolean activa;
}
'@

Write-File "$BASE/Application/DTOs/Response/SubcuentaDivisionariaResponse.java" @'
package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcuentaDivisionariaResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long cuentaId;
    private String cuentaCodigo;
    private boolean activa;
}
'@

Write-File "$BASE/Application/DTOs/Response/CentroCostoResponse.java" @'
package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroCostoResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long sucursalId;
    private boolean activo;
}
'@

Write-File "$BASE/Application/DTOs/Response/PartidaPresupuestalResponse.java" @'
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
public class PartidaPresupuestalResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Long centroCostoId;
    private BigDecimal montoPresupuestado;
    private boolean activa;
}
'@

# ---------------------------------------------------------------------------
# USE CASES + ASSEMBLERS
# ---------------------------------------------------------------------------

Write-File "$BASE/Application/UseCases/CuentaResponseAssembler.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;

public class CuentaResponseAssembler {

    public static CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getCodigo(),
                cuenta.getNombre(),
                cuenta.getTipoCuenta(),
                cuenta.getNaturaleza(),
                cuenta.isActiva()
        );
    }
}
'@

Write-File "$BASE/Application/UseCases/CrearCuentaUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearCuentaRequest;
import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearCuentaUseCase {

    private final ICuentaRepository cuentaRepository;

    public CrearCuentaUseCase(ICuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public CuentaResponse ejecutar(CrearCuentaRequest request) {
        if (cuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("Ya existe una cuenta registrada con el código " + request.getCodigo());
        }
        Cuenta cuenta = new Cuenta(request.getCodigo(), request.getNombre(), request.getTipoCuenta(),
                request.getNaturaleza());
        return CuentaResponseAssembler.toResponse(cuentaRepository.save(cuenta));
    }
}
'@

Write-File "$BASE/Application/UseCases/BuscarCuentaUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCuentaUseCase {

    private final ICuentaRepository cuentaRepository;

    public BuscarCuentaUseCase(ICuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Optional<CuentaResponse> porId(Long id) {
        return cuentaRepository.findById(id).map(CuentaResponseAssembler::toResponse);
    }

    public List<CuentaResponse> todas() {
        return cuentaRepository.findAll().stream().map(CuentaResponseAssembler::toResponse).toList();
    }

    public List<CuentaResponse> activas() {
        return cuentaRepository.findAllActivas().stream().map(CuentaResponseAssembler::toResponse).toList();
    }
}
'@

Write-File "$BASE/Application/UseCases/SubcuentaDivisionariaResponseAssembler.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;

public class SubcuentaDivisionariaResponseAssembler {

    public static SubcuentaDivisionariaResponse toResponse(SubcuentaDivisionaria subcuenta) {
        return new SubcuentaDivisionariaResponse(
                subcuenta.getId(),
                subcuenta.getCodigo(),
                subcuenta.getNombre(),
                subcuenta.getCuenta().getId(),
                subcuenta.getCuenta().getCodigo(),
                subcuenta.isActiva()
        );
    }
}
'@

Write-File "$BASE/Application/UseCases/CrearSubcuentaDivisionariaUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearSubcuentaDivisionariaRequest;
import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearSubcuentaDivisionariaUseCase {

    private final ISubcuentaDivisionariaRepository subcuentaRepository;
    private final ICuentaRepository cuentaRepository;

    public CrearSubcuentaDivisionariaUseCase(ISubcuentaDivisionariaRepository subcuentaRepository,
                                              ICuentaRepository cuentaRepository) {
        this.subcuentaRepository = subcuentaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public SubcuentaDivisionariaResponse ejecutar(CrearSubcuentaDivisionariaRequest request) {
        if (subcuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe una subcuenta divisionaria registrada con el código " + request.getCodigo());
        }
        Cuenta cuenta = cuentaRepository.findById(request.getCuentaId())
                .orElseThrow(() -> new BadRequestException("Cuenta no encontrada: " + request.getCuentaId()));

        SubcuentaDivisionaria subcuenta = new SubcuentaDivisionaria(request.getCodigo(), request.getNombre(), cuenta);
        return SubcuentaDivisionariaResponseAssembler.toResponse(subcuentaRepository.save(subcuenta));
    }
}
'@

Write-File "$BASE/Application/UseCases/BuscarSubcuentaDivisionariaUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarSubcuentaDivisionariaUseCase {

    private final ISubcuentaDivisionariaRepository subcuentaRepository;

    public BuscarSubcuentaDivisionariaUseCase(ISubcuentaDivisionariaRepository subcuentaRepository) {
        this.subcuentaRepository = subcuentaRepository;
    }

    public Optional<SubcuentaDivisionariaResponse> porId(Long id) {
        return subcuentaRepository.findById(id).map(SubcuentaDivisionariaResponseAssembler::toResponse);
    }

    public List<SubcuentaDivisionariaResponse> todas() {
        return subcuentaRepository.findAll().stream().map(SubcuentaDivisionariaResponseAssembler::toResponse).toList();
    }

    public List<SubcuentaDivisionariaResponse> porCuenta(Long cuentaId) {
        return subcuentaRepository.findByCuentaId(cuentaId).stream()
                .map(SubcuentaDivisionariaResponseAssembler::toResponse).toList();
    }
}
'@

Write-File "$BASE/Application/UseCases/CentroCostoResponseAssembler.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;

public class CentroCostoResponseAssembler {

    public static CentroCostoResponse toResponse(CentroCosto centroCosto) {
        return new CentroCostoResponse(
                centroCosto.getId(),
                centroCosto.getCodigo(),
                centroCosto.getNombre(),
                centroCosto.getSucursal() == null ? null : centroCosto.getSucursal().getId(),
                centroCosto.isActivo()
        );
    }
}
'@

Write-File "$BASE/Application/UseCases/CrearCentroCostoUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearCentroCostoRequest;
import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearCentroCostoUseCase {

    private final ICentroCostoRepository centroCostoRepository;
    private final ISucursalRepository sucursalRepository;

    public CrearCentroCostoUseCase(ICentroCostoRepository centroCostoRepository,
                                    ISucursalRepository sucursalRepository) {
        this.centroCostoRepository = centroCostoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public CentroCostoResponse ejecutar(CrearCentroCostoRequest request) {
        if (centroCostoRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe un centro de costo registrado con el código " + request.getCodigo());
        }

        Sucursal sucursal = null;
        if (request.getSucursalId() != null) {
            sucursal = sucursalRepository.findById(request.getSucursalId())
                    .orElseThrow(() -> new BadRequestException("Sucursal no encontrada: " + request.getSucursalId()));
        }

        CentroCosto centroCosto = new CentroCosto(request.getCodigo(), request.getNombre(), sucursal);
        return CentroCostoResponseAssembler.toResponse(centroCostoRepository.save(centroCosto));
    }
}
'@

Write-File "$BASE/Application/UseCases/BuscarCentroCostoUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCentroCostoUseCase {

    private final ICentroCostoRepository centroCostoRepository;

    public BuscarCentroCostoUseCase(ICentroCostoRepository centroCostoRepository) {
        this.centroCostoRepository = centroCostoRepository;
    }

    public Optional<CentroCostoResponse> porId(Long id) {
        return centroCostoRepository.findById(id).map(CentroCostoResponseAssembler::toResponse);
    }

    public List<CentroCostoResponse> todos() {
        return centroCostoRepository.findAll().stream().map(CentroCostoResponseAssembler::toResponse).toList();
    }

    public List<CentroCostoResponse> activos() {
        return centroCostoRepository.findAllActivos().stream().map(CentroCostoResponseAssembler::toResponse).toList();
    }
}
'@

Write-File "$BASE/Application/UseCases/PartidaPresupuestalResponseAssembler.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Entities.PartidaPresupuestal;

public class PartidaPresupuestalResponseAssembler {

    public static PartidaPresupuestalResponse toResponse(PartidaPresupuestal partida) {
        return new PartidaPresupuestalResponse(
                partida.getId(),
                partida.getCodigo(),
                partida.getNombre(),
                partida.getCentroCosto().getId(),
                partida.getMontoPresupuestado(),
                partida.isActiva()
        );
    }
}
'@

Write-File "$BASE/Application/UseCases/CrearPartidaPresupuestalUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearPartidaPresupuestalRequest;
import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.PartidaPresupuestal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearPartidaPresupuestalUseCase {

    private final IPartidaPresupuestalRepository partidaRepository;
    private final ICentroCostoRepository centroCostoRepository;

    public CrearPartidaPresupuestalUseCase(IPartidaPresupuestalRepository partidaRepository,
                                            ICentroCostoRepository centroCostoRepository) {
        this.partidaRepository = partidaRepository;
        this.centroCostoRepository = centroCostoRepository;
    }

    @Transactional
    public PartidaPresupuestalResponse ejecutar(CrearPartidaPresupuestalRequest request) {
        if (partidaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe una partida presupuestal registrada con el código " + request.getCodigo());
        }
        CentroCosto centroCosto = centroCostoRepository.findById(request.getCentroCostoId())
                .orElseThrow(() -> new BadRequestException(
                        "Centro de costo no encontrado: " + request.getCentroCostoId()));

        PartidaPresupuestal partida = new PartidaPresupuestal(request.getCodigo(), request.getNombre(), centroCosto,
                request.getMontoPresupuestado());
        return PartidaPresupuestalResponseAssembler.toResponse(partidaRepository.save(partida));
    }
}
'@

Write-File "$BASE/Application/UseCases/BuscarPartidaPresupuestalUseCase.java" @'
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Domain.Repositories.IPartidaPresupuestalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPartidaPresupuestalUseCase {

    private final IPartidaPresupuestalRepository partidaRepository;

    public BuscarPartidaPresupuestalUseCase(IPartidaPresupuestalRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    public Optional<PartidaPresupuestalResponse> porId(Long id) {
        return partidaRepository.findById(id).map(PartidaPresupuestalResponseAssembler::toResponse);
    }

    public List<PartidaPresupuestalResponse> todas() {
        return partidaRepository.findAll().stream().map(PartidaPresupuestalResponseAssembler::toResponse).toList();
    }

    public List<PartidaPresupuestalResponse> porCentroCosto(Long centroCostoId) {
        return partidaRepository.findByCentroCostoId(centroCostoId).stream()
                .map(PartidaPresupuestalResponseAssembler::toResponse).toList();
    }
}
'@

# ---------------------------------------------------------------------------
# CONTROLLERS
# ---------------------------------------------------------------------------

Write-File "$BASE/Presentation/Controllers/CuentaController.java" @'
package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearCuentaRequest;
import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Application.UseCases.BuscarCuentaUseCase;
import FarmaciaERP.Application.UseCases.CrearCuentaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/cuentas")
public class CuentaController {

    private final CrearCuentaUseCase crearCuentaUseCase;
    private final BuscarCuentaUseCase buscarCuentaUseCase;

    public CuentaController(CrearCuentaUseCase crearCuentaUseCase, BuscarCuentaUseCase buscarCuentaUseCase) {
        this.crearCuentaUseCase = crearCuentaUseCase;
        this.buscarCuentaUseCase = buscarCuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCuentaRequest request) {
        try {
            CuentaResponse creada = crearCuentaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCuentaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> obtenerTodas(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        return ResponseEntity.ok(soloActivas ? buscarCuentaUseCase.activas() : buscarCuentaUseCase.todas());
    }
}
'@

Write-File "$BASE/Presentation/Controllers/SubcuentaDivisionariaController.java" @'
package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearSubcuentaDivisionariaRequest;
import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Application.UseCases.BuscarSubcuentaDivisionariaUseCase;
import FarmaciaERP.Application.UseCases.CrearSubcuentaDivisionariaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/subcuentas")
public class SubcuentaDivisionariaController {

    private final CrearSubcuentaDivisionariaUseCase crearSubcuentaUseCase;
    private final BuscarSubcuentaDivisionariaUseCase buscarSubcuentaUseCase;

    public SubcuentaDivisionariaController(CrearSubcuentaDivisionariaUseCase crearSubcuentaUseCase,
                                            BuscarSubcuentaDivisionariaUseCase buscarSubcuentaUseCase) {
        this.crearSubcuentaUseCase = crearSubcuentaUseCase;
        this.buscarSubcuentaUseCase = buscarSubcuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearSubcuentaDivisionariaRequest request) {
        try {
            SubcuentaDivisionariaResponse creada = crearSubcuentaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcuentaDivisionariaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarSubcuentaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SubcuentaDivisionariaResponse>> obtenerTodas(
            @RequestParam(required = false) Long cuentaId) {
        return ResponseEntity.ok(cuentaId != null
                ? buscarSubcuentaUseCase.porCuenta(cuentaId)
                : buscarSubcuentaUseCase.todas());
    }
}
'@

Write-File "$BASE/Presentation/Controllers/CentroCostoController.java" @'
package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearCentroCostoRequest;
import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Application.UseCases.BuscarCentroCostoUseCase;
import FarmaciaERP.Application.UseCases.CrearCentroCostoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/centros-costo")
public class CentroCostoController {

    private final CrearCentroCostoUseCase crearCentroCostoUseCase;
    private final BuscarCentroCostoUseCase buscarCentroCostoUseCase;

    public CentroCostoController(CrearCentroCostoUseCase crearCentroCostoUseCase,
                                  BuscarCentroCostoUseCase buscarCentroCostoUseCase) {
        this.crearCentroCostoUseCase = crearCentroCostoUseCase;
        this.buscarCentroCostoUseCase = buscarCentroCostoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCentroCostoRequest request) {
        try {
            CentroCostoResponse creado = crearCentroCostoUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroCostoResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCentroCostoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CentroCostoResponse>> obtenerTodos(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        return ResponseEntity.ok(soloActivos ? buscarCentroCostoUseCase.activos() : buscarCentroCostoUseCase.todos());
    }
}
'@

Write-File "$BASE/Presentation/Controllers/PartidaPresupuestalController.java" @'
package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearPartidaPresupuestalRequest;
import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Application.UseCases.BuscarPartidaPresupuestalUseCase;
import FarmaciaERP.Application.UseCases.CrearPartidaPresupuestalUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/partidas-presupuestales")
public class PartidaPresupuestalController {

    private final CrearPartidaPresupuestalUseCase crearPartidaUseCase;
    private final BuscarPartidaPresupuestalUseCase buscarPartidaUseCase;

    public PartidaPresupuestalController(CrearPartidaPresupuestalUseCase crearPartidaUseCase,
                                          BuscarPartidaPresupuestalUseCase buscarPartidaUseCase) {
        this.crearPartidaUseCase = crearPartidaUseCase;
        this.buscarPartidaUseCase = buscarPartidaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearPartidaPresupuestalRequest request) {
        try {
            PartidaPresupuestalResponse creada = crearPartidaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaPresupuestalResponse> obtenerPorId(@PathVariable Long id) {
        return buscarPartidaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PartidaPresupuestalResponse>> obtenerTodas(
            @RequestParam(required = false) Long centroCostoId) {
        return ResponseEntity.ok(centroCostoId != null
                ? buscarPartidaUseCase.porCentroCosto(centroCostoId)
                : buscarPartidaUseCase.todas());
    }
}
'@

Write-Host ""
Write-Host "=== Ejecución Finalizada ===" -ForegroundColor Cyan
Write-Host "Archivos creados desde cero: $script:CREATED" -ForegroundColor Green
Write-Host "Archivos existentes actualizados y corregidos (sin BOM): $script:UPDATED" -ForegroundColor Yellow
Write-Host ""
Write-Host "Todos tus archivos han sido codificados en UTF-8 puro (sin BOM). Compila de nuevo ejecutando:" -ForegroundColor Cyan
Write-Host "mvn clean compile" -ForegroundColor Cyan