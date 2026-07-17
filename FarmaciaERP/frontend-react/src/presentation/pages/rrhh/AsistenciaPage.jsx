import { useMemo, useState } from 'react';
import Table  from '@/presentation/components/ui/Table';
import Badge  from '@/presentation/components/ui/Badge';
import Button from '@/presentation/components/ui/Button';
import Input  from '@/presentation/components/ui/Input';
import Select from '@/presentation/components/ui/Select';
import Modal  from '@/presentation/components/ui/Modal';
import RrhhTabs from '@/presentation/components/rrhh/RrhhTabs';
import { useAsistencias, useAuditoriaAsistencias } from '@/presentation/hooks/useAsistencias';
import { useEmpleados } from '@/presentation/hooks/useEmpleados';
import { TURNOS_ASISTENCIA, ESTADOS_ASISTENCIA, MOVIMIENTOS_ASISTENCIA } from '@/domain/models/RegistroAsistencia';

function StageHeader({ eyebrow, title, description, badge }) {
  return (
    <div className="px-6 py-5 border-b border-slate-200 flex items-start justify-between gap-4 flex-wrap">
      <div>
        <p className="font-mono text-[11px] tracking-widest uppercase text-teal-700">{eyebrow}</p>
        <h2 className="text-xl font-semibold text-slate-800 mt-1">{title}</h2>
        <p className="text-sm text-slate-500 mt-1 max-w-2xl">{description}</p>
      </div>
      {badge && (
        <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-teal-50 text-teal-700 border border-teal-200 whitespace-nowrap">
          {badge}
        </span>
      )}
    </div>
  );
}

const TURNO_OPTIONS = Object.entries(TURNOS_ASISTENCIA).map(([value, label]) => ({ value, label }));
const ESTADO_OPTIONS = Object.entries(ESTADOS_ASISTENCIA).map(([value, label]) => ({ value, label }));
const ESTADO_EDITABLE_OPTIONS = ESTADO_OPTIONS.filter((o) =>
  ['A_TIEMPO', 'TARDANZA', 'FALTA_INJUSTIFICADA', 'JUSTIFICADO'].includes(o.value)
);

const BADGE_STYLES = {
  PROGRAMADO: 'bg-slate-100 text-slate-600 border border-slate-200',
  PENDIENTE: 'bg-sky-50 text-sky-700 border border-sky-200',
  A_TIEMPO: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
  TARDANZA: 'bg-amber-50 text-amber-700 border border-amber-200',
  FALTA_INJUSTIFICADA: 'bg-red-50 text-red-600 border border-red-200',
  JUSTIFICADO: 'bg-violet-50 text-violet-700 border border-violet-200',
};

