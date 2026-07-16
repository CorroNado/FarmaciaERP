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

// FI-AP · Fase 01 — Captura de Excepciones de Facturación (Frontera Logística)
import { excepcionFacturacionRepository } from '@/infrastructure/repositories/excepcionFacturacionRepository';
import { capturarExcepcionFacturacionUseCase } from '@/domain/usecases/excepcionFacturacion/capturarExcepcionFacturacionUseCase';
import { revisarExcepcionFacturacionUseCase } from '@/domain/usecases/excepcionFacturacion/revisarExcepcionFacturacionUseCase';
import { clasificarExcepcionFacturacionUseCase } from '@/domain/usecases/excepcionFacturacion/clasificarExcepcionFacturacionUseCase';
import { getExcepcionFacturacionByIdUseCase } from '@/domain/usecases/excepcionFacturacion/getExcepcionFacturacionByIdUseCase';
import { getExcepcionesFacturacionUseCase } from '@/domain/usecases/excepcionFacturacion/getExcepcionesFacturacionUseCase';

// FI-AP · Fase 02 — Gestión Humana de Disputas Comerciales (Workflow ERP)
import { disputaComercialRepository } from '@/infrastructure/repositories/disputaComercialRepository';
import { abrirDisputaComercialUseCase } from '@/domain/usecases/disputaComercial/abrirDisputaComercialUseCase';
import { cotejarFacturaContratoUseCase } from '@/domain/usecases/disputaComercial/cotejarFacturaContratoUseCase';
import { cuantificarImpactoFinancieroUseCase } from '@/domain/usecases/disputaComercial/cuantificarImpactoFinancieroUseCase';
import { validarDesviacionUseCase } from '@/domain/usecases/disputaComercial/validarDesviacionUseCase';
import { abrirNegociacionUseCase } from '@/domain/usecases/disputaComercial/abrirNegociacionUseCase';
import { registrarContraofertaUseCase } from '@/domain/usecases/disputaComercial/registrarContraofertaUseCase';
import { aceptarAbsorcionUseCase } from '@/domain/usecases/disputaComercial/aceptarAbsorcionUseCase';
import { reabrirNegociacionUseCase } from '@/domain/usecases/disputaComercial/reabrirNegociacionUseCase';
import { resolverWorkflowDisputaUseCase } from '@/domain/usecases/disputaComercial/resolverWorkflowDisputaUseCase';
import { getDisputaComercialByIdUseCase } from '@/domain/usecases/disputaComercial/getDisputaComercialByIdUseCase';
import { getDisputasComercialesUseCase } from '@/domain/usecases/disputaComercial/getDisputasComercialesUseCase';

// FI-AP · Fase 03 — Ajustes Contables y Regularización (Cierre de Transacción)
import { ajusteContableRepository } from '@/infrastructure/repositories/ajusteContableRepository';
import { iniciarAjusteContableUseCase } from '@/domain/usecases/ajusteContable/iniciarAjusteContableUseCase';
import { registrarRecepcionNotaCreditoUseCase } from '@/domain/usecases/ajusteContable/registrarRecepcionNotaCreditoUseCase';
import { gestionarReclamoUseCase } from '@/domain/usecases/ajusteContable/gestionarReclamoUseCase';
import { evaluarYEnviarNotaCreditoUseCase } from '@/domain/usecases/ajusteContable/evaluarYEnviarNotaCreditoUseCase';
import { registrarNotaCreditoUseCase } from '@/domain/usecases/ajusteContable/registrarNotaCreditoUseCase';
import { ejecutarAsientoRegularizacionUseCase } from '@/domain/usecases/ajusteContable/ejecutarAsientoRegularizacionUseCase';
import { desbloquearPartidaUseCase } from '@/domain/usecases/ajusteContable/desbloquearPartidaUseCase';
import { getAjusteContableByIdUseCase } from '@/domain/usecases/ajusteContable/getAjusteContableByIdUseCase';
import { getAjustesContablesUseCase } from '@/domain/usecases/ajusteContable/getAjustesContablesUseCase';

