import { authService }         from '@/infrastructure/services/authService';
import { userRepository }      from '@/infrastructure/repositories/Seguridad/userRepository';
import { reportRepository }    from '@/infrastructure/repositories/Seguridad/reportRepository';
import { medicamentoRepository } from '@/infrastructure/repositories/Logistica/medicamentoRepository';
import { clienteRepository }   from '@/infrastructure/repositories/Ventas/clienteRepository';
import { ventaRepository }     from '@/infrastructure/repositories/Ventas/ventaRepository';
import { cotizacionRepository } from '@/infrastructure/repositories/Ventas/cotizacionRepository';
import { devolucionRepository } from '@/infrastructure/repositories/Ventas/devolucionRepository';

import { loginUseCase }             from '@/domain/usecases/Seguridad/auth/LoginUseCase';
import { getUsersUseCase }          from '@/domain/usecases/Seguridad/users/getUsersUseCase';
import { getUserByIdUseCase }       from '@/domain/usecases/Seguridad/users/getUserByIdUseCase';
import { createUserUseCase }        from '@/domain/usecases/Seguridad/users/createUserUseCase';
import { editUserUseCase }          from '@/domain/usecases/Seguridad/users/editUserUseCase';
import { deleteUserUseCase }        from '@/domain/usecases/Seguridad/users/deleteUserUseCase';
import { getAccessReportUseCase }   from '@/domain/usecases/Seguridad/reports/getAccessReportUseCase';
import { getMedicamentosUseCase }   from '@/domain/usecases/Logistica/medicamentos/getMedicamentosUseCase';
import { buscarClientePorDniUseCase } from '@/domain/usecases/Ventas/clientes/buscarClientePorDniUseCase';
import { crearClienteRapidoUseCase }  from '@/domain/usecases/Ventas/clientes/crearClienteRapidoUseCase';
import { crearVentaUseCase }        from '@/domain/usecases/Ventas/ventas/crearVentaUseCase';
import { registrarPagoVentaUseCase } from '@/domain/usecases/Ventas/ventas/registrarPagoVentaUseCase';
import { anularVentaUseCase }       from '@/domain/usecases/Ventas/ventas/anularVentaUseCase';
import { getVentasUseCase }         from '@/domain/usecases/Ventas/ventas/getVentasUseCase';
import { crearCotizacionUseCase }    from '@/domain/usecases/Ventas/cotizaciones/crearCotizacionUseCase';
import { getCotizacionesUseCase }    from '@/domain/usecases/Ventas/cotizaciones/getCotizacionesUseCase';
import { aceptarCotizacionUseCase }  from '@/domain/usecases/Ventas/cotizaciones/aceptarCotizacionUseCase';
import { rechazarCotizacionUseCase } from '@/domain/usecases/Ventas/cotizaciones/rechazarCotizacionUseCase';
import { crearDevolucionUseCase } from '@/domain/usecases/Ventas/devoluciones/crearDevolucionUseCase';
import { getDevolucionesUseCase } from '@/domain/usecases/Ventas/devoluciones/getDevolucionesUseCase';

import { proveedorRepository } from '@/infrastructure/repositories/Logistica/proveedorRepository';
import { convenioRepository }  from '@/infrastructure/repositories/Ventas/convenioRepository';
import { getProveedoresUseCase }  from '@/domain/usecases/Logistica/proveedores/getProveedoresUseCase';
import { createProveedorUseCase } from '@/domain/usecases/Logistica/proveedores/createProveedorUseCase';
import { updateProveedorUseCase } from '@/domain/usecases/Logistica/proveedores/updateProveedorUseCase';
import { deleteProveedorUseCase } from '@/domain/usecases/Logistica/proveedores/deleteProveedorUseCase';
import { getConveniosUseCase }    from '@/domain/usecases/Ventas/convenios/getConveniosUseCase';
import { createConvenioUseCase }  from '@/domain/usecases/Ventas/convenios/createConvenioUseCase';

