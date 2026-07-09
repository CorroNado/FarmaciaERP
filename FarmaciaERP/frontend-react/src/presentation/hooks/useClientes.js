import { useEffect, useState } from 'react';
import { clienteService } from '@/infrastructure/services/clienteService';

export function useClientes() {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    cargarClientes();
  }, []);

  async function cargarClientes() {
    try {
      setLoading(true);
      setError(null);

      const data = await clienteService.getAll();

      setClientes(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return {
    clientes,
    loading,
    error,
    recargar: cargarClientes
  };
}