import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useCompensacionAR(contabilizacionARId) {
  const [compensacion, setCompensacion] = useState(null);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarCompensacion = useCallback(async () => {
    if (!contabilizacionARId) return null;
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.compensacionAR.getByContabilizacionAR.execute(contabilizacionARId);
      setCompensacion(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener la compensación final del lote');
      return null;
    } finally {
      setLoading(false);
    }
  }, [contabilizacionARId]);

  // 6.1 - Aplicar Compensación Automática sobre la Cuenta del Cliente
  async function aplicarCompensacion() {
    return ejecutar(() => useCases.compensacionAR.aplicar.execute({ contabilizacionARId }),
      'Error al aplicar la compensación automática');
  }

  // Generar Reporte de Rendimiento Comercial y Margen de la Cadena
  async function generarReporte() {
    if (!compensacion) return null;
    return ejecutar(() => useCases.compensacionAR.generarReporte.execute(compensacion.id),
      'Error al generar el reporte de rendimiento comercial');
  }

  // Confirmar Saldo Limpio en Cuentas Corrientes
  async function confirmarSaldo() {
    if (!compensacion) return null;
    return ejecutar(() => useCases.compensacionAR.confirmarSaldo.execute(compensacion.id),
      'Error al confirmar el saldo limpio en cuentas corrientes');
  }

  // Cerrar Ingresos por Convenio — finaliza el ciclo FI-AR
  async function cerrarIngresos() {
    if (!compensacion) return null;
    return ejecutar(() => useCases.compensacionAR.cerrarIngresos.execute(compensacion.id),
      'Error al cerrar los ingresos por convenio');
  }

  async function ejecutar(accion, mensajeError) {
    try {
      setProcesando(true);
      setError(null);
      const data = await accion();
      setCompensacion(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? mensajeError);
      return null;
    } finally {
      setProcesando(false);
    }
  }

  return {
    compensacion,
    loading,
    procesando,
    error,
    cargarCompensacion,
    aplicarCompensacion,
    generarReporte,
    confirmarSaldo,
    cerrarIngresos,
  };
}