// FI-AP · Fase 04 — Estrategia de Tesorería y Riesgo Sanitario (F110 SAP)
import { lotePagoRepository } from '@/infrastructure/repositories/lotePagoRepository';
import { iniciarLotePagoUseCase } from '@/domain/usecases/lotePago/iniciarLotePagoUseCase';
import { priorizarProveedoresCriticosUseCase } from '@/domain/usecases/lotePago/priorizarProveedoresCriticosUseCase';
import { negociarDescuentoProntoPagoUseCase } from '@/domain/usecases/lotePago/negociarDescuentoProntoPagoUseCase';
import { prepararLotePagosUseCase } from '@/domain/usecases/lotePago/prepararLotePagosUseCase';
import { verificarFondosYValidarLoteUseCase } from '@/domain/usecases/lotePago/verificarFondosYValidarLoteUseCase';
import { someterLoteAComiteUseCase } from '@/domain/usecases/lotePago/someterLoteAComiteUseCase';
import { corregirLoteUseCase } from '@/domain/usecases/lotePago/corregirLoteUseCase';
import { ejecutarPagosYConciliarUseCase } from '@/domain/usecases/lotePago/ejecutarPagosYConciliarUseCase';
import { getLotePagoByIdUseCase } from '@/domain/usecases/lotePago/getLotePagoByIdUseCase';
import { getLotesPagoUseCase } from '@/domain/usecases/lotePago/getLotesPagoUseCase';

// FI-AP · Fase 05 — Procesamiento Automático y Propuesta de Pago (F110)
import { propuestaPagoRepository } from '@/infrastructure/repositories/propuestaPagoRepository';
import { iniciarPropuestaPagoUseCase } from '@/domain/usecases/propuestaPago/iniciarPropuestaPagoUseCase';
import { introducirParametrosPagoUseCase } from '@/domain/usecases/propuestaPago/introducirParametrosPagoUseCase';
import { ejecutarPropuestaAutomaticaUseCase } from '@/domain/usecases/propuestaPago/ejecutarPropuestaAutomaticaUseCase';
import { revisarReporteExcepcionesUseCase } from '@/domain/usecases/propuestaPago/revisarReporteExcepcionesUseCase';
import { ajustarParametrosYReejecutarUseCase } from '@/domain/usecases/propuestaPago/ajustarParametrosYReejecutarUseCase';
import { aprobarPropuestaFinalUseCase } from '@/domain/usecases/propuestaPago/aprobarPropuestaFinalUseCase';
import { ejecutarPagoPropuestaUseCase } from '@/domain/usecases/propuestaPago/ejecutarPagoPropuestaUseCase';
import { generarArchivosBancariosUseCase } from '@/domain/usecases/propuestaPago/generarArchivosBancariosUseCase';
import { getPropuestaPagoByIdUseCase } from '@/domain/usecases/propuestaPago/getPropuestaPagoByIdUseCase';
import { getPropuestasPagoUseCase } from '@/domain/usecases/propuestaPago/getPropuestasPagoUseCase';

// FI-AP · Fase 06 — Dispersión Bancaria y Conciliación de Cierre (RN-AP6-01)
import { dispersionBancariaRepository } from '@/infrastructure/repositories/dispersionBancariaRepository';
import { iniciarDispersionBancariaUseCase } from '@/domain/usecases/dispersionBancaria/iniciarDispersionBancariaUseCase';
import { compilarPropuestaPagoUseCase } from '@/domain/usecases/dispersionBancaria/compilarPropuestaPagoUseCase';
import { validarPropuestaDuplicadosUseCase } from '@/domain/usecases/dispersionBancaria/validarPropuestaDuplicadosUseCase';
import { corregirErroresYReenviarLoteUseCase } from '@/domain/usecases/dispersionBancaria/corregirErroresYReenviarLoteUseCase';
import { generarArchivoBancarioUseCase } from '@/domain/usecases/dispersionBancaria/generarArchivoBancarioUseCase';
import { aplicarFirmaDigitalUseCase } from '@/domain/usecases/dispersionBancaria/aplicarFirmaDigitalUseCase';
import { ejecutarTransferenciasBancariasUseCase } from '@/domain/usecases/dispersionBancaria/ejecutarTransferenciasBancariasUseCase';
import { importarExtractoBancarioUseCase } from '@/domain/usecases/dispersionBancaria/importarExtractoBancarioUseCase';
import { conciliarCuentasPuenteUseCase } from '@/domain/usecases/dispersionBancaria/conciliarCuentasPuenteUseCase';
import { getDispersionBancariaByIdUseCase } from '@/domain/usecases/dispersionBancaria/getDispersionBancariaByIdUseCase';
import { getDispersionesBancariasUseCase } from '@/domain/usecases/dispersionBancaria/getDispersionesBancariasUseCase';

// FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD)
import { cierreCajaRepository } from '@/infrastructure/repositories/cierreCajaRepository';
import { abrirCierreCajaUseCase } from '@/domain/usecases/cierreCaja/abrirCierreCajaUseCase';
import { registrarArqueoUseCase } from '@/domain/usecases/cierreCaja/registrarArqueoUseCase';
import { registrarJustificacionUseCase } from '@/domain/usecases/cierreCaja/registrarJustificacionUseCase';
import { enviarFisicosRecetasUseCase } from '@/domain/usecases/cierreCaja/enviarFisicosRecetasUseCase';
import { clasificarCopagoCoberturaUseCase } from '@/domain/usecases/cierreCaja/clasificarCopagoCoberturaUseCase';
import { getCierreCajaByIdUseCase } from '@/domain/usecases/cierreCaja/getCierreCajaByIdUseCase';
import { getCierresCajaUseCase } from '@/domain/usecases/cierreCaja/getCierresCajaUseCase';

// FI-AR · Fase 02 — Contabilización y Declaración de Valores (SAP FI-AR)
import { contabilizacionARRepository } from '@/infrastructure/repositories/contabilizacionARRepository';
import { iniciarContabilizacionARUseCase } from '@/domain/usecases/contabilizacionAR/iniciarContabilizacionARUseCase';
import { conciliarLotesPOSUseCase } from '@/domain/usecases/contabilizacionAR/conciliarLotesPOSUseCase';
import { procesarAsientoContableUseCase } from '@/domain/usecases/contabilizacionAR/procesarAsientoContableUseCase';
import { revisarAjusteAsientosUseCase } from '@/domain/usecases/contabilizacionAR/revisarAjusteAsientosUseCase';
import { auditarRecetasUseCase } from '@/domain/usecases/contabilizacionAR/auditarRecetasUseCase';
import { solicitarDuplicadoRecetaUseCase } from '@/domain/usecases/contabilizacionAR/solicitarDuplicadoRecetaUseCase';
import { reintentarAuditoriaUseCase } from '@/domain/usecases/contabilizacionAR/reintentarAuditoriaUseCase';
import { consolidarLoteDespacharValijaUseCase } from '@/domain/usecases/contabilizacionAR/consolidarLoteDespacharValijaUseCase';
import { getContabilizacionARByIdUseCase } from '@/domain/usecases/contabilizacionAR/getContabilizacionARByIdUseCase';
import { getContabilizacionARByCierreCajaUseCase } from '@/domain/usecases/contabilizacionAR/getContabilizacionARByCierreCajaUseCase';
import { getContabilizacionesARUseCase } from '@/domain/usecases/contabilizacionAR/getContabilizacionesARUseCase';

// FI-AR · Fase 03 — Auditoría Médica e Impugnación de Recetas (ZFMR_RECHAZO / ZFMR_IMPUGNACION)
import { recetaMedicaARRepository } from '@/infrastructure/repositories/recetaMedicaARRepository';
import { registrarRecetaMedicaARUseCase } from '@/domain/usecases/recetaMedicaAR/registrarRecetaMedicaARUseCase';
import { validarTroquelesFirmasUseCase } from '@/domain/usecases/recetaMedicaAR/validarTroquelesFirmasUseCase';
import { compararPreliquidacionUseCase } from '@/domain/usecases/recetaMedicaAR/compararPreliquidacionUseCase';
import { registrarRespuestaAseguradoraUseCase } from '@/domain/usecases/recetaMedicaAR/registrarRespuestaAseguradoraUseCase';
import { getRecetaMedicaARByIdUseCase } from '@/domain/usecases/recetaMedicaAR/getRecetaMedicaARByIdUseCase';
import { getRecetasMedicasARUseCase } from '@/domain/usecases/recetaMedicaAR/getRecetasMedicasARUseCase';
import { puedeContinuarFase04UseCase } from '@/domain/usecases/recetaMedicaAR/puedeContinuarFase04UseCase';

