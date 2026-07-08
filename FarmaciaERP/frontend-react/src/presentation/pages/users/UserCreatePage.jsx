import { useState }    from 'react';
import { useNavigate } from 'react-router-dom';
import MainLayout      from '@/presentation/components/layout/MainLayout';
import Input           from '@/presentation/components/ui/Input';
import Select          from '@/presentation/components/ui/Select';
import Button          from '@/presentation/components/ui/Button';
import { useUsers }    from '@/presentation/hooks/useUsers';

const ROL_OPTIONS    = [{ value: 'ADMINISTRADOR', label: 'Administrador' }, { value: 'JEFE_GENERAL', label: 'Jefe General' },
   { value: 'JEFE_LOGISTICA', label: 'Jefe Logistica' }, { value: 'QUIMICO_FARMACEUTICO', label: 'Quimico Farmaceutico' }, 
   { value: 'TECNICO_FAMACIA', label: 'Tecnico Farmacia' }, { value: 'CONTADOR', label: 'Contador' }

];
const ESTADO_OPTIONS = [{ value: 'activo', label: 'Activo' }, { value: 'inactivo', label: 'Inactivo' }];
const INITIAL        = { nombre: '', apellido: '', email: '', password: '', rol: 'USUARIO', estado: 'activo' };

export default function UserCreatePage() {
  const navigate = useNavigate();
  const { createUser, loading, error } = useUsers();
  const [form,   setForm]   = useState(INITIAL);
  const [errors, setErrors] = useState({});

  function handleChange(e) {
    setForm((prev)   => ({ ...prev, [e.target.name]: e.target.value }));
    setErrors((prev) => ({ ...prev, [e.target.name]: '' }));
  }

  function validate() {
    const e = {};
    if (!form.nombre)                          e.nombre   = 'Requerido';
    if (!form.apellido)                        e.apellido = 'Requerido';
    if (!form.email)                           e.email    = 'Requerido';
    if (!form.password || form.password.length < 6) e.password = 'Mínimo 6 caracteres';
    return e;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const e_ = validate();
    if (Object.keys(e_).length) { setErrors(e_); return; }
    const ok = await createUser(form);
    if (ok) navigate('/users');
  }

  return (
    <MainLayout>
      <div className="mb-8">
        <button onClick={() => navigate('/users')} className="text-sm text-slate-500 hover:text-slate-700 flex items-center gap-1 mb-4">
          ← Volver a usuarios
        </button>
        <h2 className="text-2xl font-bold text-slate-800">Nuevo usuario</h2>
        <p className="text-slate-500 text-sm mt-1">Completa los datos para crear un usuario</p>
      </div>

      <div className="bg-white rounded-2xl border border-slate-200 p-8 max-w-lg">
        {error && (
          <div className="mb-5 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
            <span>⚠</span> {error}
          </div>
        )}
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          <div className="grid grid-cols-2 gap-4">
            <Input label="Nombre"   name="nombre"   value={form.nombre}   onChange={handleChange} error={errors.nombre}   />
            <Input label="Apellido" name="apellido" value={form.apellido} onChange={handleChange} error={errors.apellido} />
          </div>
          <Input label="Email"      name="email"    type="email"    value={form.email}    onChange={handleChange} error={errors.email}    />
          <Input label="Contraseña" name="password" type="password" value={form.password} onChange={handleChange} error={errors.password} />
          <div className="grid grid-cols-2 gap-4">
            <Select label="Rol"    name="rol"    value={form.rol}    onChange={handleChange} options={ROL_OPTIONS}    />
            <Select label="Estado" name="estado" value={form.estado} onChange={handleChange} options={ESTADO_OPTIONS} />
          </div>
          <div className="flex gap-3 pt-2 border-t border-slate-100">
            <Button type="submit" loading={loading} className="flex-1">Crear usuario</Button>
        {/*
          <Button variant="outline" onClick={() => navigate('/users')}>Cancelar</Button>
*/}
          </div>
        </form>
      </div>
    </MainLayout>
  );
}