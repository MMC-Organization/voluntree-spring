CREATE TABLE
  users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(150) NOT NULL,
    cep VARCHAR(8) NOT NULL CHECK (cep ~ '^[0-9]{8}$'),
    number VARCHAR(10),
    phone_number VARCHAR(25) NOT NULL,
    user_type VARCHAR(15) NOT NULL,
    -- Organization
    cnpj VARCHAR(14) NOT NULL CHECK (cnpj ~ '^[0-9]{14}$'),
    company_name VARCHAR(255),
    cause TEXT,
    -- Volunteer
    cpf VARCHAR(11) NOT NULL CHECK (cpf ~ '^[0-9]{11}$'),
    -- Constraints
    CONSTRAINT unq_users_email UNIQUE (email),
    CONSTRAINT unq_users_phone_number UNIQUE (phone_number),
    CONSTRAINT unq_users_cpf UNIQUE (cpf),
    CONSTRAINT unq_users_cnpj UNIQUE (cnpj),
    CONSTRAINT chk_users_type CHECK (user_type IN ('VOLUNTEER', 'ORGANIZATION')),
    CONSTRAINT chk_users_type_identity CHECK (
      (
        user_type = 'VOLUNTEER'
        AND cpf IS NOT NULL
        AND cnpj IS NULL
      )
      OR (
        user_type = 'ORGANIZATION'
        AND cnpj IS NOT NULL
        AND cpf IS NULL
      )
    )
  );

-- criar trigger para apenas organization poder criar atividade
CREATE TABLE
  activity (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    spots SMALLINT,
    cep VARCHAR(8) NOT NULL CHECK (cep ~ '^[0-9]{8}$'),
    number VARCHAR(10),
    activity_date TIMESTAMP NOT NULL,
    organization_id BIGINT NOT NULL,
    canceled BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_activity_organization_id FOREIGN KEY (organization_id) REFERENCES users (id),
    CONSTRAINT unq_activity_name_org_date UNIQUE (organization_id, activity_date, name, cep, number),
    CONSTRAINT chk_spots CHECK (
      (spots IS NULL)
      OR (spots > 0)
    )
  );

-- apenas volunteer pode se inscrever em atividade
CREATE TABLE
  registration (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    volunteer_id BIGINT NOT NULL,
    activity_id BIGINT NOT NULL,
    canceled BOOLEAN NOT NULL DEFAULT FALSE,
    registered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT fk_registration_activity_id FOREIGN KEY (activity_id) REFERENCES activity (id),
    CONSTRAINT fk_registration_volunteer_id FOREIGN KEY (volunteer_id) REFERENCES users (id),
    CONSTRAINT unq_registration_vol_id_act_id UNIQUE (volunteer_id, activity_id)
  );

CREATE TABLE
  log(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    message TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    affected_resource_id BIGINT,
    user_type VARCHAR(30) NOT NULL,
    action_type VARCHAR(30) NOT NULL,
    outcome VARCHAR(20) NOT NULL,
    module VARCHAR(20) NOT NULL,
    CONSTRAINT chk_log_users_type CHECK (user_type in ('VOLUNTEER', 'ORGANIZATION')),
    CONSTRAINT chk_log_action_type CHECK (
      action_type in (
        'CREATE',
        'UPDATE',
        'DELETE',
        'READ',
        'SIGNIN',
        'SIGNOUT',
        'ERROR'
      )
    ),
    CONSTRAINT chk_log_outcome CHECK (outcome in ('SUCCESS', 'FAIL', 'ATTEMPT')),
    CONSTRAINT chk_log_module CHECK (
      module in ('AUTH', 'PROFILE', 'ACTIVITY', 'REGISTRATION')
    )
  );

CREATE INDEX idx_registration_activity ON registration (activity_id);