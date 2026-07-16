# ============================================================================
# Generador Módulo Contable Frontend (FI-GL) para FarmaciaERP — VERSIÓN BLINDADA
# Soluciona los errores de escape en Literales de Plantilla (Template Literals)
# ============================================================================

$ErrorActionPreference = "Stop"

# Asegurar ruta absoluta respecto al script para evitar usar la carpeta del Backend
$BASE = Join-Path $PSScriptRoot "src"

Write-Host "Directorio objetivo absoluto del Frontend: $BASE" -ForegroundColor Yellow

if (-not (Test-Path $BASE)) {
    New-Item -ItemType Directory -Force -Path $BASE | Out-Null
}

Write-Host "Creando o verificando estructura del modulo FI-GL..." -ForegroundColor Cyan

$Directorios = @(
    (Join-Path $BASE "domain/usecases/contabilidad"),
    (Join-Path $BASE "infrastructure/repositories"),
    (Join-Path $BASE "presentation/components/ui"),
    (Join-Path $BASE "presentation/components/fiGl"),
    (Join-Path $BASE "presentation/hooks"),
    (Join-Path $BASE "presentation/pages/contabilidad")
)

foreach ($dir in $Directorios) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Force -Path $dir | Out-Null
        Write-Host "Directorio listo: $dir" -ForegroundColor Green
    }
}

function Escribir-Archivo-Literal {
    param (
        [string]$Path,
        [string]$Content
    )
    $AbsolutePath = [System.IO.Path]::GetFullPath($Path)

    # Asegurar directorio padre
    $ParentDir = Split-Path $AbsolutePath -Parent
    if (-not (Test-Path $ParentDir)) {
        New-Item -ItemType Directory -Force -Path $ParentDir | Out-Null
    }

    # Escribir UTF-8 sin BOM para compatibilidad total con Vite/React
    [System.IO.File]::WriteAllText($AbsolutePath, $Content)
    Write-Host "Archivo sobrescrito/actualizado: $AbsolutePath" -ForegroundColor Green
}

# ============================================================================
# 1. CASOS DE USO (DOMAIN)
# ============================================================================

$getPlanCuentasUseCase = @'
export const getPlanCuentasUseCase = (contabilidadRepository) => async () => {
  return await contabilidadRepository.getPlanCuentas();
};

export const crearCuentaContableUseCase = (contabilidadRepository) => async (cuentaData) => {
  return await contabilidadRepository.crearCuentaContable(cuentaData);
};
'@
Escribir-Archivo-Literal "$BASE/domain/usecases/contabilidad/getPlanCuentasUseCase.js" $getPlanCuentasUseCase

$getDiarioUseCase = @'
export const getDiarioUseCase = (contabilidadRepository) => async (fechaInicio, fechaFin) => {
  if (!fechaInicio || !fechaFin) {
    throw new Error("El rango de fechas es requerido para consultar el Libro Diario");
  }
  return await contabilidadRepository.getLibroDiario(fechaInicio, fechaFin);
};
'@
Escribir-Archivo-Literal "$BASE/domain/usecases/contabilidad/getDiarioUseCase.js" $getDiarioUseCase

$getBalanceGeneralUseCase = @'
export const getBalanceGeneralUseCase = (contabilidadRepository) => async (periodoId) => {
  return await contabilidadRepository.getBalanceGeneral(periodoId);
};
'@
Escribir-Archivo-Literal "$BASE/domain/usecases/contabilidad/getBalanceGeneralUseCase.js" $getBalanceGeneralUseCase

$getEstadoResultadosUseCase = @'
export const getEstadoResultadosUseCase = (contabilidadRepository) => async (periodoId) => {
  return await contabilidadRepository.getEstadoResultados(periodoId);
};
'@
Escribir-Archivo-Literal "$BASE/domain/usecases/contabilidad/getEstadoResultadosUseCase.js" $getEstadoResultadosUseCase

$activoFijoUseCase = @'
export const getActivosFijosUseCase = (contabilidadRepository) => async () => {
  return await contabilidadRepository.getActivosFijos();
};

