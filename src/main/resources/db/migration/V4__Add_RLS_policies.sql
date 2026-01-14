ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE activity ENABLE ROW LEVEL SECURITY;
ALTER TABLE registration ENABLE ROW LEVEL SECURITY;
ALTER TABLE log ENABLE ROW LEVEL SECURITY;


-- Usuario pode ver e editar seus próprios dados
CREATE POLICY users_view_own ON users FOR SELECT
  USING (id = current_user_id());

CREATE POLICY users_update_own ON users FOR UPDATE
  USING (id = current_user_id())
  WITH CHECK (id = current_user_id());

CREATE POLICY users_insert_new ON users FOR INSERT
  WITH CHECK (true);

CREATE POLICY users_no_delete ON users FOR DELETE
  USING (false);


-- ongs podem ver suas próprias atividades
CREATE POLICY activity_org_view_own ON activity FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND activity.organization_id = users.id
    )
  );

-- voluntarios podem ver todas as atividades não canceladas
CREATE POLICY activity_volunteer_view_all ON activity FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
    )
    AND activity.canceled = false
  );

-- ongs podem criar atividades
CREATE POLICY activity_org_create ON activity FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND organization_id = users.id
    )
  );

-- ongs podem atualizar suas próprias atividades
CREATE POLICY activity_org_update ON activity FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND activity.organization_id = users.id
    )
  )
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND activity.organization_id = users.id
    )
  );

-- ongs podem deletar (logicamente) suas próprias atividades
CREATE POLICY activity_org_delete ON activity FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND activity.organization_id = users.id
    )
  );


-- voluntarios podem ver suas próprias inscrições
CREATE POLICY registration_volunteer_view_own ON registration FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
      AND registration.volunteer_id = users.id
    )
  );

-- ongs podem ver as inscrições em suas atividades
CREATE POLICY registration_org_view_own_activity ON registration FOR SELECT
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'ORGANIZATION'
      AND registration.activity_id IN (
        SELECT id FROM activity 
        WHERE organization_id = users.id
      )
    )
  );


-- u

CREATE POLICY registration_volunteer_create ON registration FOR INSERT
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
      AND volunteer_id = users.id
    )
  );

CREATE POLICY registration_volunteer_update ON registration FOR UPDATE
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
      AND registration.volunteer_id = users.id
    )
  )
  WITH CHECK (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
      AND registration.volunteer_id = users.id
    )
  );


CREATE POLICY registration_volunteer_delete ON registration FOR DELETE
  USING (
    EXISTS (
      SELECT 1 FROM users 
      WHERE users.id = current_user_id() 
      AND users.user_type = 'VOLUNTEER'
      AND registration.volunteer_id = users.id
    )
  );


-- usuario pode ver seus próprios logs
CREATE POLICY log_view_own ON log FOR SELECT
  USING (user_id = current_user_id());

CREATE POLICY log_insert ON log FOR INSERT
  WITH CHECK (true);

CREATE POLICY log_no_update ON log FOR UPDATE
  USING (false);

CREATE POLICY log_no_delete ON log FOR DELETE
  USING (false);
