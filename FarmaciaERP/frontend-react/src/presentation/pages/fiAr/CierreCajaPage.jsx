import { useState, useMemo, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import Table    from '@/presentation/components/ui/Table';
import FiArRail, { CICLO_FI_AR_FASES } from '@/presentation/components/fiAr/FiArRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useCierreCaja } from '@/presentation/hooks/useCierreCaja';
import { useSucursales } from '@/presentation/hooks/useSucursales';
import { useCases } from '@/infrastructure';
import { ESTADO_CIERRE_CAJA_LABEL } from '@/domain/models/CierreCaja';

function fmt(n) {
  return 'S/ ' + (Math.round((n ?? 0) * 100) / 100).toFixed(2);
}

function ErrorBox({ error }) {
  if (!error) return null;
  return (
    <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
      {typeof error === 'string' ? error : 'Ocurrió un error'}
    </div>
  );
}

function Callout({ children, tone = 'default' }) {
  const tones = {
    default: 'bg-emerald-50 border-emerald-700 text-emerald-800',
    warn: 'bg-amber-50 border-amber-500 text-amber-800',
    ok: 'bg-emerald-50 border-emerald-600 text-emerald-800',
  };
  return (
    <div className={`border-l-4 rounded-r-lg px-4 py-3 text-sm ${tones[tone]}`}>
      {children}
    </div>
  );
}

function RuleTag({ children }) {
  return (
    <span className="font-mono text-[10px] bg-amber-100 text-amber-800 px-1.5 py-0.5 rounded mr-1.5">
      {children}
    </span>
  );
}

function LaneNote({ children }) {
  return (
    <div className="flex items-center gap-3 my-4 font-mono text-[11px] uppercase tracking-wide text-slate-400">
      <span className="flex-1 h-px bg-slate-200" />
      {children}
      <span className="flex-1 h-px bg-slate-200" />
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Cierre de caja (flujo de la Fase 01)
// ─────────────────────────────────────────────────────────────────────────
function CierreCajaFlowTab() {
  const {
    cierre, procesando, error,
    abrirCierre, registrarArqueo, registrarJustificacion,
    enviarFisicosRecetas, clasificarCopagoCobertura, resetCierre,
  } = useCierreCaja();
  const { sucursales, loading: loadingSucursales } = useSucursales();

  const [numero, setNumero] = useState('');
  const [sucursalId, setSucursalId] = useState('');
  const [reporteVentas, setReporteVentas] = useState('');
  const [arqueoInput, setArqueoInput] = useState('');
  const [justifInput, setJustifInput] = useState('');
  const [localError, setLocalError] = useState('');

  const sucursalOptions = sucursales.map((s) => ({ value: s.id, label: `${s.codigo} — ${s.nombre}` }));

  async function handleAbrir() {
    setLocalError('');
    if (!numero.trim()) { setLocalError('Ingresa el N° de Cierre de Caja'); return; }
    if (!sucursalId) { setLocalError('Selecciona la sucursal'); return; }
    if (!reporteVentas || Number(reporteVentas) <= 0) { setLocalError('El reporte de ventas debe ser mayor a cero'); return; }

    const nuevo = await abrirCierre({ numero, sucursalId, reporteVentas });
    if (!nuevo) setLocalError(error ?? 'No se pudo emitir el reporte consolidado de ventas');
  }

  async function handleArqueo() {
    setLocalError('');
    if (arqueoInput === '' || isNaN(Number(arqueoInput))) {
      setLocalError('Ingresa el monto contado en caja');
      return;
    }
    const actualizado = await registrarArqueo(Number(arqueoInput));
    if (!actualizado) setLocalError(error ?? 'No se pudo registrar el arqueo físico');
  }

  async function handleJustificacion() {
    setLocalError('');
    if (!justifInput.trim()) { setLocalError('Debes registrar una justificación antes de continuar'); return; }
    const actualizado = await registrarJustificacion(justifInput);
    if (!actualizado) setLocalError(error ?? 'No se pudo registrar la justificación');
  }

  async function handleEnviarFisicos() {
    setLocalError('');
    const actualizado = await enviarFisicosRecetas();
    if (!actualizado) setLocalError(error ?? 'No se pudieron enviar los físicos de recetas');
  }

  async function handleClasificar() {
    setLocalError('');
    const actualizado = await clasificarCopagoCobertura();
    if (!actualizado) setLocalError(error ?? 'No se pudo clasificar copagos y coberturas');
  }

  function handleNuevoCierre() {
    resetCierre();
    setNumero('');
    setSucursalId('');
    setReporteVentas('');
    setArqueoInput('');
    setJustifInput('');
    setLocalError('');
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-emerald-50 border border-emerald-200 rounded-lg text-xs text-emerald-800">
        <span className="font-mono font-semibold mr-1">RN: AR1-01 · POS-SD</span>
        El cierre de caja diario se concilia contra el arqueo físico. Toda variación debe justificarse y remitirse
        a auditoría médica antes de continuar el ciclo contable.
      </div>

      <ErrorBox error={localError || error} />

      {/* Paso 1.1 — Emitir Reporte Consolidado de Ventas */}
      {!cierre ? (
        <div>
          <LaneNote>Supervisor / Cajero de Sucursal</LaneNote>
          <Callout>
            <RuleTag>1.1</RuleTag>
            Emitir Reporte Consolidado de Ventas del Mostrador — el sistema ERP totaliza el cierre de caja ejecutado.
          </Callout>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4">
            <Input label="N° Cierre de Caja" name="numero" value={numero}
                   onChange={(e) => setNumero(e.target.value)} placeholder="CC-2026-0715-04" />
            <Select label="Sucursal" name="sucursalId" value={sucursalId}
                    onChange={(e) => setSucursalId(e.target.value)} options={sucursalOptions}
                    placeholder={loadingSucursales ? 'Cargando sucursales...' : 'Selecciona una sucursal...'}
                    disabled={loadingSucursales} />
            <Input label="Reporte consolidado de ventas" name="reporteVentas" type="number" value={reporteVentas}
                   onChange={(e) => setReporteVentas(e.target.value)} placeholder="S/ 0.00" />
          </div>
          <div className="flex justify-end mt-4">
            <Button onClick={handleAbrir} loading={procesando}>Emitir Reporte de Ventas</Button>
          </div>
        </div>
      ) : (
        <>
          <div className="flex items-center justify-between flex-wrap gap-2">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 flex-1">
              <Input label="N° Cierre de Caja" name="numeroDisabled" value={cierre.numero} disabled onChange={() => {}} />
              <Input label="Reporte consolidado de ventas" name="reporteVentasDisabled" value={fmt(cierre.reporteVentas)} disabled onChange={() => {}} />
            </div>
            <Badge value={cierre.cuadra === false ? 'INACTIVO' : cierre.cuadra === true ? 'ACTIVO' : undefined} />
          </div>

          {/* Paso 1.2 — Arqueo Físico */}
          <div>
            <Callout>
              <RuleTag>1.2</RuleTag>
              Realizar Arqueo Físico vs. Valores Registrados en Pantalla.
            </Callout>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4">
              <Input label="Conteo físico de caja" name="arqueoInput" type="number" value={arqueoInput}
                     onChange={(e) => setArqueoInput(e.target.value)} placeholder="S/ 0.00"
                     disabled={cierre.arqueoRegistrado} />
              <Input label="Diferencia calculada" name="diferencia"
                     value={cierre.arqueoRegistrado ? fmt(cierre.diferencia) : '—'} disabled onChange={() => {}} />
            </div>

            {!cierre.arqueoRegistrado ? (
              <div className="flex justify-end mt-4">
                <Button onClick={handleArqueo} loading={procesando}>Registrar Arqueo Físico</Button>
              </div>
            ) : cierre.cuadra === true ? (
              <div className="mt-4"><Callout tone="ok">Arqueo cuadrado al 100%. Se habilita la clasificación automática.</Callout></div>
            ) : null}
          </div>

          {/* Rama de variación — Justificación y envío de físicos */}
          {cierre.tieneVariacion && (
            <div>
              <LaneNote>Analista de Cuentas por Cobrar</LaneNote>
              <Callout tone="warn">
                <RuleTag>2.1</RuleTag>
                Registrar Justificación de Faltante o Sobrante en Sistema. Diferencia detectada: <b>{fmt(cierre.diferencia)}</b>
              </Callout>
              <div className="mt-4 flex flex-col gap-1.5">
                <label className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Justificación del descuadre</label>
                <textarea
                  className="w-full border border-slate-200 rounded-lg px-3.5 py-2.5 text-sm text-slate-800 outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 disabled:bg-slate-50 min-h-[70px]"
                  placeholder="Describe el motivo del faltante/sobrante..."
                  value={cierre.justificacion || justifInput}
                  onChange={(e) => setJustifInput(e.target.value)}
                  disabled={!!cierre.justificacion}
                />
              </div>

              {!cierre.justificacion ? (
                <div className="flex justify-end mt-4">
                  <Button variant="secondary" onClick={handleJustificacion} loading={procesando}>Guardar Justificación</Button>
                </div>
              ) : !cierre.fisicosEnviados ? (
                <div className="mt-4">
                  <LaneNote>Auditor Médico Corporativo</LaneNote>
                  <Callout>
                    <RuleTag>Fase 1</RuleTag>
                    Enviar Físicos de Recetas Médicas a Oficina Central para su verificación posterior.
                  </Callout>
                  <div className="flex justify-end mt-4">
                    <Button variant="secondary" onClick={handleEnviarFisicos} loading={procesando}>
                      Enviar Físicos de Recetas a Oficina Central
                    </Button>
                  </div>
                </div>
              ) : null}
            </div>
          )}

          {/* Paso 1.3 — Clasificar copagos y coberturas */}
          {cierre.habilitadoParaClasificar && (
            <div>
              <LaneNote>Sistema ERP</LaneNote>
              <Callout>
                <RuleTag>1.3</RuleTag>
                Clasificar de forma Automática Copagos y Coberturas de Aseguradoras.
              </Callout>
              {!cierre.clasificado ? (
                <div className="flex justify-end mt-4">
                  <Button onClick={handleClasificar} loading={procesando}>Clasificar Copagos y Coberturas</Button>
                </div>
              ) : (
                <>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4">
                    <Input label="Copago del paciente (30%)" name="copago" value={fmt(cierre.copago)} disabled onChange={() => {}} />
                    <Input label="Cobertura de aseguradoras (70%)" name="coberturaAseg" value={fmt(cierre.coberturaAseg)} disabled onChange={() => {}} />
                  </div>
                  <div className="mt-4">
                    <Callout tone="ok">
                      Cierre de caja finalizado. Se libera a <b>Fase 02 — Contabilización y Declaración de Valores</b>.
                    </Callout>
                  </div>
                  <div className="flex justify-between items-center mt-4 gap-3 flex-wrap">
                    <button onClick={handleNuevoCierre} className="text-xs font-medium text-emerald-700 underline hover:no-underline">
                      Registrar otro cierre de caja
                    </button>
                    <div className="flex flex-col items-end gap-1">
                      <Button disabled>Continuar a Fase 02 — Contabilización →</Button>
                      <span className="text-[11px] text-slate-400">La pantalla de la Fase 02 estará disponible próximamente</span>
                    </div>
                  </div>
                </>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Historial de cierres
// ─────────────────────────────────────────────────────────────────────────
function HistorialCierresTab() {
  const [cierres, setCierres] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const columns = useMemo(() => [
    { key: 'numero', label: 'N° Cierre' },
    { key: 'sucursalNombre', label: 'Sucursal' },
    { key: 'reporteVentas', label: 'Ventas', render: (val) => fmt(val) },
    { key: 'cuadra', label: 'Arqueo', render: (val) => val === null ? '— pendiente' : (val ? 'Cuadrado' : 'Con variación') },
    { key: 'estado', label: 'Estado', render: (val) => ESTADO_CIERRE_CAJA_LABEL[val] ?? val },
  ], []);

  const cargar = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.cierreCaja.getAll.execute({});
      setCierres(data);
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los cierres de caja');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    cargar();
  }, [cargar]);

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{cierres.length} cierres de caja registrados</p>
        <button onClick={cargar} className="text-xs text-emerald-700 hover:underline font-medium">Actualizar</button>
      </div>

      <ErrorBox error={error} />

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mt-3">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando cierres de caja...</div>
        ) : (
          <Table columns={columns} data={cierres} />
        )}
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 01: Recepción y Auditoría del Cierre de Venta (POS-SD)
// ─────────────────────────────────────────────────────────────────────────
export default function CierreCajaPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('flujo');

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <FiArRail activeStep={0} maxReached={0} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: AR1-01 · POS-SD"
          title="Recepción y Auditoría del Cierre de Venta"
          description="El cierre de caja diario se concilia contra el arqueo físico. Toda variación debe justificarse y remitirse a auditoría médica antes de continuar el ciclo contable."
          badge="FI-AR"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'flujo', label: 'Cierre de caja' },
            { id: 'historial', label: 'Historial' },
          ].map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={[
                'px-4 py-2.5 text-sm font-medium rounded-t-lg border-b-2 -mb-px transition-colors',
                tab === t.id
                  ? 'border-emerald-700 text-emerald-800'
                  : 'border-transparent text-slate-500 hover:text-slate-700',
              ].join(' ')}
            >
              {t.label}
            </button>
          ))}
        </div>

        {tab === 'flujo' ? <CierreCajaFlowTab /> : <HistorialCierresTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 01 de {CICLO_FI_AR_FASES.length} — la clasificación de copagos y coberturas habilita la Fase 02 (Contabilización y Declaración de Valores)
      </p>
    </div>
  );
}
