import { useState } from 'react';
import Table  from '@/presentation/components/ui/Table';
import Badge  from '@/presentation/components/ui/Badge';
import Button from '@/presentation/components/ui/Button';
import Input  from '@/presentation/components/ui/Input';
import Select from '@/presentation/components/ui/Select';
import Modal  from '@/presentation/components/ui/Modal';
import { useEmpleados, useAuditoriaEmpleados } from '@/presentation/hooks/useEmpleados';
import { ROLES_EMPLEADO, CONTRATOS_EMPLEADO, MOVIMIENTOS_RRHH } from '@/domain/models/Empleado';
import RrhhTabs from '@/presentation/components/rrhh/RrhhTabs';

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

const ROL_OPTIONS = Object.entries(ROLES_EMPLEADO).map(([value, label]) => ({ value, label }));
const CONTRATO_OPTIONS = Object.entries(CONTRATOS_EMPLEADO).map(([value, label]) => ({ value, label }));
const ESTADO_OPTIONS = [
  { value: 'ACTIVO', label: 'Activo' },
  { value: 'INACTIVO', label: 'Inactivo' },
];

const INITIAL_FORM = {
  apellidoPaterno: '', apellidoMaterno: '', nombres: '', dni: '',
  rol: 'QUIMICO_FARMACEUTICO', area: '', fechaIngreso: '', salario: '',
  contrato: 'INDEFINIDO', correo: '', telefono: '',
};