// FI-AR · Fase 04 — Conciliación de Débitos y Ajustes Técnicos (RN-AR4-01)
import { debitoARRepository } from '@/infrastructure/repositories/debitoARRepository';
import { calcularDebitoARUseCase } from '@/domain/usecases/debitoAR/calcularDebitoARUseCase';
import { evaluarJustificacionDebitoARUseCase } from '@/domain/usecases/debitoAR/evaluarJustificacionDebitoARUseCase';
import { tramitarReclamoDebitoARUseCase } from '@/domain/usecases/debitoAR/tramitarReclamoDebitoARUseCase';
import { aplicarAjusteTecnicoContableUseCase } from '@/domain/usecases/debitoAR/aplicarAjusteTecnicoContableUseCase';
import { getDebitoARByIdUseCase } from '@/domain/usecases/debitoAR/getDebitoARByIdUseCase';
import { getDebitosARUseCase } from '@/domain/usecases/debitoAR/getDebitosARUseCase';
import { puedeContinuarFase05UseCase } from '@/domain/usecases/debitoAR/puedeContinuarFase05UseCase';
import { getAjusteTotalUseCase } from '@/domain/usecases/debitoAR/getAjusteTotalUseCase';

// FI-AR · Fase 05 — Procesamiento de Cobros e Imputación Bancaria (RN-AR5-01)
import { cobroARRepository } from '@/infrastructure/repositories/cobroARRepository';
import { interpretarArchivoTransferenciaUseCase } from '@/domain/usecases/cobroAR/interpretarArchivoTransferenciaUseCase';
import { conciliarComisionesRetencionesUseCase } from '@/domain/usecases/cobroAR/conciliarComisionesRetencionesUseCase';
import { ingresarAjusteContableCobroUseCase } from '@/domain/usecases/cobroAR/ingresarAjusteContableCobroUseCase';
import { registrarIngresoImputacionUseCase } from '@/domain/usecases/cobroAR/registrarIngresoImputacionUseCase';
import { getCobroARByIdUseCase } from '@/domain/usecases/cobroAR/getCobroARByIdUseCase';
import { getCobroARByContabilizacionUseCase } from '@/domain/usecases/cobroAR/getCobroARByContabilizacionUseCase';
import { getCobrosARUseCase } from '@/domain/usecases/cobroAR/getCobrosARUseCase';
import { puedeContinuarFase06UseCase } from '@/domain/usecases/cobroAR/puedeContinuarFase06UseCase';

