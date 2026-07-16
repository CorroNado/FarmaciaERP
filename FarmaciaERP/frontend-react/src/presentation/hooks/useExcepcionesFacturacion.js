import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useExcepcionesFacturacion() {
  const [excepciones, setExcepciones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarExcepciones = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.excepcionFacturacion.getAll.execute({});
      setExcepciones(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las excepciones de facturación');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertExcepcion(actualizada) {
    setExcepciones((prev) => {
      const existe = prev.some((e) => e.id === actualizada.id);
      return existe ? prev.map((e) => (e.id === actualizada.id ? actualizada : e)) : [actualizada, ...prev];
    });
  }

  // Sistema ERP: captura automática de la excepción desde una conciliación bloqueada en MRBR.
  async function capturarExcepcion({ conciliacionTresViasId }) {
    try {
      setProcesandoId('nueva');
      setError(null);
      const nueva = await useCases.excepcionFacturacion.capturar.execute({ conciliacionTresViasId });
      upsertExcepcion(nueva);
      return nueva;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al capturar la excepción de facturación');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // 1.1 - Revisar Panel de Facturas Bloqueadas
  async function revisarExcepcion(id) {
    try {
      setProcesandoId(id);
      setError(null);
      const actualizada = await useCases.excepcionFacturacion.revisar.execute(id);
      upsertExcepcion(actualizada);
      return actualizada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al revisar la excepción de facturación');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // 1.2 - Analizar y Clasificar Discrepancia (dispara notificación 1.3)
  async function clasificarExcepcion(id, tipoDiscrepancia) {
    try {
      setProcesandoId(id);
      setError(null);
      const actualizada = await useCases.excepcionFacturacion.clasificar.execute(id, { tipoDiscrepancia });
      upsertExcepcion(actualizada);
      return actualizada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al clasificar la discrepancia');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  return {
    excepciones,
    loading,
    procesandoId,
    error,
    cargarExcepciones,
    capturarExcepcion,
    revisarExcepcion,
    clasificarExcepcion,
  };
}