export const crearActivoFijoUseCase = (contabilidadRepository) => async (activoData) => {
  if (activoData.costoAdquisicion <= 0) {
    throw new Error("El costo de adquisicion debe ser mayor a cero");
  }
  return await contabilidadRepository.crearActivoFijo(activoData);
};
'@
Escribir-Archivo-Literal "$BASE/domain/usecases/contabilidad/activoFijoUseCase.js" $activoFijoUseCase


# ============================================================================
# 2. REPOSITORIO DE INFRAESTRUCTURA (Corregido)
# ============================================================================

$contabilidadRepository = @'
import { apiClient } from '../services/apiClient';

export const contabilidadRepository = {
  getPlanCuentas: async () => {
    return await apiClient.get('/api/contabilidad/plan-cuentas');
  },
  crearCuentaContable: async (cuenta) => {
    return await apiClient.post('/api/contabilidad/plan-cuentas', cuenta);
  },
  getPlanCostos: async () => {
    return await apiClient.get('/api/contabilidad/plan-costos');
  },
  getPartidasPresupuestales: async () => {
    return await apiClient.get('/api/contabilidad/partidas-presupuestales');
  },
  getLibroDiario: async (fechaInicio, fechaFin) => {
    return await apiClient.get(`/api/contabilidad/libro-diario?desde=${fechaInicio}&hasta=${fechaFin}`);
  },
  getBalanceGeneral: async (periodoId) => {
    return await apiClient.get(`/api/contabilidad/reportes/balance-general?periodo=${periodoId}`);
  },
  getEstadoResultados: async (periodoId) => {
    return await apiClient.get(`/api/contabilidad/reportes/estado-resultados?periodo=${periodoId}`);
  },
  getActivosFijos: async () => {
    return await apiClient.get('/api/contabilidad/activos-fijos');
  },
  crearActivoFijo: async (activo) => {
    return await apiClient.post('/api/contabilidad/activos-fijos', activo);
  }
};
'@
Escribir-Archivo-Literal "$BASE/infrastructure/repositories/contabilidadRepository.js" $contabilidadRepository


# ============================================================================
# 3. COMPONENTES UI REUTILIZABLES (Corregido)
# ============================================================================

$CatalogoJerarquicoTree = @'
import React, { useState } from 'react';

const TreeNode = ({ node, onAddChild, level = 0 }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const hasChildren = node.children && node.children.length > 0;

  return (
    <div className="select-none">
      <div
        className={`flex items-center justify-between p-2 rounded-lg hover:bg-slate-50 transition-colors duration-150 border-b border-slate-100 ${
          level === 0 ? 'bg-slate-50/50 font-semibold' : ''
        }`}
        style={{ paddingLeft: `${Math.max(level * 1.5, 0.5)}rem` }}
      >
        <div className="flex items-center gap-2">
          {hasChildren ? (
            <button
              onClick={() => setIsExpanded(!isExpanded)}
              type="button"
              className="p-1 hover:bg-slate-200 rounded text-slate-500 transition-transform"
            >
              <svg
                className={`w-4 h-4 transform transition-transform ${isExpanded ? 'rotate-90' : ''}`}
                fill="none" viewBox="0 0 24 24" stroke="currentColor"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          ) : (
            <span className="w-6" />
          )}
          <span className="text-sm font-mono text-indigo-600 font-bold">{node.codigo}</span>
          <span className="text-sm text-slate-700">{node.descripcion}</span>
          {node.tipo && (
            <span className="text-xs px-2 py-0.5 bg-slate-200/60 rounded text-slate-600 font-medium">
              {node.tipo}
            </span>
          )}
        </div>

        <button
          onClick={() => onAddChild(node)}
          type="button"
          className="text-xs text-indigo-600 hover:text-indigo-800 hover:underline px-2 py-1"
        >
          + Agregar Subnivel
        </button>
      </div>

      {hasChildren && isExpanded && (
        <div className="mt-1">
          {node.children.map((child) => (
            <TreeNode key={child.codigo} node={child} onAddChild={onAddChild} level={level + 1} />
          ))}
        </div>
      )}
    </div>
  );
};

