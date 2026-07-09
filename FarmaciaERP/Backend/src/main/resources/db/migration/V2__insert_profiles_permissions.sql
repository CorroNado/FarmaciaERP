-- ============================================================
-- 1. MODULO PERMISOS (Solo inserta si el código no existe)
-- ============================================================

-- Modulo: OMNICHANNEL
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'OMNICHANNEL_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('OMNICHANNEL_TOTAL', 'Acceso total a Customer Checkout: tickets, cobros y recetas', 'OMNICHANNEL', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'OMNICHANNEL_SUPERVISOR')
    INSERT INTO permission (code, description, module, status) VALUES ('OMNICHANNEL_SUPERVISOR', 'Anulaciones, devoluciones y cierre de caja de sucursal', 'OMNICHANNEL', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'OMNICHANNEL_MODIFICAR')
    INSERT INTO permission (code, description, module, status) VALUES ('OMNICHANNEL_MODIFICAR', 'Autorizacion de dispensacion con receta y gestion click & collect', 'OMNICHANNEL', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'OMNICHANNEL_CATALOGO')
    INSERT INTO permission (code, description, module, status) VALUES ('OMNICHANNEL_CATALOGO', 'Carga de banners, catalogo digital, precios web y pasarelas de pago', 'OMNICHANNEL', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'OMNICHANNEL_VISUALIZAR')
    INSERT INTO permission (code, description, module, status) VALUES ('OMNICHANNEL_VISUALIZAR', 'Auditoria de ventas fisicas y digitales para conciliacion', 'OMNICHANNEL', 'ACTIVO');

-- Modulo: MM
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'MM_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('MM_TOTAL', 'Contratos marco, OC masivas y homologacion de proveedores', 'MM', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'MM_MODIFICAR')
    INSERT INTO permission (code, description, module, status) VALUES ('MM_MODIFICAR', 'Validacion de inventarios ciclicos y reabastecimiento de emergencia', 'MM', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'MM_CREAR')
    INSERT INTO permission (code, description, module, status) VALUES ('MM_CREAR', 'Registro de entrada de mercaderia y reporte de mermas locales', 'MM', 'ACTIVO');

-- Modulo: QM
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'QM_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('QM_TOTAL', 'Liberacion de lotes, farmacovigilancia y control de psicotropicos', 'QM', 'ACTIVO');

-- Modulo: CRM
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'CRM_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('CRM_TOTAL', 'Fidelidad, cupones de descuento y convenios corporativos automaticos', 'CRM', 'ACTIVO');

-- Modulo: FI_CO
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'FI_CO_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('FI_CO_TOTAL', 'Contabilidad, impuestos, cuentas por pagar/cobrar y cierre mensual', 'FI_CO', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'FI_CO_VISUALIZAR')
    INSERT INTO permission (code, description, module, status) VALUES ('FI_CO_VISUALIZAR', 'Consulta de estados de cuenta de proveedores y lineas de credito', 'FI_CO', 'ACTIVO');

-- Modulo: HCM
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'HCM_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('HCM_TOTAL', 'Nomina general, contratos laborales y capacitaciones de ley', 'HCM', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'HCM_MODIFICAR')
    INSERT INTO permission (code, description, module, status) VALUES ('HCM_MODIFICAR', 'Control de asistencia, incidencias y turnos rotativos de sucursal', 'HCM', 'ACTIVO');

-- Modulo: PM_PS
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'PM_PS_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('PM_PS_TOTAL', 'Plan de mantenimiento, presupuesto de obras y ordenes correctivas', 'PM_PS', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'PM_PS_VISUALIZAR')
    INSERT INTO permission (code, description, module, status) VALUES ('PM_PS_VISUALIZAR', 'Monitoreo del tablero de control de temperaturas cadena de frio', 'PM_PS', 'ACTIVO');

-- Modulo: SAC
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'SAC_TOTAL')
    INSERT INTO permission (code, description, module, status) VALUES ('SAC_TOTAL', 'Tableros analiticos, rentabilidad, metas y proyecciones comerciales', 'SAC', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM permission WHERE code = 'SAC_VISUALIZAR')
    INSERT INTO permission (code, description, module, status) VALUES ('SAC_VISUALIZAR', 'Acceso transversal de solo lectura en cualquier modulo del sistema', 'SAC', 'ACTIVO');


-- ============================================================
-- 2. MODULO PERFILES (Solo inserta si el nombre no existe)
-- ============================================================

IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'ADMINISTRADOR')
    INSERT INTO profile (name, description, status) VALUES ('ADMINISTRADOR', 'Gerente y Administrador de Tienda', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'JEFE_GENERAL')
    INSERT INTO profile (name, description, status) VALUES ('JEFE_GENERAL', 'Alta Gerencia - Directorio y Gerencia General', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'JEFE_LOGISTICA')
    INSERT INTO profile (name, description, status) VALUES ('JEFE_LOGISTICA', 'Comprador y Analista de Logistica Central', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'QUIMICO_FARMACEUTICO')
    INSERT INTO profile (name, description, status) VALUES ('QUIMICO_FARMACEUTICO', 'Director Técnico - Quimico Farmaceutico', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'TECNICO_FARMACIA')
    INSERT INTO profile (name, description, status) VALUES ('TECNICO_FARMACIA', 'Cajero y Técnico en Farmacia', 'ACTIVO');
IF NOT EXISTS (SELECT 1 FROM profile WHERE name = 'CONTADOR')
    INSERT INTO profile (name, description, status) VALUES ('CONTADOR', 'Contador y Analista Financiero', 'ACTIVO');


-- ============================================================
-- 3. RELACION PERFIL - PERMISOS (Evita duplicar llaves compuestas)
-- ============================================================

-- TECNICO_FARMACIA
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'TECNICO_FARMACIA' AND pe.code IN ('OMNICHANNEL_TOTAL', 'OMNICHANNEL_MODIFICAR', 'MM_CREAR')
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);

-- QUIMICO_FARMACEUTICO
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'QUIMICO_FARMACEUTICO' AND pe.code IN ('QM_TOTAL', 'OMNICHANNEL_MODIFICAR', 'PM_PS_VISUALIZAR')
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);

-- ADMINISTRADOR
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'ADMINISTRADOR' AND pe.code IN ('OMNICHANNEL_SUPERVISOR', 'HCM_MODIFICAR', 'MM_MODIFICAR')
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);

-- JEFE_LOGISTICA
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'JEFE_LOGISTICA' AND pe.code IN ('MM_TOTAL', 'FI_CO_VISUALIZAR')
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);

-- CONTADOR
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'CONTADOR' AND pe.code IN ('FI_CO_TOTAL', 'OMNICHANNEL_VISUALIZAR')
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);

-- JEFE_GENERAL
INSERT INTO perfil_permisos (profile_id, permission_id)
SELECT p.profile_id, pe.permission_id FROM profile p, permission pe
WHERE p.name = 'JEFE_GENERAL' AND pe.code IN (
                                              'SAC_TOTAL', 'SAC_VISUALIZAR', 'OMNICHANNEL_VISUALIZAR',
                                              'MM_TOTAL', 'QM_TOTAL', 'CRM_TOTAL',
                                              'FI_CO_VISUALIZAR', 'HCM_TOTAL', 'PM_PS_VISUALIZAR'
    )
  AND NOT EXISTS (SELECT 1 FROM perfil_permisos pp WHERE pp.profile_id = p.profile_id AND pp.permission_id = pe.permission_id);