function exportEmpleadosCSV(empleados) {
  if (!empleados.length) return;
  const headers = ['Código', 'Apellido Paterno', 'Apellido Materno', 'Nombres', 'DNI', 'Rol', 'Salario', 'Contrato', 'Estado'];
  const rows = empleados.map((e) => [
    e.codigo, e.apellidoPaterno, e.apellidoMaterno, e.nombres, e.dni,
    ROLES_EMPLEADO[e.rol] ?? e.rol, e.salario, CONTRATOS_EMPLEADO[e.contrato] ?? e.contrato, e.estado,
  ]);
  const csv = [headers, ...rows]
    .map((r) => r.map((v) => `"${String(v ?? '').replace(/"/g, '""')}"`).join(','))
    .join('\n');
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'Colaboradores_Contratacion.csv';
  a.click();
  URL.revokeObjectURL(url);
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Auditoría
// ─────────────────────────────────────────────────────────────────────────
function AuditoriaModal({ isOpen, onClose }) {
  const { logs, loading, loadAuditoria } = useAuditoriaEmpleados();
  const [codigo, setCodigo] = useState('');

  function handleOpenFilter(e) {
    const value = e.target.value;
    setCodigo(value);
    loadAuditoria(value || undefined);
  }

  return (
    <Modal isOpen={isOpen} title="Auditoría de Contratación (RRHH.01)" onClose={onClose}>
      <div className="flex flex-col gap-4">
        <Input
          label="Filtrar por código de empleado"
          name="codigo"
          value={codigo}
          onChange={handleOpenFilter}
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
                    <td className="px-3 py-2">{MOVIMIENTOS_RRHH[log.tipo] ?? log.tipo}</td>
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
// Modal: Cambio de estado (baja inteligente / reactivar)
// ─────────────────────────────────────────────────────────────────────────
function StatusModal({ empleado, mode, onClose, onSubmit, loading }) {
  const [modoBaja, setModoBaja] = useState('sin-turnos');
  const [motivo, setMotivo] = useState('');
  const [turnoInfoInmediata, setTurnoInfoInmediata] = useState('');
  const [fechaEfectiva, setFechaEfectiva] = useState('');
  const [observacion, setObservacion] = useState('');
  const [turnoInfoProgramada, setTurnoInfoProgramada] = useState('');

  if (!empleado) return null;
  const isReactivar = mode === 'reactivar';

  function reset() {
    setModoBaja('sin-turnos'); setMotivo(''); setTurnoInfoInmediata('');
    setFechaEfectiva(''); setObservacion(''); setTurnoInfoProgramada('');
  }
  function handleClose() { reset(); onClose(); }

  async function handleConfirm() {
    if (isReactivar) {
      await onSubmit({ tipo: 'reactivar' });
      reset();
      return;
    }
    if (modoBaja === 'sin-turnos') {
      await onSubmit({ tipo: 'sin-turnos' });
    } else if (modoBaja === 'inmediata') {
      if (!motivo.trim()) return;
      await onSubmit({ tipo: 'inmediata', motivo, turnoInfo: turnoInfoInmediata });
    } else {
      if (!fechaEfectiva) return;
      await onSubmit({ tipo: 'programada', fechaEfectiva, observacion, turnoInfo: turnoInfoProgramada });
    }
    reset();
  }

  return (
    <Modal
      isOpen={!!empleado}
      title={isReactivar ? 'Reactivar colaborador' : 'Dar de baja'}
      onClose={handleClose}
      onConfirm={handleConfirm}
      confirmText={isReactivar ? 'Reactivar' : 'Confirmar'}
      confirmVariant={isReactivar ? 'primary' : 'danger'}
      loading={loading}
    >
      <div className="flex flex-col gap-4">
        <div className="px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-lg text-sm text-slate-600">
          <span className="font-semibold text-slate-800">{empleado.codigo}</span> — {empleado.nombreCompleto || empleado.nombres}
        </div>

        {!isReactivar && (
          <>
            <Select
              label="¿Tiene turnos activos o programados?"
              name="modoBaja"
              value={modoBaja}
              onChange={(e) => setModoBaja(e.target.value)}
              placeholder=""
              options={[
                { value: 'sin-turnos', label: 'No — dar de baja directamente' },
                { value: 'inmediata', label: 'Sí — baja de inmediato (cancela turno)' },
                { value: 'programada', label: 'Sí — programar la baja al fin del último turno' },
              ]}
            />

            {modoBaja === 'inmediata' && (
              <>
                <div className="px-3 py-2.5 bg-amber-50 border border-amber-200 rounded-lg text-xs text-amber-700 leading-relaxed">
                  Se cancelará el turno pendiente. El colaborador quedará <strong>Inactivo</strong> y ya no podrá
                  registrar asistencia ni recibir nuevos turnos.
                </div>
                <Input label="Motivo de la baja *" name="motivo" value={motivo}
                  onChange={(e) => setMotivo(e.target.value)}
                  placeholder="Ej. Renuncia, incumplimiento de horario..." />
                <Input label="Información del turno a cancelar" name="turnoInfoInmediata" value={turnoInfoInmediata}
                  onChange={(e) => setTurnoInfoInmediata(e.target.value)}
                  placeholder="Ej. Turno Tarde 15:00-23:00 del 20/07/2026" />
              </>
            )}

            {modoBaja === 'programada' && (
              <>
                <div className="px-3 py-2.5 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
                  El colaborador finalizará su último turno antes de quedar inactivo.
                </div>
                <Input label="Fecha y hora efectiva *" name="fechaEfectiva" type="datetime-local"
                  value={fechaEfectiva} onChange={(e) => setFechaEfectiva(e.target.value)} />
                <Input label="Información del último turno activo" name="turnoInfoProgramada" value={turnoInfoProgramada}
                  onChange={(e) => setTurnoInfoProgramada(e.target.value)}
                  placeholder="Ej. Turno Noche 23:00-07:00 del 20/07/2026" />
                <Input label="Observación (opcional)" name="observacion" value={observacion}
                  onChange={(e) => setObservacion(e.target.value)} />
              </>
            )}
          </>
        )}
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — RRHH.01 Contratación
// ─────────────────────────────────────────────────────────────────────────
export default function ContratacionPage() {
  const {
    empleados, loading, error, applyFilters, clearFilters,
    createEmpleado, editEmpleado, deleteEmpleado,
    reactivarEmpleado, bajaSinTurnos, bajaInmediata, bajaProgramada,
  } = useEmpleados();

  const [localFilters, setLocalFilters] = useState({ texto: '', estado: '' });
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState(INITIAL_FORM);
  const [formErrors, setFormErrors] = useState({});
  const [creating, setCreating] = useState(false);

  const [showEdit, setShowEdit] = useState(false);
  const [editId, setEditId] = useState(null);
  const [editForm, setEditForm] = useState(INITIAL_FORM);
  const [editing, setEditing] = useState(false);

  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const [statusTarget, setStatusTarget] = useState(null);
  const [statusMode, setStatusMode] = useState('baja');
  const [statusLoading, setStatusLoading] = useState(false);

  const [showAuditoria, setShowAuditoria] = useState(false);

  const COLUMNS = [
    { key: 'codigo', label: 'Código' },
    { key: 'nombreCompleto', label: 'Colaborador' },
    { key: 'dni', label: 'DNI' },
    { key: 'rol', label: 'Rol', render: (val) => ROLES_EMPLEADO[val] ?? val },
    { key: 'salario', label: 'Salario', render: (val) => `S/. ${Number(val).toFixed(2)}` },
    { key: 'contrato', label: 'Contrato', render: (val) => CONTRATOS_EMPLEADO[val] ?? val },
    {
      key: 'estado', label: 'Estado', render: (val, row) => (
        <div className="flex flex-col gap-1">
          <Badge value={val} />
          {row.bajaProgramadaFechaEfectiva && (
            <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 border border-sky-200 w-fit">
              baja programada: {new Date(row.bajaProgramadaFechaEfectiva).toLocaleString('es-PE')}
            </span>
          )}
        </div>
      ),
    },
    {
      key: 'accionesEstado', label: 'Estado / Baja', render: (_val, row) => (
        row.estado === 'ACTIVO' ? (
          <Button variant="outline" className="!px-3 !py-1.5 !text-xs"
            onClick={() => { setStatusTarget(row); setStatusMode('baja'); }}>
            Dar de baja
          </Button>
        ) : (
          <Button variant="outline" className="!px-3 !py-1.5 !text-xs"
            onClick={() => { setStatusTarget(row); setStatusMode('reactivar'); }}>
            Reactivar
          </Button>
        )
      ),
    },
  ];

  function handleFilterChange(e) {
    const next = { ...localFilters, [e.target.name]: e.target.value };
    setLocalFilters(next);
    applyFilters(next);
  }
  function handleClear() {
    setLocalFilters({ texto: '', estado: '' });
    clearFilters();
  }

  function handleFormChange(e) {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    setFormErrors((p) => ({ ...p, [e.target.name]: '' }));
  }
  function validateForm(f) {
    const e = {};
    if (!f.apellidoPaterno) e.apellidoPaterno = 'Requerido';
    if (!f.apellidoMaterno) e.apellidoMaterno = 'Requerido';
    if (!f.nombres) e.nombres = 'Requerido';
    if (!f.dni) e.dni = 'Requerido';
    if (f.salario === '' || Number(f.salario) < 0) e.salario = 'Salario inválido';
    return e;
  }
  async function handleCreate() {
    const e = validateForm(form);
    if (Object.keys(e).length) { setFormErrors(e); return; }
    setCreating(true);
    const ok = await createEmpleado(form);
    setCreating(false);
    if (ok) { setShowCreate(false); setForm(INITIAL_FORM); setFormErrors({}); }
  }
  function handleCloseCreate() {
    setShowCreate(false); setForm(INITIAL_FORM); setFormErrors({});
  }

  function handleOpenEdit(row) {
    setEditId(row.id);
    setEditForm({
      apellidoPaterno: row.apellidoPaterno ?? '',
      apellidoMaterno: row.apellidoMaterno ?? '',
      nombres: row.nombres ?? '',
      dni: row.dni ?? '',
      rol: row.rol ?? 'QUIMICO_FARMACEUTICO',
      area: row.area ?? '',
      fechaIngreso: row.fechaIngreso ?? '',
      salario: row.salario ?? '',
      contrato: row.contrato ?? 'INDEFINIDO',
      correo: row.correo ?? '',
      telefono: row.telefono ?? '',
    });
    setShowEdit(true);
  }
  function handleEditFormChange(e) {
    setEditForm((p) => ({ ...p, [e.target.name]: e.target.value }));
  }
  async function handleEdit() {
    const e = validateForm(editForm);
    if (Object.keys(e).length) return;
    setEditing(true);
    const ok = await editEmpleado(editId, editForm);
    setEditing(false);
    if (ok) { setShowEdit(false); setEditId(null); }
  }

  async function handleConfirmDelete() {
    setDeleting(true);
    await deleteEmpleado(toDelete.id);
    setDeleting(false);
    setToDelete(null);
  }

  async function handleStatusSubmit(payload) {
    setStatusLoading(true);
    if (payload.tipo === 'reactivar') {
      await reactivarEmpleado(statusTarget.id);
    } else if (payload.tipo === 'sin-turnos') {
      await bajaSinTurnos(statusTarget.id);
    } else if (payload.tipo === 'inmediata') {
      await bajaInmediata(statusTarget.id, { motivo: payload.motivo, turnoInfo: payload.turnoInfo });
    } else if (payload.tipo === 'programada') {
      await bajaProgramada(statusTarget.id, {
        fechaEfectiva: payload.fechaEfectiva, observacion: payload.observacion, turnoInfo: payload.turnoInfo,
      });
    }
    setStatusLoading(false);
    setStatusTarget(null);
  }

  return (
    <div>
      <RrhhTabs />
      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RRHH.01 · Contratación"
          title="Gestión de Contratación"
          description="Registro de colaboradores y datos laborales, con lógica de bajas inteligentes según turnos activos."
          badge="Maestro de Empleados"
        />

        <div className="p-6">
          <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
            <p className="text-sm text-slate-500">{empleados.length} colaboradores registrados</p>
            <div className="flex gap-2 flex-wrap">
              <Button variant="outline" onClick={() => exportEmpleadosCSV(empleados)}>📥 Exportar</Button>
              <Button variant="secondary" onClick={() => setShowAuditoria(true)}>📄 Auditoría</Button>
              <Button onClick={() => setShowCreate(true)}>+ Nuevo empleado</Button>
            </div>
          </div>

          <div className="bg-slate-50 rounded-xl border border-slate-200 p-4 mb-5">
            <div className="grid grid-cols-1 sm:grid-cols-4 gap-3">
              <Input name="texto" placeholder="Buscar por código, nombre o DNI..." value={localFilters.texto} onChange={handleFilterChange} />
              <Select name="estado" placeholder="Todos los estados" value={localFilters.estado} onChange={handleFilterChange} options={ESTADO_OPTIONS} />
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
              <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando colaboradores...</div>
            ) : (
              <Table columns={COLUMNS} data={empleados} onEdit={handleOpenEdit} onDelete={(row) => setToDelete(row)} />
            )}
          </div>
        </div>
      </div>

      {/* Crear */}
      <Modal
        isOpen={showCreate}
        title="Nuevo empleado (RRHH.01)"
        onClose={handleCloseCreate}
        onConfirm={handleCreate}
        confirmText="Registrar empleado"
        loading={creating}
      >
        <div className="flex flex-col gap-4 max-h-[65vh] overflow-y-auto pr-1">
          <div className="grid grid-cols-2 gap-4">
            <Input label="Apellido Paterno *" name="apellidoPaterno" value={form.apellidoPaterno} onChange={handleFormChange} error={formErrors.apellidoPaterno} />
            <Input label="Apellido Materno *" name="apellidoMaterno" value={form.apellidoMaterno} onChange={handleFormChange} error={formErrors.apellidoMaterno} />
          </div>
          <Input label="Nombres *" name="nombres" value={form.nombres} onChange={handleFormChange} error={formErrors.nombres} />
          <Input label="DNI / Documento *" name="dni" value={form.dni} onChange={handleFormChange} error={formErrors.dni} />
          <div className="grid grid-cols-2 gap-4">
            <Select label="Rol Farmacéutico *" name="rol" value={form.rol} onChange={handleFormChange} options={ROL_OPTIONS} />
            <Input label="Área / Departamento" name="area" value={form.area} onChange={handleFormChange} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Fecha de Ingreso" name="fechaIngreso" type="date" value={form.fechaIngreso} onChange={handleFormChange} />
            <Input label="Salario Base Mensual (S/.) *" name="salario" type="number" value={form.salario} onChange={handleFormChange} error={formErrors.salario} />
          </div>
          <Select label="Tipo de Contrato *" name="contrato" value={form.contrato} onChange={handleFormChange} options={CONTRATO_OPTIONS} />
          <div className="grid grid-cols-2 gap-4">
            <Input label="Correo Electrónico" name="correo" type="email" value={form.correo} onChange={handleFormChange} />
            <Input label="Teléfono" name="telefono" value={form.telefono} onChange={handleFormChange} />
          </div>
        </div>
      </Modal>

      {/* Editar */}
      <Modal
        isOpen={showEdit}
        title={`Editar empleado #${editId ?? ''}`}
        onClose={() => setShowEdit(false)}
        onConfirm={handleEdit}
        confirmText="Guardar cambios"
        loading={editing}
      >
        <div className="flex flex-col gap-4 max-h-[65vh] overflow-y-auto pr-1">
          <div className="grid grid-cols-2 gap-4">
            <Input label="Apellido Paterno *" name="apellidoPaterno" value={editForm.apellidoPaterno} onChange={handleEditFormChange} />
            <Input label="Apellido Materno *" name="apellidoMaterno" value={editForm.apellidoMaterno} onChange={handleEditFormChange} />
          </div>
          <Input label="Nombres *" name="nombres" value={editForm.nombres} onChange={handleEditFormChange} />
          <Input label="DNI / Documento *" name="dni" value={editForm.dni} onChange={handleEditFormChange} />
          <div className="grid grid-cols-2 gap-4">
            <Select label="Rol Farmacéutico *" name="rol" value={editForm.rol} onChange={handleEditFormChange} options={ROL_OPTIONS} />
            <Input label="Área / Departamento" name="area" value={editForm.area} onChange={handleEditFormChange} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <Input label="Fecha de Ingreso" name="fechaIngreso" type="date" value={editForm.fechaIngreso} onChange={handleEditFormChange} />
            <Input label="Salario Base Mensual (S/.) *" name="salario" type="number" value={editForm.salario} onChange={handleEditFormChange} />
          </div>
          <Select label="Tipo de Contrato *" name="contrato" value={editForm.contrato} onChange={handleEditFormChange} options={CONTRATO_OPTIONS} />
          <div className="grid grid-cols-2 gap-4">
            <Input label="Correo Electrónico" name="correo" type="email" value={editForm.correo} onChange={handleEditFormChange} />
            <Input label="Teléfono" name="telefono" value={editForm.telefono} onChange={handleEditFormChange} />
          </div>
        </div>
      </Modal>

      {/* Eliminar */}
      <Modal
        isOpen={!!toDelete}
        title="Eliminar colaborador"
        onClose={() => setToDelete(null)}
        onConfirm={handleConfirmDelete}
        confirmText="Eliminar"
        confirmVariant="danger"
        loading={deleting}
      >
        <p className="text-sm text-slate-600">
          ¿Deseas eliminar a <span className="font-semibold text-slate-800">{toDelete?.nombreCompleto}</span>?
          Esta acción es permanente.
        </p>
      </Modal>

      {/* Cambio de estado */}
      <StatusModal
        empleado={statusTarget}
        mode={statusMode}
        onClose={() => setStatusTarget(null)}
        onSubmit={handleStatusSubmit}
        loading={statusLoading}
      />

      {/* Auditoría */}
      <AuditoriaModal isOpen={showAuditoria} onClose={() => setShowAuditoria(false)} />
    </div>
  );
}
