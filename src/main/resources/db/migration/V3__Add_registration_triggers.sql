CREATE
OR REPLACE FUNCTION vrf_registration_creator_and_spots () RETURNS TRIGGER AS $$
DECLARE
  total_spots SMALLINT;
  occupied_spots BIGINT;
BEGIN
  IF NOT EXISTS(SELECT 1 FROM users WHERE id = NEW.volunteer_id AND user_type = 'VOLUNTEER') THEN
    RAISE EXCEPTION USING MESSAGE = 'Usuário não pode se inscrever na atividade, pois não é voluntário!', ERRCODE = 23514;
  END IF;

  SELECT spots INTO STRICT total_spots FROM activity where id = NEW.activity_id FOR UPDATE;

  SELECT count(id) INTO occupied_spots FROM registration where activity_id = NEW.activity_id;

  IF occupied_spots >= total_spots THEN
    RAISE EXCEPTION USING MESSAGE = 'Usuário não pode se inscrever na atividade, vagas já preenchidas!', ERRCODE = 23514;
  END IF;

  RETURN NEW;
END;
$$ language plpgsql;

CREATE
OR REPLACE TRIGGER vrf_registration_creator_and_spots BEFORE INSERT ON registration FOR EACH ROW
EXECUTE FUNCTION vrf_registration_creator_and_spots ();