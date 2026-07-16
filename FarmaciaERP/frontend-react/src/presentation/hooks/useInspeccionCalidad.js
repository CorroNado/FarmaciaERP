import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useInspeccionCalidad() {
  const [inspecciones, setInspecciones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const loadInspecciones = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.inspeccionCalidad.getAll.execute({});
      setInspecciones(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las inspecciones de calidad');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadInspecciones();
  }, [loadInspecciones]);

  // formData: { entradaMercanciaId, muestreoConforme, registroSanitarioVigente, empaqueConforme }
  async function aprobarLote(formData) {
    try {
      setProcesando(true);
      setError(null);
      const inspeccion = await useCases.inspeccionCalidad.aprobar.execute(formData);
      await loadInspecciones();
      return inspeccion;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al aprobar el lote (Decisión de Empleo)');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // formData: { entradaMercanciaId, motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme }
  async function rechazarLote(formData) {
    try {
      setProcesando(true);
      setError(null);
      const inspeccion = await useCases.inspeccionCalidad.rechazar.execute(formData);
      await loadInspecciones();
      return inspeccion;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al rechazar el lote');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  return {
    inspecciones,
    loading,
    procesando,
    error,
    refetch: loadInspecciones,
    aprobarLote,
    rechazarLote,
  };
}
