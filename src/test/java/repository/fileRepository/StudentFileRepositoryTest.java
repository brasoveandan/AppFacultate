package repository.fileRepository;

import domains.Student;
import domains.validators.StudentValidator;
import domains.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentFileRepositoryTest {
    StudentFileRepository studentRepository;
    Student student;

    @BeforeEach
    void setUp() {
        studentRepository = new StudentFileRepository(new StudentValidator(), "data/test/test.txt");
        student = new Student("Ceausescu", "Nicolae", "nicolae_ceausecu@yahoo.com", "Stalin", 221);
        student.setId("1");
        studentRepository.save(student);
    }

    @Test
    void createEntity() {
        String[] fields = new String[7];
        fields[0] = "1";
        fields[1] = "Ceausescu";
        fields[2] = "Nicolae";
        fields[3] = "nicolae_ceausecu@yahoo.com";
        fields[4] = "Stalin";
        fields[5] = "221";
        fields[6] = "0";
        assertEquals(student, studentRepository.createEntity(fields));
    }

    @Test
    public void findOneSuccessfull() {
        assertEquals("Ceausescu", studentRepository.findOne("1").getNume());
    }

    @Test
    public void findOneFailed() {
        assertEquals(null, studentRepository.findOne("1924"));
        assertEquals("Ceausescu", studentRepository.findOne("1").getNume());
    }

    @Test
    public void findOneThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.findOne(null);
        });
    }

    @Test
    public void findAll() {
        int size = 0;
        for (Student s : studentRepository.findAll()) {
            size++;
        }
        assertEquals(1, size);
    }

    @Test
    public void saveSuccessfull() {
        Student student2 = new Student("Vladimir", "Putin", "vladimir@yahoo.com", "John", 225);
        student2.setId("2");
        assertEquals(null, studentRepository.save(student2));
    }

    @Test
    public void saveFailed() {
        assertEquals(student, studentRepository.save(student));
    }

    @Test
    public void saveThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Student student = new Student("", "", "", "", -1);
            student.setId(null);
            studentRepository.save(student);
        });
    }

    @Test
    public void saveThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.save(null);
        });
    }

    @Test
    public void deleteSuccessfull() {
        assertEquals("Ceausescu", studentRepository.delete("1").getNume());
    }

    @Test
    public void deleteFailed() {
        assertEquals(null, studentRepository.delete("10"));
    }

    @Test
    public void deleteThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.delete(null);
        });
    }

    @Test
    public void updateSuccessfull() {
        student = new Student("Ceausescu", "Elena", "elena_ceausecu@yahoo.com", "Hitler", 221);
        student.setId("1");
        assertEquals(null, studentRepository.update(student));
    }

    @Test
    public void updateFailed() {
        assertEquals(student, studentRepository.update(student));
    }

    @Test
    public void updateThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.update(null);
        });
    }

    @Test
    public void updateThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Student student = new Student("", "", "", "", -1);
            student.setId("0");
            studentRepository.update(student);
        });
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("data/test/test.txt");
        writer.print("");
        writer.close();
    }
}