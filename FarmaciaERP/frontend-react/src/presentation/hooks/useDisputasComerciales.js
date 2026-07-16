import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useDisputasComerciales() {
  const [disputas, setDisputas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarDisputas = useCallback(async (filters = {}) => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.disputaComercial.getAll.execute(filters);
      setDisputas(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las disputas comerciales');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertDisputa(actualizada) {
    setDisputas((prev) => {
      const existe = prev.some((d) => d.id === actualizada.id);
      return existe ? prev.map((d) => (d.id === actualizada.id ? actualizada : d)) : [actualizada, ...prev];
    });
  }

  async function runAction(id, key, fn) {
    try {
      setProcesandoId(id ?? key);
      setError(null);
      const actualizada = await fn();
      upsertDisputa(actualizada);
      return actualizada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error al procesar la disputa comercial');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // Notificación de discrepancia recibida — abre la disputa desde la excepción de Fase 01
  const abrirDisputa = (excepcionFacturacionId) =>
    runAction('nueva', 'nueva', () => useCases.disputaComercial.abrir.execute({ excepcionFacturacionId }));

  // 2.1.1 - Extracción y Cotejo de Datos de Facturación vs. Acuerdos
  const cotejar = (id) => runAction(id, null, () => useCases.disputaComercial.cotejar.execute(id));

  // 2.1.2 - Cuantificación del Impacto Financiero de la Desviación
  const cuantificar = (id) => runAction(id, null, () => useCases.disputaComercial.cuantificar.execute(id));

  // 2.1.3 - Revisión y Validación de la Desviación
  const validarDesviacion = (id) => runAction(id, null, () => useCases.disputaComercial.validarDesviacion.execute(id));

  // 2.2.1 - Apertura de Disputa con Ejecutivo de Droguería (nueva ronda)
  const abrirNegociacion = (id) => runAction(id, null, () => useCases.disputaComercial.abrirNegociacion.execute(id));

  // 2.2.1 (cont.) - el proveedor envía su contrapropuesta
  const registrarContraoferta = (id, montoContraoferta) =>
    runAction(id, null, () => useCases.disputaComercial.contraoferta.execute(id, { montoContraoferta }));

  // 2.2.2 - ¿Se Absorbe la Diferencia Comercial? — Sí
  const aceptarAbsorcion = (id) => runAction(id, null, () => useCases.disputaComercial.aceptarAbsorcion.execute(id));

  // 2.2.2 - ¿Se Absorbe la Diferencia Comercial? — No, reabrir
  const reabrirNegociacion = (id) => runAction(id, null, () => useCases.disputaComercial.reabrirNegociacion.execute(id));

  // 2.3.1 - Registrar Resolución en Workflow del ERP
  const resolverWorkflow = (id) => runAction(id, null, () => useCases.disputaComercial.resolverWorkflow.execute(id));

  return {
    disputas,
    loading,
    procesandoId,
    error,
    cargarDisputas,
    abrirDisputa,
    cotejar,
    cuantificar,
    validarDesviacion,
    abrirNegociacion,
    registrarContraoferta,
    aceptarAbsorcion,
    reabrirNegociacion,
    resolverWorkflow,
  };
}
