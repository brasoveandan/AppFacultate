import com.sun.tools.javac.util.Pair;
import domains.Nota;
import domains.StructuraAnUniversitar;
import domains.Student;
import domains.Tema;
import domains.validators.*;
import repository.inMemoryRepository.CrudRepository;
import repository.xmlFileRepository.XMLNotaFileRepository;
import repository.xmlFileRepository.XMLStudentFileRepository;
import repository.xmlFileRepository.XMLTemaFileRepository;
import services.Service;
import userinterface.Consola;
import utils.Paths;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws ValidationException {

        StructuraAnUniversitar structuraAnUniversitar = StructuraAnUniversitar.getInstance(Paths.AN_UNIVERSITAR);

        Validator<Student> validatorStudent = new StudentValidator();
        Validator<Tema> validatorTema = new TemaValidator();
        Validator<Nota> validatorNota = new NotaValidator();

        //CrudRepository<String, Student> studentRepository = new StudentRepository(validatorStudent);
        //CrudRepository<String, Tema> temaRepository = new TemaRepository(validatorTema);

        CrudRepository<String, Student> studentFileRepository = new XMLStudentFileRepository(validatorStudent, Paths.STUDENT);
        CrudRepository<String, Tema> temaFileRepository = new XMLTemaFileRepository(validatorTema, Paths.TEMA);
        CrudRepository<Pair<String, String>, Nota> notaFileRepository = new XMLNotaFileRepository(validatorNota, Paths.NOTA);
        Service service = new Service(studentFileRepository, temaFileRepository, notaFileRepository);
        Consola.setCurrentWeek(structuraAnUniversitar.getWeek(LocalDate.now(), structuraAnUniversitar.getSemestru(LocalDate.now())));
        Consola consola = new Consola(service);
        //consola.runMenu();
        MainApp.main(args);
    }
}