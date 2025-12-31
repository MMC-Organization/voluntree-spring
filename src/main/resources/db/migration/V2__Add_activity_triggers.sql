CREATE
OR REPLACE FUNCTION vrf_activity_creator () RETURNS TRIGGER AS $$
BEGIN
  IF NOT EXISTS(SELECT 1 FROM users WHERE id = NEW.organization_id AND user_type = 'ORGANIZATION') THEN
    RAISE EXCEPTION USING MESSAGE = 'Organização associada à atividade não encontrado!', ERRCODE = 23514;
  END IF;

  RETURN NEW;
END;
$$ language plpgsql;

CREATE
OR REPLACE TRIGGER vrf_activity_creator BEFORE INSERT ON activity FOR EACH ROW
EXECUTE FUNCTION vrf_activity_creator ();