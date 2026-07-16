import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useCobroAR(contabilizacionARId) {
  const [cobro, setCobro] = useState(null);
  const [loading, setLoading] = useState(false);
  const [procesando, setProcesando] = useState(false);
  const [error, setError] = useState(null);

  const cargarCobro = useCallback(async () => {
    if (!contabilizacionARId) return null;
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.cobroAR.getByContabilizacionAR.execute(contabilizacionARId);
      setCobro(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener el cobro del lote');
      return null;
    } finally {
      setLoading(false);
    }
  }, [contabilizacionARId]);

  // 5.1 - Interpretar Archivo de Transferencia bancaria
  async function interpretar(retenciones) {
    return ejecutar(() => useCases.cobroAR.interpretar.execute({ contabilizacionARId, retenciones }),
      'Error al interpretar el archivo de transferencia bancaria');
  }

  // 5.2 - Conciliar Comisiones de Tarjetas y Retenciones
  async function conciliarComisiones(comisionPct) {
    if (!cobro) return null;
    return ejecutar(() => useCases.cobroAR.conciliarComisiones.execute(cobro.id, { comisionPct }),
      'Error al conciliar comisiones y retenciones');
  }

  // 5.3 - Ingresar Ajuste Contable por Diferencia
  async function ajustarDiferencia() {
    if (!cobro) return null;
    return ejecutar(() => useCases.cobroAR.ajustarDiferencia.execute(cobro.id),
      'Error al ingresar el ajuste contable por diferencia');
  }

  // 6.1 - Registrar Ingreso de Dinero e Imputación en la cuenta del cliente
  async function registrarIngreso() {
    if (!cobro) return null;
    return ejecutar(() => useCases.cobroAR.registrarIngreso.execute(cobro.id),
      'Error al registrar el ingreso e imputación');
  }

  async function ejecutar(accion, mensajeError) {
    try {
      setProcesando(true);
      setError(null);
      const data = await accion();
      setCobro(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? mensajeError);
      return null;
    } finally {
      setProcesando(false);
    }
  }

  return {
    cobro,
    loading,
    procesando,
    error,
    cargarCobro,
    interpretar,
    conciliarComisiones,
    ajustarDiferencia,
    registrarIngreso,
  };
}
