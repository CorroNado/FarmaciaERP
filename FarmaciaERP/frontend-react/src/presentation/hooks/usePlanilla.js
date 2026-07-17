import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function usePlanilla() {
  const [planilla, setPlanilla] = useState(null);
  const [historial, setHistorial] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const loadHistorial = useCallback(async () => {
    try {
      const data = await useCases.planilla.getHistorial.execute();
      setHistorial(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener el historial de planillas');
      return [];
    }
  }, []);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      const data = await loadHistorial();
      if (cancelled) return;
      void data;
    })();
    return () => { cancelled = true; };
  }, [loadHistorial]);

  async function calcular(mes, anio) {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.planilla.calcular.execute(mes, anio);
      setPlanilla(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al calcular la planilla');
      setPlanilla(null);
      return null;
    } finally {
      setLoading(false);
    }
  }

  async function guardar(formData) {
    setSaving(true);
    setError(null);
    try {
      const data = await useCases.planilla.guardar.execute(formData);
      setPlanilla(data);
      await loadHistorial();
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al guardar la planilla');
      return null;
    } finally {
      setSaving(false);
    }
  }

  async function cargarGuardada(id) {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.planilla.getById.execute(id);
      setPlanilla(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al cargar la planilla');
      return null;
    } finally {
      setLoading(false);
    }
  }

  async function eliminarGuardada(id) {
    setLoading(true);
    setError(null);
    try {
      await useCases.planilla.eliminar.execute(id);
      if (planilla?.id === id) setPlanilla(null);
      await loadHistorial();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al eliminar la planilla');
      return false;
    } finally {
      setLoading(false);
    }
  }

  function limpiarPlanilla() {
    setPlanilla(null);
  }

  function actualizarBonoMetas(empleadoId, bonoMetas) {
    setPlanilla((prev) => {
      if (!prev) return prev;
      const detalles = prev.detalles.map((d) => (d.empleadoId === empleadoId ? { ...d, bonoMetas } : d));
      return { ...prev, detalles };
    });
  }

  return {
    planilla,
    historial,
    loading,
    saving,
    error,
    calcular,
    guardar,
    cargarGuardada,
    eliminarGuardada,
    limpiarPlanilla,
    actualizarBonoMetas,
    refetchHistorial: loadHistorial,
  };
}
