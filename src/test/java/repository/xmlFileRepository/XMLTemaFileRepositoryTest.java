package repository.xmlFileRepository;

import domains.Tema;
import domains.validators.TemaValidator;
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

class XMLTemaFileRepositoryTest {
    XMLTemaFileRepository temaRepository;
    Tema tema;

    @BeforeEach
    void setUp() {
        temaRepository = new XMLTemaFileRepository(new TemaValidator(), "data/test/test.xml");
        tema = new Tema("MAP", 2, 10);
        tema.setId("1");
        temaRepository.save(tema);
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
}