// RRHH.01 — Contratación: maestro de colaboradores y bajas inteligentes
import { empleadoRepository } from '@/infrastructure/repositories/RRHH/empleadoRepository';
import { getEmpleadosUseCase } from '@/domain/usecases/RRHH/empleados/getEmpleadosUseCase';
import { createEmpleadoUseCase } from '@/domain/usecases/RRHH/empleados/createEmpleadoUseCase';
import { updateEmpleadoUseCase } from '@/domain/usecases/RRHH/empleados/updateEmpleadoUseCase';
import { deleteEmpleadoUseCase } from '@/domain/usecases/RRHH/empleados/deleteEmpleadoUseCase';
import { reactivarEmpleadoUseCase } from '@/domain/usecases/RRHH/empleados/reactivarEmpleadoUseCase';
import { darBajaSinTurnosUseCase } from '@/domain/usecases/RRHH/empleados/darBajaSinTurnosUseCase';
import { darBajaInmediataUseCase } from '@/domain/usecases/RRHH/empleados/darBajaInmediataUseCase';
import { programarBajaUseCase } from '@/domain/usecases/RRHH/empleados/programarBajaUseCase';
import { getAuditoriaEmpleadosUseCase } from '@/domain/usecases/RRHH/empleados/getAuditoriaEmpleadosUseCase';

// RRHH.02 — Control de Asistencia: programación, marcación y auditoría de turnos
import { asistenciaRepository } from '@/infrastructure/repositories/RRHH/asistenciaRepository';
import { getAsistenciasUseCase } from '@/domain/usecases/RRHH/asistencias/getAsistenciasUseCase';
import { programarAsistenciaUseCase } from '@/domain/usecases/RRHH/asistencias/programarAsistenciaUseCase';
import { marcarEntradaUseCase } from '@/domain/usecases/RRHH/asistencias/marcarEntradaUseCase';
import { marcarSalidaUseCase } from '@/domain/usecases/RRHH/asistencias/marcarSalidaUseCase';
import { justificarInasistenciaUseCase } from '@/domain/usecases/RRHH/asistencias/justificarInasistenciaUseCase';
import { editarAsistenciaUseCase } from '@/domain/usecases/RRHH/asistencias/editarAsistenciaUseCase';
import { eliminarAsistenciaUseCase } from '@/domain/usecases/RRHH/asistencias/eliminarAsistenciaUseCase';
import { getAuditoriaAsistenciaUseCase } from '@/domain/usecases/RRHH/asistencias/getAuditoriaAsistenciaUseCase';

// RRHH.03 — Nómina / Planilla: cálculo mensual del sueldo neto por colaborador
import { planillaRepository } from '@/infrastructure/repositories/RRHH/planillaRepository';
import { calcularPlanillaUseCase } from '@/domain/usecases/RRHH/planilla/calcularPlanillaUseCase';
import { guardarPlanillaUseCase } from '@/domain/usecases/RRHH/planilla/guardarPlanillaUseCase';
import { getHistorialPlanillasUseCase } from '@/domain/usecases/RRHH/planilla/getHistorialPlanillasUseCase';
import { getPlanillaByIdUseCase } from '@/domain/usecases/RRHH/planilla/getPlanillaByIdUseCase';
import { getPlanillaByMesAnioUseCase } from '@/domain/usecases/RRHH/planilla/getPlanillaByMesAnioUseCase';
import { eliminarPlanillaUseCase } from '@/domain/usecases/RRHH/planilla/eliminarPlanillaUseCase';

import { solPedRepository } from '@/infrastructure/repositories/Logistica/solPedRepository';
import { calcularMRPUseCase } from '@/domain/usecases/Logistica/solped/calcularMRPUseCase';
import { getSolPedsUseCase }  from '@/domain/usecases/Logistica/solped/getSolPedsUseCase';
import { crearSolPedUseCase } from '@/domain/usecases/Logistica/solped/crearSolPedUseCase';
import { aprobarFuenteUseCase } from '@/domain/usecases/Logistica/solped/aprobarFuenteUseCase';
import { rechazarSolPedUseCase } from '@/domain/usecases/Logistica/solped/rechazarSolPedUseCase';

// LOG.04 — Fase 03: Orden de Compra
import { ordenCompraRepository } from '@/infrastructure/repositories/Logistica/ordenCompraRepository';
import { getOrdenesCompraUseCase } from '@/domain/usecases/Logistica/ordenCompra/getOrdenesCompraUseCase';
import { getOrdenCompraByIdUseCase } from '@/domain/usecases/Logistica/ordenCompra/getOrdenCompraByIdUseCase';
import { crearOrdenCompraUseCase } from '@/domain/usecases/Logistica/ordenCompra/crearOrdenCompraUseCase';
import { firmarOrdenCompraUseCase } from '@/domain/usecases/Logistica/ordenCompra/firmarOrdenCompraUseCase';

