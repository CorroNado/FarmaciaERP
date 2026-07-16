import { authService }         from '@/infrastructure/services/authService';
import { userRepository }      from '@/infrastructure/repositories/userRepository';
import { reportRepository }    from '@/infrastructure/repositories/reportRepository';
import { medicamentoRepository } from '@/infrastructure/repositories/medicamentoRepository';
import { clienteRepository }   from '@/infrastructure/repositories/clienteRepository';
import { ventaRepository }     from '@/infrastructure/repositories/ventaRepository';
import { cotizacionRepository } from '@/infrastructure/repositories/cotizacionRepository';
import { devolucionRepository } from '@/infrastructure/repositories/devolucionRepository';

import { loginUseCase }             from '@/domain/usecases/auth/LoginUseCase';
import { getUsersUseCase }          from '@/domain/usecases/users/getUsersUseCase';
import { getUserByIdUseCase }       from '@/domain/usecases/users/getUserByIdUseCase';
import { createUserUseCase }        from '@/domain/usecases/users/createUserUseCase';
import { editUserUseCase }          from '@/domain/usecases/users/editUserUseCase';
import { deleteUserUseCase }        from '@/domain/usecases/users/deleteUserUseCase';
import { getAccessReportUseCase }   from '@/domain/usecases/reports/getAccessReportUseCase';
import { getMedicamentosUseCase }   from '@/domain/usecases/medicamentos/getMedicamentosUseCase';
import { buscarClientePorDniUseCase } from '@/domain/usecases/clientes/buscarClientePorDniUseCase';
import { crearClienteRapidoUseCase }  from '@/domain/usecases/clientes/crearClienteRapidoUseCase';
import { crearVentaUseCase }        from '@/domain/usecases/ventas/crearVentaUseCase';
import { registrarPagoVentaUseCase } from '@/domain/usecases/ventas/registrarPagoVentaUseCase';
import { anularVentaUseCase }       from '@/domain/usecases/ventas/anularVentaUseCase';
import { getVentasUseCase }         from '@/domain/usecases/ventas/getVentasUseCase';
import { crearCotizacionUseCase }    from '@/domain/usecases/cotizaciones/crearCotizacionUseCase';
import { getCotizacionesUseCase }    from '@/domain/usecases/cotizaciones/getCotizacionesUseCase';
import { aceptarCotizacionUseCase }  from '@/domain/usecases/cotizaciones/aceptarCotizacionUseCase';
import { rechazarCotizacionUseCase } from '@/domain/usecases/cotizaciones/rechazarCotizacionUseCase';
import { crearDevolucionUseCase } from '@/domain/usecases/devoluciones/crearDevolucionUseCase';
import { getDevolucionesUseCase } from '@/domain/usecases/devoluciones/getDevolucionesUseCase';

import { proveedorRepository } from '@/infrastructure/repositories/proveedorRepository';
import { convenioRepository }  from '@/infrastructure/repositories/convenioRepository';
import { getProveedoresUseCase }  from '@/domain/usecases/proveedores/getProveedoresUseCase';
import { createProveedorUseCase } from '@/domain/usecases/proveedores/createProveedorUseCase';
import { updateProveedorUseCase } from '@/domain/usecases/proveedores/updateProveedorUseCase';
import { deleteProveedorUseCase } from '@/domain/usecases/proveedores/deleteProveedorUseCase';
import { getConveniosUseCase }    from '@/domain/usecases/convenios/getConveniosUseCase';
import { createConvenioUseCase }  from '@/domain/usecases/convenios/createConvenioUseCase';

import { solPedRepository } from '@/infrastructure/repositories/solPedRepository';
import { calcularMRPUseCase } from '@/domain/usecases/solped/calcularMRPUseCase';
import { getSolPedsUseCase }  from '@/domain/usecases/solped/getSolPedsUseCase';
import { crearSolPedUseCase } from '@/domain/usecases/solped/crearSolPedUseCase';
import { aprobarFuenteUseCase } from '@/domain/usecases/solped/aprobarFuenteUseCase';
import { rechazarSolPedUseCase } from '@/domain/usecases/solped/rechazarSolPedUseCase';

// LOG.04 — Fase 03: Orden de Compra
import { ordenCompraRepository } from '@/infrastructure/repositories/ordenCompraRepository';
import { getOrdenesCompraUseCase } from '@/domain/usecases/ordenCompra/getOrdenesCompraUseCase';
import { getOrdenCompraByIdUseCase } from '@/domain/usecases/ordenCompra/getOrdenCompraByIdUseCase';
import { crearOrdenCompraUseCase } from '@/domain/usecases/ordenCompra/crearOrdenCompraUseCase';
import { firmarOrdenCompraUseCase } from '@/domain/usecases/ordenCompra/firmarOrdenCompraUseCase';

