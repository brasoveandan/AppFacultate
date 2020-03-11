package repository.fileRepository;

import com.sun.tools.javac.util.Pair;
import domains.Nota;
import domains.validators.NotaValidator;
import domains.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotaFileRepositoryTest {
    NotaFileRepository notaRepository;
    Nota nota;

    @BeforeEach
    void setUp() {
        notaRepository = new NotaFileRepository(new NotaValidator(), "data/test/test.txt");
        nota = new Nota(LocalDate.of(2019, 11, 14), "Vasile", 10, feedback);
        nota.setId(Pair.of("1", "1"));
        notaRepository.save(nota);
    }

    @Test
    void createEntity() {
        String[] fields = new String[5];
        fields[0] = "1";
        fields[1] = "1";
        fields[2] = "14/11/2019";
        fields[3] = "Vasile";
        fields[4] = "10";
        Nota nota1 = notaRepository.createEntity(fields);
        assertEquals(nota, notaRepository.createEntity(fields));
    }

    @Test
    public void findOneSuccessfull() {
        assertEquals(nota, notaRepository.findOne(Pair.of("1", "1")));
    }

    @Test
    public void findOneFailed() {
        assertEquals(null, notaRepository.findOne(Pair.of("2", "2")));
    }

    @Test
    public void findOneThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            notaRepository.findOne(null);
        });
    }

    @Test
    public void findAll() {
        int size = 0;
        for (Nota s : notaRepository.findAll()) {
            size++;
        }
        assertEquals(1, size);
    }

    @Test
    public void saveSuccessfull() {
        Nota nota2 = new Nota(LocalDate.of(2019, 11, 14), "Horia", 9, feedback);
        nota2.setId(Pair.of("2", "2"));
        assertEquals(null, notaRepository.save(nota2));
    }

    @Test
    public void saveFailed() {
        assertEquals(nota, notaRepository.save(nota));
    }

    @Test
    public void saveThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Nota nota = new Nota(LocalDate.of(2019, 11, 14), "", -1, feedback);
            nota.setId(Pair.of("", ""));
            notaRepository.save(nota);
        });
    }

    @Test
    public void saveThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            notaRepository.save(null);
        });
    }

    @Test
    public void deleteSuccessfull() {
        assertEquals(nota, notaRepository.delete(Pair.of("1", "1")));
    }

    @Test
    public void deleteFailed() {
        assertEquals(null, notaRepository.delete(Pair.of("2", "2")));
    }

    @Test
    public void deleteThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            notaRepository.delete(null);
        });
    }

    @Test
    public void updateSuccessfull() {
        Nota nota1 = new Nota(LocalDate.of(2019, 11, 14), "Vasile", 5, feedback);
        nota1.setId(Pair.of("1", "1"));
        assertEquals(null, notaRepository.update(nota1));
    }

    @Test
    public void updateFailed() {
        assertEquals(nota, notaRepository.update(nota));
    }

    @Test
    public void updateThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            notaRepository.update(null);
        });
    }

    @Test
    public void updateThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Nota nota = new Nota(LocalDate.of(2019, 11, 11), "", -1, feedback);
            nota.setId(Pair.of("0", "0"));
            notaRepository.update(nota);
        });
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("data/test/test.txt");
        writer.print("");
        writer.close();
    }
}