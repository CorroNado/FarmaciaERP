import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useOrdenCompra() {
  const [ordenes, setOrdenes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [creando, setCreando] = useState(false);
  const [firmando, setFirmando] = useState(false);
  const [error, setError] = useState(null);

  const loadOrdenes = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.ordenCompra.getAll.execute({});
      setOrdenes(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las Órdenes de Compra');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadOrdenes();
  }, [loadOrdenes]);

  async function crearOrdenCompra(formData) {
    try {
      setCreando(true);
      setError(null);
      const oc = await useCases.ordenCompra.crear.execute(formData);
      await loadOrdenes();
      return oc;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al generar la Orden de Compra');
      return null;
    } finally {
      setCreando(false);
    }
  }

  async function firmarOrdenCompra(id, fechaEntregaLimite) {
    try {
      setFirmando(true);
      setError(null);
      const oc = await useCases.ordenCompra.firmar.execute(id, fechaEntregaLimite);
      await loadOrdenes();
      return oc;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al firmar la Orden de Compra');
      return null;
    } finally {
      setFirmando(false);
    }
  }

  return {
    ordenes,
    loading,
    creando,
    firmando,
    error,
    refetch: loadOrdenes,
    crearOrdenCompra,
    firmarOrdenCompra,
  };
}
