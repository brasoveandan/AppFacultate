package utils.events;

import domains.Entity;

public class EntityChangeEvent<ID, E extends Entity<ID>> implements Event {
    private ChangeEventType type;
    private E data, oldData;

    public EntityChangeEvent(ChangeEventType type, E data) {
        this.type = type;
        this.data = data;
    }
    public EntityChangeEvent(ChangeEventType type, E data, E oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }
}

