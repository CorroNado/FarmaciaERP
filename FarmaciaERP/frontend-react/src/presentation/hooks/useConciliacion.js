import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useConciliacion() {
  const [conciliaciones, setConciliaciones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [ejecutando, setEjecutando] = useState(false);
  const [error, setError] = useState(null);

  const loadConciliaciones = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.conciliacion.getAll.execute({});
      setConciliaciones(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las conciliaciones de 3 vías');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadConciliaciones();
  }, [loadConciliaciones]);

  // formData: { ordenCompraId }
  async function ejecutarConciliacion(formData) {
    try {
      setEjecutando(true);
      setError(null);
      const conciliacion = await useCases.conciliacion.ejecutar.execute(formData);
      await loadConciliaciones();
      return conciliacion;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al ejecutar la conciliación de 3 vías (MRBR)');
      return null;
    } finally {
      setEjecutando(false);
    }
  }

  return {
    conciliaciones,
    loading,
    ejecutando,
    error,
    refetch: loadConciliaciones,
    ejecutarConciliacion,
  };
}
