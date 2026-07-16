import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useCierreCaja() {
  const [cierre, setCierre] = useState(null);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarCierre = useCallback(async (id) => {
    if (!id) return null;
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.cierreCaja.getById.execute(id);
      setCierre(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener el cierre de caja');
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  // 1.1 - Emitir Reporte Consolidado de Ventas del Mostrador
  async function abrirCierre({ numero, sucursalId, reporteVentas }) {
    try {
      setProcesando(true);
      setError(null);
      const nuevo = await useCases.cierreCaja.abrir.execute({ numero, sucursalId, reporteVentas });
      setCierre(nuevo);
      return nuevo;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al emitir el reporte consolidado de ventas');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // 1.2 - Realizar Arqueo Físico vs. Valores Registrados en Pantalla
  async function registrarArqueo(montoContado) {
    if (!cierre) return null;
    try {
      setProcesando(true);
      setError(null);
      const actualizado = await useCases.cierreCaja.registrarArqueo.execute(cierre.id, { montoContado });
      setCierre(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al registrar el arqueo físico');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // 2.1 - Registrar Justificación de Faltante o Sobrante en Sistema
  async function registrarJustificacion(justificacion) {
    if (!cierre) return null;
    try {
      setProcesando(true);
      setError(null);
      const actualizado = await useCases.cierreCaja.registrarJustificacion.execute(cierre.id, { justificacion });
      setCierre(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al registrar la justificación del descuadre');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // Fase 1 (rama de variación) - Enviar Físicos de Recetas a Oficina Central
  async function enviarFisicosRecetas() {
    if (!cierre) return null;
    try {
      setProcesando(true);
      setError(null);
      const actualizado = await useCases.cierreCaja.enviarFisicosRecetas.execute(cierre.id);
      setCierre(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al enviar los físicos de recetas');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  // 1.3 - Clasificar de forma Automática Copagos y Coberturas de Aseguradoras
  async function clasificarCopagoCobertura() {
    if (!cierre) return null;
    try {
      setProcesando(true);
      setError(null);
      const actualizado = await useCases.cierreCaja.clasificarCopagoCobertura.execute(cierre.id);
      setCierre(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al clasificar copagos y coberturas');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  function resetCierre() {
    setCierre(null);
    setError(null);
  }

  return {
    cierre,
    loading,
    procesando,
    error,
    cargarCierre,
    abrirCierre,
    registrarArqueo,
    registrarJustificacion,
    enviarFisicosRecetas,
    clasificarCopagoCobertura,
    resetCierre,
  };
}