// LOG.05 — Fase 04: Entrada de mercancía (MIGO)
import { migoRepository } from '@/infrastructure/repositories/Logistica/migoRepository';
import { getEntradasMIGOUseCase } from '@/domain/usecases/Logistica/migo/getEntradasMIGOUseCase';
import { getMIGOByIdUseCase } from '@/domain/usecases/Logistica/migo/getMIGOByIdUseCase';
import { registrarMIGOUseCase } from '@/domain/usecases/Logistica/migo/registrarMIGOUseCase';

// LOG.06 — Fase 05: inspección y aseguramiento de calidad (QA11)
import { inspeccionCalidadRepository } from '@/infrastructure/repositories/Logistica/inspeccionCalidadRepository';
import { getInspeccionesCalidadUseCase } from '@/domain/usecases/Logistica/inspeccionCalidad/getInspeccionesCalidadUseCase';
import { getInspeccionCalidadByIdUseCase } from '@/domain/usecases/Logistica/inspeccionCalidad/getInspeccionCalidadByIdUseCase';
import { aprobarLoteUseCase } from '@/domain/usecases/Logistica/inspeccionCalidad/aprobarLoteUseCase';
import { rechazarLoteUseCase } from '@/domain/usecases/Logistica/inspeccionCalidad/rechazarLoteUseCase';

// LOG.07 — Fase 06: gestión de stocks y distribución capilar (ME27 / MB1B)
import { sucursalRepository } from '@/infrastructure/repositories/Logistica/sucursalRepository';
import { getSucursalesUseCase } from '@/domain/usecases/Logistica/sucursales/getSucursalesUseCase';
import { createSucursalUseCase } from '@/domain/usecases/Logistica/sucursales/createSucursalUseCase';
import { ordenTrasladoRepository } from '@/infrastructure/repositories/Logistica/ordenTrasladoRepository';
import { getOrdenesTrasladoUseCase } from '@/domain/usecases/Logistica/ordenTraslado/getOrdenesTrasladoUseCase';
import { getOrdenTrasladoByIdUseCase } from '@/domain/usecases/Logistica/ordenTraslado/getOrdenTrasladoByIdUseCase';
import { generarOrdenTrasladoUseCase } from '@/domain/usecases/Logistica/ordenTraslado/generarOrdenTrasladoUseCase';
import { confirmarRecepcionUseCase } from '@/domain/usecases/Logistica/ordenTraslado/confirmarRecepcionUseCase';

// LOG.07 — Fase 07: verificación de factura del proveedor (MIRO)
import { facturaMiroRepository } from '@/infrastructure/repositories/Logistica/facturaMiroRepository';
import { getFacturasMiroUseCase } from '@/domain/usecases/Logistica/facturaMiro/getFacturasMiroUseCase';
import { getFacturaMiroByIdUseCase } from '@/domain/usecases/Logistica/facturaMiro/getFacturaMiroByIdUseCase';
import { registrarFacturaMiroUseCase } from '@/domain/usecases/Logistica/facturaMiro/registrarFacturaMiroUseCase';

// LOG.08 — Fase 08: conciliación de 3 vías (3-Way Match / MRBR)
import { conciliacionRepository } from '@/infrastructure/repositories/Contabilidad/conciliacionRepository';
import { getConciliacionesUseCase } from '@/domain/usecases/Contabilidad/conciliacion/getConciliacionesUseCase';
import { getConciliacionByIdUseCase } from '@/domain/usecases/Contabilidad/conciliacion/getConciliacionByIdUseCase';
import { ejecutarConciliacionUseCase } from '@/domain/usecases/Contabilidad/conciliacion/ejecutarConciliacionUseCase';

// LOG.09 — Fase 09: gestión y ejecución del pago (F110)
import { pagoRepository } from '@/infrastructure/repositories/FI_AP/pagoRepository';
import { getPagosUseCase } from '@/domain/usecases/FI_AP/pago/getPagosUseCase';
import { getPagoByIdUseCase } from '@/domain/usecases/FI_AP/pago/getPagoByIdUseCase';
import { ejecutarPagoUseCase } from '@/domain/usecases/FI_AP/pago/ejecutarPagoUseCase';

