import { useState, useEffect, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useProveedores() {
  const [allProveedores, setAllProveedores] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadProveedores = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.proveedores.getAll.execute({});
      setAllProveedores(data);
      setProveedores(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener proveedores');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      const data = await loadProveedores();
      if (cancelled) return;
      void data;
    })();
    return () => { cancelled = true; };
  }, [loadProveedores]);

  function applyFilters(filters) {
    let result = [...allProveedores];
    if (filters.razonSocial) {
      const q = filters.razonSocial.toLowerCase();
      result = result.filter((p) =>
        p.razonSocial?.toLowerCase().includes(q) || p.ruc?.includes(q)
      );
    }
    if (filters.estado) {
      result = result.filter((p) => p.estado === filters.estado);
    }
    setProveedores(result);
  }

  function clearFilters() {
    setProveedores(allProveedores);
  }

  async function createProveedor(formData) {
    try {
      setLoading(true);
      setError(null);
      await useCases.proveedores.create.execute(formData);
      await loadProveedores();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al crear el proveedor');
      return false;
    } finally {
      setLoading(false);
    }
  }

  async function editProveedor(id, formData) {
    try {
      setLoading(true);
      setError(null);
      await useCases.proveedores.update.execute(id, formData);
      await loadProveedores();
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al actualizar el proveedor');
      return false;
    } finally {
      setLoading(false);
    }
  }

  async function deleteProveedor(id) {
    try {
      setLoading(true);
      setError(null);
      await useCases.proveedores.delete.execute(id);
      setAllProveedores((prev) => prev.filter((p) => p.id !== id));
      setProveedores((prev) => prev.filter((p) => p.id !== id));
      return true;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al eliminar el proveedor');
      return false;
    } finally {
      setLoading(false);
    }
  }

  return {
    proveedores,
    loading,
    error,
    refetch: loadProveedores,
    createProveedor,
    editProveedor,
    deleteProveedor,
    applyFilters,
    clearFilters,
  };
}
