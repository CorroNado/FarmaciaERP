import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useLotesPago() {
  const [lotes, setLotes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarLotes = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.lotePago.getAll.execute();
      setLotes(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los lotes de pago');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertLote(actualizado) {
    setLotes((prev) => {
      const existe = prev.some((l) => l.id === actualizado.id);
      return existe ? prev.map((l) => (l.id === actualizado.id ? actualizado : l)) : [actualizado, ...prev];
    });
  }

  async function runAction(id, key, fn) {
    try {
      setProcesandoId(id ?? key);
      setError(null);
      const actualizado = await fn();
      upsertLote(actualizado);
      return actualizado;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error al procesar el lote de pagos');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // Inicio del armado del lote — a partir de los ajustes contables regularizados en Fase 03
  const iniciarLote = (ajusteContableIds) =>
    runAction('nuevo', 'nuevo', () => useCases.lotePago.iniciar.execute({ ajusteContableIds }));

  // 4.1 - Priorizar Proveedores Críticos de Medicamentos
  const priorizarProveedoresCriticos = (id) =>
    runAction(id, null, () => useCases.lotePago.priorizar.execute(id));

  // 4.2 - Negociar Descuento por Pronto Pago
  const negociarDescuentoProntoPago = (id, descuentoPct) =>
    runAction(id, null, () => useCases.lotePago.negociarDescuento.execute(id, { descuentoPct }));

  // 4.3 - Preparar Lote de Pagos (F110 SAP)
  const prepararLotePagos = (id) =>
    runAction(id, null, () => useCases.lotePago.preparar.execute(id));

  // 4.4 - Verificar Fondos y Validar Lote
  const verificarFondosYValidarLote = (id) =>
    runAction(id, null, () => useCases.lotePago.verificarFondos.execute(id));

  // 4.4 (cont.) - Someter Lote al Comité Semanal de Tesorería
  const someterLoteAComite = (id) =>
    runAction(id, null, () => useCases.lotePago.someterComite.execute(id));

  // 4.4 (cont.) - Corregir Lote según Observaciones
  const corregirLote = (id) =>
    runAction(id, null, () => useCases.lotePago.corregir.execute(id));

  // 4.5 - Ejecutar Pagos y Conciliar en SAP FI-AP
  const ejecutarPagosYConciliar = (id) =>
    runAction(id, null, () => useCases.lotePago.ejecutarConciliar.execute(id));

  return {
    lotes,
    loading,
    procesandoId,
    error,
    cargarLotes,
    iniciarLote,
    priorizarProveedoresCriticos,
    negociarDescuentoProntoPago,
    prepararLotePagos,
    verificarFondosYValidarLote,
    someterLoteAComite,
    corregirLote,
    ejecutarPagosYConciliar,
  };
}
