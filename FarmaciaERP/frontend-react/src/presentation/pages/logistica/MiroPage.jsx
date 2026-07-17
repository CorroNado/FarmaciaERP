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
import { useFacturaMiro } from '@/presentation/hooks/useFacturaMiro';
import { useOrdenCompra } from '@/presentation/hooks/useOrdenCompra';
import { useMigo } from '@/presentation/hooks/useMigo';
import { FacturaMIRO, ESTADO_MIRO } from '@/domain/models/FacturaMIRO';

const ESTADO_BADGE = {
  [ESTADO_MIRO.REGISTRADA]: 'ADMINISTRADOR',
  [ESTADO_MIRO.ANULADA]: 'INACTIVO',
};

const ESTADO_LABEL = {
  [ESTADO_MIRO.REGISTRADA]: 'Registrada — asociada a la Orden de Compra',
  [ESTADO_MIRO.ANULADA]: 'Anulada',
};

function money(n) {
  return `S/ ${Number(n ?? 0).toFixed(2)}`;
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Registrar factura (LOG.07 — MIRO, a partir de una OC con MIGO registrada)
// ─────────────────────────────────────────────────────────────────────────
function RegistrarFacturaTab({ onRegistered }) {
  const { ordenes, loading: loadingOC } = useOrdenCompra();
  const { entradas, loading: loadingMigo } = useMigo();
  const { facturas, registrarMIRO, registrando, error } = useFacturaMiro();

  const [ordenCompraId, setOrdenCompraId] = useState('');
  const [numeroFactura, setNumeroFactura] = useState('');
  const [fechaEmision, setFechaEmision] = useState('');
  const [localError, setLocalError] = useState('');

  // RN-F07-004: la OC debe contar con entrada de mercancía (MIGO) registrada
  // y no tener ya una factura (MIRO) asociada.
  const idsConMigo = useMemo(
    () => new Set(entradas.map((e) => e.ordenCompraId)),
    [entradas]
  );
  const idsConFactura = useMemo(
    () => new Set(facturas.filter((f) => f.registrada).map((f) => f.ordenCompraId)),
    [facturas]
  );
  const ocElegibles = useMemo(
    () => ordenes.filter((o) => idsConMigo.has(o.id) && !idsConFactura.has(o.id)),
    [ordenes, idsConMigo, idsConFactura]
  );
  const ocOptions = ocElegibles.map((o) => ({
    value: o.id,
    label: `${o.numero} — ${o.razonSocialProveedor} — ${money(o.montoTotal)}`,
  }));

  const ocSeleccionada = ocElegibles.find((o) => String(o.id) === String(ordenCompraId));
  const montoNeto = ocSeleccionada?.montoTotal ?? 0;
  const igv = montoNeto * 0.18;
  const montoTotal = montoNeto + igv;

  function resetForm() {
    setOrdenCompraId('');
    setNumeroFactura('');
    setFechaEmision('');
  }

  async function handleRegistrar() {
    setLocalError('');
    if (!ocSeleccionada) {
      setLocalError('RN-F07-004: selecciona una Orden de Compra con entrada de mercancía (MIGO) registrada');
      return;
    }
    if (!numeroFactura.trim()) {
      setLocalError('El N° de factura es obligatorio');
      return;
    }
    if (!fechaEmision.trim()) {
      setLocalError('La fecha de emisión es obligatoria');
      return;
    }

    const factura = await registrarMIRO({
      ordenCompraId: ocSeleccionada.id,
      numeroFactura,
      fechaEmision,
    });
    if (factura) {
      resetForm();
      onRegistered?.();
    } else {
      setLocalError(error ?? 'No se pudo registrar la factura en SAP (MIRO)');
    }
  }

  const loadingBase = loadingOC || loadingMigo;

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN: MM-T12 · RN-F07-004</span>
        Se recibe la factura electrónica del laboratorio, se registra en SAP y se valida contra los datos tributarios y comerciales, asociándola a la Orden de Compra.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Orden de Compra con entrada de mercancía (MIGO)</p>
        <Select
          name="ordenCompraId"
          placeholder={loadingBase ? 'Cargando órdenes de compra...' : 'Selecciona una Orden de Compra...'}
          value={ordenCompraId}
          onChange={(e) => setOrdenCompraId(e.target.value)}
          options={ocOptions}
          disabled={loadingBase}
        />
        {!loadingBase && ocOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay Órdenes de Compra con entrada de mercancía (MIGO) registrada y sin factura previa. Vuelve a la Fase 04 para registrar una entrada.
          </p>
        )}
      </div>

      {ocSeleccionada && (
        <>
          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Datos de la factura electrónica</p>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <Input
                label="N° Factura"
                name="numeroFactura"
                value={numeroFactura}
                onChange={(e) => setNumeroFactura(e.target.value)}
                placeholder="F001-000452"
              />
              <Input label="N° Orden de Compra" name="numeroOC" value={ocSeleccionada.numero} disabled onChange={() => {}} />
              <Input
                label="Fecha de emisión"
                name="fechaEmision"
                value={fechaEmision}
                onChange={(e) => setFechaEmision(e.target.value)}
                placeholder="dd/mm/aaaa"
              />
            </div>
          </div>

          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">3. Validación tributaria y comercial</p>
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <table className="w-full text-sm">
                <tbody className="divide-y divide-slate-100">
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">Monto neto</td>
                    <td className="px-4 py-3.5 text-right font-mono text-slate-800">{money(montoNeto)}</td>
                  </tr>
                  <tr>
                    <td className="px-4 py-3.5 text-slate-600">IGV (18%)</td>
                    <td className="px-4 py-3.5 text-right font-mono text-slate-800">{money(igv)}</td>
                  </tr>
                  <tr className="bg-slate-50 font-semibold">
                    <td className="px-4 py-3.5 text-slate-700">Monto total</td>
                    <td className="px-4 py-3.5 text-right font-mono text-teal-800">{money(montoTotal)}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <p className="text-xs text-slate-400 mt-2">
              El monto se calcula sobre el total de la Orden de Compra; SAP recalculará y validará el total definitivo al registrar la factura.
            </p>
          </div>

          <div className="flex justify-end">
            <Button onClick={handleRegistrar} loading={registrando}>
              Registrar factura en SAP (MIRO) →
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Facturas MIRO (listado)
// ─────────────────────────────────────────────────────────────────────────
function FacturasMiroTab() {
  const { facturas, loading, error } = useFacturaMiro();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° MIRO' },
    { key: 'numeroFactura', label: 'N° Factura' },
    { key: 'numeroOrdenCompra', label: 'Orden de Compra' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'montoTotal', label: 'Monto total', render: (val) => money(val) },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Detalle', render: (_val, row) => (
        <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{facturas.length} facturas (MIRO) registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando facturas MIRO...</div>
        ) : (
          <Table columns={COLUMNS} data={facturas} />
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
              <span className="text-slate-500">Fecha de emisión</span>
              <span>{detalleRow.fechaEmision}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Monto neto</span>
              <span className="font-mono">{money(detalleRow.montoNeto)}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">IGV</span>
              <span className="font-mono">{money(detalleRow.igv)}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Monto total</span>
              <b className="font-mono text-teal-800">{money(detalleRow.montoTotal)}</b>
            </div>
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
// PÁGINA — Fase 07: Verificación de factura del proveedor (MIRO)
// ─────────────────────────────────────────────────────────────────────────
export default function MiroPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('registrar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={6} maxReached={7} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN: MM-T12 · RN-F07-004 · LOG.07"
          title="Verificación de facturas (MIRO)"
          description="Se recibe la factura electrónica del laboratorio, se registra en SAP y se valida contra los datos tributarios y comerciales, asociándola a la Orden de Compra."
          badge="MIRO"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'registrar', label: 'Registrar factura' },
            { id: 'facturas', label: 'Facturas MIRO' },
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
          ? <RegistrarFacturaTab key={refreshKey} onRegistered={() => { setRefreshKey((k) => k + 1); setTab('facturas'); }} />
          : <FacturasMiroTab key={`miro-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 07 de {BLOQUE_A_FASES.length} — la factura registrada queda lista para la conciliación de 3 vías (Fase 08)
      </p>
    </div>
  );
}
