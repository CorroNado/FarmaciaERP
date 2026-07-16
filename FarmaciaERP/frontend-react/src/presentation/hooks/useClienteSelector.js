import { useState } from 'react';
import { useCases } from '@/infrastructure';

// Encapsula la búsqueda/alta rápida de cliente (SD.01.01), reutilizable
// entre Punto de Venta y Cotizaciones.
export function useClienteSelector() {
  const [cliente, setCliente] = useState(null);
  const [error, setError] = useState(null);

  async function buscarClientePorDni(dni) {
    setError(null);
    try {
      const encontrado = await useCases.clientes.buscarPorDni.execute(dni);
      if (!encontrado) {
        setError('No se encontró ningún cliente con ese DNI');
        return null;
      }
      setCliente(encontrado);
      return encontrado;
    } catch (err) {
      setError(err.message ?? 'Error al buscar el cliente');
      return null;
    }
  }

  async function registrarClienteRapido(formData) {
    setError(null);
    try {
      const nuevo = await useCases.clientes.crearRapido.execute(formData);
      setCliente(nuevo);
      return nuevo;
    } catch (err) {
      setError(err.message ?? 'Error al registrar el cliente');
      return null;
    }
  }

  function limpiarCliente() {
    setCliente(null);
  }

  return {
    cliente,
    buscarClientePorDni,
    registrarClienteRapido,
    limpiarCliente,
    errorCliente: error,
    setErrorCliente: setError,
  };
}
