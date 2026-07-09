CREATE TABLE address
(
    address_id  bigint IDENTITY (1, 1) NOT NULL,
    owner_id    bigint       NOT NULL,
    owner_type  varchar(255) NOT NULL,
    district_id bigint       NOT NULL,
    description varchar(255) NOT NULL,
    label       varchar(255) NOT NULL,
    status      varchar(255) NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (address_id)
)
    GO

CREATE TABLE app_user
(
    user_id        bigint IDENTITY (1, 1) NOT NULL,
    profile_id     bigint       NOT NULL,
    created_at     datetime,
    username       varchar(255) NOT NULL,
    password       varchar(255) NOT NULL,
    name           varchar(100),
    last_name      varchar(100),
    full_name      varchar(255),
    login_attempts int          NOT NULL,
    lock_until     datetime,
    estado         varchar(255) NOT NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (user_id)
)
    GO

CREATE TABLE department
(
    departamento_id bigint       NOT NULL,
    nombre          varchar(255) NOT NULL,
    ubigeo_reniec   varchar(255) NOT NULL,
    ubigeo_inei     varchar(255) NOT NULL,
    ubigeo_type     varchar(255) NOT NULL,
    CONSTRAINT pk_department PRIMARY KEY (departamento_id)
)
    GO

CREATE TABLE district
(
    distrito_id   bigint       NOT NULL,
    nombre        varchar(255) NOT NULL,
    province_id   bigint       NOT NULL,
    ubigeo_reniec varchar(255) NOT NULL,
    ubigeo_inei   varchar(255) NOT NULL,
    ubigeo_type   varchar(255) NOT NULL,
    CONSTRAINT pk_district PRIMARY KEY (distrito_id)
)
    GO

CREATE TABLE email_contact
(
    id            bigint IDENTITY (1, 1) NOT NULL,
    owner_id      bigint       NOT NULL,
    owner_type    varchar(255) NOT NULL,
    etiqueta      varchar(255) NOT NULL,
    estado        varchar(255) NOT NULL,
    created_at    datetime     NOT NULL,
    email_address varchar(255) NOT NULL,
    CONSTRAINT pk_email_contact PRIMARY KEY (id)
)
    GO

CREATE TABLE login_history
(
    login_historial_id bigint IDENTITY (1, 1) NOT NULL,
    user_id            bigint       NOT NULL,
    user_agent         varchar(255) NOT NULL,
    accion             varchar(255),
    ip                 varchar(45)  NOT NULL,
    fecha              datetime     NOT NULL,
    CONSTRAINT pk_login_history PRIMARY KEY (login_historial_id)
)
    GO

CREATE TABLE perfil_permisos
(
    permission_id bigint NOT NULL,
    profile_id    bigint NOT NULL
)
    GO

CREATE TABLE permission
(
    permission_id bigint IDENTITY (1, 1) NOT NULL,
    code          varchar(255) NOT NULL,
    description   varchar(255) NOT NULL,
    module        varchar(255) NOT NULL,
    status        varchar(255) NOT NULL,
    CONSTRAINT pk_permission PRIMARY KEY (permission_id)
)
    GO

CREATE TABLE profile
(
    profile_id  bigint IDENTITY (1, 1) NOT NULL,
    name        varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    status      varchar(255) NOT NULL,
    CONSTRAINT pk_profile PRIMARY KEY (profile_id)
)
    GO

CREATE TABLE province
(
    provincia_id    bigint       NOT NULL,
    nombre          varchar(255) NOT NULL,
    departamento_id bigint       NOT NULL,
    ubigeo_reniec   varchar(255) NOT NULL,
    ubigeo_inei     varchar(255) NOT NULL,
    ubigeo_type     varchar(255) NOT NULL,
    CONSTRAINT pk_province PRIMARY KEY (provincia_id)
)
    GO

CREATE TABLE user_telephones
(
    user_id     bigint       NOT NULL,
    prefix      varchar(5)   NOT NULL,
    area_code   varchar(5),
    number      varchar(15)  NOT NULL,
    description varchar(100),
    tipo        varchar(255) NOT NULL,
    full_number varchar(255)
)
    GO

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_username UNIQUE (username)
    GO

ALTER TABLE email_contact
    ADD CONSTRAINT uc_email_contact_email_address UNIQUE (email_address)
    GO

ALTER TABLE permission
    ADD CONSTRAINT uc_permission_code UNIQUE (code)
    GO

ALTER TABLE profile
    ADD CONSTRAINT uc_profile_name UNIQUE (name)
    GO

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_DISTRICT FOREIGN KEY (district_id) REFERENCES district (distrito_id)
    GO

-- ALTER TABLE address
--     ADD CONSTRAINT FK_ADDRESS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES app_user (user_id)
--     GO

ALTER TABLE app_user
    ADD CONSTRAINT FK_APP_USER_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profile (profile_id)
    GO

ALTER TABLE district
    ADD CONSTRAINT FK_DISTRICT_ON_PROVINCE FOREIGN KEY (province_id) REFERENCES province (provincia_id)
    GO

-- ALTER TABLE email_contact
--     ADD CONSTRAINT FK_EMAIL_CONTACT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES app_user (user_id)
--     GO

ALTER TABLE login_history
    ADD CONSTRAINT FK_LOGIN_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (user_id)
    GO

ALTER TABLE province
    ADD CONSTRAINT FK_PROVINCE_ON_DEPARTAMENTO FOREIGN KEY (departamento_id) REFERENCES department (departamento_id)
    GO

ALTER TABLE perfil_permisos
    ADD CONSTRAINT fk_perper_on_permission_j_p_a FOREIGN KEY (permission_id) REFERENCES permission (permission_id)
    GO

ALTER TABLE perfil_permisos
    ADD CONSTRAINT fk_perper_on_profile_j_p_a FOREIGN KEY (profile_id) REFERENCES profile (profile_id)
    GO

ALTER TABLE user_telephones
    ADD CONSTRAINT fk_user_telephones_on_user_j_p_a FOREIGN KEY (user_id) REFERENCES app_user (user_id)
    GO