export const CatalogoJerarquicoTree = ({ data, onAddNode }) => {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
      <div className="p-4 bg-slate-50 border-b border-slate-200 flex justify-between items-center">
        <h3 className="text-sm font-semibold text-slate-800">Estructura Jerárquica del Catálogo</h3>
        <button
          onClick={() => onAddNode(null)}
          type="button"
          className="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-colors"
        >
          + Nueva Raíz
        </button>
      </div>
      <div className="p-4 space-y-1 max-h-[600px] overflow-y-auto">
        {data.length === 0 ? (
          <p className="text-sm text-slate-400 text-center py-8">No hay registros definidos en este catálogo.</p>
        ) : (
          data.map((rootNode) => (
            <TreeNode key={rootNode.codigo} node={rootNode} onAddChild={onAddNode} />
          ))
        )}
      </div>
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/components/ui/CatalogoJerarquicoTree.jsx" $CatalogoJerarquicoTree

$PeriodoSelector = @'
import React from 'react';

export const PeriodoSelector = ({ selectedPeriod, onChange }) => {
  const years = [2026, 2025, 2024];
  const months = [
    { value: '01', label: 'Enero' }, { value: '02', label: 'Febrero' },
    { value: '03', label: 'Marzo' }, { value: '04', label: 'Abril' },
    { value: '05', label: 'Mayo' }, { value: '06', label: 'Junio' },
    { value: '07', label: 'Julio' }, { value: '08', label: 'Agosto' },
    { value: '09', label: 'Septiembre' }, { value: '10', label: 'Octubre' },
    { value: '11', label: 'Noviembre' }, { value: '12', label: 'Diciembre' }
  ];

  const handleYearChange = (e) => {
    onChange({ ...selectedPeriod, year: e.target.value });
  };

  const handleMonthChange = (e) => {
    onChange({ ...selectedPeriod, month: e.target.value });
  };

  return (
    <div className="flex items-center gap-3 bg-white p-3 rounded-xl border border-slate-200 shadow-sm">
      <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Periodo Fiscal</span>
      <div className="flex items-center gap-2">
        <select
          value={selectedPeriod.year}
          onChange={handleYearChange}
          className="text-sm border border-slate-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-500 bg-slate-50 text-slate-700"
        >
          {years.map(y => (
            <option key={y} value={y}>{y}</option>
          ))}
        </select>
        <select
          value={selectedPeriod.month}
          onChange={handleMonthChange}
          className="text-sm border border-slate-300 rounded-lg p-2 focus:ring-2 focus:ring-indigo-500 bg-slate-50 text-slate-700"
        >
          {months.map(m => (
            <option key={m.value} value={m.value}>{m.label}</option>
          ))}
        </select>
      </div>
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/components/ui/PeriodoSelector.jsx" $PeriodoSelector

$FiGlRail = @'
import React from 'react';

export const FiGlRail = ({ activeTab, onTabChange }) => {
  const phases = [
    { id: 'catalogo', label: 'Plan de Cuentas', sub: 'Catálogo Maestro' },
    { id: 'costos', label: 'Centros de Costo', sub: 'Plan analítico' },
    { id: 'diario', label: 'Libro Diario', sub: 'Asientos contables' },
    { id: 'balance', label: 'Balance General', sub: 'Estado de situación' },
    { id: 'resultados', label: 'Estado de Resultados', sub: 'Perdidas y Ganancias' },
    { id: 'activos', label: 'Activos Fijos', sub: 'Control & Depreciacion' }
  ];

  return (
    <div className="w-64 bg-slate-900 border-r border-slate-800 text-slate-300 flex flex-col h-full min-h-screen">
      <div className="p-4 border-b border-slate-800">
        <h2 className="text-sm font-bold text-white tracking-wider uppercase">Finanzas FI-GL</h2>
        <span className="text-xs text-slate-400">Contabilidad General</span>
      </div>
      <nav className="flex-1 p-3 space-y-1">
        {phases.map((phase) => (
          <button
            key={phase.id}
            onClick={() => onTabChange(phase.id)}
            type="button"
            className={`w-full text-left p-3 rounded-xl transition-all duration-150 flex flex-col gap-0.5 ${
              activeTab === phase.id
                ? 'bg-indigo-600 text-white font-semibold shadow-md shadow-indigo-600/20'
                : 'hover:bg-slate-800 text-slate-400 hover:text-white'
            }`}
          >
            <span className="text-xs uppercase tracking-wider opacity-60">MODULO</span>
            <span className="text-sm">{phase.label}</span>
            <span className="text-[10px] opacity-80 font-normal">{phase.sub}</span>
          </button>
        ))}
      </nav>
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/components/fiGl/FiGlRail.jsx" $FiGlRail


# ============================================================================
# 4. CUSTOM REACT HOOK (useContabilidad)
# ============================================================================

$useContabilidad = @'
import { useState, useCallback } from 'react';
import { contabilidadRepository } from '../../infrastructure/repositories/contabilidadRepository';
import { getPlanCuentasUseCase, crearCuentaContableUseCase } from '../../domain/usecases/contabilidad/getPlanCuentasUseCase';
import { getBalanceGeneralUseCase } from '../../domain/usecases/contabilidad/getBalanceGeneralUseCase';
import { getEstadoResultadosUseCase } from '../../domain/usecases/contabilidad/getEstadoResultadosUseCase';

export const useContabilidad = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [planCuentas, setPlanCuentas] = useState([]);
  const [balanceGeneral, setBalanceGeneral] = useState(null);
  const [estadoResultados, setEstadoResultados] = useState(null);

  const fetchPlanCuentas = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getPlanCuentasUseCase(contabilidadRepository)();
      setPlanCuentas(data);
    } catch (err) {
      setError(err.message || "Error al cargar el plan de cuentas");
    } finally {
      setLoading(false);
    }
  }, []);

  const addCuentaContable = async (nuevaCuenta) => {
    setLoading(true);
    try {
      await crearCuentaContableUseCase(contabilidadRepository)(nuevaCuenta);
      await fetchPlanCuentas();
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const fetchBalanceGeneral = useCallback(async (periodoId) => {
    setLoading(true);
    setError(null);
    try {
      const data = await getBalanceGeneralUseCase(contabilidadRepository)(periodoId);
      setBalanceGeneral(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchEstadoResultados = useCallback(async (periodoId) => {
    setLoading(true);
    setError(null);
    try {
      const data = await getEstadoResultadosUseCase(contabilidadRepository)(periodoId);
      setEstadoResultados(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    loading,
    error,
    planCuentas,
    balanceGeneral,
    estadoResultados,
    fetchPlanCuentas,
    addCuentaContable,
    fetchBalanceGeneral,
    fetchEstadoResultados
  };
};
'@
Escribir-Archivo-Literal "$BASE/presentation/hooks/useContabilidad.js" $useContabilidad


# ============================================================================
# 5. VISTAS Y PÁGINAS (Corregidas)
# ============================================================================

$PlanCuentasPage = @'
import React, { useEffect, useState } from 'react';
import { useContabilidad } from '../../hooks/useContabilidad';
import { CatalogoJerarquicoTree } from '../../components/ui/CatalogoJerarquicoTree';

export const PlanCuentasPage = () => {
  const { planCuentas, loading, fetchPlanCuentas, addCuentaContable } = useContabilidad();
  const [selectedParent, setSelectedParent] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [newCuenta, setNewCuenta] = useState({ codigo: '', descripcion: '', tipo: 'ACTIVO' });

  useEffect(() => {
    fetchPlanCuentas();
  }, [fetchPlanCuentas]);

  const handleAddNode = (parentNode) => {
    setSelectedParent(parentNode);
    if (parentNode) {
      setNewCuenta(prev => ({ ...prev, codigo: `${parentNode.codigo}.` }));
    } else {
      setNewCuenta({ codigo: '', descripcion: '', tipo: 'ACTIVO' });
    }
    setModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      await addCuentaContable({
        ...newCuenta,
        padreId: selectedParent ? selectedParent.id : null
      });
      setModalOpen(false);
    } catch (err) {
      alert("Error al agregar cuenta: " + err.message);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Plan de Cuentas Contables</h1>
          <p className="text-sm text-slate-500">Definición de cuentas contables estructuradas para el mapeo con el Plan Contable General Empresarial (PCGE).</p>
        </div>
      </div>

      {loading && <p className="text-indigo-600 animate-pulse text-sm font-semibold">Procesando catálogo...</p>}

      <CatalogoJerarquicoTree data={planCuentas} onAddNode={handleAddNode} />

      {modalOpen && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center z-50">
          <form onSubmit={handleSave} className="bg-white p-6 rounded-2xl border border-slate-200 shadow-xl w-full max-w-md space-y-4">
            <h3 className="font-bold text-slate-800 text-lg">
              {selectedParent ? `Crear subcuenta de [${selectedParent.codigo}]` : 'Crear Cuenta Raíz'}
            </h3>

            <div className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">Código Cuenta</label>
                <input
                  type="text"
                  required
                  value={newCuenta.codigo}
                  onChange={e => setNewCuenta({...newCuenta, codigo: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">Descripción</label>
                <input
                  type="text"
                  required
                  value={newCuenta.descripcion}
                  onChange={e => setNewCuenta({...newCuenta, descripcion: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setModalOpen(false)}
                className="px-4 py-2 text-xs text-slate-500 font-semibold hover:bg-slate-100 rounded-lg"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-xs bg-indigo-600 text-white font-semibold hover:bg-indigo-700 rounded-lg"
              >
                Guardar Cuenta
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/pages/contabilidad/PlanCuentasPage.jsx" $PlanCuentasPage

$BalanceGeneralPage = @'
import React, { useState, useEffect } from 'react';
import { useContabilidad } from '../../hooks/useContabilidad';
import { PeriodoSelector } from '../../components/ui/PeriodoSelector';

export const BalanceGeneralPage = () => {
  const { balanceGeneral, loading, fetchBalanceGeneral } = useContabilidad();
  const [period, setPeriod] = useState({ year: '2026', month: '07' });

  useEffect(() => {
    fetchBalanceGeneral(`${period.year}-${period.month}`);
  }, [period, fetchBalanceGeneral]);

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Balance General</h1>
          <p className="text-sm text-slate-500">Estado de situación financiera de la farmacia en tiempo real.</p>
        </div>
        <PeriodoSelector selectedPeriod={period} onChange={setPeriod} />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Activos Totales</span>
          <span className="text-2xl font-bold text-slate-800 mt-2">
            S/ {balanceGeneral?.totalActivo?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Pasivos Totales</span>
          <span className="text-2xl font-bold text-emerald-600 mt-2">
            S/ {balanceGeneral?.totalPasivo?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Patrimonio Neto</span>
          <span className="text-2xl font-bold text-indigo-600 mt-2">
            S/ {balanceGeneral?.totalPatrimonio?.toFixed(2) || '0.00'}
          </span>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-slate-400">Procesando estructura del Balance...</div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 text-xs font-semibold text-slate-500 uppercase border-b border-slate-200">
                <th className="p-4">Cuenta / Rubro</th>
                <th className="p-4 text-right">Saldo Periodo</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
              {balanceGeneral?.partidas?.map((p, idx) => (
                <tr key={idx} className={p.esCabecera ? 'font-semibold bg-slate-50/50' : 'hover:bg-slate-50'}>
                  <td className="p-4">{p.descripcion}</td>
                  <td className={`p-4 text-right ${p.monto < 0 ? 'text-red-500' : ''}`}>
                    S/ {p.monto?.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/pages/contabilidad/BalanceGeneralPage.jsx" $BalanceGeneralPage

$EstadoResultadosPage = @'
import React, { useState, useEffect } from 'react';
import { useContabilidad } from '../../hooks/useContabilidad';
import { PeriodoSelector } from '../../components/ui/PeriodoSelector';

export const EstadoResultadosPage = () => {
  const { estadoResultados, loading, fetchEstadoResultados } = useContabilidad();
  const [period, setPeriod] = useState({ year: '2026', month: '07' });

  useEffect(() => {
    fetchEstadoResultados(`${period.year}-${period.month}`);
  }, [period, fetchEstadoResultados]);

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Estado de Resultados</h1>
          <p className="text-sm text-slate-500">Análisis de pérdidas y ganancias acumuladas por el periodo fiscal.</p>
        </div>
        <PeriodoSelector selectedPeriod={period} onChange={setPeriod} />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Ingresos Operacionales</span>
          <span className="text-2xl font-bold text-emerald-600 mt-2">
            S/ {estadoResultados?.totalIngresos?.toFixed(2) || '0.00'}
          </span>
        </div>
        <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col">
          <span className="text-xs font-semibold text-slate-400 uppercase">Utilidad Neta</span>
          <span className="text-2xl font-bold text-indigo-600 mt-2">
            S/ {estadoResultados?.utilidadNeta?.toFixed(2) || '0.00'}
          </span>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12 text-slate-400">Generando Estado de Resultados...</div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 text-xs font-semibold text-slate-500 uppercase border-b border-slate-200">
                <th className="p-4">Concepto Contable</th>
                <th className="p-4 text-right">Monto Acumulado</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
              {estadoResultados?.conceptos?.map((c, idx) => (
                <tr key={idx} className={c.esCabecera ? 'font-semibold bg-slate-50/50' : 'hover:bg-slate-50'}>
                  <td className="p-4">{c.descripcion}</td>
                  <td className={`p-4 text-right ${c.monto < 0 ? 'text-red-500' : ''}`}>
                    S/ {c.monto?.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/pages/contabilidad/EstadoResultadosPage.jsx" $EstadoResultadosPage

$ActivosFijosPage = @'
import React, { useState, useEffect } from 'react';
import { contabilidadRepository } from '../../../infrastructure/repositories/contabilidadRepository';

export const ActivosFijosPage = () => {
  const [activos, setActivos] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [nuevoActivo, setNuevoActivo] = useState({
    descripcion: '',
    categoriaSunat: 'MAQUINARIA',
    costoAdquisicion: '',
    fechaAdquisicion: '',
    vidaUtilMeses: 120
  });

  const fetchActivos = async () => {
    try {
      const data = await contabilidadRepository.getActivosFijos();
      setActivos(data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    fetchActivos();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await contabilidadRepository.crearActivoFijo({
        ...nuevoActivo,
        costoAdquisicion: parseFloat(nuevoActivo.costoAdquisicion)
      });
      setOpenModal(false);
      fetchActivos();
    } catch (err) {
      alert("Error al registrar activo");
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Control de Activos Fijos</h1>
          <p className="text-sm text-slate-500">Catálogo e imputación de amortización de activos con directiva SUNAT.</p>
        </div>
        <button
          onClick={() => setOpenModal(true)}
          type="button"
          className="bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-semibold px-4 py-2.5 rounded-xl transition-colors shadow-sm"
        >
          + Registrar Activo Fijo
        </button>
      </div>

      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-50 text-xs font-semibold text-slate-500 border-b border-slate-200 uppercase">
              <th className="p-4">Código / Descripción</th>
              <th className="p-4">Categoría SUNAT</th>
              <th className="p-4">Costo Adquisición</th>
              <th className="p-4">Depreciación Acumulada</th>
              <th className="p-4 text-right">Valor Neto</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
            {activos.map((a, idx) => (
              <tr key={idx} className="hover:bg-slate-50">
                <td className="p-4 font-medium">{a.descripcion}</td>
                <td className="p-4 text-xs font-mono">{a.categoriaSunat}</td>
                <td className="p-4">S/ {a.costoAdquisicion?.toFixed(2)}</td>
                <td className="p-4 text-amber-600 font-semibold">S/ {a.depreciacionAcumulada?.toFixed(2)}</td>
                <td className="p-4 text-right font-bold">S/ {(a.costoAdquisicion - a.depreciacionAcumulada).toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {openModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center z-50">
          <form onSubmit={handleSubmit} className="bg-white p-6 rounded-2xl border border-slate-200 shadow-xl w-full max-w-md space-y-4">
            <h3 className="font-bold text-slate-800 text-lg">Registrar Activo Fijo</h3>
            <div className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">Descripción</label>
                <input
                  type="text" required
                  value={nuevoActivo.descripcion}
                  onChange={e => setNuevoActivo({...nuevoActivo, descripcion: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">Categoría SUNAT</label>
                <select
                  value={nuevoActivo.categoriaSunat}
                  onChange={e => setNuevoActivo({...nuevoActivo, categoriaSunat: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none bg-slate-50"
                >
                  <option value="MAQUINARIA">Maquinaria y Equipo (10%)</option>
                  <option value="EQUIPO_COMPUTO">Equipos de Cómputo (25%)</option>
                  <option value="VEHICULO">Vehículos (20%)</option>
                  <option value="EDIFICIO">Edificaciones (5%)</option>
                </select>
              </div>
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block text-xs font-semibold text-slate-500 uppercase">Costo</label>
                  <input
                    type="number" step="0.01" required
                    value={nuevoActivo.costoAdquisicion}
                    onChange={e => setNuevoActivo({...nuevoActivo, costoAdquisicion: e.target.value})}
                    className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-500 uppercase">Fecha Adq.</label>
                  <input
                    type="date" required
                    value={nuevoActivo.fechaAdquisicion}
                    onChange={e => setNuevoActivo({...nuevoActivo, fechaAdquisicion: e.target.value})}
                    className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                  />
                </div>
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button" onClick={() => setOpenModal(false)}
                className="px-4 py-2 text-xs text-slate-500 font-semibold hover:bg-slate-100 rounded-lg"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-xs bg-indigo-600 text-white font-semibold hover:bg-indigo-700 rounded-lg"
              >
                Registrar Activo
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/pages/contabilidad/ActivosFijosPage.jsx" $ActivosFijosPage

$ContabilidadPage = @'
import React, { useState } from 'react';
import { FiGlRail } from '../../components/fiGl/FiGlRail';
import { PlanCuentasPage } from './PlanCuentasPage';
import { BalanceGeneralPage } from './BalanceGeneralPage';
import { EstadoResultadosPage } from './EstadoResultadosPage';
import { ActivosFijosPage } from './ActivosFijosPage';

export const ContabilidadPage = () => {
  const [activeTab, setActiveTab] = useState('catalogo');

  const renderContent = () => {
    switch (activeTab) {
      case 'catalogo':
        return <PlanCuentasPage />;
      case 'balance':
        return <BalanceGeneralPage />;
      case 'resultados':
        return <EstadoResultadosPage />;
      case 'activos':
        return <ActivosFijosPage />;
      case 'costos':
      case 'diario':
        return (
          <div className="p-8 text-center text-slate-500">
            <h2 className="text-xl font-semibold mb-2">Sección en integración...</h2>
            <p className="text-sm">Esta sección del General Ledger (FI-GL) está lista para enlazar con la lógica de asientos de Diario.</p>
          </div>
        );
      default:
        return <PlanCuentasPage />;
    }
  };

  return (
    <div className="flex bg-slate-50 min-h-screen">
      <FiGlRail activeTab={activeTab} onTabChange={setActiveTab} />
      <div className="flex-1 overflow-x-hidden">
        {renderContent()}
      </div>
    </div>
  );
};
'@
Escribir-Archivo-Literal "$BASE/presentation/pages/contabilidad/ContabilidadPage.jsx" $ContabilidadPage

Write-Host "`n=== [EXITO] ¡Todos los archivos del Frontend han sido completamente saneados y regenerados! ===" -ForegroundColor Cyan