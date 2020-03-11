package repository.xmlFileRepository;

import domains.Entity;

public interface CrudXMLFileRepository<ID, E extends Entity<ID>> {
    void loadData();

    void saveAllToFile();
}
