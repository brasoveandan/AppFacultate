package repository.fileRepository;

import domains.Entity;

/**
 * Clasa folosita pe post de interfata
 * Incapsuleaza metode specifice lucrului cu fisier
 *
 * @param <E>  tipului de obiect pe care il are
 * @param <ID> tipul id-ului obiectului respectiv
 */
public interface CrudFileRepository<ID, E extends Entity<ID>> {
    /**
     * Functie folosita pentru a scrie intr-un fisier tot continutul repo-ului
     */
    void saveAllToFile();

    /**
     * Functie folosita pentru a scrie un singur obiect in fisier
     *
     * @param entity obiectul care trebuie scris in fisier
     */
    void saveOneToFile(E entity);

    /**
     * Functie folosita pentru a citit tot continutul fisierului
     * care apoi va fi incarcat in repo
     */
    void loadData();
}