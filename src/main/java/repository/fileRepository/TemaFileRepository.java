package repository.fileRepository;

import domains.Tema;
import domains.validators.Validator;

public class TemaFileRepository extends FileRepository<Tema, String> {
    public TemaFileRepository(Validator<Tema> validator, String nameFile) {
        super(validator, nameFile);
    }

    @Override
    public Tema createEntity(String[] fields) {
        Tema tema = new Tema(fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]));
        tema.setId(fields[0]);
        return tema;
    }

    @Override
    public Tema update(Tema entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity " + Tema.class.getName() + " NULL!");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Tema oldValue = entities.get(entity.getId());
        oldValue.setDescriere(entity.getDescriere());
        oldValue.setStartWeek(entity.getStartWeek());
        oldValue.setDeadlineWeek(entity.getDeadlineWeek());
        saveAllToFile();
        return null;
    }
}
