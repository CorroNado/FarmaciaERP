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
import { useOrdenTraslado } from '@/presentation/hooks/useOrdenTraslado';
import { useSucursales } from '@/presentation/hooks/useSucursales';
import { useInspeccionCalidad } from '@/presentation/hooks/useInspeccionCalidad';
import { ESTADO_STO } from '@/domain/models/OrdenTraslado';
import { DECISION_QA } from '@/domain/models/InspeccionCalidad';

const ESTADO_BADGE = {
  [ESTADO_STO.EN_TRANSITO]: 'INACTIVO',
  [ESTADO_STO.RECIBIDO]: 'ACTIVO',
};

const ESTADO_LABEL = {
  [ESTADO_STO.EN_TRANSITO]: 'En tránsito — no disponible para venta',
  [ESTADO_STO.RECIBIDO]: 'Recibido — disponible para venta',
};

// ─────────────────────────────────────────────────────────────────────────
// TAB: Generar STO (LOG.07 — Push/Pull a partir de un lote QA aprobado)
// ─────────────────────────────────────────────────────────────────────────
function GenerarSTOTab({ onGenerado }) {
  const { inspecciones, loading: loadingQA } = useInspeccionCalidad();
  const { ordenes, generarSTO, generando, error } = useOrdenTraslado();
  const { sucursales, loading: loadingSucursales } = useSucursales();

  const [inspeccionCalidadId, setInspeccionCalidadId] = useState('');
  const [sucursalDestinoId, setSucursalDestinoId] = useState('');
  const [guiaRemision, setGuiaRemision] = useState('');
  const [localError, setLocalError] = useState('');

  // RN-E6-002: solo procede sobre lotes con Decisión de Empleo aprobada que
  // aún no tengan una Orden de Traslado generada.
  const idsConSTO = useMemo(
    () => new Set(ordenes.map((o) => o.inspeccionCalidadId)),
    [ordenes]
  );
  const lotesElegibles = useMemo(
    () => inspecciones.filter((i) => i.decision === DECISION_QA.APROBADO && !idsConSTO.has(i.id)),
    [inspecciones, idsConSTO]
  );
  const loteOptions = lotesElegibles.map((i) => ({
    value: i.id,
    label: `${i.numero} — Lote ${i.lote} (${i.numeroEntradaMercancia})`,
  }));

  const sucursalesActivas = useMemo(() => sucursales.filter((s) => s.activa), [sucursales]);
  const sucursalOptions = sucursalesActivas.map((s) => ({
    value: s.id,
    label: `${s.codigo} — ${s.nombre}`,
  }));

  const loteSeleccionado = lotesElegibles.find((i) => String(i.id) === String(inspeccionCalidadId));

  function resetForm() {
    setInspeccionCalidadId('');
    setSucursalDestinoId('');
    setGuiaRemision('');
  }

  async function handleDespachar() {
    setLocalError('');
    if (!loteSeleccionado) {
      setLocalError('RN-E6-002: selecciona un lote con Decisión de Empleo aprobada');
      return;
    }
    if (!sucursalDestinoId) {
      setLocalError('Selecciona la sucursal destino');
      return;
    }
    if (!guiaRemision.trim()) {
      setLocalError('La guía de remisión es obligatoria');
      return;
    }
    const orden = await generarSTO({
      inspeccionCalidadId: loteSeleccionado.id,
      sucursalDestinoId,
      guiaRemision,
    });
    if (orden) {
      resetForm();
      onGenerado?.();
    } else {
      setLocalError(error ?? 'No se pudo generar la Orden de Traslado');
    }
  }

  return (
    <div className="p-6 flex flex-col gap-6">
      <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700">
        <span className="font-mono font-semibold mr-1">RN-E6-004 · RN-E6-005 · RN-E6-007</span>
        El lote liberado dispara el algoritmo Push/Pull: se genera la Orden de Traslado (STO) hacia la sucursal destino y el inventario queda en tránsito hasta la confirmación del POS.
      </div>

      {(localError || error) && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {localError || (typeof error === 'string' ? error : 'Ocurrió un error')}
        </div>
      )}

      <div>
        <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">1. Lote en Libre Utilización</p>
        <Select
          name="inspeccionCalidadId"
          placeholder={loadingQA ? 'Cargando lotes aprobados...' : 'Selecciona un lote aprobado por QA...'}
          value={inspeccionCalidadId}
          onChange={(e) => setInspeccionCalidadId(e.target.value)}
          options={loteOptions}
          disabled={loadingQA}
        />
        {!loadingQA && loteOptions.length === 0 && (
          <p className="text-xs text-amber-700 mt-2">
            No hay lotes en Libre Utilización pendientes de distribuir. Vuelve a la Fase 05 para aprobar una Decisión de Empleo.
          </p>
        )}
      </div>

      {loteSeleccionado && (
        <>
          <div>
            <p className="text-xs font-semibold text-slate-600 uppercase tracking-wide mb-2">2. Destino y despacho (RN-E6-005 · RN-E6-007)</p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <Select
                  label="Sucursal destino"
                  name="sucursalDestinoId"
                  placeholder={loadingSucursales ? 'Cargando sucursales...' : 'Selecciona la sucursal destino...'}
                  value={sucursalDestinoId}
                  onChange={(e) => setSucursalDestinoId(e.target.value)}
                  options={sucursalOptions}
                  disabled={loadingSucursales}
                />
                {!loadingSucursales && sucursalOptions.length === 0 && (
                  <p className="text-xs text-amber-700 mt-2">
                    No hay sucursales activas registradas. Crea una en la pestaña "Sucursales".
                  </p>
                )}
              </div>
              <Input
                label="Guía de remisión"
                name="guiaRemision"
                value={guiaRemision}
                onChange={(e) => setGuiaRemision(e.target.value)}
                placeholder="GR-2026-005647"
              />
            </div>
          </div>

          <div className="px-3 py-2.5 bg-amber-50 border border-amber-200 rounded-lg text-xs text-amber-700">
            <span className="font-mono font-semibold mr-1">RN-E6-008</span>
            Mientras la sucursal no confirme recepción en POS, el stock permanece "en tránsito" y no está disponible para venta.
          </div>

          <div className="flex justify-end">
            <Button onClick={handleDespachar} loading={generando}>
              Generar STO y despachar →
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Órdenes de traslado (listado + confirmar recepción POS)
// ─────────────────────────────────────────────────────────────────────────
function OrdenesTrasladoTab() {
  const { ordenes, loading, error, confirmarRecepcion, confirmandoId } = useOrdenTraslado();
  const [detalleRow, setDetalleRow] = useState(null);

  const COLUMNS = [
    { key: 'numero', label: 'N° STO' },
    { key: 'lote', label: 'Lote' },
    { key: 'sucursalDestinoNombre', label: 'Sucursal destino' },
    { key: 'guiaRemision', label: 'Guía de remisión' },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={ESTADO_BADGE[val] ?? val} /> },
    { key: 'id', label: 'Acción', render: (_val, row) => (
        row.estado === ESTADO_STO.EN_TRANSITO ? (
          <Button
            variant="outline"
            className="!px-3 !py-1.5 !text-xs"
            loading={confirmandoId === row.id}
            onClick={() => confirmarRecepcion(row.id)}
          >
            Confirmar recepción POS
          </Button>
        ) : (
          <button onClick={() => setDetalleRow(row)} className="text-xs text-teal-700 hover:underline font-medium">Ver detalle</button>
        )
      ) },
  ];

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{ordenes.length} órdenes de traslado (STO) registradas</p>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando órdenes de traslado...</div>
        ) : (
          <Table columns={COLUMNS} data={ordenes} />
        )}
      </div>

      <Modal isOpen={!!detalleRow} title={`Detalle — ${detalleRow?.numero ?? ''}`} onClose={() => setDetalleRow(null)}>
        {detalleRow && (
          <div className="flex flex-col gap-2 text-sm">
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Lote</span>
              <b className="font-mono">{detalleRow.lote}</b>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Sucursal destino</span>
              <span>{detalleRow.sucursalDestinoNombre}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Guía de remisión</span>
              <span className="font-mono">{detalleRow.guiaRemision}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Fecha de despacho</span>
              <span>{detalleRow.fechaDespacho ?? '—'}</span>
            </div>
            <div className="flex justify-between border-b border-slate-100 pb-2">
              <span className="text-slate-500">Fecha de recepción</span>
              <span>{detalleRow.fechaRecepcion ?? '—'}</span>
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
// TAB: Sucursales (maestro de destinos de distribución)
// ─────────────────────────────────────────────────────────────────────────
function SucursalesTab() {
  const { sucursales, loading, error, createSucursal, creando } = useSucursales();
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState({ codigo: '', nombre: '' });
  const [formError, setFormError] = useState('');

  const COLUMNS = [
    { key: 'codigo', label: 'Código' },
    { key: 'nombre', label: 'Nombre' },
    { key: 'activa', label: 'Estado', render: (val) => <Badge value={val ? 'ACTIVO' : 'INACTIVO'} /> },
  ];

  function handleChange(e) {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    setFormError('');
  }

  async function handleCreate() {
    if (!form.codigo.trim() || !form.nombre.trim()) {
      setFormError('El código y el nombre son obligatorios');
      return;
    }
    const sucursal = await createSucursal(form);
    if (sucursal) {
      setForm({ codigo: '', nombre: '' });
      setShowCreate(false);
    } else {
      setFormError(error ?? 'No se pudo crear la sucursal');
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{sucursales.length} sucursales destino registradas</p>
        <Button onClick={() => setShowCreate(true)}>+ Nueva sucursal</Button>
      </div>

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando sucursales...</div>
        ) : (
          <Table columns={COLUMNS} data={sucursales} />
        )}
      </div>

      <Modal
        isOpen={showCreate}
        title="Nueva sucursal destino"
        onClose={() => { setShowCreate(false); setForm({ codigo: '', nombre: '' }); setFormError(''); }}
        onConfirm={handleCreate}
        confirmText="Crear sucursal"
        loading={creando}
      >
        <div className="flex flex-col gap-4">
          {formError && (
            <div className="px-3 py-2.5 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">{formError}</div>
          )}
          <Input label="Código" name="codigo" value={form.codigo} onChange={handleChange} placeholder="FAR-LN-01" />
          <Input label="Nombre" name="nombre" value={form.nombre} onChange={handleChange} placeholder="Farmacia Lima Norte" />
        </div>
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 06: Gestión de stocks y distribución capilar (STO)
// ─────────────────────────────────────────────────────────────────────────
export default function DistribucionPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('generar');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRailNavigate(fase) {
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={5} maxReached={6} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-E6-004 · RN-E6-005 · RN-E6-007 · RN-E6-008 · LOG.07"
          title="Gestión de stocks y distribución capilar"
          description="El lote liberado por QA dispara el algoritmo Push/Pull, se generan órdenes de traslado (STO) por sucursal y el inventario queda en tránsito hasta la confirmación de recepción en el POS."
          badge="ME27 / MB1B"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'generar', label: 'Generar STO' },
            { id: 'ordenes', label: 'Órdenes de traslado' },
            { id: 'sucursales', label: 'Sucursales' },
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

        {tab === 'generar' && <GenerarSTOTab key={refreshKey} onGenerado={() => { setRefreshKey((k) => k + 1); setTab('ordenes'); }} />}
        {tab === 'ordenes' && <OrdenesTrasladoTab key={`sto-${refreshKey}`} />}
        {tab === 'sucursales' && <SucursalesTab key={`suc-${refreshKey}`} />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        Fase 06 de {BLOQUE_A_FASES.length} — al confirmar la recepción en POS, el stock queda disponible para venta en la sucursal (Fase 07)
      </p>
    </div>
  );
}