// LOG.05 — Fase 04: Entrada de mercancía (MIGO)
import { migoRepository } from '@/infrastructure/repositories/migoRepository';
import { getEntradasMIGOUseCase } from '@/domain/usecases/migo/getEntradasMIGOUseCase';
import { getMIGOByIdUseCase } from '@/domain/usecases/migo/getMIGOByIdUseCase';
import { registrarMIGOUseCase } from '@/domain/usecases/migo/registrarMIGOUseCase';

// LOG.06 — Fase 05: inspección y aseguramiento de calidad (QA11)
import { inspeccionCalidadRepository } from '@/infrastructure/repositories/inspeccionCalidadRepository';
import { getInspeccionesCalidadUseCase } from '@/domain/usecases/inspeccionCalidad/getInspeccionesCalidadUseCase';
import { getInspeccionCalidadByIdUseCase } from '@/domain/usecases/inspeccionCalidad/getInspeccionCalidadByIdUseCase';
import { aprobarLoteUseCase } from '@/domain/usecases/inspeccionCalidad/aprobarLoteUseCase';
import { rechazarLoteUseCase } from '@/domain/usecases/inspeccionCalidad/rechazarLoteUseCase';

// LOG.07 — Fase 06: gestión de stocks y distribución capilar (ME27 / MB1B)
import { sucursalRepository } from '@/infrastructure/repositories/sucursalRepository';
import { getSucursalesUseCase } from '@/domain/usecases/sucursales/getSucursalesUseCase';
import { createSucursalUseCase } from '@/domain/usecases/sucursales/createSucursalUseCase';
import { ordenTrasladoRepository } from '@/infrastructure/repositories/ordenTrasladoRepository';
import { getOrdenesTrasladoUseCase } from '@/domain/usecases/ordenTraslado/getOrdenesTrasladoUseCase';
import { getOrdenTrasladoByIdUseCase } from '@/domain/usecases/ordenTraslado/getOrdenTrasladoByIdUseCase';
import { generarOrdenTrasladoUseCase } from '@/domain/usecases/ordenTraslado/generarOrdenTrasladoUseCase';
import { confirmarRecepcionUseCase } from '@/domain/usecases/ordenTraslado/confirmarRecepcionUseCase';

// LOG.07 — Fase 07: verificación de factura del proveedor (MIRO)
import { facturaMiroRepository } from '@/infrastructure/repositories/facturaMiroRepository';
import { getFacturasMiroUseCase } from '@/domain/usecases/facturaMiro/getFacturasMiroUseCase';
import { getFacturaMiroByIdUseCase } from '@/domain/usecases/facturaMiro/getFacturaMiroByIdUseCase';
import { registrarFacturaMiroUseCase } from '@/domain/usecases/facturaMiro/registrarFacturaMiroUseCase';

// LOG.08 — Fase 08: conciliación de 3 vías (3-Way Match / MRBR)
import { conciliacionRepository } from '@/infrastructure/repositories/conciliacionRepository';
import { getConciliacionesUseCase } from '@/domain/usecases/conciliacion/getConciliacionesUseCase';
import { getConciliacionByIdUseCase } from '@/domain/usecases/conciliacion/getConciliacionByIdUseCase';
import { ejecutarConciliacionUseCase } from '@/domain/usecases/conciliacion/ejecutarConciliacionUseCase';

// LOG.09 — Fase 09: gestión y ejecución del pago (F110)
import { pagoRepository } from '@/infrastructure/repositories/pagoRepository';
import { getPagosUseCase } from '@/domain/usecases/pago/getPagosUseCase';
import { getPagoByIdUseCase } from '@/domain/usecases/pago/getPagoByIdUseCase';
import { ejecutarPagoUseCase } from '@/domain/usecases/pago/ejecutarPagoUseCase';

// FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD)
import { cierreCajaRepository } from '@/infrastructure/repositories/cierreCajaRepository';
import { abrirCierreCajaUseCase } from '@/domain/usecases/cierreCaja/abrirCierreCajaUseCase';
import { registrarArqueoUseCase } from '@/domain/usecases/cierreCaja/registrarArqueoUseCase';
import { registrarJustificacionUseCase } from '@/domain/usecases/cierreCaja/registrarJustificacionUseCase';
import { enviarFisicosRecetasUseCase } from '@/domain/usecases/cierreCaja/enviarFisicosRecetasUseCase';
import { clasificarCopagoCoberturaUseCase } from '@/domain/usecases/cierreCaja/clasificarCopagoCoberturaUseCase';
import { getCierreCajaByIdUseCase } from '@/domain/usecases/cierreCaja/getCierreCajaByIdUseCase';
import { getCierresCajaUseCase } from '@/domain/usecases/cierreCaja/getCierresCajaUseCase';

