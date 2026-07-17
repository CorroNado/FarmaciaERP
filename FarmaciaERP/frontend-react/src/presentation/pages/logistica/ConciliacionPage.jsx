import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import StageHeader from '@/presentation/components/logistica/StageHeader';
import { useConciliacion } from '@/presentation/hooks/useConciliacion';
import { useFacturaMiro } from '@/presentation/hooks/useFacturaMiro';
import { RESULTADO_CONCILIACION, RESULTADO_CONCILIACION_LABEL } from '@/domain/models/ConciliacionTresVias';

const RESULTADO_BADGE = {
  [RESULTADO_CONCILIACION.MATCH_OK]: 'ACTIVO',
  [RESULTADO_CONCILIACION.BLOQUEADO_MRBR]: 'INACTIVO',
};

function CheckPill({ ok, labelOk, labelFail }) {
  return (
    <span
      className={[
        'inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-medium',
        ok
          ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
          : 'bg-red-50 text-red-600 border border-red-200',
      ].join(' ')}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${ok ? 'bg-emerald-500' : 'bg-red-400'}`} />
      {ok ? labelOk : labelFail}
    </span>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Ejecutar conciliación (LOG.08 — 3-Way Match, a partir de una OC con factura MIRO)
// ─────────────────────────────────────────────────────────────────────────
function EjecutarConciliacionTab({ onEjecutada }) {
  const { facturas, loading: loadingMiro } = useFacturaMiro();
  const { conciliaciones, ejecutarConciliacion, ejecutando, error } = useConciliacion();

  const [ordenCompraId, setOrdenCompraId] = useState('');
  const [localError, setLocalError] = useState('');
  const [resultado, setResultado] = useState(null);

  // RN-MM-T13: la OC debe contar con factura (MIRO) registrada y no tener ya
  // una conciliación previa con resultado Match OK.
  const idsConMatchOk = useMemo(
    () => new Set(conciliaciones.filter((c) => c.matchOk).map((c) => c.ordenCompraId)),
    [conciliaciones]
  );
  const facturasElegibles = useMemo(
    () => facturas.filter((f) => f.registrada && !idsConMatchOk.has(f.ordenCompraId)),
    [facturas, idsConMatchOk]
  );
  const ocOptions = facturasElegibles.map((f) => ({
    value: f.ordenCompraId,
    label: `${f.numeroOrdenCompra} — ${f.razonSocialProveedor} — Factura ${f.numeroFactura}`,
  }));

  const facturaSeleccionada = facturasElegibles.find((f) => String(f.ordenCompraId) === String(ordenCompraId));

  function resetForm() {
    setOrdenCompraId('');
    setResultado(null);
  }

  async function handleEjecutar() {
    setLocalError('');
    if (!facturaSeleccionada) {
      setLocalError('RN-MM-T13: selecciona una Orden de Compra con factura (MIRO) registrada');
      return;
    }

    const conciliacion = await ejecutarConciliacion({ ordenCompraId: facturaSeleccionada.ordenCompraId });
    if (conciliacion) {
      setResultado(conciliacion);
      onEjecutada?.();
    } else {
      setLocalError(error ?? 'No se pudo ejecutar la conciliación de 3 vías');
    }
  }

  const loadingBase = loadingMiro;

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN: MM-T13 · LOG.08</span>
        El sistema compara automáticamente la Orden de Compra, la Entrada de Mercancía (MIGO) y la Factura (MIRO). Cualquier discrepancia bloquea la factura en MRBR para revisión de Contabilidad y Compras.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Orden de Compra con factura (MIRO) registrada</p>
        <Select
          name="ordenCompraId"
          placeholder={loadingBase ? 'Cargando facturas...' : 'Selecciona una Orden de Compra...'}
          value={ordenCompraId}
          onChange={(e) => { setOrdenCompraId(e.target.value); setResultado(null); }}
          options={ocOptions}
          disabled={loadingBase}
        />
        {!loadingBase && ocOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay Órdenes de Compra con factura (MIRO) registrada pendientes de conciliar. Vuelve a la Fase 07 para registrar una factura.
          </p>
        )}
      </div>

      {facturaSeleccionada && (
        <>
          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Comparación de 3 vías</p>
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-slate-200 bg-slate-50">
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Comparación</th>
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Orden de Compra</th>
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wider">Factura</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">N° documento</td>
                    <td className="px-4 py-3.5 font-mono text-slate-800">{facturaSeleccionada.numeroOrdenCompra}</td>
                    <td className="px-4 py-3.5 font-mono text-slate-800">{facturaSeleccionada.numeroFactura}</td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">Proveedor</td>
                    <td className="px-4 py-3.5 text-slate-800" colSpan={2}>{facturaSeleccionada.razonSocialProveedor}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <p className="text-xs text-slate-400 mt-2">
              El sistema validará automáticamente que la cantidad y el precio coincidan entre la OC, la entrada de mercancía (MIGO) y la factura (MIRO), y que el lote cuente con aprobación de calidad (QA11).
            </p>
          </div>

          <div className="flex justify-end">
            <Button onClick={handleEjecutar} loading={ejecutando}>
              Ejecutar conciliación automática →
            </Button>
          </div>
        </>
      )}

      {resultado && (
        <div
          className={[
            'px-4 py-3.5 rounded-xl border text-sm',
            resultado.matchOk
              ? 'bg-emerald-50 border-emerald-200 text-emerald-700'
              : 'bg-red-50 border-red-200 text-red-600',
          ].join(' ')}
        >
          <p className="font-semibold mb-2">
            {resultado.matchOk ? 'Match OK' : 'Bloqueo MRBR'}
          </p>
          <div className="flex flex-wrap gap-2 mb-2">
            <CheckPill ok={resultado.cantidadCoincide} labelOk="Cantidad coincide" labelFail="Diferencia en cantidad" />
            <CheckPill ok={resultado.precioCoincide} labelOk="Precio coincide" labelFail="Diferencia en precio" />
            <CheckPill ok={resultado.facturaVinculada} labelOk="Factura vinculada" labelFail="Factura no vinculada" />
            <CheckPill ok={resultado.qaAprobado} labelOk="Calidad aprobada" labelFail="Calidad no aprobada" />
          </div>
          <p>{RESULTADO_CONCILIACION_LABEL[resultado.resultado]}</p>
          <button
            onClick={resetForm}
            className="text-xs mt-2 font-medium underline hover:no-underline"
          >
            Conciliar otra Orden de Compra
          </button>
        </div>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Conciliaciones (listado)
// ─────────────────────────────────────────────────────────────────────────
function ConciliacionesTab() {
  const { conciliaciones, loading, error } = useConciliacion();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° Conciliación' },
    { key: 'numeroOrdenCompra', label: 'Orden de Compra' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'numeroFactura', label: 'N° Factura' },
    { key: 'resultado', label: 'Resultado', render: (val) => <Badge value={RESULTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{conciliaciones.length} conciliaciones de 3 vías registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando conciliaciones...</div>
        ) : (
          <Table columns={COLUMNS} data={conciliaciones} />
        )}
      </div>

      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        {detalleRow && (
          <div className="flex flex-col gap-2 text-sm">
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Orden de Compra</span>
              <span className="font-mono">{detalleRow.numeroOrdenCompra}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Proveedor</span>
              <span>{detalleRow.razonSocialProveedor}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Entrada de mercancía (MIGO)</span>
              <span className="font-mono">{detalleRow.numeroEntradaMercancia || '—'}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Factura (MIRO)</span>
              <span className="font-mono">{detalleRow.numeroFactura || '—'}</span>
            </div>
            <div className="flex flex-wrap gap-2 py-2">
              <CheckPill ok={detalleRow.cantidadCoincide} labelOk="Cantidad coincide" labelFail="Diferencia en cantidad" />
              <CheckPill ok={detalleRow.precioCoincide} labelOk="Precio coincide" labelFail="Diferencia en precio" />
              <CheckPill ok={detalleRow.facturaVinculada} labelOk="Factura vinculada" labelFail="Factura no vinculada" />
              <CheckPill ok={detalleRow.qaAprobado} labelOk="Calidad aprobada" labelFail="Calidad no aprobada" />
            </div>
            <div className="mt-1 px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
              {RESULTADO_CONCILIACION_LABEL[detalleRow.resultado] ?? detalleRow.resultado}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 08: Conciliación de 3 vías (3-Way Match / MRBR)
// ─────────────────────────────────────────────────────────────────────────
export default function ConciliacionPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('ejecutar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={7} maxReached={8} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: MM-T13 · LOG.08"
          title="Conciliación de 3 vías (3-Way Match)"
          description="El sistema compara automáticamente la Orden de Compra, la Entrada de Mercancía (MIGO) y la Factura (MIRO). Cualquier discrepancia bloquea el pago en MRBR."
          badge="MRBR"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'ejecutar', label: 'Ejecutar conciliación' },
            { id: 'conciliaciones', label: 'Conciliaciones' },
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

        {tab === 'ejecutar'
          ? <EjecutarConciliacionTab key={refreshKey} onEjecutada={() => { setRefreshKey((k) => k + 1); }} />
          : <ConciliacionesTab key={`conc-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 08 de {BLOQUE_A_FASES.length} — las facturas con Match OK quedan listas para la ejecución del pago (Fase 09)
      </p>
    </div>
  );
}
