-- Função para retornar o ID do usuário atual da sessão
CREATE OR REPLACE FUNCTION current_user_id()
RETURNS BIGINT AS $$
BEGIN
  RETURN current_setting('app.current_user_id', true)::BIGINT;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END;
$$ LANGUAGE plpgsql STABLE SECURITY DEFINER;
