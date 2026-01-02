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
OR REPLACE TRIGGER vrf_activity_creator_trg BEFORE INSERT ON activity FOR EACH ROW
EXECUTE FUNCTION vrf_activity_creator ();

CREATE
OR REPLACE FUNCTION activity_logical_delete () RETURNS TRIGGER AS $$
BEGIN
  IF NOT OLD.canceled THEN
    UPDATE activity SET canceled = TRUE WHERE id = OLD.id;
  END IF;

  RETURN NULL;
END;
$$ language plpgsql;

CREATE
OR REPLACE TRIGGER activity_logical_delete_trg BEFORE DELETE ON activity FOR EACH ROW
EXECUTE FUNCTION activity_logical_delete ();

/* 
uma atividade só pode ser CRIADA por uma organização [x]
uma atividade não pode ser DELETADA de verdade apenas logicamente [x]
 */