// FI-AP · Fase 01 — Captura de Excepciones de Facturación (Frontera Logística)
import { excepcionFacturacionRepository } from '@/infrastructure/repositories/FI_AR/excepcionFacturacionRepository';
import { capturarExcepcionFacturacionUseCase } from '@/domain/usecases/FI_AR/excepcionFacturacion/capturarExcepcionFacturacionUseCase';
import { revisarExcepcionFacturacionUseCase } from '@/domain/usecases/FI_AR/excepcionFacturacion/revisarExcepcionFacturacionUseCase';
import { clasificarExcepcionFacturacionUseCase } from '@/domain/usecases/FI_AR/excepcionFacturacion/clasificarExcepcionFacturacionUseCase';
import { getExcepcionFacturacionByIdUseCase } from '@/domain/usecases/FI_AR/excepcionFacturacion/getExcepcionFacturacionByIdUseCase';
import { getExcepcionesFacturacionUseCase } from '@/domain/usecases/FI_AR/excepcionFacturacion/getExcepcionesFacturacionUseCase';

// FI-AP · Fase 02 — Gestión Humana de Disputas Comerciales (Workflow ERP)
import { disputaComercialRepository } from '@/infrastructure/repositories/FI_AR/disputaComercialRepository';
import { abrirDisputaComercialUseCase } from '@/domain/usecases/FI_AR/disputaComercial/abrirDisputaComercialUseCase';
import { cotejarFacturaContratoUseCase } from '@/domain/usecases/FI_AR/disputaComercial/cotejarFacturaContratoUseCase';
import { cuantificarImpactoFinancieroUseCase } from '@/domain/usecases/FI_AR/disputaComercial/cuantificarImpactoFinancieroUseCase';
import { validarDesviacionUseCase } from '@/domain/usecases/FI_AR/disputaComercial/validarDesviacionUseCase';
import { abrirNegociacionUseCase } from '@/domain/usecases/FI_AR/disputaComercial/abrirNegociacionUseCase';
import { registrarContraofertaUseCase } from '@/domain/usecases/FI_AR/disputaComercial/registrarContraofertaUseCase';
import { aceptarAbsorcionUseCase } from '@/domain/usecases/FI_AR/disputaComercial/aceptarAbsorcionUseCase';
import { reabrirNegociacionUseCase } from '@/domain/usecases/FI_AR/disputaComercial/reabrirNegociacionUseCase';
import { resolverWorkflowDisputaUseCase } from '@/domain/usecases/FI_AR/disputaComercial/resolverWorkflowDisputaUseCase';
import { getDisputaComercialByIdUseCase } from '@/domain/usecases/FI_AR/disputaComercial/getDisputaComercialByIdUseCase';
import { getDisputasComercialesUseCase } from '@/domain/usecases/FI_AR/disputaComercial/getDisputasComercialesUseCase';

