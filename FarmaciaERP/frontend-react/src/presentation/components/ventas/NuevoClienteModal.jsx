import Modal from '@/presentation/components/ui/Modal';
import Input from '@/presentation/components/ui/Input';
import Select from '@/presentation/components/ui/Select';

// Modal de alta rápida de cliente, compartido entre Punto de Venta y Cotizaciones.
export default function NuevoClienteModal({ isOpen, onClose, nuevoCliente, setNuevoCliente, onConfirm }) {
  return (
    <Modal
      isOpen={isOpen}
      title="Registrar cliente"
      onClose={onClose}
      onConfirm={onConfirm}
      confirmText="Registrar"
    >
      <div className="flex flex-col gap-3">
        <Input label="Nombres" name="nombre" value={nuevoCliente.nombre}
               onChange={(e) => setNuevoCliente((p) => ({ ...p, nombre: e.target.value }))} />
        <Input label="Apellidos" name="apellido" value={nuevoCliente.apellido}
               onChange={(e) => setNuevoCliente((p) => ({ ...p, apellido: e.target.value }))} />
        <Input label="DNI" name="dni" value={nuevoCliente.dni}
               onChange={(e) => setNuevoCliente((p) => ({ ...p, dni: e.target.value }))} />
        <Select
          label="Tipo de seguro"
          name="tipoSeguro"
          value={nuevoCliente.tipoSeguro}
          onChange={(e) => setNuevoCliente((p) => ({ ...p, tipoSeguro: e.target.value }))}
          options={[
            { value: 'SIN_SEGURO', label: 'Sin seguro' },
            { value: 'SIS', label: 'SIS' },
            { value: 'ESSALUD', label: 'ESSALUD' },
            { value: 'OTRO', label: 'Otro' },
          ]}
        />
      </div>
    </Modal>
  );
}