function EstadoBadge({ value }) {
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${BADGE_STYLES[value] ?? 'bg-slate-100 text-slate-600'}`}>
      {ESTADOS_ASISTENCIA[value] ?? value}
    </span>
  );
}

function formatHora(hora) {
  if (!hora) return '—';
  return hora.length > 5 ? hora.slice(0, 8) : hora;
}

function exportAsistenciasCSV(asistencias) {
  if (!asistencias.length) return;
  const headers = ['Fecha', 'Colaborador', 'Rol', 'Turno', 'Hora Entrada', 'Hora Salida', 'Horas Trabajadas', 'Horas Extras', 'Estado'];
  const rows = asistencias.map((r) => [
    r.fecha, r.colaborador, r.rol, TURNOS_ASISTENCIA[r.turno] ?? r.turno,
    formatHora(r.horaEntrada), formatHora(r.horaSalida),
    r.horasTrabajadas ?? '', r.horasExtras ?? '', ESTADOS_ASISTENCIA[r.estado] ?? r.estado,
  ]);
  const csv = [headers, ...rows]
    .map((r) => r.map((v) => `"${String(v ?? '').replace(/"/g, '""')}"`).join(','))
    .join('\n');
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'Control_Asistencia.csv';
  a.click();
  URL.revokeObjectURL(url);
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Programar turno (Registrar Entrada)
// ─────────────────────────────────────────────────────────────────────────
function ProgramarModal({ isOpen, onClose, onSubmit, loading, empleadosActivos }) {
  const [form, setForm] = useState({ empleadoId: '', fecha: '', turno: 'MANANA' });
  const [error, setError] = useState('');

  function handleChange(e) {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    setError('');
  }

  function handleClose() {
    setForm({ empleadoId: '', fecha: '', turno: 'MANANA' });
    setError('');
    onClose();
  }

  async function handleConfirm() {
    if (!form.empleadoId) { setError('Seleccione un colaborador.'); return; }
    if (!form.fecha) { setError('Seleccione una fecha.'); return; }
    const ok = await onSubmit(form);
    if (ok) handleClose();
  }

  const empleadoOptions = empleadosActivos.map((e) => ({ value: String(e.id), label: `${e.codigo} — ${e.nombreCompleto}` }));

  return (
    <Modal
      isOpen={isOpen}
      title="Registrar Nueva Entrada (RRHH.02)"
      onClose={handleClose}
      onConfirm={handleConfirm}
      confirmText="Registrar"
      loading={loading}
    >
      <div className="flex flex-col gap-4">
        <Select label="Colaborador *" name="empleadoId" value={form.empleadoId} onChange={handleChange} options={empleadoOptions} />
        <Input label="Fecha de Programación *" name="fecha" type="date" value={form.fecha} onChange={handleChange} />
        <Select label="Turno Laboral *" name="turno" value={form.turno} onChange={handleChange} options={TURNO_OPTIONS} placeholder="" />
        <div className="px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
          El registro quedará en estado <strong>«Programado»</strong> hasta que se marque la entrada con el botón ✅ Entrada.
        </div>
        {error && <span className="text-xs text-red-500 flex items-center gap-1"><span>⚠</span> {error}</span>}
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Justificación de inasistencia
// ─────────────────────────────────────────────────────────────────────────
function JustificarModal({ registro, onClose, onSubmit, loading }) {
  const [motivo, setMotivo] = useState('');

  function handleClose() { setMotivo(''); onClose(); }

  async function handleConfirm() {
    if (!motivo.trim()) return;
    const ok = await onSubmit({ motivo });
    if (ok) handleClose();
  }

  if (!registro) return null;

  return (
    <Modal
      isOpen={!!registro}
      title="📄 Justificación de Inasistencia"
      onClose={handleClose}
      onConfirm={handleConfirm}
      confirmText="Guardar Justificación"
      confirmVariant="primary"
      loading={loading}
    >
      <div className="flex flex-col gap-4">
        <div className="px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-lg text-sm text-slate-600">
          <span className="font-semibold text-slate-800">{registro.colaborador}</span> — {registro.fecha} · {TURNOS_ASISTENCIA[registro.turno] ?? registro.turno}
        </div>
        <Input label="Asunto / Motivo *" name="motivo" value={motivo} onChange={(e) => setMotivo(e.target.value)}
          placeholder="Ej. Emergencia médica, accidente, etc." />
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Auditoría de registro — Editar / Eliminar
// ─────────────────────────────────────────────────────────────────────────
function AuditoriaAccionModal({ registro, mode, onClose, onSubmit, loading }) {
  const [horaEntrada, setHoraEntrada] = useState('');
  const [estado, setEstado] = useState('A_TIEMPO');
  const [motivoAuditoria, setMotivoAuditoria] = useState('');

  if (!registro) return null;
  const isEdit = mode === 'edit';

  function handleClose() {
    setHoraEntrada(''); setEstado('A_TIEMPO'); setMotivoAuditoria('');
    onClose();
  }

  async function handleConfirm() {
    if (!motivoAuditoria.trim()) return;
    const ok = isEdit
      ? await onSubmit({ horaEntrada: horaEntrada || null, estado, motivoAuditoria })
      : await onSubmit({ motivoAuditoria });
    if (ok) handleClose();
  }

  return (
    <Modal
      isOpen={!!registro}
      title={isEdit ? 'Auditoría de Registro — Editar' : 'Auditoría de Registro — Eliminar'}
      onClose={handleClose}
      onConfirm={handleConfirm}
      confirmText="Confirmar"
      confirmVariant="danger"
      loading={loading}
    >
      <div className="flex flex-col gap-4">
        <div className="px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-lg text-sm text-slate-600">
          <span className="font-semibold text-slate-800">{registro.colaborador}</span> — {registro.fecha} · {TURNOS_ASISTENCIA[registro.turno] ?? registro.turno}
        </div>

        {isEdit && (
          <>
            <Input label="Modificar Hora de Entrada" name="horaEntrada" type="time" value={horaEntrada} onChange={(e) => setHoraEntrada(e.target.value)} />
            <Select label="Modificar Estado" name="estado" value={estado} onChange={(e) => setEstado(e.target.value)} options={ESTADO_EDITABLE_OPTIONS} placeholder="" />
          </>
        )}

        {!isEdit && (
          <div className="px-3 py-2.5 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">
            Esta acción eliminará el registro de asistencia de forma permanente.
          </div>
        )}

        <Input
          label="Asunto de Auditoría (Obligatorio) *"
          name="motivoAuditoria"
          value={motivoAuditoria}
          onChange={(e) => setMotivoAuditoria(e.target.value)}
          placeholder="Registre el motivo de la auditoría interna..."
        />
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Auditoría (historial)
// ─────────────────────────────────────────────────────────────────────────
function AuditoriaHistorialModal({ isOpen, onClose }) {
  const { logs, loading, loadAuditoria } = useAuditoriaAsistencias();
  const [codigo, setCodigo] = useState('');

  function handleFilter(e) {
    const value = e.target.value;
    setCodigo(value);
    loadAuditoria(value || undefined);
  }

  return (
    <Modal isOpen={isOpen} title="Auditoría de Asistencia (RRHH.02)" onClose={onClose}>
      <div className="flex flex-col gap-4">
        <Input
          label="Filtrar por código de empleado"
          name="codigo"
          value={codigo}
          onChange={handleFilter}
          placeholder="Ej. EMP-001"
        />
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden max-h-[420px] overflow-y-auto">
          {loading ? (
            <div className="flex items-center justify-center py-10 text-slate-400 text-sm">Cargando auditoría...</div>
          ) : (
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 sticky top-0 bg-white">
                  <th className="px-3 py-2 text-left text-xs font-semibold text-slate-500 uppercase">Fecha</th>
                  <th className="px-3 py-2 text-left text-xs font-semibold text-slate-500 uppercase">Colaborador</th>
                  <th className="px-3 py-2 text-left text-xs font-semibold text-slate-500 uppercase">Tipo</th>
                  <th className="px-3 py-2 text-left text-xs font-semibold text-slate-500 uppercase">Detalle</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {logs.length === 0 && (
                  <tr><td colSpan={4} className="px-3 py-6 text-center text-slate-400">Sin registros de auditoría.</td></tr>
                )}
                {logs.map((log) => (
                  <tr key={log.id}>
                    <td className="px-3 py-2 text-slate-600 whitespace-nowrap">{new Date(log.fecha).toLocaleString('es-PE')}</td>
                    <td className="px-3 py-2 text-slate-700">{log.colaborador} <span className="text-xs text-slate-400">({log.codigoEmpleado})</span></td>
                    <td className="px-3 py-2">{MOVIMIENTOS_ASISTENCIA[log.tipo] ?? log.tipo}</td>
                    <td className="px-3 py-2 text-slate-500">{log.detalle}{log.motivo ? ` — ${log.motivo}` : ''}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — RRHH.02 Control de Asistencia
// ─────────────────────────────────────────────────────────────────────────
export default function AsistenciaPage() {
  const {
    asistencias, loading, error, applyFilters, clearFilters,
    programarAsistencia, marcarEntrada, marcarSalida, justificar,
    editarAsistencia, eliminarAsistencia,
  } = useAsistencias();
  const { empleados } = useEmpleados();

  const empleadosActivos = useMemo(() => empleados.filter((e) => e.estado === 'ACTIVO'), [empleados]);

  const [localFilters, setLocalFilters] = useState({ texto: '', estado: '', fecha: '' });
  const [showProgramar, setShowProgramar] = useState(false);
  const [programando, setProgramando] = useState(false);

  const [justificarTarget, setJustificarTarget] = useState(null);
  const [justificando, setJustificando] = useState(false);

  const [auditoriaTarget, setAuditoriaTarget] = useState(null);
  const [auditoriaMode, setAuditoriaMode] = useState('edit');
  const [auditoriaLoading, setAuditoriaLoading] = useState(false);

  const [showAuditoriaHistorial, setShowAuditoriaHistorial] = useState(false);

  const kpis = useMemo(() => ({
    total: asistencias.length,
    aTiempo: asistencias.filter((r) => r.estado === 'A_TIEMPO').length,
    tardanzas: asistencias.filter((r) => r.estado === 'TARDANZA').length,
    faltasAlertas: asistencias.filter((r) => r.estado === 'FALTA_INJUSTIFICADA' || r.estado === 'PENDIENTE').length,
  }), [asistencias]);

  const COLUMNS = [
    { key: 'fecha', label: 'Fecha' },
    { key: 'colaborador', label: 'Colaborador' },
    { key: 'rol', label: 'Rol Farmacéutico' },
    { key: 'turno', label: 'Turno', render: (val) => TURNOS_ASISTENCIA[val] ?? val },
    { key: 'horaEntrada', label: 'Hora Entrada', render: (val) => formatHora(val) },
    { key: 'horaSalida', label: 'Hora Salida', render: (val) => formatHora(val) },
    { key: 'horasTrabajadas', label: 'Horas Trab.', render: (val) => (val != null ? Number(val).toFixed(2) : '—') },
    { key: 'estado', label: 'Estado', render: (val) => <EstadoBadge value={val} /> },
    {
      key: 'acciones', label: 'Marcación', render: (_val, row) => (
        <div className="flex items-center gap-1.5 flex-wrap">
          <Button
            variant="outline" className="!px-2.5 !py-1.5 !text-xs"
            disabled={!row.puedeMarcarEntrada}
            onClick={() => marcarEntrada(row.id)}
          >
            ✅ Entrada
          </Button>
          <Button
            variant="outline" className="!px-2.5 !py-1.5 !text-xs"
            disabled={!row.puedeMarcarSalida}
            onClick={() => marcarSalida(row.id)}
          >
            🚪 Salida
          </Button>
          <Button
            variant="outline" className="!px-2.5 !py-1.5 !text-xs"
            disabled={!row.puedeJustificar}
            onClick={() => setJustificarTarget(row)}
          >
            📄 Justificar
          </Button>
        </div>
      ),
    },
    {
      key: 'auditoria', label: 'Auditoría', render: (_val, row) => (
        <div className="flex items-center gap-1.5">
          <Button
            variant="secondary" className="!px-2.5 !py-1.5 !text-xs"
            disabled={!row.puedeEditar}
            onClick={() => { setAuditoriaTarget(row); setAuditoriaMode('edit'); }}
          >
            Editar
          </Button>
          <Button
            variant="danger" className="!px-2.5 !py-1.5 !text-xs"
            onClick={() => { setAuditoriaTarget(row); setAuditoriaMode('delete'); }}
          >
            Borrar
          </Button>
        </div>
      ),
    },
  ];

  function handleFilterChange(e) {
    const next = { ...localFilters, [e.target.name]: e.target.value };
    setLocalFilters(next);
    applyFilters(next);
  }
  function handleClear() {
    setLocalFilters({ texto: '', estado: '', fecha: '' });
    clearFilters();
  }

  async function handleProgramar(formData) {
    setProgramando(true);
    const ok = await programarAsistencia(formData);
    setProgramando(false);
    return ok;
  }

  async function handleJustificar(datos) {
    setJustificando(true);
    const ok = await justificar(justificarTarget.id, datos);
    setJustificando(false);
    if (ok) setJustificarTarget(null);
    return ok;
  }

  async function handleAuditoriaSubmit(datos) {
    setAuditoriaLoading(true);
    const ok = auditoriaMode === 'edit'
      ? await editarAsistencia(auditoriaTarget.id, datos)
      : await eliminarAsistencia(auditoriaTarget.id, datos);
    setAuditoriaLoading(false);
    if (ok) setAuditoriaTarget(null);
    return ok;
  }

  return (
    <div>
      <RrhhTabs />
      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RRHH.02 · Control de Asistencia"
          title="Monitoreo de Asistencia en Tiempo Real"
          description="Programación de turnos, marcación de entrada/salida con cálculo de horas extras, justificación de inasistencias y edición/eliminación auditadas."
          badge="Asistencia Diaria"
        />

        <div className="p-6">
          {/* KPIs */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 mb-5">
            <div className="rounded-xl border border-blue-100 bg-blue-50 px-4 py-3">
              <p className="text-[11px] font-semibold uppercase tracking-wide text-blue-500">Total Registros</p>
              <p className="text-2xl font-semibold text-blue-800 mt-1">{kpis.total}</p>
            </div>
            <div className="rounded-xl border border-emerald-100 bg-emerald-50 px-4 py-3">
              <p className="text-[11px] font-semibold uppercase tracking-wide text-emerald-600">A Tiempo</p>
              <p className="text-2xl font-semibold text-emerald-800 mt-1">{kpis.aTiempo}</p>
            </div>
            <div className="rounded-xl border border-amber-100 bg-amber-50 px-4 py-3">
              <p className="text-[11px] font-semibold uppercase tracking-wide text-amber-600">Tardanzas</p>
              <p className="text-2xl font-semibold text-amber-800 mt-1">{kpis.tardanzas}</p>
            </div>
            <div className="rounded-xl border border-red-100 bg-red-50 px-4 py-3">
              <p className="text-[11px] font-semibold uppercase tracking-wide text-red-500">Faltas / Alertas</p>
              <p className="text-2xl font-semibold text-red-700 mt-1">{kpis.faltasAlertas}</p>
            </div>
          </div>

          {/* Leyenda de turnos */}
          <div className="bg-slate-50 border border-slate-200 rounded-xl p-4 mb-5 flex flex-wrap gap-2 text-xs text-slate-600">
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200">🌅 Mañana 07:00–15:00 (1 cupo)</span>
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200">🌆 Tarde 15:00–23:00 (2 cupos)</span>
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200">🌌 Noche/Guardia 23:00–07:00 (1 cupo)</span>
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200">⏱️ Medio Tiempo 08:00–12:00 (1 cupo)</span>
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200">🤝 Apoyo/Refuerzo 18:00–22:00 (1 cupo)</span>
            <span className="px-2.5 py-1 rounded-full bg-white border border-slate-200 w-full sm:w-auto">⏱️ Puntualidad: A tiempo ≤15min · Tardanza ≤30min · Falta &gt;30min</span>
          </div>

          <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
            <p className="text-sm text-slate-500">{asistencias.length} registros de asistencia</p>
            <div className="flex gap-2 flex-wrap">
              <Button variant="outline" onClick={() => exportAsistenciasCSV(asistencias)}>📥 Exportar</Button>
              <Button variant="secondary" onClick={() => setShowAuditoriaHistorial(true)}>📄 Auditoría</Button>
              <Button onClick={() => setShowProgramar(true)}>+ Registrar Entrada</Button>
            </div>
          </div>

          <div className="bg-slate-50 rounded-xl border border-slate-200 p-4 mb-5">
            <div className="grid grid-cols-1 sm:grid-cols-4 gap-3">
              <Input name="texto" placeholder="Buscar por colaborador o código..." value={localFilters.texto} onChange={handleFilterChange} />
              <Select name="estado" placeholder="Todos los estados" value={localFilters.estado} onChange={handleFilterChange} options={ESTADO_OPTIONS} />
              <Input name="fecha" type="date" value={localFilters.fecha} onChange={handleFilterChange} />
              <Button variant="outline" onClick={handleClear}>Limpiar filtros</Button>
            </div>
          </div>

          {error && (
            <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
              <span>⚠</span> {typeof error === 'string' ? error : 'Ocurrió un error'}
            </div>
          )}

          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
            {loading ? (
              <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando registros de asistencia...</div>
            ) : (
              <Table columns={COLUMNS} data={asistencias} />
            )}
          </div>
        </div>
      </div>

      <ProgramarModal
        isOpen={showProgramar}
        onClose={() => setShowProgramar(false)}
        onSubmit={handleProgramar}
        loading={programando}
        empleadosActivos={empleadosActivos}
      />

      <JustificarModal
        registro={justificarTarget}
        onClose={() => setJustificarTarget(null)}
        onSubmit={handleJustificar}
        loading={justificando}
      />

      <AuditoriaAccionModal
        registro={auditoriaTarget}
        mode={auditoriaMode}
        onClose={() => setAuditoriaTarget(null)}
        onSubmit={handleAuditoriaSubmit}
        loading={auditoriaLoading}
      />

      <AuditoriaHistorialModal isOpen={showAuditoriaHistorial} onClose={() => setShowAuditoriaHistorial(false)} />
    </div>
  );
}
