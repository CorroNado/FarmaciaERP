export class Proveedor {
  constructor({ id, razonSocial, ruc, contactoEmail, contactoTelefono, estado }) {
    this.id = id;
    this.razonSocial = razonSocial ?? '';
    this.ruc = ruc ?? '';
    this.contactoEmail = contactoEmail ?? '';
    this.contactoTelefono = contactoTelefono ?? '';
    this.estado = estado ?? 'ACTIVO';
  }

  get estaActivo() {
    return this.estado === 'ACTIVO';
  }

  static fromApi(raw) {
    return new Proveedor({
      id: raw.id,
      razonSocial: raw.razonSocial,
      ruc: raw.ruc,
      contactoEmail: raw.contactoEmail,
      contactoTelefono: raw.contactoTelefono,
      estado: raw.estado,
    });
  }

  // CrearProveedorRequest
  static toApiCreate(formData) {
    return {
      razonSocial: formData.razonSocial,
      ruc: formData.ruc,
      contactoEmail: formData.contactoEmail,
      contactoTelefono: formData.contactoTelefono,
    };
  }

  // ActualizarProveedorRequest (no incluye ruc: es inmutable tras el alta)
  static toApiUpdate(formData) {
    return {
      razonSocial: formData.razonSocial,
      contactoEmail: formData.contactoEmail,
      contactoTelefono: formData.contactoTelefono,
    };
  }
}
