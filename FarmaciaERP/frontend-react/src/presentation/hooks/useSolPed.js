import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useSolPed() {
  const [solpeds, setSolpeds] = useState([]);
  const [sugerencia, setSugerencia] = useState([]);
  const [loading, setLoading] = useState(false);
  const [calculando, setCalculando] = useState(false);
  const [error, setError] = useState(null);

  const loadSolPeds = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.solped.getAll.execute({});
      setSolpeds(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las SolPed');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadSolPeds();
  }, [loadSolPeds]);

  async function calcularMRP(items) {
    setCalculando(true);
    setError(null);
    try {
      const data = await useCases.solped.calcularMRP.execute(items);
      setSugerencia(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al calcular la sugerencia MRP');
      return null;
    } finally {
      setCalculando(false);
    }
  }

  function clearSugerencia() {
    setSugerencia([]);
  }

  async function crearSolPed(formData) {
    try {
      setLoading(true);
      setError(null);
      await useCases.solped.crear.execute(formData);
      await loadSolPeds();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al crear la SolPed');
      return false;
    } finally {
      setLoading(false);
    }
  }

  async function aprobarFuente(id, data) {
    try {
      setLoading(true);
      setError(null);
      await useCases.solped.aprobarFuente.execute(id, data);
      await loadSolPeds();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al aprobar la fuente de aprovisionamiento');
      return false;
    } finally {
      setLoading(false);
    }
  }

  async function rechazarSolPed(id, motivo) {
    try {
      setLoading(true);
      setError(null);
      await useCases.solped.rechazar.execute(id, motivo);
      await loadSolPeds();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al rechazar la SolPed');
      return false;
    } finally {
      setLoading(false);
    }
  }

  return {
    solpeds,
    sugerencia,
    loading,
    calculando,
    error,
    refetch: loadSolPeds,
    calcularMRP,
    clearSugerencia,
    crearSolPed,
    aprobarFuente,
    rechazarSolPed,
  };
}
