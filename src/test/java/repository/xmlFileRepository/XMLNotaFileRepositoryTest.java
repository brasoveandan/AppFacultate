package repository.xmlFileRepository;

import com.sun.tools.javac.util.Pair;
import domains.Nota;
import domains.validators.NotaValidator;
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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLNotaFileRepositoryTest {
    XMLNotaFileRepository notaRepository;
    Nota nota;

    @BeforeEach
    void setUp() {
        notaRepository = new XMLNotaFileRepository(new NotaValidator(), "data/test/test.xml");
        nota = new Nota(LocalDate.of(2019, 11, 14), "Vasile", 10, feedback);
        nota.setId(Pair.of("1", "1"));
        notaRepository.save(nota);
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
}