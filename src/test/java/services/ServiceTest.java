package services;

import com.sun.tools.javac.util.Pair;
import domains.Nota;
import domains.StructuraAnUniversitar;
import domains.Student;
import domains.Tema;
import domains.validators.NotaValidator;
import domains.validators.StudentValidator;
import domains.validators.TemaValidator;
import domains.validators.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.inMemoryRepository.CrudRepository;
import repository.inMemoryRepository.NotaRepository;
import repository.inMemoryRepository.StudentRepository;
import repository.inMemoryRepository.TemaRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServiceTest {
    Service service;

    @BeforeEach
    void setUp() {
        StructuraAnUniversitar anUniversitar = StructuraAnUniversitar.getInstance("data/test/anUniversitar.txt");
        Service.setCurrentWeek(anUniversitar.getWeek(LocalDate.of(2019, 11, 15),
                anUniversitar.getSemestru(LocalDate.of(2019, 11, 15))));
        Validator<Student> studentValidator = new StudentValidator();
        CrudRepository<String, Student> studentCrudRepository = new StudentRepository(studentValidator);
        Validator<Tema> temaValidator = new TemaValidator();
        CrudRepository<String, Tema> temaCrudRepository = new TemaRepository(temaValidator);
        Validator<Nota> notaValidator = new NotaValidator();
        CrudRepository<Pair<String, String>, Nota> notaCrudRepository = new NotaRepository(notaValidator);
        service = new Service(studentCrudRepository, temaCrudRepository, notaCrudRepository);
        Service.setCurrentWeek(5);
    }

    @Test
    void saveNotaThrowServiceExceptionIdInvalid() {
        assertThrows(ServiceException.class, () -> {
            service.saveNota("", "0", LocalDate.of(2019, 11, 11), "Horia", 10, "OK", "NU");
        });
    }

    @Test
    void filtrareStudentGrupaSuccessfull() {
        service.saveStudent("1", "Marina", "Gheorghe", "mariana@yahoo.com", "Vasile", 221);
        service.saveStudent("2", "Daniel", "Gheorghe", "daniel@yahoo.com", "Vasile", 221);
        assertEquals(2, service.filtrareStudentGrupa(221).size());
    }

    @Test
    void filtrareNotaStudentTemaSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", "mariana@yahoo.com", "Vasile", 221);
        service.saveStudent("2", "Daniel", "Gheorghe", "daniel@yahoo.com", "Vasile", 221);
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareNotaStudentTema("1").size());
    }

    @Test
    void filtrareNotaProfesorSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", "mariana@yahoo.com", "Vasile", 221);
        service.saveStudent("2", "Daniel", "Gheorghe", "daniel@yahoo.com", "Vasile", 221);
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareNotaProfesor("1", "Vasile").size());
    }

    @Test
    void filtrareNotaSaptamnaDataSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", "mariana@yahoo.com", "Vasile", 221);
        service.saveStudent("2", "Daniel", "Gheorghe", "daniel@yahoo.com", "Vasile", 221);
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareNotaSaptamnaData("1", 7).size());
    }
}