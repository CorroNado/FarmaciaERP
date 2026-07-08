import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useUsers() {
  const [allUsers, setAllUsers] = useState([]); // ← todos sin filtrar
  const [users,    setUsers]    = useState([]);
  const [loading,  setLoading]  = useState(false);
  const [error,    setError]    = useState(null);

  // ─── GET ALL ────────────────────────────────────────────────────────────────
  useEffect(() => {
    let cancelled = false;

    async function loadUsers() {
      setLoading(true);
      setError(null);
      try {
        const data = await useCases.users.getAll.execute({});
const activos = data.filter((u) => u.estado === 'ACTIVO');
if (!cancelled) {
  setAllUsers(activos);
  setUsers(activos);
}
      } catch (err) {
        if (!cancelled) setError(err.message ?? 'Error al obtener usuarios');
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    loadUsers();
    return () => { cancelled = true; };
  }, []);

  // ─── FILTROS EN JS ──────────────────────────────────────────────────────────
  function applyFilters(filters) {
    let result = [...allUsers];

    if (filters.nombre) {
      const q = filters.nombre.toLowerCase();
      result = result.filter((u) =>
          u.nombre?.toLowerCase().includes(q) ||
          u.apellido?.toLowerCase().includes(q)
      );
    }

    if (filters.estado) {
      result = result.filter((u) => u.estado === filters.estado);
    }

    if (filters.rol) {
      result = result.filter((u) => u.rol === filters.rol);
    }

    setUsers(result);
  }

  function clearFilters() {
    setUsers(allUsers);
  }

  // ─── REFETCH ────────────────────────────────────────────────────────────────
  const fetchUsers = useCallback(() => {
    setUsers([...allUsers]);
  }, [allUsers]);

  // ─── GET BY ID ──────────────────────────────────────────────────────────────
  async function getUserById(id) {
    try {
      setLoading(true);
      setError(null);
      return await useCases.users.getById.execute(id);
    } catch (err) {
      setError(err.message ?? 'Error al obtener el usuario');
      return null;
    } finally {
      setLoading(false);
    }
  }

  // ─── CREATE ─────────────────────────────────────────────────────────────────
async function createUser(formData) {
  try {
    setLoading(true);
    setError(null);
    await useCases.users.create.execute(formData);

    // ← recarga la lista completa desde la API
    const data = await useCases.users.getAll.execute({});
const activos = data.filter((u) => u.estado === 'ACTIVO');
setAllUsers(activos);
setUsers(activos);
    return true;
  } catch (err) {
    setError(err.message ?? 'Error al crear el usuario');
    return false;
  } finally {
    setLoading(false);
  }
}
  // ─── EDIT ───────────────────────────────────────────────────────────────────
 async function editUser(id, formData) {
  try {
    setLoading(true);
    setError(null);
    await useCases.users.edit.execute(id, formData);

    // ← recarga la lista completa
    const data    = await useCases.users.getAll.execute({});
    const activos = data.filter((u) => u.estado === 'ACTIVO');
    setAllUsers(activos);
    setUsers(activos);
    return true;
  } catch (err) {
    setError(err.message ?? 'Error al editar el usuario');
    return false;
  } finally {
    setLoading(false);
  }
}
  // ─── DELETE ─────────────────────────────────────────────────────────────────
  async function deleteUser(id) {
    try {
      setLoading(true);
      setError(null);
      await useCases.users.delete.execute(id);
      setAllUsers((prev) => prev.filter((u) => u.id !== id));
      setUsers((prev)    => prev.filter((u) => u.id !== id));
      return true;
    } catch (err) {
      setError(err.message ?? 'Error al eliminar el usuario');
      return false;
    } finally {
      setLoading(false);
    }
  }

  return {
    users,
    loading,
    error,
    fetchUsers,
    getUserById,
    createUser,
    editUser,
    deleteUser,
    applyFilters,
    clearFilters,
  };
}