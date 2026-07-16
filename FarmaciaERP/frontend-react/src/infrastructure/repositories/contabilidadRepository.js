import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const contabilidadRepository = {
  getPlanCuentas: async () => {
    // Si no tienes dado de alta el endpoint en ENDPOINTS aún, puedes mapearlo temporalmente así:
    const url = ENDPOINTS?.contabilidad?.planCuentas ?? '/api/contabilidad/plan-cuentas';
    const { data } = await apiClient.get(url);
    return data;
  },

  crearCuentaContable: async (cuenta) => {
    const url = ENDPOINTS?.contabilidad?.crearCuenta ?? '/api/contabilidad/plan-cuentas';
    const { data } = await apiClient.post(url, cuenta);
    return data;
  },

  getLibroDiario: async (fechaInicio, fechaFin) => {
    const url = ENDPOINTS?.contabilidad?.libroDiario ?? '/api/contabilidad/libro-diario';
    const { data } = await apiClient.get(url, {
      params: { desde: fechaInicio, hasta: fechaFin }
    });
    return data;
  },

  getBalanceGeneral: async (periodoId) => {
    const url = ENDPOINTS?.contabilidad?.balanceGeneral ?? '/api/contabilidad/reportes/balance-general';
    const { data } = await apiClient.get(url, {
      params: { periodo: periodoId }
    });
    return data;
  },

  getEstadoResultados: async (periodoId) => {
    const url = ENDPOINTS?.contabilidad?.estadoResultados ?? '/api/contabilidad/reportes/estado-resultados';
    const { data } = await apiClient.get(url, {
      params: { periodo: periodoId }
    });
    return data;
  },

  getActivosFijos: async () => {
    const url = ENDPOINTS?.contabilidad?.activosFijos ?? '/api/contabilidad/activos-fijos';
    const { data } = await apiClient.get(url);
    return data;
  },

  crearActivoFijo: async (activo) => {
    const url = ENDPOINTS?.contabilidad?.crearActivoFijo ?? '/api/contabilidad/activos-fijos';
    const { data } = await apiClient.post(url, activo);
    return data;
  }
};