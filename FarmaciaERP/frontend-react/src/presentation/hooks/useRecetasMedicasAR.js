import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useRecetasMedicasAR(contabilizacionARId) {
  const [recetas, setRecetas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarRecetas = useCallback(async () => {
    if (!contabilizacionARId) return [];
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.recetaMedicaAR.getAll.execute({ contabilizacionARId });
      setRecetas(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las recetas médicas del lote');
      return [];
    } finally {
      setLoading(false);
    }
  }, [contabilizacionARId]);

  function actualizarEnLista(receta) {
    setRecetas((prev) => {
      const idx = prev.findIndex((r) => r.id === receta.id);
      if (idx === -1) return [...prev, receta];
      const copia = [...prev];
      copia[idx] = receta;
      return copia;
    });
  }

  // Recepción física de la receta médica dentro del lote consolidado (Fase 02)
  async function registrarReceta(formData) {
    try {
      setProcesando(true);
      setError(null);
      const receta = await useCases.recetaMedicaAR.registrar.execute({ ...formData, contabilizacionARId });
      actualizarEnLista(receta);
      return receta;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al registrar la receta médica');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // Validación de Troqueles, Firmas y Vigencia
  async function validarTroquelesFirmas(id, { valido, motivoRechazo }) {
    return ejecutar(() => useCases.recetaMedicaAR.validarTroquelesFirmas.execute(id, { valido, motivoRechazo }),
      'Error al validar troqueles, firmas y vigencia');
  }

  // Comparación contra la Pre-liquidación de la Aseguradora
  async function compararPreliquidacion(id, { coincide, inconsistencia }) {
    return ejecutar(() => useCases.recetaMedicaAR.compararPreliquidacion.execute(id, { coincide, inconsistencia }),
      'Error al comparar contra la pre-liquidación de la aseguradora');
  }

  // Registro de la Respuesta de la Aseguradora a la impugnación
  async function registrarRespuestaAseguradora(id, { aceptaImpugnacion }) {
    return ejecutar(() => useCases.recetaMedicaAR.registrarRespuestaAseguradora.execute(id, { aceptaImpugnacion }),
      'Error al registrar la respuesta de la aseguradora');
  }

  async function ejecutar(accion, mensajeError) {
    try {
      setProcesando(true);
      setError(null);
      const receta = await accion();
      actualizarEnLista(receta);
      return receta;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? mensajeError);
      return null;
    } finally {
      setProcesando(false);
    }
  }

  return {
    recetas,
    loading,
    procesando,
    error,
    cargarRecetas,
    registrarReceta,
    validarTroquelesFirmas,
    compararPreliquidacion,
    registrarRespuestaAseguradora,
  };
}
