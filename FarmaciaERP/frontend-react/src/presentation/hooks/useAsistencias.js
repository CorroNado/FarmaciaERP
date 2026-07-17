import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';
import { useAuthContext } from '@/presentation/hooks/useAuthContext';

function useUsuarioActual() {
  const { user } = useAuthContext?.() ?? {};
  if (user?.nombre) return `${user.nombre} ${user.apellido ?? ''}`.trim();
  return 'Sistema';
}

export function useAsistencias() {
  const usuario = useUsuarioActual();
  const [allAsistencias, setAllAsistencias] = useState([]);
  const [asistencias, setAsistencias] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadAsistencias = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.asistencias.getAll.execute({});
      setAllAsistencias(data);
      setAsistencias(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener los registros de asistencia');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      const data = await loadAsistencias();
      if (cancelled) return;
      void data;
    })();
    return () => { cancelled = true; };
  }, [loadAsistencias]);

  function applyFilters(filters) {
    let result = [...allAsistencias];
    if (filters.texto) {
      const q = filters.texto.toLowerCase();
      result = result.filter((r) =>
        r.colaborador?.toLowerCase().includes(q) ||
        r.codigoEmpleado?.toLowerCase().includes(q)
      );
    }
    if (filters.estado) {
      result = result.filter((r) => r.estado === filters.estado);
    }
    if (filters.fecha) {
      result = result.filter((r) => r.fecha === filters.fecha);
    }
    setAsistencias(result);
  }

  function clearFilters() {
    setAsistencias(allAsistencias);
  }

  async function runAction(action) {
    try {
      setLoading(true);
      setError(null);
      const result = await action();
      await loadAsistencias();
      return result ?? true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Ocurrió un error');
      return false;
    } finally {
      setLoading(false);
    }
  }

  const programarAsistencia = (formData) => runAction(() => useCases.asistencias.programar.execute(formData));
  const marcarEntrada = (id) => runAction(() => useCases.asistencias.marcarEntrada.execute(id, usuario));
  const marcarSalida = (id) => runAction(() => useCases.asistencias.marcarSalida.execute(id, usuario));
  const justificar = (id, datos) => runAction(() => useCases.asistencias.justificar.execute(id, datos, usuario));
  const editarAsistencia = (id, datos) => runAction(() => useCases.asistencias.editar.execute(id, datos, usuario));
  const eliminarAsistencia = (id, datos) => runAction(() => useCases.asistencias.eliminar.execute(id, datos, usuario));

  return {
    asistencias,
    loading,
    error,
    refetch: loadAsistencias,
    applyFilters,
    clearFilters,
    programarAsistencia,
    marcarEntrada,
    marcarSalida,
    justificar,
    editarAsistencia,
    eliminarAsistencia,
  };
}

export function useAuditoriaAsistencias() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadAuditoria = useCallback(async (codigoEmpleado) => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.asistencias.auditoria.execute(codigoEmpleado);
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
