import { useState }    from 'react';
import { useNavigate } from 'react-router-dom';
import Table           from '@/presentation/components/ui/Table';
import Badge           from '@/presentation/components/ui/Badge';
import Button          from '@/presentation/components/ui/Button';
import Input           from '@/presentation/components/ui/Input';
import Select          from '@/presentation/components/ui/Select';
import { useUsers }    from '@/presentation/hooks/useUsers';

const ROL_OPTIONS = [
  { value: 'ADMINISTRADOR',        label: 'Administrador'        },
  { value: 'JEFE_GENERAL',         label: 'Jefe General'         },
  { value: 'JEFE_LOGISTICA',       label: 'Jefe de Logística'    },
  { value: 'QUIMICO_FARMACEUTICO', label: 'Químico Farmacéutico' },
  { value: 'TECNICO_FARMACIA',     label: 'Técnico de Farmacia'  },
  { value: 'CONTADOR',             label: 'Contador'             },
];

const COLUMNS = [
  { key: 'id',       label: 'ID'       },
  { key: 'nombre',   label: 'Nombre'   },
  { key: 'apellido', label: 'Apellido' },
  { key: 'email',    label: 'Email'    },
  { key: 'rol',      label: 'Rol',    render: (val) => <Badge value={val} /> },
  { key: 'fecha',    label: 'Fecha',  render: (val) => val?.slice(0, 10) ?? '—' },
];
const INITIAL_EDIT = {
  nombre:   '',
  apellido: '',
  email:    '',
  password: '',
  role:     'ADMINISTRADOR',
  estado:   'ACTIVO',
};

const INITIAL_FORM = {
  nombre: '', apellido: '', email: '',
  password: '', role: 'ADMINISTRADOR',
};

