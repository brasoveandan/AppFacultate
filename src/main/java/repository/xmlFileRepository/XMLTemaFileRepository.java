package repository.xmlFileRepository;

import domains.Tema;
import domains.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLTemaFileRepository extends XMLFileRepository<Tema, String> {
    public XMLTemaFileRepository(Validator<Tema> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Tema createEntityFromElement(Element entityElement) {
        String id = entityElement.getAttribute("id");
        String descriere = entityElement.getElementsByTagName("descriere")
                .item(0)
                .getTextContent();
        String startWeek = entityElement.getElementsByTagName("startWeek")
                .item(0)
                .getTextContent();
        String deadlineWeek = entityElement.getElementsByTagName("deadlineWeek")
                .item(0)
                .getTextContent();
        Tema tema = new Tema(descriere, Integer.parseInt(startWeek), Integer.parseInt(deadlineWeek));
        tema.setId(id);
        return tema;
    }

    @Override
    public Element createElementFromEntity(Document document, Tema entity) {
        Element element = document.createElement("tema");
        element.setAttribute("id", entity.getId());
        Element descriere = document.createElement("descriere");
        descriere.setTextContent(entity.getDescriere());
        element.appendChild(descriere);
        Element startWeek = document.createElement("startWeek");
        startWeek.setTextContent(((Integer) entity.getStartWeek()).toString());
        element.appendChild(startWeek);
        Element deadlineWeek = document.createElement("deadlineWeek");
        deadlineWeek.setTextContent(((Integer) entity.getDeadlineWeek()).toString());
        element.appendChild(deadlineWeek);
        return element;
    }

    @Override
    public Tema update(Tema entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity " + Tema.class.getName() + " NULL!");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Tema oldValue = entities.get(entity.getId());
        oldValue.setDescriere(entity.getDescriere());
        oldValue.setStartWeek(entity.getStartWeek());
        oldValue.setDeadlineWeek(entity.getDeadlineWeek());
        saveAllToFile();
        return null;
    }
}