// FI-AP · Fase 03 — Ajustes Contables y Regularización (Cierre de Transacción)
import { ajusteContableRepository } from '@/infrastructure/repositories/Contabilidad/ajusteContableRepository';
import { iniciarAjusteContableUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/iniciarAjusteContableUseCase';
import { registrarRecepcionNotaCreditoUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/registrarRecepcionNotaCreditoUseCase';
import { gestionarReclamoUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/gestionarReclamoUseCase';
import { evaluarYEnviarNotaCreditoUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/evaluarYEnviarNotaCreditoUseCase';
import { registrarNotaCreditoUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/registrarNotaCreditoUseCase';
import { ejecutarAsientoRegularizacionUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/ejecutarAsientoRegularizacionUseCase';
import { desbloquearPartidaUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/desbloquearPartidaUseCase';
import { getAjusteContableByIdUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/getAjusteContableByIdUseCase';
import { getAjustesContablesUseCase } from '@/domain/usecases/Contabilidad/ajusteContable/getAjustesContablesUseCase';

// FI-AP · Fase 04 — Estrategia de Tesorería y Riesgo Sanitario (F110 SAP)
import { lotePagoRepository } from '@/infrastructure/repositories/FI_AP/lotePagoRepository';
import { iniciarLotePagoUseCase } from '@/domain/usecases/FI_AP/lotePago/iniciarLotePagoUseCase';
import { priorizarProveedoresCriticosUseCase } from '@/domain/usecases/FI_AP/lotePago/priorizarProveedoresCriticosUseCase';
import { negociarDescuentoProntoPagoUseCase } from '@/domain/usecases/FI_AP/lotePago/negociarDescuentoProntoPagoUseCase';
import { prepararLotePagosUseCase } from '@/domain/usecases/FI_AP/lotePago/prepararLotePagosUseCase';
import { verificarFondosYValidarLoteUseCase } from '@/domain/usecases/FI_AP/lotePago/verificarFondosYValidarLoteUseCase';
import { someterLoteAComiteUseCase } from '@/domain/usecases/FI_AP/lotePago/someterLoteAComiteUseCase';
import { corregirLoteUseCase } from '@/domain/usecases/FI_AP/lotePago/corregirLoteUseCase';
import { ejecutarPagosYConciliarUseCase } from '@/domain/usecases/FI_AP/lotePago/ejecutarPagosYConciliarUseCase';
import { getLotePagoByIdUseCase } from '@/domain/usecases/FI_AP/lotePago/getLotePagoByIdUseCase';
import { getLotesPagoUseCase } from '@/domain/usecases/FI_AP/lotePago/getLotesPagoUseCase';

// FI-AP · Fase 05 — Procesamiento Automático y Propuesta de Pago (F110)
import { propuestaPagoRepository } from '@/infrastructure/repositories/FI_AP/propuestaPagoRepository';
import { iniciarPropuestaPagoUseCase } from '@/domain/usecases/FI_AP/propuestaPago/iniciarPropuestaPagoUseCase';
import { introducirParametrosPagoUseCase } from '@/domain/usecases/FI_AP/propuestaPago/introducirParametrosPagoUseCase';
import { ejecutarPropuestaAutomaticaUseCase } from '@/domain/usecases/FI_AP/propuestaPago/ejecutarPropuestaAutomaticaUseCase';
import { revisarReporteExcepcionesUseCase } from '@/domain/usecases/FI_AP/propuestaPago/revisarReporteExcepcionesUseCase';
import { ajustarParametrosYReejecutarUseCase } from '@/domain/usecases/FI_AP/propuestaPago/ajustarParametrosYReejecutarUseCase';
import { aprobarPropuestaFinalUseCase } from '@/domain/usecases/FI_AP/propuestaPago/aprobarPropuestaFinalUseCase';
import { ejecutarPagoPropuestaUseCase } from '@/domain/usecases/FI_AP/propuestaPago/ejecutarPagoPropuestaUseCase';
import { generarArchivosBancariosUseCase } from '@/domain/usecases/FI_AP/propuestaPago/generarArchivosBancariosUseCase';
import { getPropuestaPagoByIdUseCase } from '@/domain/usecases/FI_AP/propuestaPago/getPropuestaPagoByIdUseCase';
import { getPropuestasPagoUseCase } from '@/domain/usecases/FI_AP/propuestaPago/getPropuestasPagoUseCase';

// FI-AP · Fase 06 — Dispersión Bancaria y Conciliación de Cierre (RN-AP6-01)
import { dispersionBancariaRepository } from '@/infrastructure/repositories/FI_AP/dispersionBancariaRepository';
import { iniciarDispersionBancariaUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/iniciarDispersionBancariaUseCase';
import { compilarPropuestaPagoUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/compilarPropuestaPagoUseCase';
import { validarPropuestaDuplicadosUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/validarPropuestaDuplicadosUseCase';
import { corregirErroresYReenviarLoteUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/corregirErroresYReenviarLoteUseCase';
import { generarArchivoBancarioUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/generarArchivoBancarioUseCase';
import { aplicarFirmaDigitalUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/aplicarFirmaDigitalUseCase';
import { ejecutarTransferenciasBancariasUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/ejecutarTransferenciasBancariasUseCase';
import { importarExtractoBancarioUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/importarExtractoBancarioUseCase';
import { conciliarCuentasPuenteUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/conciliarCuentasPuenteUseCase';
import { getDispersionBancariaByIdUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/getDispersionBancariaByIdUseCase';
import { getDispersionesBancariasUseCase } from '@/domain/usecases/FI_AP/dispersionBancaria/getDispersionesBancariasUseCase';

// FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD)
import { cierreCajaRepository } from '@/infrastructure/repositories/Ventas/cierreCajaRepository';
import { abrirCierreCajaUseCase } from '@/domain/usecases/Ventas/cierreCaja/abrirCierreCajaUseCase';
import { registrarArqueoUseCase } from '@/domain/usecases/Ventas/cierreCaja/registrarArqueoUseCase';
import { registrarJustificacionUseCase } from '@/domain/usecases/Ventas/cierreCaja/registrarJustificacionUseCase';
import { enviarFisicosRecetasUseCase } from '@/domain/usecases/Ventas/cierreCaja/enviarFisicosRecetasUseCase';
import { clasificarCopagoCoberturaUseCase } from '@/domain/usecases/Ventas/cierreCaja/clasificarCopagoCoberturaUseCase';
import { getCierreCajaByIdUseCase } from '@/domain/usecases/Ventas/cierreCaja/getCierreCajaByIdUseCase';
import { getCierresCajaUseCase } from '@/domain/usecases/Ventas/cierreCaja/getCierresCajaUseCase';

// FI-AR · Fase 02 — Contabilización y Declaración de Valores (SAP FI-AR)
import { contabilizacionARRepository } from '@/infrastructure/repositories/FI_AR/contabilizacionARRepository';
import { iniciarContabilizacionARUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/iniciarContabilizacionARUseCase';
import { conciliarLotesPOSUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/conciliarLotesPOSUseCase';
import { procesarAsientoContableUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/procesarAsientoContableUseCase';
import { revisarAjusteAsientosUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/revisarAjusteAsientosUseCase';
import { auditarRecetasUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/auditarRecetasUseCase';
import { solicitarDuplicadoRecetaUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/solicitarDuplicadoRecetaUseCase';
import { reintentarAuditoriaUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/reintentarAuditoriaUseCase';
import { consolidarLoteDespacharValijaUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/consolidarLoteDespacharValijaUseCase';
import { getContabilizacionARByIdUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/getContabilizacionARByIdUseCase';
import { getContabilizacionARByCierreCajaUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/getContabilizacionARByCierreCajaUseCase';
import { getContabilizacionesARUseCase } from '@/domain/usecases/FI_AR/contabilizacionAR/getContabilizacionesARUseCase';

// FI-AR · Fase 03 — Auditoría Médica e Impugnación de Recetas (ZFMR_RECHAZO / ZFMR_IMPUGNACION)
import { recetaMedicaARRepository } from '@/infrastructure/repositories/FI_AR/recetaMedicaARRepository';
import { registrarRecetaMedicaARUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/registrarRecetaMedicaARUseCase';
import { validarTroquelesFirmasUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/validarTroquelesFirmasUseCase';
import { compararPreliquidacionUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/compararPreliquidacionUseCase';
import { registrarRespuestaAseguradoraUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/registrarRespuestaAseguradoraUseCase';
import { getRecetaMedicaARByIdUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/getRecetaMedicaARByIdUseCase';
import { getRecetasMedicasARUseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/getRecetasMedicasARUseCase';
import { puedeContinuarFase04UseCase } from '@/domain/usecases/FI_AR/recetaMedicaAR/puedeContinuarFase04UseCase';

// FI-AR · Fase 04 — Conciliación de Débitos y Ajustes Técnicos (RN-AR4-01)
import { debitoARRepository } from '@/infrastructure/repositories/FI_AR/debitoARRepository';
import { calcularDebitoARUseCase } from '@/domain/usecases/FI_AR/debitoAR/calcularDebitoARUseCase';
import { evaluarJustificacionDebitoARUseCase } from '@/domain/usecases/FI_AR/debitoAR/evaluarJustificacionDebitoARUseCase';
import { tramitarReclamoDebitoARUseCase } from '@/domain/usecases/FI_AR/debitoAR/tramitarReclamoDebitoARUseCase';
import { aplicarAjusteTecnicoContableUseCase } from '@/domain/usecases/FI_AR/debitoAR/aplicarAjusteTecnicoContableUseCase';
import { getDebitoARByIdUseCase } from '@/domain/usecases/FI_AR/debitoAR/getDebitoARByIdUseCase';
import { getDebitosARUseCase } from '@/domain/usecases/FI_AR/debitoAR/getDebitosARUseCase';
import { puedeContinuarFase05UseCase } from '@/domain/usecases/FI_AR/debitoAR/puedeContinuarFase05UseCase';
import { getAjusteTotalUseCase } from '@/domain/usecases/FI_AR/debitoAR/getAjusteTotalUseCase';

// FI-AR · Fase 05 — Procesamiento de Cobros e Imputación Bancaria (RN-AR5-01)
import { cobroARRepository } from '@/infrastructure/repositories/FI_AR/cobroARRepository';
import { interpretarArchivoTransferenciaUseCase } from '@/domain/usecases/FI_AR/cobroAR/interpretarArchivoTransferenciaUseCase';
import { conciliarComisionesRetencionesUseCase } from '@/domain/usecases/FI_AR/cobroAR/conciliarComisionesRetencionesUseCase';
import { ingresarAjusteContableCobroUseCase } from '@/domain/usecases/FI_AR/cobroAR/ingresarAjusteContableCobroUseCase';
import { registrarIngresoImputacionUseCase } from '@/domain/usecases/FI_AR/cobroAR/registrarIngresoImputacionUseCase';
import { getCobroARByIdUseCase } from '@/domain/usecases/FI_AR/cobroAR/getCobroARByIdUseCase';
import { getCobroARByContabilizacionUseCase } from '@/domain/usecases/FI_AR/cobroAR/getCobroARByContabilizacionUseCase';
import { getCobrosARUseCase } from '@/domain/usecases/FI_AR/cobroAR/getCobrosARUseCase';
import { puedeContinuarFase06UseCase } from '@/domain/usecases/FI_AR/cobroAR/puedeContinuarFase06UseCase';

// FI-AR · Fase 06 — Compensación Final y Análisis de Margen Neto (RN-AR6-01)
import { compensacionARRepository } from '@/infrastructure/repositories/FI_AR/compensacionARRepository';
import { aplicarCompensacionAutomaticaUseCase } from '@/domain/usecases/FI_AR/compensacionAR/aplicarCompensacionAutomaticaUseCase';
import { generarReporteRendimientoUseCase } from '@/domain/usecases/FI_AR/compensacionAR/generarReporteRendimientoUseCase';
import { confirmarSaldoLimpioUseCase } from '@/domain/usecases/FI_AR/compensacionAR/confirmarSaldoLimpioUseCase';
import { cerrarIngresosConvenioUseCase } from '@/domain/usecases/FI_AR/compensacionAR/cerrarIngresosConvenioUseCase';
import { getCompensacionARByIdUseCase } from '@/domain/usecases/FI_AR/compensacionAR/getCompensacionARByIdUseCase';
import { getCompensacionARByContabilizacionUseCase } from '@/domain/usecases/FI_AR/compensacionAR/getCompensacionARByContabilizacionUseCase';
import { getCompensacionesARUseCase } from '@/domain/usecases/FI_AR/compensacionAR/getCompensacionesARUseCase';
import { cicloFinalizadoUseCase } from '@/domain/usecases/FI_AR/compensacionAR/cicloFinalizadoUseCase';

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
  empleados: {
    getAll: getEmpleadosUseCase(empleadoRepository),
    create: createEmpleadoUseCase(empleadoRepository),
    update: updateEmpleadoUseCase(empleadoRepository),
    delete: deleteEmpleadoUseCase(empleadoRepository),
    reactivar: reactivarEmpleadoUseCase(empleadoRepository),
    bajaSinTurnos: darBajaSinTurnosUseCase(empleadoRepository),
    bajaInmediata: darBajaInmediataUseCase(empleadoRepository),
    bajaProgramada: programarBajaUseCase(empleadoRepository),
    auditoria: getAuditoriaEmpleadosUseCase(empleadoRepository),
  },
  asistencias: {
    getAll: getAsistenciasUseCase(asistenciaRepository),
    programar: programarAsistenciaUseCase(asistenciaRepository),
    marcarEntrada: marcarEntradaUseCase(asistenciaRepository),
    marcarSalida: marcarSalidaUseCase(asistenciaRepository),
    justificar: justificarInasistenciaUseCase(asistenciaRepository),
    editar: editarAsistenciaUseCase(asistenciaRepository),
    eliminar: eliminarAsistenciaUseCase(asistenciaRepository),
    auditoria: getAuditoriaAsistenciaUseCase(asistenciaRepository),
  },
  planilla: {
    calcular: calcularPlanillaUseCase(planillaRepository),
    guardar: guardarPlanillaUseCase(planillaRepository),
    getHistorial: getHistorialPlanillasUseCase(planillaRepository),
    getById: getPlanillaByIdUseCase(planillaRepository),
    getByMesAnio: getPlanillaByMesAnioUseCase(planillaRepository),
    eliminar: eliminarPlanillaUseCase(planillaRepository),
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
