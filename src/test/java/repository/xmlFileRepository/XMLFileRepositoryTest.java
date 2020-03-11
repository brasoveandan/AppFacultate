package repository.xmlFileRepository;

import domains.Student;
import domains.validators.StudentValidator;
import domains.validators.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLFileRepositoryTest {
    XMLStudentFileRepository studentRepository;
    Student student;

    @BeforeEach
    void setUp() {
        studentRepository = new XMLStudentFileRepository(new StudentValidator(), "data/test/test.xml");
        student = new Student("Ceausescu", "Nicolae", "nicolae_ceausecu@yahoo.com", "Stalin", 221);
        student.setId("1");
        studentRepository.save(student);
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
    void tearDown() throws ParserConfigurationException, TransformerException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();
        Element root = document.createElement("test");
        document.appendChild(root);

        //write Document to file
        Transformer transformer = TransformerFactory.
                newInstance().newTransformer();

        Source source = new DOMSource(document);

        transformer.transform(source,
                new StreamResult("data/test/test.xml"));
    }
}