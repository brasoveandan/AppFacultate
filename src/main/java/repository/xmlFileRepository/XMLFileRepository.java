package repository.xmlFileRepository;

import domains.Entity;
import domains.validators.ValidationException;
import domains.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import repository.inMemoryRepository.InMemoryRepository;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class XMLFileRepository<E extends Entity<ID>, ID> extends InMemoryRepository<ID, E> implements CrudXMLFileRepository<ID, E> {
    private String fileName;

    public XMLFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    @Override
    public E findOne(ID id) {
        E rez = super.findOne(id);
        return rez;
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E rez = super.save(entity);
        if (rez == null)
            saveAllToFile();
        return rez;
    }

    @Override
    public E delete(ID id) {
        E rez = super.delete(id);
        saveAllToFile();
        return rez;
    }

    @Override
    public void loadData() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node entityElement = children.item(i);
                if (entityElement instanceof Element) {
                    E entity = createEntityFromElement((Element) entityElement);
                    super.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract E createEntityFromElement(Element entityElement);

    public abstract Element createElementFromEntity(Document document, E entity);

    @Override
    public void saveAllToFile() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root = document.createElement("enities");
            document.appendChild(root);
            super.findAll().forEach(s -> {
                Element e = createElementFromEntity(document, s);
                root.appendChild(e);
            });

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();

            Source source = new DOMSource(document);

            transformer.transform(source,
                    new StreamResult(fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
