import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useMigo }        from '@/presentation/hooks/Logistica/useMigo';
import { useOrdenCompra } from '@/presentation/hooks/Logistica/useOrdenCompra';
import { EntradaMercancia, ESTADO_MIGO } from '@/domain/models/Logistica/EntradaMercancia';

const ESTADO_BADGE = {
  [ESTADO_MIGO.REGISTRADA]: 'ADMINISTRADOR',
  [ESTADO_MIGO.SUSPENDIDA]: 'INACTIVO',
};

const ESTADO_LABEL = {
  [ESTADO_MIGO.REGISTRADA]: 'Registrada — en cuarentena',
  [ESTADO_MIGO.SUSPENDIDA]: 'Suspendida — diferencia excede tolerancia',
};

// ─────────────────────────────────────────────────────────────────────────
// TAB: Registrar entrada (LOG.05 — MIGO, a partir de una OC firmada)
// ─────────────────────────────────────────────────────────────────────────
function RegistrarEntradaTab({ onRegistered }) {
  const { ordenes, loading: loadingOC } = useOrdenCompra();
  const { registrarMIGO, registrando, error } = useMigo();

  const [ordenCompraId, setOrdenCompraId] = useState('');
  const [lote, setLote] = useState('');
  const [vencimiento, setVencimiento] = useState('');
  const [temperatura, setTemperatura] = useState('4.5');
  const [cantidadRecibida, setCantidadRecibida] = useState('');
  const [localError, setLocalError] = useState('');
  const [confirmarExcepcion, setConfirmarExcepcion] = useState(false);

  // RN-F04-002: solo procede sobre Órdenes de Compra firmadas y despachadas
  const ocElegibles = useMemo(
    () => ordenes.filter((o) => o.firmada),
    [ordenes]
  );
  const ocOptions = ocElegibles.map((o) => ({
    value: o.id,
    label: `${o.numero} — ${o.razonSocialProveedor} — entrega ${o.fechaEntregaLimite}`,
  }));

  const ocSeleccionada = ocElegibles.find((o) => String(o.id) === String(ordenCompraId));
  const cantidadPedida = ocSeleccionada
    ? ocSeleccionada.detalles.reduce((s, d) => s + d.cantidad, 0)
    : 0;

  const cantidadRecibidaNum = cantidadRecibida === '' ? cantidadPedida : Number(cantidadRecibida);
  const { dif, pct, dentroDeTolerancia } = EntradaMercancia.calcularDiferencia(cantidadPedida, cantidadRecibidaNum);
  const fueraDeTolerancia = ocSeleccionada && !dentroDeTolerancia;
  const temperaturaFueraDeRango = EntradaMercancia.temperaturaFueraDeRango(temperatura);

  function resetForm() {
    setOrdenCompraId('');
    setLote('');
    setVencimiento('');
    setTemperatura('4.5');
    setCantidadRecibida('');
    setConfirmarExcepcion(false);
  }

  async function handleRegistrar() {
    setLocalError('');
    if (!ocSeleccionada) {
      setLocalError('Selecciona una Orden de Compra firmada y despachada');
      return;
    }
    if (!lote.trim() || !vencimiento.trim()) {
      setLocalError('RN-F04-013: el N° de lote y la fecha de vencimiento son obligatorios');
      return;
    }
    // RN-F04-003: diferencia mayor al 2% requiere confirmar la excepción documentada
    if (fueraDeTolerancia && !confirmarExcepcion) {
      setLocalError('RN-F04-003: la diferencia supera el 2% permitido. Marca la casilla para notificar al proveedor y continuar como excepción documentada.');
      return;
    }

    const entrada = await registrarMIGO({
      ordenCompraId: ocSeleccionada.id,
      lote,
      fechaVencimiento: vencimiento,
      temperaturaArribo: temperatura,
      cantidadRecibida: cantidadRecibidaNum,
      confirmarExcepcion,
    });
    if (entrada) {
      resetForm();
      onRegistered?.();
    } else {
      setLocalError(error ?? 'No se pudo registrar la entrada en SAP (MIGO)');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN-F04-001 · RN-F04-013 · RN-F04-014</span>
        Se verifica la cantidad física contra la Orden de Compra, se registra lote y vencimiento, y se valida la cadena de frío antes de mover el material a cuarentena.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Orden de Compra firmada</p>
        <Select
          name="ordenCompraId"
          placeholder={loadingOC ? 'Cargando órdenes de compra...' : 'Selecciona una Orden de Compra...'}
          value={ordenCompraId}
          onChange={(e) => { setOrdenCompraId(e.target.value); setCantidadRecibida(''); setConfirmarExcepcion(false); }}
          options={ocOptions}
          disabled={loadingOC}
        />
        {!loadingOC && ocOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay Órdenes de Compra firmadas y despachadas. Vuelve a la Fase 03 para firmar una.
          </p>
        )}
      </div>

      {ocSeleccionada && (
        <>
          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Registro de lote y cadena de frío (RN-F04-013 · RN-F04-014)</p>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Input
                label="N° Lote"
                name="lote"
                value={lote}
                onChange={(e) => setLote(e.target.value)}
                placeholder="LT-2026-A882"
              />
              <Input
                label="Fecha de vencimiento"
                name="vencimiento"
                value={vencimiento}
                onChange={(e) => setVencimiento(e.target.value)}
                placeholder="dd/mm/aaaa"
              />
              <Input
                label="Temperatura de arribo (°C)"
                name="temperatura"
                type="number"
                value={temperatura}
                onChange={(e) => setTemperatura(e.target.value)}
              />
            </div>
            {temperaturaFueraDeRango && (
              <div className="mt-2 px-3 py-2.5 bg-amber-50 border border-amber-200 rounded-lg text-xs text-amber-700">
                <span className="font-mono font-semibold mr-1">RN-F04-014</span>
                Temperatura fuera del rango 2–8°C. El lote quedará marcado (alertaCadenaFrio) para revisión estricta en QA (Fase 05).
              </div>
            )}
          </div>

          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">3. Cantidad recibida vs. Orden de Compra (RN-F04-001 · RN-F04-003)</p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Input label="Cantidad pedida (OC)" name="cantidadPedida" value={String(cantidadPedida)} disabled onChange={() => {}} />
              <Input
                label="Cantidad recibida"
                name="cantidadRecibida"
                type="number"
                value={cantidadRecibida === '' ? cantidadPedida : cantidadRecibida}
                onChange={(e) => { setCantidadRecibida(e.target.value); setConfirmarExcepcion(false); }}
              />
            </div>

            <div className={[
              'mt-3 px-3 py-2.5 rounded-lg text-xs border',
              dif === 0
                ? 'bg-emerald-50 border-emerald-200 text-emerald-700'
                : dentroDeTolerancia
                  ? 'bg-amber-50 border-amber-200 text-amber-700'
                  : 'bg-red-50 border-red-200 text-red-600',
            ].join(' ')}>
              {dif === 0
                ? 'Cantidad conforme con la Orden de Compra (RN-F04-001).'
                : dentroDeTolerancia
                  ? `Diferencia de ${dif} unidades (${pct.toFixed(1)}%) — dentro de tolerancia.`
                  : (
                    <>
                      <span className="font-mono font-semibold mr-1">RN-F04-003</span>
                      Diferencia de {dif} unidades ({pct.toFixed(1)}%) supera el 2% permitido. Quedará registrada como <b>SUSPENDIDA</b> hasta notificar al proveedor.
                    </>
                  )}
            </div>

            {fueraDeTolerancia && (
              <label className="mt-3 flex items-start gap-2 text-xs text-slate-600 cursor-pointer">
                <input
                  type="checkbox"
                  className="mt-0.5"
                  checked={confirmarExcepcion}
                  onChange={(e) => setConfirmarExcepcion(e.target.checked)}
                />
                Notificar al proveedor y continuar como excepción documentada (confirmarExcepcion).
              </label>
            )}
          </div>

          <div className="flex justify-end">
            <Button onClick={handleRegistrar} loading={registrando}>
              Registrar entrada en SAP (MIGO) →
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Entradas MIGO (listado)
// ─────────────────────────────────────────────────────────────────────────
function EntradasMigoTab() {
  const { entradas, loading, error } = useMigo();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° Entrada' },
    { key: 'numeroOrdenCompra', label: 'Orden de Compra' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'lote', label: 'Lote' },
    { key: 'fechaVencimiento', label: 'Vencimiento' },
    { key: 'cantidadRecibida', label: 'Cant. recibida' },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{entradas.length} entradas de mercancía registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando entradas MIGO...</div>
        ) : (
          <Table columns={COLUMNS} data={entradas} />
        )}
      </div>

      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        {detalleRow && (
          <div className="flex flex-col gap-2 text-sm">
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Orden de Compra</span>
              <b className="font-mono">{detalleRow.numeroOrdenCompra}</b>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Proveedor</span>
              <span>{detalleRow.razonSocialProveedor}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Lote</span>
              <span className="font-mono">{detalleRow.lote}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Vencimiento</span>
              <span>{detalleRow.fechaVencimiento}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Temperatura de arribo</span>
              <span>{detalleRow.temperaturaArribo}°C</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Cant. pedida / recibida</span>
              <span>{detalleRow.cantidadPedida} / {detalleRow.cantidadRecibida}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Diferencia</span>
              <span>{detalleRow.diferencia} ({detalleRow.porcentajeDiferencia.toFixed(1)}%)</span>
            </div>
            {detalleRow.alertaCadenaFrio && (
              <div className="px-3 py-2 bg-amber-50 border border-amber-200 rounded-lg text-xs text-amber-700">
                <span className="font-mono font-semibold mr-1">RN-F04-014</span>
                Temperatura fuera del rango 2–8°C. Lote marcado para revisión estricta en QA.
              </div>
            )}
            <div className="mt-1 px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
              Estado: {ESTADO_LABEL[detalleRow.estado] ?? detalleRow.estado}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 04: Entrada de mercancía y registro (MIGO)
// ─────────────────────────────────────────────────────────────────────────
export default function MigoPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('registrar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={3} maxReached={4} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-F04-001 · RN-F04-003 · RN-F04-013 · RN-F04-014 · LOG.05"
          title="Entrada de mercancía y registro (MIGO)"
          description="Registra la recepción física de la mercancía contra la Orden de Compra firmada: valida cantidad, lote, vencimiento y cadena de frío antes de derivar el material a cuarentena para la inspección de calidad (Fase 05)."
          badge="MIGO"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'registrar', label: 'Registrar entrada' },
            { id: 'entradas', label: 'Entradas MIGO' },
          ].map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={[
                'px-4 py-2.5 text-sm font-medium rounded-t-lg border-b-2 -mb-px transition-colors',
                tab === t.id
                  ? 'border-teal-700 text-teal-800'
                  : 'border-transparent text-slate-500 hover:text-slate-700',
              ].join(' ')}
            >
              {t.label}
            </button>
          ))}
        </div>

        {tab === 'registrar'
          ? <RegistrarEntradaTab key={refreshKey} onRegistered={() => { setRefreshKey((k) => k + 1); setTab('entradas'); }} />
          : <EntradasMigoTab key={`migo-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 04 de {BLOQUE_A_FASES.length} — el material ingresa a cuarentena hasta la Decisión de Empleo de Calidad (Fase 05)
      </p>
    </div>
  );
}
