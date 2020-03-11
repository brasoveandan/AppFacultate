package repository.fileRepository;

import domains.Tema;
import domains.validators.TemaValidator;
import domains.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemaFileRepositoryTest {
    TemaFileRepository temaRepository;
    Tema tema;

    @BeforeEach
    void setUp() {
        temaRepository = new TemaFileRepository(new TemaValidator(), "data/test/test.txt");
        tema = new Tema("MAP", 2, 10);
        tema.setId("1");
        temaRepository.save(tema);
    }

    @Test
    void createEntity() {
        String[] fields = new String[4];
        fields[0] = "1";
        fields[1] = "MAP";
        fields[2] = "2";
        fields[3] = "10";
        assertEquals(tema, temaRepository.createEntity(fields));
    }

    @Test
    public void findOneSuccessfull() {
        assertEquals(tema, temaRepository.findOne("1"));
    }

    @Test
    public void findOneFailed() {
        assertEquals(null, temaRepository.findOne("1924"));
    }

    @Test
    public void findOneThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            temaRepository.findOne(null);
        });
    }

    @Test
    public void findAll() {
        int size = 0;
        for (Tema s : temaRepository.findAll()) {
            size++;
        }
        assertEquals(1, size);
    }

    @Test
    public void saveSuccessfull() {
        Tema tema2 = new Tema("MAP", 5, 7);
        tema2.setId("2");
        assertEquals(null, temaRepository.save(tema2));
    }

    @Test
    public void saveFailed() {
        assertEquals(tema, temaRepository.save(tema));
    }

    @Test
    public void saveThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Tema tema = new Tema("", -3, -1);
            tema.setId(null);
            temaRepository.save(tema);
        });
    }

    @Test
    public void saveThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            temaRepository.save(null);
        });
    }

    @Test
    public void deleteSuccessfull() {
        assertEquals(tema, temaRepository.delete("1"));
    }

    @Test
    public void deleteFailed() {
        assertEquals(null, temaRepository.delete("10"));
    }

    @Test
    public void deleteThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            temaRepository.delete(null);
        });
    }

    @Test
    public void updateSuccessfull() {
        Tema tema1 = new Tema("PLF", 2, 14);
        tema1.setId("1");
        assertEquals(null, temaRepository.update(tema1));
    }

    @Test
    public void updateFailed() {
        assertEquals(tema, temaRepository.update(tema));
    }

    @Test
    public void updateThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            temaRepository.update(null);
        });
    }

    @Test
    public void updateThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Tema tema = new Tema("", 0, 0);
            tema.setId("0");
            temaRepository.update(tema);
        });
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("data/test/test.txt");
        writer.print("");
        writer.close();
    }
}