export default function UserListPage() {
  const navigate = useNavigate();
  const { users, loading, error, createUser, editUser, deleteUser, applyFilters, clearFilters } = useUsers();
  const [localFilters, setLocalFilters] = useState({ nombre: '', estado: '', rol: '' });
  const [toDelete,     setToDelete]     = useState(null);
  const [deleting,     setDeleting]     = useState(false);
  const [showCreate,   setShowCreate]   = useState(false);
  const [form,         setForm]         = useState(INITIAL_FORM);
  const [formErrors,   setFormErrors]   = useState({});
  const [creating,     setCreating]     = useState(false);
const [showEdit,   setShowEdit]   = useState(false);
const [editForm,   setEditForm]   = useState(INITIAL_EDIT);
const [editErrors, setEditErrors] = useState({});
const [editing,    setEditing]    = useState(false);
const [editId,     setEditId]     = useState(null);
async function handleOpenEdit(row) {
  setEditId(row.id);
  setEditForm({
    nombre:   row.nombre   ?? '',
    apellido: row.apellido ?? '',
    email:    row.email    ?? '',
    password: '',
    role:     row.rol      ?? 'ADMINISTRADOR',
    estado:   row.estado   ?? 'ACTIVO',
  });
  setEditErrors({});
  setShowEdit(true);
}

function handleEditFormChange(e) {
  setEditForm((prev)   => ({ ...prev, [e.target.name]: e.target.value }));
  setEditErrors((prev) => ({ ...prev, [e.target.name]: '' }));
}

function validateEdit() {
  const e = {};
  if (!editForm.nombre)   e.nombre   = 'Requerido';
  if (!editForm.apellido) e.apellido = 'Requerido';
  if (!editForm.email)    e.email    = 'Requerido';
  return e;
}

async function handleEdit() {
  const e = validateEdit();
  if (Object.keys(e).length) { setEditErrors(e); return; }
  setEditing(true);
  const ok = await editUser(editId, editForm);
  setEditing(false);
  if (ok) {
    setShowEdit(false);
    setEditForm(INITIAL_EDIT);
    setEditId(null);
  }
}

function handleCloseEdit() {
  setShowEdit(false);
  setEditForm(INITIAL_EDIT);
  setEditId(null);
  setEditErrors({});
}
  // ─── Filtros ────────────────────────────────────────────────────────────────
  function handleFilterChange(e) {
    const newFilters = { ...localFilters, [e.target.name]: e.target.value };
    setLocalFilters(newFilters);
    applyFilters(newFilters); // ← filtra al instante
  }
  function handleClear() {
    setLocalFilters({ nombre: '', rol: '' });
    clearFilters();
  }

  // ─── Crear usuario ──────────────────────────────────────────────────────────
  function handleFormChange(e) {
    setForm((prev)       => ({ ...prev, [e.target.name]: e.target.value }));
    setFormErrors((prev) => ({ ...prev, [e.target.name]: '' }));
  }

  function validateForm() {
    const e = {};
    if (!form.nombre)                            e.nombre   = 'Requerido';
    if (!form.apellido)                          e.apellido = 'Requerido';
    if (!form.email)                             e.email    = 'Requerido';
    if (!form.password || form.password.length < 6) e.password = 'Mínimo 6 caracteres';
    return e;
  }

  async function handleCreate() {
    const e = validateForm();
    if (Object.keys(e).length) { setFormErrors(e); return; }
    setCreating(true);
    const ok = await createUser(form);
    setCreating(false);
    if (ok) {
      setShowCreate(false);
      setForm(INITIAL_FORM);
      setFormErrors({});
    }
  }

  function handleCloseCreate() {
    setShowCreate(false);
    setForm(INITIAL_FORM);
    setFormErrors({});
  }
  
  // ─── Eliminar ───────────────────────────────────────────────────────────────
  async function handleConfirmDelete() {
    setDeleting(true);
    await deleteUser(toDelete.id);
    setDeleting(false);
    setToDelete(null);
  }

  return (
    <>

      {/* Header */}
      <div className="flex items-center justify-between mb-8">
        <div>
          <h2 className="text-2xl font-bold text-slate-800">Usuarios</h2>
          <p className="text-slate-500 text-sm mt-1">{users.length} registros encontrados</p>
        </div>
        <Button onClick={() => setShowCreate(true)}>
          + Nuevo usuario
        </Button>
      </div>

      {/* Filtros */}
      <div className="bg-white rounded-2xl border border-slate-200 p-5 mb-6">
        <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-4">Filtros</p>
        <div className="grid grid-cols-1 sm:grid-cols-4 gap-3">
          <Input
              name="nombre"
              placeholder="Buscar por nombre..."
              value={localFilters.nombre}
              onChange={handleFilterChange}
          />
          <Select
              name="rol"
              placeholder="Todos los roles"
              value={localFilters.rol}
              onChange={handleFilterChange}
              options={ROL_OPTIONS}
          />
          <Button variant="outline" onClick={handleClear}>
            Limpiar filtros
          </Button>
        </div>
      </div>

      {/* Error */}
      {error && (
        <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
          <span>⚠</span> {error}
        </div>
      )}

      {/* Tabla */}
      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-16 text-slate-400 text-sm gap-2">
            <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"/>
            </svg>
            Cargando usuarios...
          </div>
        ) : (
          <Table
            columns={COLUMNS}
            data={users}
            onEdit={(row)   => handleOpenEdit(row)}
            onDelete={(row) => setToDelete(row)}
            onReport={(row) => navigate(`/reports/access/${row.id}`)}
          />
        )}
      </div>

      {/* ── Modal Crear Usuario ─────────────────────────────────────────────── */}
      {showCreate && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* Backdrop */}
          <div className="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" onClick={handleCloseCreate} />

          {/* Modal */}
          <div className="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg z-10 overflow-hidden">

            {/* Header */}
            <div className="px-6 py-5 border-b border-slate-100 flex items-center justify-between">
              <div>
                <h2 className="text-base font-semibold text-slate-800">Nuevo usuario</h2>
                <p className="text-xs text-slate-500 mt-0.5">Completa los datos para crear un usuario</p>
              </div>
              <button
                onClick={handleCloseCreate}
                className="w-8 h-8 flex items-center justify-center rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 transition-all"
              >
                ✕
              </button>
            </div>

            {/* Body */}
            <div className="px-6 py-5 flex flex-col gap-4">
              {error && (
                <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
                  <span>⚠</span> {error}
                </div>
              )}
              <div className="grid grid-cols-2 gap-4">
                <Input label="Nombre"   name="nombre"   value={form.nombre}   onChange={handleFormChange} error={formErrors.nombre}   />
                <Input label="Apellido" name="apellido" value={form.apellido} onChange={handleFormChange} error={formErrors.apellido} />
              </div>
              <Input label="Email"      name="email"    type="email"    value={form.email}    onChange={handleFormChange} error={formErrors.email}    />
              <Input label="Contraseña" name="password" type="password" value={form.password} onChange={handleFormChange} error={formErrors.password} />
              <div className="grid grid-cols-2 gap-4">
                <Select label="Rol"    name="role"   value={form.role}   onChange={handleFormChange} options={ROL_OPTIONS}    />
              </div>
            </div>

            {/* Footer */}
            <div className="px-6 py-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3">
              <Button variant="outline" onClick={handleCloseCreate} disabled={creating}>
                Cancelar
              </Button>
              <Button onClick={handleCreate} loading={creating}>
                Crear usuario
              </Button>
            </div>
          </div>
        </div>
      )}

      {/* ── Modal Eliminar ──────────────────────────────────────────────────── */}
      {toDelete && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" onClick={() => setToDelete(null)} />
          <div className="relative bg-white rounded-2xl shadow-2xl w-full max-w-md z-10 overflow-hidden">
            <div className="px-6 py-5 border-b border-slate-100 flex items-center justify-between">
              <h2 className="text-base font-semibold text-slate-800">Eliminar usuario</h2>
              <button onClick={() => setToDelete(null)} className="w-8 h-8 flex items-center justify-center rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 transition-all">
                ✕
              </button>
            </div>
            <div className="px-6 py-5">
              <p className="text-slate-600 text-sm leading-relaxed">
                ¿Estás seguro que deseas eliminar a{' '}
                <span className="font-semibold text-slate-800">
                  {toDelete?.nombre} {toDelete?.apellido}
                </span>?
                Esta acción no se puede deshacer.
              </p>
            </div>
            <div className="px-6 py-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3">
              <Button variant="outline" onClick={() => setToDelete(null)} disabled={deleting}>Cancelar</Button>
              <Button variant="danger" onClick={handleConfirmDelete} loading={deleting}>Eliminar</Button>
            </div>
          </div>
        </div>
      )}
      {/* ── Modal Editar ────────────────────────────────────────────────────── */}
      {showEdit && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" onClick={handleCloseEdit} />
          <div className="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg z-10 overflow-hidden">
            <div className="px-6 py-5 border-b border-slate-100 flex items-center justify-between">
              <div>
                <h2 className="text-base font-semibold text-slate-800">Editar usuario</h2>
                <p className="text-xs text-slate-500 mt-0.5">Modifica los datos del usuario #{editId}</p>
              </div>
              <button onClick={handleCloseEdit} className="w-8 h-8 flex items-center justify-center rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 transition-all">✕</button>
            </div>
            <div className="px-6 py-5 flex flex-col gap-4">
              <div className="grid grid-cols-2 gap-4">
                <Input label="Nombre"   name="nombre"   value={editForm.nombre}   onChange={handleEditFormChange} error={editErrors.nombre}   />
                <Input label="Apellido" name="apellido" value={editForm.apellido} onChange={handleEditFormChange} error={editErrors.apellido} />
              </div>
              <Input label="Email"      name="email"    type="email"    value={editForm.email}    onChange={handleEditFormChange} error={editErrors.email} />
              <Input label="Nueva contraseña (opcional)" name="password" type="password" value={editForm.password} onChange={handleEditFormChange} placeholder="Dejar vacío para no cambiar" />
              <div className="grid grid-cols-2 gap-4">
                <Select label="Rol"    name="role"   value={editForm.role}   onChange={handleEditFormChange} options={ROL_OPTIONS}    />
              </div>
            </div>
            <div className="px-6 py-4 bg-slate-50 border-t border-slate-100 flex justify-end gap-3">
              <Button variant="outline" onClick={handleCloseEdit} disabled={editing}>Cancelar</Button>
              <Button onClick={handleEdit} loading={editing}>Guardar cambios</Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}