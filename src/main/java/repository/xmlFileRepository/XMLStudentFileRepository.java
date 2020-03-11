package repository.xmlFileRepository;

import domains.Student;
import domains.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLStudentFileRepository extends XMLFileRepository<Student, String> {
    public XMLStudentFileRepository(Validator<Student> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Student createEntityFromElement(Element entityElement) {
        String id = entityElement.getAttribute("id");
        String nume = entityElement.getElementsByTagName("nume")
                .item(0)
                .getTextContent();
        String prenume = entityElement.getElementsByTagName("prenume")
                .item(0)
                .getTextContent();
        String email = entityElement.getElementsByTagName("email")
                .item(0)
                .getTextContent();
        String cadruDidacticLab = entityElement.getElementsByTagName("cadruDidacticLab")
                .item(0)
                .getTextContent();
        String grupa = entityElement.getElementsByTagName("grupa")
                .item(0)
                .getTextContent();
        String nr_motivari = entityElement.getElementsByTagName("nr_motivari")
                .item(0)
                .getTextContent();

        Student student = new Student(nume, prenume, email, cadruDidacticLab, Integer.parseInt(grupa));
        student.setId(id);
        student.setNr_motivari(Integer.parseInt(nr_motivari));
        return student;
    }

    @Override
    public Element createElementFromEntity(Document document, Student entity) {
        Element element = document.createElement("student");

        element.setAttribute("id", entity.getId());

        Element name = document.createElement("nume");
        name.setTextContent(entity.getNume());
        element.appendChild(name);

        Element prenume = document.createElement("prenume");
        prenume.setTextContent(entity.getPrenume());
        element.appendChild(prenume);

        Element email = document.createElement("email");
        email.setTextContent(entity.getEmail());
        element.appendChild(email);

        Element cadruDidacticLab = document.createElement("cadruDidacticLab");
        cadruDidacticLab.setTextContent(entity.getCadruDidacticLab());
        element.appendChild(cadruDidacticLab);

        Element grupa = document.createElement("grupa");
        grupa.setTextContent(((Integer) entity.getGrupa()).toString());
        element.appendChild(grupa);

        Element nr_motivari = document.createElement("nr_motivari");
        nr_motivari.setTextContent(((Integer) entity.getNr_motivari()).toString());
        element.appendChild(nr_motivari);

        return element;
    }

    @Override
    public Student update(Student entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity " + Student.class.getName() + " NULL!");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Student oldValue = entities.get(entity.getId());
        oldValue.setEmail(entity.getEmail());
        oldValue.setNume(entity.getNume());
        oldValue.setPrenume(entity.getPrenume());
        oldValue.setGrupa(entity.getGrupa());
        oldValue.setNr_motivari(entity.getNr_motivari());
        saveAllToFile();
        return null;
    }
}