export const useCases = {
  auth: {
    login: loginUseCase(authService),
  },
  users: {
    getAll:  getUsersUseCase(userRepository),
    getById: getUserByIdUseCase(userRepository),
    create:  createUserUseCase(userRepository),
    edit:    editUserUseCase(userRepository),
    delete:  deleteUserUseCase(userRepository),
  },
  reports: {
    getAccess: getAccessReportUseCase(reportRepository),
  },
  medicamentos: {
    getAll: getMedicamentosUseCase(medicamentoRepository),
  },
  clientes: {
    buscarPorDni: buscarClientePorDniUseCase(clienteRepository),
    crearRapido:  crearClienteRapidoUseCase(clienteRepository),
  },
  ventas: {
    crear:   crearVentaUseCase(ventaRepository),
    pagar:   registrarPagoVentaUseCase(ventaRepository),
    anular:  anularVentaUseCase(ventaRepository),
    getAll:  getVentasUseCase(ventaRepository),
  },
  cotizaciones: {
    crear:    crearCotizacionUseCase(cotizacionRepository),
    getAll:   getCotizacionesUseCase(cotizacionRepository),
    aceptar:  aceptarCotizacionUseCase(cotizacionRepository),
    rechazar: rechazarCotizacionUseCase(cotizacionRepository),
  },
  devoluciones: {
    crear:  crearDevolucionUseCase(devolucionRepository),
    getAll: getDevolucionesUseCase(devolucionRepository),
  },
  proveedores: {
    getAll: getProveedoresUseCase(proveedorRepository),
    create: createProveedorUseCase(proveedorRepository),
    update: updateProveedorUseCase(proveedorRepository),
    delete: deleteProveedorUseCase(proveedorRepository),
  },
  convenios: {
    getAll: getConveniosUseCase(convenioRepository),
    create: createConvenioUseCase(convenioRepository),
  },
  solped: {
    calcularMRP: calcularMRPUseCase(solPedRepository),
    getAll: getSolPedsUseCase(solPedRepository),
    crear: crearSolPedUseCase(solPedRepository),
    aprobarFuente: aprobarFuenteUseCase(solPedRepository),
    rechazar: rechazarSolPedUseCase(solPedRepository),
  },
  ordenCompra: {
    getAll: getOrdenesCompraUseCase(ordenCompraRepository),
    getById: getOrdenCompraByIdUseCase(ordenCompraRepository),
    crear: crearOrdenCompraUseCase(ordenCompraRepository),
    firmar: firmarOrdenCompraUseCase(ordenCompraRepository),
  },
  migo: {
    getAll: getEntradasMIGOUseCase(migoRepository),
    getById: getMIGOByIdUseCase(migoRepository),
    registrar: registrarMIGOUseCase(migoRepository),
  },
  inspeccionCalidad: {
    getAll: getInspeccionesCalidadUseCase(inspeccionCalidadRepository),
    getById: getInspeccionCalidadByIdUseCase(inspeccionCalidadRepository),
    aprobar: aprobarLoteUseCase(inspeccionCalidadRepository),
    rechazar: rechazarLoteUseCase(inspeccionCalidadRepository),
  },
  sucursales: {
    getAll: getSucursalesUseCase(sucursalRepository),
    create: createSucursalUseCase(sucursalRepository),
  },
  ordenTraslado: {
    getAll: getOrdenesTrasladoUseCase(ordenTrasladoRepository),
    getById: getOrdenTrasladoByIdUseCase(ordenTrasladoRepository),
    generar: generarOrdenTrasladoUseCase(ordenTrasladoRepository),
    confirmarRecepcion: confirmarRecepcionUseCase(ordenTrasladoRepository),
  },
  facturaMiro: {
    getAll: getFacturasMiroUseCase(facturaMiroRepository),
    getById: getFacturaMiroByIdUseCase(facturaMiroRepository),
    registrar: registrarFacturaMiroUseCase(facturaMiroRepository),
  },
  conciliacion: {
    getAll: getConciliacionesUseCase(conciliacionRepository),
    getById: getConciliacionByIdUseCase(conciliacionRepository),
    ejecutar: ejecutarConciliacionUseCase(conciliacionRepository),
  },
  pago: {
    getAll: getPagosUseCase(pagoRepository),
    getById: getPagoByIdUseCase(pagoRepository),
    ejecutar: ejecutarPagoUseCase(pagoRepository),
  },
  cierreCaja: {
    getAll: getCierresCajaUseCase(cierreCajaRepository),
    getById: getCierreCajaByIdUseCase(cierreCajaRepository),
    abrir: abrirCierreCajaUseCase(cierreCajaRepository),
    registrarArqueo: registrarArqueoUseCase(cierreCajaRepository),
    registrarJustificacion: registrarJustificacionUseCase(cierreCajaRepository),
    enviarFisicosRecetas: enviarFisicosRecetasUseCase(cierreCajaRepository),
    clasificarCopagoCobertura: clasificarCopagoCoberturaUseCase(cierreCajaRepository),
  },
};
