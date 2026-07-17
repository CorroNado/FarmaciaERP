import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';
import { useAuthContext } from '@/presentation/hooks/useAuthContext';

function useUsuarioActual() {
  const { user } = useAuthContext?.() ?? {};
  if (user?.nombre) return `${user.nombre} ${user.apellido ?? ''}`.trim();
  return 'Sistema';
}

export function useEmpleados() {
  const usuario = useUsuarioActual();
  const [allEmpleados, setAllEmpleados] = useState([]);
  const [empleados, setEmpleados] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadEmpleados = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.empleados.getAll.execute({});
      setAllEmpleados(data);
      setEmpleados(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener colaboradores');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      const data = await loadEmpleados();
      if (cancelled) return;
      void data;
    })();
    return () => { cancelled = true; };
  }, [loadEmpleados]);

  function applyFilters(filters) {
    let result = [...allEmpleados];
    if (filters.texto) {
      const q = filters.texto.toLowerCase();
      result = result.filter((e) =>
        e.codigo?.toLowerCase().includes(q) ||
        e.nombreCompleto?.toLowerCase().includes(q) ||
        e.dni?.includes(q)
      );
    }
    if (filters.estado) {
      result = result.filter((e) => e.estado === filters.estado);
    }
    setEmpleados(result);
  }

  function clearFilters() {
    setEmpleados(allEmpleados);
  }

  async function runAction(action) {
    try {
      setLoading(true);
      setError(null);
      const result = await action();
      await loadEmpleados();
      return result ?? true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error');
      return false;
    } finally {
      setLoading(false);
    }
  }

  const createEmpleado = (formData) => runAction(() => useCases.empleados.create.execute(formData, usuario));
  const editEmpleado = (id, formData) => runAction(() => useCases.empleados.update.execute(id, formData, usuario));
  const deleteEmpleado = (id) => runAction(() => useCases.empleados.delete.execute(id, usuario));
  const reactivarEmpleado = (id) => runAction(() => useCases.empleados.reactivar.execute(id, usuario));
  const bajaSinTurnos = (id) => runAction(() => useCases.empleados.bajaSinTurnos.execute(id, usuario));
  const bajaInmediata = (id, datos) => runAction(() => useCases.empleados.bajaInmediata.execute(id, datos, usuario));
  const bajaProgramada = (id, datos) => runAction(() => useCases.empleados.bajaProgramada.execute(id, datos, usuario));

  return {
    empleados,
    loading,
    error,
    refetch: loadEmpleados,
    applyFilters,
    clearFilters,
    createEmpleado,
    editEmpleado,
    deleteEmpleado,
    reactivarEmpleado,
    bajaSinTurnos,
    bajaInmediata,
    bajaProgramada,
  };
}

export function useAuditoriaEmpleados() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadAuditoria = useCallback(async (codigo) => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.empleados.auditoria.execute(codigo);
      setLogs(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener la auditoría');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  return { logs, loading, error, loadAuditoria };
}
