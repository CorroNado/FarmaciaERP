import { CierreCaja } from '../../models/CierreCaja';

export const abrirCierreCajaUseCase = (cierreCajaRepository) => ({
  async execute({ numero, sucursalId, reporteVentas }) {
    if (!sucursalId) {
      throw new Error('Debe seleccionar la sucursal del cierre de caja');
    }
    if (!reporteVentas || Number(reporteVentas) <= 0) {
      throw new Error('El reporte consolidado de ventas debe ser mayor a cero');
    }

    const payload = CierreCaja.toApiAbrir({ numero, sucursalId, reporteVentas });
    const raw = await cierreCajaRepository.abrir(payload);
    return CierreCaja.fromApi(raw);
  },
});
