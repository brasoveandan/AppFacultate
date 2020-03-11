package utils.events;

import domains.Entity;

public class EntityStatusEvent<ID, E extends Entity<ID>> implements Event {
    private EntityExecutionStatusEventType type;
    private E entity;

    public EntityStatusEvent(EntityExecutionStatusEventType type, E entity) {
        this.entity = entity;
        this.type = type;
    }

    public E getE() {
        return entity;
    }

    public void setE(E entity) {
        this.entity = entity;
    }

    public EntityExecutionStatusEventType getType() {
        return type;
    }

    public void setType(EntityExecutionStatusEventType type) {
        this.type = type;
    }
}