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
import { usePago } from '@/presentation/hooks/usePago';
import { useFacturaMiro } from '@/presentation/hooks/useFacturaMiro';
import { useConciliacion } from '@/presentation/hooks/useConciliacion';
import { ESTADO_PAGO, ESTADO_PAGO_LABEL } from '@/domain/models/Pago';

const ESTADO_BADGE = {
  [ESTADO_PAGO.EJECUTADO]: 'ACTIVO',
  [ESTADO_PAGO.ANULADO]: 'INACTIVO',
};

function money(n) {
  return `S/ ${Number(n ?? 0).toFixed(2)}`;
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Ejecutar pago (LOG.09 — F110, a partir de una factura con Match OK)
// ─────────────────────────────────────────────────────────────────────────
function EjecutarPagoTab({ onEjecutado }) {
  const { facturas, loading: loadingMiro } = useFacturaMiro();
  const { conciliaciones, loading: loadingConciliacion } = useConciliacion();
  const { pagos, ejecutarPago, ejecutando, error } = usePago();

  const [facturaMIROId, setFacturaMIROId] = useState('');
  const [banco, setBanco] = useState('');
  const [fechaPago, setFechaPago] = useState('');
  const [localError, setLocalError] = useState('');

  // RN-F09-001 / RN-F09-002: la factura debe tener una conciliación de 3 vías
  // vigente con resultado Match OK, y no contar ya con un pago ejecutado.
  const idsConMatchOk = useMemo(
    () => new Set(conciliaciones.filter((c) => c.matchOk).map((c) => c.ordenCompraId)),
    [conciliaciones]
  );
  const idsConPago = useMemo(
    () => new Set(pagos.filter((p) => p.ejecutado).map((p) => p.facturaMIROId)),
    [pagos]
  );
  const facturasElegibles = useMemo(
    () => facturas.filter((f) => idsConMatchOk.has(f.ordenCompraId) && !idsConPago.has(f.id)),
    [facturas, idsConMatchOk, idsConPago]
  );
  const facturaOptions = facturasElegibles.map((f) => ({
    value: f.id,
    label: `${f.numeroFactura} — ${f.razonSocialProveedor} — ${money(f.montoTotal)}`,
  }));

  const facturaSeleccionada = facturasElegibles.find((f) => String(f.id) === String(facturaMIROId));

  function resetForm() {
    setFacturaMIROId('');
    setBanco('');
    setFechaPago('');
  }

  async function handleEjecutar() {
    setLocalError('');
    if (!facturaSeleccionada) {
      setLocalError('RN-F09-001/002: selecciona una factura con conciliación de 3 vías (MRBR) exitosa');
      return;
    }
    if (!banco.trim()) {
      setLocalError('RN-F09-003: el banco/cuenta de origen es obligatorio');
      return;
    }
    if (!fechaPago.trim()) {
      setLocalError('RN-F09-003: la fecha de pago es obligatoria');
      return;
    }

    const pago = await ejecutarPago({
      facturaMIROId: facturaSeleccionada.id,
      banco,
      fechaPago,
    });
    if (pago) {
      resetForm();
      onEjecutado?.();
    } else {
      setLocalError(error ?? 'No se pudo ejecutar el pago (F110)');
    }
  }

  const loadingBase = loadingMiro || loadingConciliacion;

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN: F09-001 · F09-002 · F09-003</span>
        Se programa el flujo de caja, se libera la factura aprobada para pago (conciliada sin discrepancias en el 3-way match), se ejecuta la transferencia bancaria y se cierra contablemente la cuenta por pagar (FIPO).
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Factura liberada para pago (Match OK)</p>
        <Select
          name="facturaMIROId"
          placeholder={loadingBase ? 'Cargando facturas...' : 'Selecciona una factura...'}
          value={facturaMIROId}
          onChange={(e) => setFacturaMIROId(e.target.value)}
          options={facturaOptions}
          disabled={loadingBase}
        />
        {!loadingBase && facturaOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay facturas liberadas para pago. Vuelve a la Fase 08 para ejecutar la conciliación de 3 vías con resultado Match OK.
          </p>
        )}
      </div>

      {facturaSeleccionada && (
        <>
          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Datos de la transferencia bancaria</p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Input
                label="Banco / cuenta de origen"
                name="banco"
                value={banco}
                onChange={(e) => setBanco(e.target.value)}
                placeholder="BCP — Cta. Cte. 191-xxxxxxxx"
              />
              <Input
                label="Fecha de pago"
                name="fechaPago"
                value={fechaPago}
                onChange={(e) => setFechaPago(e.target.value)}
                placeholder="dd/mm/aaaa"
              />
            </div>
          </div>

          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">3. Resumen del pago</p>
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <table className="w-full text-sm">
                <tbody className="divide-y divide-slate-100">
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">N° Factura</td>
                    <td className="px-4 py-3.5 text-right font-mono text-slate-800">{facturaSeleccionada.numeroFactura}</td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">Orden de Compra</td>
                    <td className="px-4 py-3.5 text-right font-mono text-slate-800">{facturaSeleccionada.numeroOrdenCompra}</td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">Proveedor</td>
                    <td className="px-4 py-3.5 text-right text-slate-800">{facturaSeleccionada.razonSocialProveedor}</td>
                  </tr>
                  <tr className="bg-slate-50 font-semibold">
                    <td className="px-4 py-3.5 text-slate-700">Monto a transferir</td>
                    <td className="px-4 py-3.5 text-right font-mono text-teal-800">{money(facturaSeleccionada.montoTotal)}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div className="flex justify-end">
            <Button onClick={handleEjecutar} loading={ejecutando}>
              Ejecutar pago automático (F110) →
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Pagos (listado)
// ─────────────────────────────────────────────────────────────────────────
function PagosTab() {
  const { pagos, loading, error } = usePago();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° Pago' },
    { key: 'numeroFactura', label: 'N° Factura' },
    { key: 'numeroOrdenCompra', label: 'Orden de Compra' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'banco', label: 'Banco' },
    { key: 'monto', label: 'Monto', render: (val) => money(val) },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{pagos.length} pagos ejecutados</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando pagos...</div>
        ) : (
          <Table columns={COLUMNS} data={pagos} />
        )}
      </div>

      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        {detalleRow && (
          <div className="flex flex-col gap-2 text-sm">
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">N° Factura</span>
              <b className="font-mono">{detalleRow.numeroFactura}</b>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Orden de Compra</span>
              <span className="font-mono">{detalleRow.numeroOrdenCompra}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Proveedor</span>
              <span>{detalleRow.razonSocialProveedor}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Banco / cuenta</span>
              <span>{detalleRow.banco}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Fecha de pago</span>
              <span>{detalleRow.fechaPago}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Monto</span>
              <b className="font-mono text-teal-800">{money(detalleRow.monto)}</b>
            </div>
            <div className="mt-1 px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
              Estado: {ESTADO_PAGO_LABEL[detalleRow.estado] ?? detalleRow.estado}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 09: Gestión y ejecución del pago (F110)
// ─────────────────────────────────────────────────────────────────────────
export default function PagoPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('ejecutar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={8} maxReached={8} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: Ejecución del pago · LOG.09"
          title="Gestión y ejecución del pago"
          description="Se programa el flujo de caja, se libera la factura aprobada para pago, se ejecuta la transferencia bancaria y se cierra contablemente la cuenta por pagar (FIPO)."
          badge="F110"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'ejecutar', label: 'Ejecutar pago' },
            { id: 'pagos', label: 'Pagos' },
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
          ? <EjecutarPagoTab key={refreshKey} onEjecutado={() => { setRefreshKey((k) => k + 1); setTab('pagos'); }} />
          : <PagosTab key={`pago-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 09 de {BLOQUE_A_FASES.length} — cuenta por pagar cerrada (FIPO); ciclo Procure-to-Pay completado
      </p>
    </div>
  );
}
