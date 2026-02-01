SET search_path TO develop, public;

CREATE
OR REPLACE FUNCTION vrf_activity_creator () RETURNS TRIGGER AS $$
BEGIN
  IF NOT EXISTS(SELECT 1 FROM develop.users WHERE id = NEW.organization_id AND user_type = 'ORGANIZATION') THEN
    RAISE EXCEPTION USING MESSAGE = 'Organização associada à atividade não encontrado!', ERRCODE = 23514;
  END IF;

  RETURN NEW;
END;
$$ language plpgsql;

CREATE
OR REPLACE TRIGGER vrf_activity_creator_trg BEFORE INSERT ON activity FOR EACH ROW
EXECUTE FUNCTION vrf_activity_creator ();

/* 
uma atividade só pode ser CRIADA por uma organização [x]
uma atividade não pode ser DELETADA de verdade apenas logicamente [x] - implementado na aplicação via soft delete
 */