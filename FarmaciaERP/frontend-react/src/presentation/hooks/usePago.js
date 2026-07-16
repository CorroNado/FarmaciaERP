import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function usePago() {
  const [pagos, setPagos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [ejecutando, setEjecutando] = useState(false);
  const [error, setError] = useState(null);

  const loadPagos = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.pago.getAll.execute({});
      setPagos(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los pagos');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadPagos();
  }, [loadPagos]);

  // formData: { facturaMIROId, banco, fechaPago }
  async function ejecutarPago(formData) {
    try {
      setEjecutando(true);
      setError(null);
      const pago = await useCases.pago.ejecutar.execute(formData);
      await loadPagos();
      return pago;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al ejecutar el pago (F110)');
      return null;
    } finally {
      setEjecutando(false);
    }
  }

  return {
    pagos,
    loading,
    ejecutando,
    error,
    refetch: loadPagos,
    ejecutarPago,
  };
}
