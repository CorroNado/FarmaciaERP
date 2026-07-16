import { useState, useCallback, useEffect } from 'react';
import { useCases } from '@/infrastructure';

export function useFacturaMiro() {
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [registrando, setRegistrando] = useState(false);
  const [error, setError] = useState(null);

  const loadFacturas = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await useCases.facturaMiro.getAll.execute({});
      setFacturas(data);
      return data;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al obtener las facturas (MIRO)');
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadFacturas();
  }, [loadFacturas]);

  // formData: { ordenCompraId, numeroFactura, fechaEmision }
  async function registrarMIRO(formData) {
    try {
      setRegistrando(true);
      setError(null);
      const factura = await useCases.facturaMiro.registrar.execute(formData);
      await loadFacturas();
      return factura;
    } catch (err) {
      setError(err.response?.data ?? err.message ?? 'Error al registrar la factura en SAP (MIRO)');
      return null;
    } finally {
      setRegistrando(false);
    }
  }

  return {
    facturas,
    loading,
    registrando,
    error,
    refetch: loadFacturas,
    registrarMIRO,
  };
}
