import { useState, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useVentas(initialFilters = {}) {
  const [ventas,   setVentas]   = useState([]);
  const [loading,  setLoading]  = useState(false);
  const [error,    setError]    = useState(null);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data = await useCases.ventas.getAll.execute(initialFilters);
        if (!cancelled) setVentas(data);
      } catch (err) {
        if (!cancelled) setError(err.message ?? 'Error al cargar ventas');
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
    load();
    return () => { cancelled = true; };
  }, []);

  async function crearVenta(formData) {
    try {
      setLoading(true);
      setError(null);
      const nueva = await useCases.ventas.crear.execute(formData);
      setVentas((prev) => [nueva, ...prev]);
      return nueva;
    } catch (err) {
      setError(err.message ?? 'Error al crear venta');
      return null;
    } finally {
      setLoading(false);
    }
  }

  async function pagarVenta(id) {
    try {
      setLoading(true);
      setError(null);
      const updated = await useCases.ventas.pagar.execute(id);
      setVentas((prev) => prev.map((v) => (v.id === id ? updated : v)));
      return true;
    } catch (err) {
      setError(err.message ?? 'Error al pagar venta');
      return false;
    } finally {
      setLoading(false);
    }
  }

  async function anularVenta(id) {
    try {
      setLoading(true);
      setError(null);
      const updated = await useCases.ventas.anular.execute(id);
      setVentas((prev) => prev.map((v) => (v.id === id ? updated : v)));
      return true;
    } catch (err) {
      setError(err.message ?? 'Error al anular venta');
      return false;
    } finally {
      setLoading(false);
    }
  }

  return { ventas, loading, error, crearVenta, pagarVenta, anularVenta };
}