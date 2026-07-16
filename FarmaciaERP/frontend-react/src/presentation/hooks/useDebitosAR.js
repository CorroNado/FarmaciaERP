import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useDebitosAR(contabilizacionARId) {
  const [debitos, setDebitos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarDebitos = useCallback(async () => {
    if (!contabilizacionARId) return [];
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.debitoAR.getAll.execute({ contabilizacionARId });
      setDebitos(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los débitos del lote');
      return [];
    } finally {
      setLoading(false);
    }
  }, [contabilizacionARId]);

  function actualizarEnLista(debito) {
    setDebitos((prev) => {
      const idx = prev.findIndex((d) => d.id === debito.id);
      if (idx === -1) return [...prev, debito];
      const copia = [...prev];
      copia[idx] = debito;
      return copia;
    });
  }

  // RN-AR4-01: cálculo automático del débito a partir de una receta rechazada o con débito confirmado (Fase 03)
  async function calcularDebito(recetaMedicaARId) {
    return ejecutar(() => useCases.debitoAR.calcular.execute({ recetaMedicaARId }),
      'Error al calcular el débito');
  }

  // ¿Débito Justificado? — Sí / No
  async function evaluarJustificacion(id, justificado) {
    return ejecutar(() => useCases.debitoAR.evaluarJustificacion.execute(id, { justificado }),
      'Error al evaluar la justificación del débito');
  }

  // Tramitar Reclamo (Área Técnica)
  async function tramitarReclamo(id) {
    return ejecutar(() => useCases.debitoAR.tramitarReclamo.execute(id),
      'Error al tramitar el reclamo ante el Área Técnica');
  }

  // Aplicar Ajustes Técnicos Contables — cierra el ciclo del débito
  async function aplicarAjusteTecnico(id) {
    return ejecutar(() => useCases.debitoAR.aplicarAjusteTecnico.execute(id),
      'Error al aplicar el ajuste técnico contable');
  }

  async function ejecutar(accion, mensajeError) {
    try {
      setProcesando(true);
      setError(null);
      const debito = await accion();
      actualizarEnLista(debito);
      return debito;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? mensajeError);
      return null;
    } finally {
      setProcesando(false);
    }
  }

  return {
    debitos,
    loading,
    procesando,
    error,
    cargarDebitos,
    calcularDebito,
    evaluarJustificacion,
    tramitarReclamo,
    aplicarAjusteTecnico,
  };
}
