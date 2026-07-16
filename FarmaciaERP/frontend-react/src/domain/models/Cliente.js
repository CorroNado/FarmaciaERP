export class Cliente {
  constructor({ id, nombres, dni, tipoSeguro }) {
    this.id = id;
    this.nombre = nombres?.Nombres ?? nombres?.nombres ?? '';
    this.apellido = nombres?.Apellidos ?? nombres?.apellidos ?? '';
    this.dni = dni?.dni ?? dni ?? '';
    this.tipoSeguro = tipoSeguro ?? 'SIN_SEGURO';
  }

  get nombreCompleto() {
    return `${this.nombre} ${this.apellido}`.trim();
  }

  static fromApi(raw) {
    return new Cliente({
      id: raw.id,
      nombres: raw.nombres,
      dni: raw.dni,
      tipoSeguro: raw.tipoSeguro,
    });
  }

  static toApi(formData) {
    return {
      nombre: formData.nombre,
      apellido: formData.apellido,
      dni: formData.dni,
      tipoSeguro: formData.tipoSeguro ?? 'SIN_SEGURO',
    };
  }
}
