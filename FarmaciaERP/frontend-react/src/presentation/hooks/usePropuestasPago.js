import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function usePropuestasPago() {
  const [propuestas, setPropuestas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarPropuestas = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.propuestaPago.getAll.execute();
      setPropuestas(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las propuestas de pago');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertPropuesta(actualizada) {
    setPropuestas((prev) => {
      const existe = prev.some((p) => p.id === actualizada.id);
      return existe ? prev.map((p) => (p.id === actualizada.id ? actualizada : p)) : [actualizada, ...prev];
    });
  }

  async function runAction(id, key, fn) {
    try {
      setProcesandoId(id ?? key);
      setError(null);
      const actualizada = await fn();
      upsertPropuesta(actualizada);
      return actualizada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error al procesar la propuesta de pago');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // Inicio de la propuesta — a partir del lote de pagos conciliado en Fase 04
  const iniciarPropuesta = (lotePagoTesoreriaId) =>
    runAction('nuevo', 'nuevo', () => useCases.propuestaPago.iniciar.execute({ lotePagoTesoreriaId }));

  // 5.1 - Introducir Parámetros de Pago en F110
  const introducirParametrosPago = (id, parametros) =>
    runAction(id, null, () => useCases.propuestaPago.introducirParametros.execute(id, parametros));

  // 5.1 (cont.) - Ejecutar Propuesta de Pago Automática
  const ejecutarPropuestaAutomatica = (id) =>
    runAction(id, null, () => useCases.propuestaPago.ejecutarPropuesta.execute(id));

  // 5.2 - Revisar Reporte de Excepciones y Bloqueos
  const revisarReporteExcepciones = (id) =>
    runAction(id, null, () => useCases.propuestaPago.revisarExcepciones.execute(id));

  // 5.2 (cont.) - Ajustar Parámetros y Reejecutar Propuesta
  const ajustarParametrosYReejecutar = (id) =>
    runAction(id, null, () => useCases.propuestaPago.ajustarReejecutar.execute(id));

  // 5.2 (cont.) - Aprobar Propuesta de Pago Final
  const aprobarPropuestaFinal = (id) =>
    runAction(id, null, () => useCases.propuestaPago.aprobar.execute(id));

  // 5.3 - Ejecutar Ejecución de Pago
  const ejecutarPagoPropuesta = (id) =>
    runAction(id, null, () => useCases.propuestaPago.ejecutarPago.execute(id));

  // 5.3 (cont.) - Generar Archivos Bancarios Planos (IDoc / N43)
  const generarArchivosBancarios = (id) =>
    runAction(id, null, () => useCases.propuestaPago.generarArchivos.execute(id));

  return {
    propuestas,
    loading,
    procesandoId,
    error,
    cargarPropuestas,
    iniciarPropuesta,
    introducirParametrosPago,
    ejecutarPropuestaAutomatica,
    revisarReporteExcepciones,
    ajustarParametrosYReejecutar,
    aprobarPropuestaFinal,
    ejecutarPagoPropuesta,
    generarArchivosBancarios,
  };
}
