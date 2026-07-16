import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useContabilizacionAR() {
  const [contabilizacion, setContabilizacion] = useState(null);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarPorId = useCallback(async (id) => {
    if (!id) return null;
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.contabilizacionAR.getById.execute(id);
      setContabilizacion(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener la contabilización AR');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const cargarPorCierreCaja = useCallback(async (cierreCajaId) => {
    if (!cierreCajaId) return null;
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.contabilizacionAR.getByCierreCaja.execute(cierreCajaId);
      setContabilizacion(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener la contabilización AR');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  // Inicia la Fase 02 a partir de un cierre de caja clasificado (Fase 01)
  async function iniciar(cierreCajaId) {
    try {
      setProcesando(true);
      setError(null);
      const nueva = await useCases.contabilizacionAR.iniciar.execute({ cierreCajaId });
      setContabilizacion(nueva);
      return nueva;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al iniciar la contabilización AR');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // 2.1.1 - Conciliación Primaria de Lotes de Tarjetas Físicas (POS)
  async function conciliarLotesPOS() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.conciliarLotesPOS.execute(contabilizacion.id),
      'Error al conciliar los lotes de tarjetas POS');
  }

  // 2.2.2 - Procesar Asiento Automatizado de Ventas y Cuadraturas
  async function procesarAsiento() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.procesarAsiento.execute(contabilizacion.id),
      'Error al procesar el asiento contable');
  }

  // 2.2.3 - Revisión y Ajuste de Asientos Descuadrados
  async function revisarAjusteAsientos() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.revisarAjusteAsientos.execute(contabilizacion.id),
      'Error al revisar y ajustar los asientos descuadrados');
  }

  // 2.3.1 - Auditoría de Integridad y Firmas de Recetas Médicas
  async function auditarRecetas({ conforme, motivoObservacion }) {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.auditarRecetas.execute(contabilizacion.id, { conforme, motivoObservacion }),
      'Error al auditar las recetas médicas');
  }

  // 2.3.3 - Subsanación de Recetas y Solicitud de Duplicados a Sucursal / Médico
  async function solicitarDuplicadoReceta() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.solicitarDuplicadoReceta.execute(contabilizacion.id),
      'Error al solicitar el duplicado de receta');
  }

  // Reintento de la auditoría de integridad tras recibir el duplicado
  async function reintentarAuditoria() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.reintentarAuditoria.execute(contabilizacion.id),
      'Error al reintentar la auditoría de recetas');
  }

  // 2.3.2 - Consolidación del Lote y Despacho de Valija Física
  async function consolidarLote() {
    if (!contabilizacion) return null;
    return ejecutar(() => useCases.contabilizacionAR.consolidarLote.execute(contabilizacion.id),
      'Error al consolidar el lote y despachar la valija');
  }

  async function ejecutar(accion, mensajeError) {
    try {
      setProcesando(true);
      setError(null);
      const actualizado = await accion();
      setContabilizacion(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? mensajeError);
      return null;
    } finally {
      setProcesando(false);
    }
  }

  function resetContabilizacion() {
    setContabilizacion(null);
    setError(null);
  }

  return {
    contabilizacion,
    loading,
    procesando,
    error,
    cargarPorId,
    cargarPorCierreCaja,
    iniciar,
    conciliarLotesPOS,
    procesarAsiento,
    revisarAjusteAsientos,
    auditarRecetas,
    solicitarDuplicadoReceta,
    reintentarAuditoria,
    consolidarLote,
    resetContabilizacion,
  };
}
