package repository.inMemoryRepository;

import domains.StructuraAnUniversitar;
import domains.Tema;
import domains.validators.Validator;
import repository.RepositoryException;

import java.time.LocalDate;

public class TemaRepository extends InMemoryRepository<String, Tema> {
    public TemaRepository(Validator<Tema> validator) {
        super(validator);
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
        if (oldValue.getDeadlineWeek() < StructuraAnUniversitar.getInstance().getWeek(LocalDate.now(), StructuraAnUniversitar.getInstance().getSemestru(LocalDate.now())))
            throw new RepositoryException("Tema nu poate fi modificata, deadlineWeek < currentWeek");
        oldValue.setDescriere(entity.getDescriere());
        oldValue.setStartWeek(entity.getStartWeek());
        oldValue.setDeadlineWeek(entity.getDeadlineWeek());
        return null;
    }
}
