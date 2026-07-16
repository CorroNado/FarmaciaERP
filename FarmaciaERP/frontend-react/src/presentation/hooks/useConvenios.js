import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useConvenios() {
  const [convenios, setConvenios] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadConvenios = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.convenios.getAll.execute({});
      setConvenios(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener convenios');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadConvenios();
  }, [loadConvenios]);

  async function createConvenio(formData) {
    try {
      setLoading(true);
      setError(null);
      await useCases.convenios.create.execute(formData);
      await loadConvenios();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al crear el convenio');
      return false;
    } finally {
      setLoading(false);
    }
  }

  return {
    convenios,
    loading,
    error,
    refetch: loadConvenios,
    createConvenio,
  };
}
