package repository.inMemoryRepository;

import domains.Entity;
import domains.validators.ValidationException;
import domains.validators.Validator;

import java.util.HashMap;


public abstract class InMemoryRepository<ID, E extends Entity<ID>> implements CrudRepository<ID, E> {
    protected HashMap<ID, E> entities;
    protected Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.entities = new HashMap<ID, E>();
        this.validator = validator;
    }

    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entity is null!");
        E oldValue = entities.get(entity.getId());
        if (oldValue == null) {
            validator.validate(entity);
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        if (entities.containsKey(id))
            return entities.remove(id);
        return null;
    }

    @Override
    public abstract E update(E entity);
}
