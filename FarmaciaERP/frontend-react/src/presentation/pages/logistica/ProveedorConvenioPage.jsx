import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Table    from '@/presentation/components/ui/Table';
import Badge    from '@/presentation/components/ui/Badge';
import Button   from '@/presentation/components/ui/Button';
import Input    from '@/presentation/components/ui/Input';
import Select   from '@/presentation/components/ui/Select';
import Modal    from '@/presentation/components/ui/Modal';
import LogisticaRail, { BLOQUE_A_FASES } from '@/presentation/components/logistica/LogisticaRail';
import { useProveedores } from '@/presentation/hooks/useProveedores';
import { useConvenios }   from '@/presentation/hooks/useConvenios';
import { useCases }       from '@/infrastructure';

const ESTADO_OPTIONS = [
  { value: 'ACTIVO',   label: 'Activo'   },
  { value: 'INACTIVO', label: 'Inactivo' },
];

const INITIAL_PROVEEDOR_FORM = { razonSocial: '', ruc: '', contactoEmail: '', contactoTelefono: '' };

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

// ─────────────────────────────────────────────────────────────────────────
// TAB: Proveedores (LOG.00 — Maestro de proveedores homologados)
// ─────────────────────────────────────────────────────────────────────────
function ProveedoresTab() {
  const { proveedores, loading, error, createProveedor, editProveedor, deleteProveedor, applyFilters, clearFilters } = useProveedores();
  const [localFilters, setLocalFilters] = useState({ razonSocial: '', estado: '' });
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState(INITIAL_PROVEEDOR_FORM);
  const [formErrors, setFormErrors] = useState({});
  const [creating, setCreating] = useState(false);

  const [showEdit, setShowEdit] = useState(false);
  const [editId, setEditId] = useState(null);
  const [editForm, setEditForm] = useState(INITIAL_PROVEEDOR_FORM);
  const [editing, setEditing] = useState(false);

  const [toDelete, setToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const COLUMNS = [
    { key: 'razonSocial', label: 'Razón social' },
    { key: 'ruc', label: 'RUC' },
    { key: 'contactoEmail', label: 'Contacto' },
    { key: 'contactoTelefono', label: 'Teléfono' },
    { key: 'estado', label: 'Estado', render: (val) => <Badge value={val} /> },
  ];

  function handleFilterChange(e) {
    const next = { ...localFilters, [e.target.name]: e.target.value };
    setLocalFilters(next);
    applyFilters(next);
  }
  function handleClear() {
    setLocalFilters({ razonSocial: '', estado: '' });
    clearFilters();
  }

  function handleFormChange(e) {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    setFormErrors((p) => ({ ...p, [e.target.name]: '' }));
  }
  function validateForm() {
    const e = {};
    if (!form.razonSocial) e.razonSocial = 'Requerido';
    if (!form.ruc) e.ruc = 'Requerido';
    else if (!/^\d{11}$/.test(form.ruc)) e.ruc = 'Debe tener 11 dígitos';
    return e;
  }
  async function handleCreate() {
    const e = validateForm();
    if (Object.keys(e).length) { setFormErrors(e); return; }
    setCreating(true);
    const ok = await createProveedor(form);
    setCreating(false);
    if (ok) { setShowCreate(false); setForm(INITIAL_PROVEEDOR_FORM); setFormErrors({}); }
  }
  function handleCloseCreate() {
    setShowCreate(false); setForm(INITIAL_PROVEEDOR_FORM); setFormErrors({});
  }

  function handleOpenEdit(row) {
    setEditId(row.id);
    setEditForm({
      razonSocial: row.razonSocial ?? '',
      ruc: row.ruc ?? '',
      contactoEmail: row.contactoEmail ?? '',
      contactoTelefono: row.contactoTelefono ?? '',
    });
    setShowEdit(true);
  }
  function handleEditFormChange(e) {
    setEditForm((p) => ({ ...p, [e.target.name]: e.target.value }));
  }
  async function handleEdit() {
    if (!editForm.razonSocial) return;
    setEditing(true);
    const ok = await editProveedor(editId, editForm);
    setEditing(false);
    if (ok) { setShowEdit(false); setEditId(null); }
  }

  async function handleConfirmDelete() {
    setDeleting(true);
    await deleteProveedor(toDelete.id);
    setDeleting(false);
    setToDelete(null);
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{proveedores.length} proveedores homologados</p>
        <Button onClick={() => setShowCreate(true)}>+ Nuevo proveedor</Button>
      </div>

      <div className="bg-slate-50 rounded-xl border border-slate-200 p-4 mb-5">
        <div className="grid grid-cols-1 sm:grid-cols-4 gap-3">
          <Input name="razonSocial" placeholder="Buscar por razón social o RUC..." value={localFilters.razonSocial} onChange={handleFilterChange} />
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
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando proveedores...</div>
        ) : (
          <Table columns={COLUMNS} data={proveedores} onEdit={handleOpenEdit} onDelete={(row) => setToDelete(row)} />
        )}
      </div>

      {/* Crear */}
      <Modal
        isOpen={showCreate}
        title="Nuevo proveedor (LOG.00)"
        onClose={handleCloseCreate}
        onConfirm={handleCreate}
        confirmText="Registrar proveedor"
        loading={creating}
      >
        <div className="flex flex-col gap-4">
          <Input label="Razón social" name="razonSocial" value={form.razonSocial} onChange={handleFormChange} error={formErrors.razonSocial} />
          <Input label="RUC" name="ruc" value={form.ruc} onChange={handleFormChange} error={formErrors.ruc} placeholder="11 dígitos" />
          <Input label="Correo de contacto" name="contactoEmail" type="email" value={form.contactoEmail} onChange={handleFormChange} />
          <Input label="Teléfono de contacto" name="contactoTelefono" value={form.contactoTelefono} onChange={handleFormChange} />
        </div>
      </Modal>

      {/* Editar */}
      <Modal
        isOpen={showEdit}
        title={`Editar proveedor #${editId ?? ''}`}
        onClose={() => setShowEdit(false)}
        onConfirm={handleEdit}
        confirmText="Guardar cambios"
        loading={editing}
      >
        <div className="flex flex-col gap-4">
          <Input label="Razón social" name="razonSocial" value={editForm.razonSocial} onChange={handleEditFormChange} />
          <Input label="RUC" name="ruc" value={editForm.ruc} disabled />
          <Input label="Correo de contacto" name="contactoEmail" type="email" value={editForm.contactoEmail} onChange={handleEditFormChange} />
          <Input label="Teléfono de contacto" name="contactoTelefono" value={editForm.contactoTelefono} onChange={handleEditFormChange} />
        </div>
      </Modal>

      {/* Eliminar */}
      <Modal
        isOpen={!!toDelete}
        title="Eliminar proveedor"
        onClose={() => setToDelete(null)}
        onConfirm={handleConfirmDelete}
        confirmText="Eliminar"
        confirmVariant="danger"
        loading={deleting}
      >
        <p className="text-sm text-slate-600">
          ¿Deseas eliminar a <span className="font-semibold text-slate-800">{toDelete?.razonSocial}</span>?
          Esta acción no se puede deshacer.
        </p>
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// TAB: Convenios (LOG.02 — Contrato Marco / Info-Record, RN-MM-001 y RN-MM-004)
// ─────────────────────────────────────────────────────────────────────────
function emptyItem() { return { medicamentoId: '', precioPactado: '' }; }
const INITIAL_CONVENIO_FORM = { numero: '', proveedorId: '', fechaInicio: '', fechaFin: '', itemsPactados: [emptyItem()] };

function ConveniosTab() {
  const { convenios, loading, error, createConvenio } = useConvenios();
  const { proveedores } = useProveedores();
  const [medicamentos, setMedicamentos] = useState([]);
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState(INITIAL_CONVENIO_FORM);
  const [formError, setFormError] = useState('');
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    useCases.medicamentos.getAll.execute({}).then(setMedicamentos).catch(() => setMedicamentos([]));
  }, []);

  const proveedorOptions = proveedores
    .filter((p) => p.estaActivo)
    .map((p) => ({ value: p.id, label: `${p.razonSocial} — RUC ${p.ruc}` }));

  const medicamentoOptions = medicamentos.map((m) => ({ value: m.id, label: m.nombre }));

  const COLUMNS = [
    { key: 'numero', label: 'N° Contrato' },
    { key: 'razonSocialProveedor', label: 'Proveedor' },
    { key: 'fechaInicio', label: 'Inicio' },
    { key: 'fechaFin', label: 'Fin' },
    { key: 'estado', label: 'Estado', render: (val, row) => (
        <div className="flex items-center gap-2">
          <Badge value={val === 'VIGENTE' ? 'ACTIVO' : 'INACTIVO'} />
          <span className="text-xs text-slate-400 font-mono">{val}</span>
          {row.vigente && <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 border border-amber-200">blindado</span>}
        </div>
      ) },
    { key: 'itemsPactados', label: 'Ítems pactados', render: (val) => `${val?.length ?? 0} SKU` },
  ];

  function handleFormChange(e) {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
  }
  function handleItemChange(idx, field, value) {
    setForm((p) => {
      const items = [...p.itemsPactados];
      items[idx] = { ...items[idx], [field]: value };
      return { ...p, itemsPactados: items };
    });
  }
  function addItemRow() {
    setForm((p) => ({ ...p, itemsPactados: [...p.itemsPactados, emptyItem()] }));
  }
  function removeItemRow(idx) {
    setForm((p) => ({ ...p, itemsPactados: p.itemsPactados.filter((_, i) => i !== idx) }));
  }

  async function handleCreate() {
    setFormError('');
    setCreating(true);
    try {
      const ok = await createConvenio(form);
      if (ok) {
        setShowCreate(false);
        setForm(INITIAL_CONVENIO_FORM);
      } else {
        setFormError('No se pudo registrar el convenio');
      }
    } catch (err) {
      setFormError(err.message ?? 'Error al registrar el convenio');
    } finally {
      setCreating(false);
    }
  }
  function handleClose() {
    setShowCreate(false); setForm(INITIAL_CONVENIO_FORM); setFormError('');
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-5 gap-3 flex-wrap">
        <p className="text-sm text-slate-500">{convenios.length} contratos marco registrados</p>
        <Button onClick={() => setShowCreate(true)} disabled={proveedorOptions.length === 0}>
          + Nuevo convenio marco
        </Button>
      </div>

      {proveedorOptions.length === 0 && !loading && (
        <div className="mb-4 px-4 py-3 bg-amber-50 border border-amber-200 rounded-xl text-sm text-amber-700">
          Registra primero un proveedor activo en la pestaña <b>Proveedores</b> para poder pactar un convenio marco.
        </div>
      )}

      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600">
          {typeof error === 'string' ? error : 'Ocurrió un error'}
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Cargando convenios...</div>
        ) : (
          <Table columns={COLUMNS} data={convenios} />
        )}
      </div>

      <Modal
        isOpen={showCreate}
        title="Nuevo convenio marco (LOG.02)"
        onClose={handleClose}
        onConfirm={handleCreate}
        confirmText="Registrar convenio"
        loading={creating}
      >
        <div className="flex flex-col gap-4 max-h-[65vh] overflow-y-auto pr-1">
          <div className="px-3 py-2.5 bg-teal-50 border border-teal-200 rounded-lg text-xs text-teal-700 leading-relaxed">
            <span className="font-mono font-semibold mr-1">RN-MM-004</span>
            Los precios pactados aquí quedan congelados (Info-Record) para la Orden de Compra.
          </div>

          {formError && (
            <div className="px-3 py-2.5 bg-red-50 border border-red-200 rounded-lg text-xs text-red-600">{formError}</div>
          )}

          <Input label="N° de contrato marco" name="numero" placeholder="CM-2026-000000" value={form.numero} onChange={handleFormChange} />
          <Select label="Proveedor homologado" name="proveedorId" value={form.proveedorId} onChange={handleFormChange} options={proveedorOptions} />

          <div className="grid grid-cols-2 gap-4">
            <Input label="Vigencia — inicio" name="fechaInicio" type="date" value={form.fechaInicio} onChange={handleFormChange} />
            <Input label="Vigencia — fin" name="fechaFin" type="date" value={form.fechaFin} onChange={handleFormChange} />
          </div>

          <div>
            <div className="flex items-center justify-between mb-2">
              <span className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Ítems pactados (Info-Record)</span>
              <button type="button" onClick={addItemRow} className="text-xs text-teal-700 hover:underline font-medium">+ Agregar ítem</button>
            </div>
            <div className="flex flex-col gap-3">
              {form.itemsPactados.map((item, idx) => (
                <div key={idx} className="grid grid-cols-[1fr_120px_auto] gap-2 items-end">
                  <Select
                    name={`medicamento-${idx}`}
                    placeholder="Medicamento..."
                    value={item.medicamentoId}
                    onChange={(e) => handleItemChange(idx, 'medicamentoId', e.target.value)}
                    options={medicamentoOptions}
                  />
                  <Input
                    name={`precio-${idx}`}
                    type="number"
                    placeholder="Precio S/"
                    value={item.precioPactado}
                    onChange={(e) => handleItemChange(idx, 'precioPactado', e.target.value)}
                  />
                  <button
                    type="button"
                    onClick={() => removeItemRow(idx)}
                    disabled={form.itemsPactados.length === 1}
                    className="h-[42px] w-9 flex items-center justify-center text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all disabled:opacity-30 disabled:cursor-not-allowed"
                  >
                    ✕
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — Fase 01: Proveedor y Convenio
// ─────────────────────────────────────────────────────────────────────────
export default function ProveedorConvenioPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('proveedores');

  function handleRailNavigate(fase) {
    if (fase.id === 0) return;
    navigate(fase.path);
  }

  return (
    <div>
      <LogisticaRail activeStep={0} maxReached={3} onNavigate={handleRailNavigate} />

      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RN-MM-001 · RN-MM-004 · LOG.00 / LOG.02"
          title="Proveedor y Convenio Marco"
          description="Administra el maestro de proveedores (laboratorios) homologados y los contratos marco que congelan los precios pactados (Info-Record) para el proceso Procure-to-Pay."
          badge="ME1x / ME33K"
        />

        <div className="px-6 pt-4 flex gap-1 border-b border-slate-200">
          {[
            { id: 'proveedores', label: 'Proveedores' },
            { id: 'convenios', label: 'Convenios marco' },
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

        {tab === 'proveedores' ? <ProveedoresTab /> : <ConveniosTab />}
      </div>

      <p className="text-xs text-slate-400 font-mono mt-4 text-center">
        {BLOQUE_A_FASES.length} fases del Bloque A — Cimientos · Fase 01 completada al registrar proveedor y convenio vigente
      </p>
    </div>
  );
}
