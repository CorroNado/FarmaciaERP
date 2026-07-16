import { useState, useCallback } from 'react';
import { contabilidadRepository } from '../../infrastructure/repositories/contabilidadRepository';
import { getPlanCuentasUseCase, crearCuentaContableUseCase } from '../../domain/usecases/contabilidad/getPlanCuentasUseCase';
import { getBalanceGeneralUseCase } from '../../domain/usecases/contabilidad/getBalanceGeneralUseCase';
import { getEstadoResultadosUseCase } from '../../domain/usecases/contabilidad/getEstadoResultadosUseCase';

export const useContabilidad = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [planCuentas, setPlanCuentas] = useState([]);
  const [balanceGeneral, setBalanceGeneral] = useState(null);
  const [estadoResultados, setEstadoResultados] = useState(null);

  const fetchPlanCuentas = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getPlanCuentasUseCase(contabilidadRepository)();
      setPlanCuentas(data);
    } catch (err) {
      setError(err.message || "Error al cargar el plan de cuentas");
    } finally {
      setLoading(false);
    }
  }, []);

  const addCuentaContable = async (nuevaCuenta) => {
    setLoading(true);
    try {
      await crearCuentaContableUseCase(contabilidadRepository)(nuevaCuenta);
      await fetchPlanCuentas();
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const fetchBalanceGeneral = useCallback(async (periodoId) => {
    setLoading(true);
    setError(null);
    try {
      const data = await getBalanceGeneralUseCase(contabilidadRepository)(periodoId);
      setBalanceGeneral(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchEstadoResultados = useCallback(async (periodoId) => {
    setLoading(true);
    setError(null);
    try {
      const data = await getEstadoResultadosUseCase(contabilidadRepository)(periodoId);
      setEstadoResultados(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    loading,
    error,
    planCuentas,
    balanceGeneral,
    estadoResultados,
    fetchPlanCuentas,
    addCuentaContable,
    fetchBalanceGeneral,
    fetchEstadoResultados
  };
};