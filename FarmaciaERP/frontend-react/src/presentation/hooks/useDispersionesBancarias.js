import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useDispersionesBancarias() {
  const [dispersiones, setDispersiones] = useState([]);
  const [loading, setLoading] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarDispersiones = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.dispersionBancaria.getAll.execute();
      setDispersiones(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las dispersiones bancarias');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  function upsertDispersion(actualizada) {
    setDispersiones((prev) => {
      const existe = prev.some((d) => d.id === actualizada.id);
      return existe ? prev.map((d) => (d.id === actualizada.id ? actualizada : d)) : [actualizada, ...prev];
    });
  }

  async function runAction(id, key, fn) {
    try {
      setProcesandoId(id ?? key);
      setError(null);
      const actualizada = await fn();
      upsertDispersion(actualizada);
      return actualizada;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error al procesar la dispersión bancaria');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  // Inicio de la dispersión — a partir de la propuesta de pago concluida en Fase 05
  const iniciarDispersion = (propuestaPagoAutomaticaId) =>
    runAction('nuevo', 'nuevo', () => useCases.dispersionBancaria.iniciar.execute({ propuestaPagoAutomaticaId }));

  // 6.1 - Compilar Propuesta de Pago (F110) recibida desde la Fase 05
  const compilarPropuestaPago = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.compilar.execute(id));

  // 6.2 - Validar Propuesta de Duplicados / Bloqueos
  const validarPropuestaDuplicados = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.validar.execute(id));

  // 6.2 (cont.) - Corregir Errores y Reenviar Lote
  const corregirErroresYReenviarLote = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.corregirReenviar.execute(id));

  // 6.3 - Generar Archivo Bancario Plano (IDoc)
  const generarArchivoBancario = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.generarArchivo.execute(id));

  // 6.4 - Aplicar Firma Digital con Token Bancario
  const aplicarFirmaDigital = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.firmar.execute(id));

  // 6.5 - Ejecutar Transferencias en Banca Empresa (Token)
  const ejecutarTransferenciasBancarias = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.transferir.execute(id));

  // 6.6 - Importar Extracto Bancario Digital del Día (FF.5)
  const importarExtractoBancario = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.importarExtracto.execute(id));

  // 6.7 - Conciliar Cuentas Puente Financieras y Compensar Cuenta Transitoria del Banco
  const conciliarCuentasPuente = (id) =>
    runAction(id, null, () => useCases.dispersionBancaria.conciliar.execute(id));

  return {
    dispersiones,
    loading,
    procesandoId,
    error,
    cargarDispersiones,
    iniciarDispersion,
    compilarPropuestaPago,
    validarPropuestaDuplicados,
    corregirErroresYReenviarLote,
    generarArchivoBancario,
    aplicarFirmaDigital,
    ejecutarTransferenciasBancarias,
    importarExtractoBancario,
    conciliarCuentasPuente,
  };
}
