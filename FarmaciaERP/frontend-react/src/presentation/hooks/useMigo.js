import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useMigo() {
  const [entradas, setEntradas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [registrando, setRegistrando] = useState(false);
  const [error, setError] = useState(null);

  const loadEntradas = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.migo.getAll.execute({});
      setEntradas(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las entradas de mercancía (MIGO)');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadEntradas();
  }, [loadEntradas]);

  // formData debe incluir: ordenCompraId, lote, fechaVencimiento, temperaturaArribo,
  // cantidadRecibida, confirmarExcepcion (true si el usuario ya confirmó la excepción RN-F04-003)
  async function registrarMIGO(formData) {
    try {
      setRegistrando(true);
      setError(null);
      const entrada = await useCases.migo.registrar.execute(formData);
      await loadEntradas();
      return entrada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al registrar la entrada en SAP (MIGO)');
      return null;
    } finally {
      setRegistrando(false);
    }
  }

  return {
    entradas,
    loading,
    registrando,
    error,
    refetch: loadEntradas,
    registrarMIGO,
  };
}
