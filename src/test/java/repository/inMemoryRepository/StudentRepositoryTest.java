package repository.inMemoryRepository;

import domains.Student;
import domains.validators.StudentValidator;
import domains.validators.ValidationException;
import domains.validators.Validator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentRepositoryTest {
    Student student;
    StudentRepository studentRepository;

    @Before
    public void setUp() throws Exception {
        Validator<Student> validatorStudent = new StudentValidator();
        studentRepository = new StudentRepository(validatorStudent);
        student = new Student("Ceausescu", "Nicolae", "nicolae_ceausecu@yahoo.com", "Stalin", 221);
        student.setId("1");
        studentRepository.save(student);
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
}