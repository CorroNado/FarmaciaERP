import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useAjustesContables() {
  const [ajustes, setAjustes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarAjustes = useCallback(async (filters = {}) => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.ajusteContable.getAll.execute(filters);
      setAjustes(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los ajustes contables');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertAjuste(actualizado) {
    setAjustes((prev) => {
      const existe = prev.some((a) => a.id === actualizado.id);
      return existe ? prev.map((a) => (a.id === actualizado.id ? actualizado : a)) : [actualizado, ...prev];
    });
  }

  async function runAction(id, key, fn) {
    try {
      setProcesandoId(id ?? key);
      setError(null);
      const actualizado = await fn();
      upsertAjuste(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error al procesar el ajuste contable');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // Inicio del cierre de transacción — a partir de la disputa resuelta en Fase 02
  const iniciarAjuste = (disputaComercialId) =>
    runAction('nuevo', 'nuevo', () => useCases.ajusteContable.iniciar.execute({ disputaComercialId }));

  // 3.1 - ¿Se Recibe Nota de Crédito? — Sí / No
  const registrarRecepcionNotaCredito = (id, recibida) =>
    runAction(id, null, () => useCases.ajusteContable.recepcionNotaCredito.execute(id, { recibida }));

  // 3.1.a - Gestionar Reclamo con el Laboratorio/Droguería
  const gestionarReclamo = (id) => runAction(id, null, () => useCases.ajusteContable.gestionarReclamo.execute(id));

  // 3.1.a (cont.) - el proveedor evalúa y envía la Nota de Crédito
  const evaluarEnvioNotaCredito = (id) =>
    runAction(id, null, () => useCases.ajusteContable.evaluarEnvioNotaCredito.execute(id));

  // 3.1.b - Registrar Nota de Crédito en SAP (recibida directamente)
  const registrarNotaCredito = (id) =>
    runAction(id, null, () => useCases.ajusteContable.registrarNotaCredito.execute(id));

  // 3.2 - Ejecutar Asiento de Regularización
  const ejecutarAsientoRegularizacion = (id) =>
    runAction(id, null, () => useCases.ajusteContable.asientoRegularizacion.execute(id));

  // 3.3 - Desbloquear Partida Presupuestaria / Actualizar Estado de Pago
  const desbloquearPartida = (id) =>
    runAction(id, null, () => useCases.ajusteContable.desbloquearPartida.execute(id));

  return {
    ajustes,
    loading,
    procesandoId,
    error,
    cargarAjustes,
    iniciarAjuste,
    registrarRecepcionNotaCredito,
    gestionarReclamo,
    evaluarEnvioNotaCredito,
    registrarNotaCredito,
    ejecutarAsientoRegularizacion,
    desbloquearPartida,
  };
}
