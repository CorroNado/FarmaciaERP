import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useOrdenTraslado() {
  const [ordenes, setOrdenes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [generando, setGenerando] = useState(false);
  const [confirmandoId, setConfirmandoId] = useState(null);
  const [error, setError] = useState(null);

  const loadOrdenes = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.ordenTraslado.getAll.execute({});
      setOrdenes(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las órdenes de traslado (STO)');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadOrdenes();
  }, [loadOrdenes]);

  // formData: { inspeccionCalidadId, sucursalDestinoId, guiaRemision }
  async function generarSTO(formData) {
    try {
      setGenerando(true);
      setError(null);
      const orden = await useCases.ordenTraslado.generar.execute(formData);
      await loadOrdenes();
      return orden;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al generar la Orden de Traslado (STO)');
      return null;
    } finally {
      setGenerando(false);
    }
  }

  async function confirmarRecepcion(id) {
    try {
      setConfirmandoId(id);
      setError(null);
      const orden = await useCases.ordenTraslado.confirmarRecepcion.execute(id);
      await loadOrdenes();
      return orden;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al confirmar la recepción en POS');
      return null;
    } finally {
      setConfirmandoId(null);
    }
  }

  return {
    ordenes,
    loading,
    generando,
    confirmandoId,
    error,
    refetch: loadOrdenes,
    generarSTO,
    confirmarRecepcion,
  };
}
