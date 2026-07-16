import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useSucursales() {
  const [sucursales, setSucursales] = useState([]);
  const [loading, setLoading] = useState(false);
  const [creando, setCreando] = useState(false);
  const [error, setError] = useState(null);

  const loadSucursales = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.sucursales.getAll.execute({});
      setSucursales(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las sucursales');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadSucursales();
  }, [loadSucursales]);

  async function createSucursal(formData) {
    try {
      setCreando(true);
      setError(null);
      const sucursal = await useCases.sucursales.create.execute(formData);
      await loadSucursales();
      return sucursal;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al crear la sucursal');
      return null;
    } finally {
      setCreando(false);
    }
  }

  return {
    sucursales,
    loading,
    creando,
    error,
    refetch: loadSucursales,
    createSucursal,
  };
}
