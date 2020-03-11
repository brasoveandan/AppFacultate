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

class XMLStudentFileRepositoryTest {
    XMLStudentFileRepository studentRepository;
    Student student;

    @BeforeEach
    void setUp() {
        studentRepository = new XMLStudentFileRepository(new StudentValidator(), "data/test/test.xml");
        student = new Student("Ceausescu", "Nicolae", "nicolae_ceausecu@yahoo.com", "Stalin", 221);
        student.setId("1");
        studentRepository.save(student);
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