// FI-AR · Fase 06 — Compensación Final y Análisis de Margen Neto (RN-AR6-01)
import { compensacionARRepository } from '@/infrastructure/repositories/compensacionARRepository';
import { aplicarCompensacionAutomaticaUseCase } from '@/domain/usecases/compensacionAR/aplicarCompensacionAutomaticaUseCase';
import { generarReporteRendimientoUseCase } from '@/domain/usecases/compensacionAR/generarReporteRendimientoUseCase';
import { confirmarSaldoLimpioUseCase } from '@/domain/usecases/compensacionAR/confirmarSaldoLimpioUseCase';
import { cerrarIngresosConvenioUseCase } from '@/domain/usecases/compensacionAR/cerrarIngresosConvenioUseCase';
import { getCompensacionARByIdUseCase } from '@/domain/usecases/compensacionAR/getCompensacionARByIdUseCase';
import { getCompensacionARByContabilizacionUseCase } from '@/domain/usecases/compensacionAR/getCompensacionARByContabilizacionUseCase';
import { getCompensacionesARUseCase } from '@/domain/usecases/compensacionAR/getCompensacionesARUseCase';
import { cicloFinalizadoUseCase } from '@/domain/usecases/compensacionAR/cicloFinalizadoUseCase';

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
  excepcionFacturacion: {
    getAll: getExcepcionesFacturacionUseCase(excepcionFacturacionRepository),
    getById: getExcepcionFacturacionByIdUseCase(excepcionFacturacionRepository),
    capturar: capturarExcepcionFacturacionUseCase(excepcionFacturacionRepository),
    revisar: revisarExcepcionFacturacionUseCase(excepcionFacturacionRepository),
    clasificar: clasificarExcepcionFacturacionUseCase(excepcionFacturacionRepository),
  },
  disputaComercial: {
    getAll: getDisputasComercialesUseCase(disputaComercialRepository),
    getById: getDisputaComercialByIdUseCase(disputaComercialRepository),
    abrir: abrirDisputaComercialUseCase(disputaComercialRepository),
    cotejar: cotejarFacturaContratoUseCase(disputaComercialRepository),
    cuantificar: cuantificarImpactoFinancieroUseCase(disputaComercialRepository),
    validarDesviacion: validarDesviacionUseCase(disputaComercialRepository),
    abrirNegociacion: abrirNegociacionUseCase(disputaComercialRepository),
    contraoferta: registrarContraofertaUseCase(disputaComercialRepository),
    aceptarAbsorcion: aceptarAbsorcionUseCase(disputaComercialRepository),
    reabrirNegociacion: reabrirNegociacionUseCase(disputaComercialRepository),
    resolverWorkflow: resolverWorkflowDisputaUseCase(disputaComercialRepository),
  },
  ajusteContable: {
    getAll: getAjustesContablesUseCase(ajusteContableRepository),
    getById: getAjusteContableByIdUseCase(ajusteContableRepository),
    iniciar: iniciarAjusteContableUseCase(ajusteContableRepository),
    recepcionNotaCredito: registrarRecepcionNotaCreditoUseCase(ajusteContableRepository),
    gestionarReclamo: gestionarReclamoUseCase(ajusteContableRepository),
    evaluarEnvioNotaCredito: evaluarYEnviarNotaCreditoUseCase(ajusteContableRepository),
    registrarNotaCredito: registrarNotaCreditoUseCase(ajusteContableRepository),
    asientoRegularizacion: ejecutarAsientoRegularizacionUseCase(ajusteContableRepository),
    desbloquearPartida: desbloquearPartidaUseCase(ajusteContableRepository),
  },
  lotePago: {
    getAll: getLotesPagoUseCase(lotePagoRepository),
    getById: getLotePagoByIdUseCase(lotePagoRepository),
    iniciar: iniciarLotePagoUseCase(lotePagoRepository),
    priorizar: priorizarProveedoresCriticosUseCase(lotePagoRepository),
    negociarDescuento: negociarDescuentoProntoPagoUseCase(lotePagoRepository),
    preparar: prepararLotePagosUseCase(lotePagoRepository),
    verificarFondos: verificarFondosYValidarLoteUseCase(lotePagoRepository),
    someterComite: someterLoteAComiteUseCase(lotePagoRepository),
    corregir: corregirLoteUseCase(lotePagoRepository),
    ejecutarConciliar: ejecutarPagosYConciliarUseCase(lotePagoRepository),
  },
  propuestaPago: {
    getAll: getPropuestasPagoUseCase(propuestaPagoRepository),
    getById: getPropuestaPagoByIdUseCase(propuestaPagoRepository),
    iniciar: iniciarPropuestaPagoUseCase(propuestaPagoRepository),
    introducirParametros: introducirParametrosPagoUseCase(propuestaPagoRepository),
    ejecutarPropuesta: ejecutarPropuestaAutomaticaUseCase(propuestaPagoRepository),
    revisarExcepciones: revisarReporteExcepcionesUseCase(propuestaPagoRepository),
    ajustarReejecutar: ajustarParametrosYReejecutarUseCase(propuestaPagoRepository),
    aprobar: aprobarPropuestaFinalUseCase(propuestaPagoRepository),
    ejecutarPago: ejecutarPagoPropuestaUseCase(propuestaPagoRepository),
    generarArchivos: generarArchivosBancariosUseCase(propuestaPagoRepository),
  },
  dispersionBancaria: {
    getAll: getDispersionesBancariasUseCase(dispersionBancariaRepository),
    getById: getDispersionBancariaByIdUseCase(dispersionBancariaRepository),
    iniciar: iniciarDispersionBancariaUseCase(dispersionBancariaRepository),
    compilar: compilarPropuestaPagoUseCase(dispersionBancariaRepository),
    validar: validarPropuestaDuplicadosUseCase(dispersionBancariaRepository),
    corregirReenviar: corregirErroresYReenviarLoteUseCase(dispersionBancariaRepository),
    generarArchivo: generarArchivoBancarioUseCase(dispersionBancariaRepository),
    firmar: aplicarFirmaDigitalUseCase(dispersionBancariaRepository),
    transferir: ejecutarTransferenciasBancariasUseCase(dispersionBancariaRepository),
    importarExtracto: importarExtractoBancarioUseCase(dispersionBancariaRepository),
    conciliar: conciliarCuentasPuenteUseCase(dispersionBancariaRepository),
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
  contabilizacionAR: {
    iniciar: iniciarContabilizacionARUseCase(contabilizacionARRepository),
    conciliarLotesPOS: conciliarLotesPOSUseCase(contabilizacionARRepository),
    procesarAsiento: procesarAsientoContableUseCase(contabilizacionARRepository),
    revisarAjusteAsientos: revisarAjusteAsientosUseCase(contabilizacionARRepository),
    auditarRecetas: auditarRecetasUseCase(contabilizacionARRepository),
    solicitarDuplicadoReceta: solicitarDuplicadoRecetaUseCase(contabilizacionARRepository),
    reintentarAuditoria: reintentarAuditoriaUseCase(contabilizacionARRepository),
    consolidarLote: consolidarLoteDespacharValijaUseCase(contabilizacionARRepository),
    getById: getContabilizacionARByIdUseCase(contabilizacionARRepository),
    getByCierreCaja: getContabilizacionARByCierreCajaUseCase(contabilizacionARRepository),
    getAll: getContabilizacionesARUseCase(contabilizacionARRepository),
  },
  recetaMedicaAR: {
    registrar: registrarRecetaMedicaARUseCase(recetaMedicaARRepository),
    validarTroquelesFirmas: validarTroquelesFirmasUseCase(recetaMedicaARRepository),
    compararPreliquidacion: compararPreliquidacionUseCase(recetaMedicaARRepository),
    registrarRespuestaAseguradora: registrarRespuestaAseguradoraUseCase(recetaMedicaARRepository),
    getById: getRecetaMedicaARByIdUseCase(recetaMedicaARRepository),
    getAll: getRecetasMedicasARUseCase(recetaMedicaARRepository),
    puedeContinuarFase04: puedeContinuarFase04UseCase(recetaMedicaARRepository),
  },
  debitoAR: {
    calcular: calcularDebitoARUseCase(debitoARRepository),
    evaluarJustificacion: evaluarJustificacionDebitoARUseCase(debitoARRepository),
    tramitarReclamo: tramitarReclamoDebitoARUseCase(debitoARRepository),
    aplicarAjusteTecnico: aplicarAjusteTecnicoContableUseCase(debitoARRepository),
    getById: getDebitoARByIdUseCase(debitoARRepository),
    getAll: getDebitosARUseCase(debitoARRepository),
    puedeContinuarFase05: puedeContinuarFase05UseCase(debitoARRepository),
    getAjusteTotal: getAjusteTotalUseCase(debitoARRepository),
  },
  cobroAR: {
    interpretar: interpretarArchivoTransferenciaUseCase(cobroARRepository),
    conciliarComisiones: conciliarComisionesRetencionesUseCase(cobroARRepository),
    ajustarDiferencia: ingresarAjusteContableCobroUseCase(cobroARRepository),
    registrarIngreso: registrarIngresoImputacionUseCase(cobroARRepository),
    getById: getCobroARByIdUseCase(cobroARRepository),
    getByContabilizacionAR: getCobroARByContabilizacionUseCase(cobroARRepository),
    getAll: getCobrosARUseCase(cobroARRepository),
    puedeContinuarFase06: puedeContinuarFase06UseCase(cobroARRepository),
  },
  compensacionAR: {
    aplicar: aplicarCompensacionAutomaticaUseCase(compensacionARRepository),
    generarReporte: generarReporteRendimientoUseCase(compensacionARRepository),
    confirmarSaldo: confirmarSaldoLimpioUseCase(compensacionARRepository),
    cerrarIngresos: cerrarIngresosConvenioUseCase(compensacionARRepository),
    getById: getCompensacionARByIdUseCase(compensacionARRepository),
    getByContabilizacionAR: getCompensacionARByContabilizacionUseCase(compensacionARRepository),
    getAll: getCompensacionesARUseCase(compensacionARRepository),
    cicloFinalizado: cicloFinalizadoUseCase(compensacionARRepository),
  },
};
