package repository.fileRepository;

import com.sun.tools.javac.util.Pair;
import domains.Nota;
import domains.NotaDTO;
import domains.validators.NotaValidator;
import domains.validators.Validator;
import utils.Paths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static utils.Constants.DATE_FORMAT;


public class NotaFileRepository extends FileRepository<Nota, Pair<String, String>> {
    private static NotaFileRepository single_instance = null;

    public NotaFileRepository(Validator<Nota> validator, String nameFile) {
        super(validator, nameFile);
    }

    public static NotaFileRepository getInstance() {
        if (single_instance == null)
            single_instance = new NotaFileRepository(new NotaValidator(), Paths.NOTA);
        return single_instance;
    }

    @Override
    public Nota createEntity(String[] fields) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate data = LocalDate.from(formatter.parse(fields[2]));
        Nota nota = new Nota(data, fields[3], Float.parseFloat(fields[4]), fields[5]);
        nota.setId(new Pair<>(fields[0], fields[1]));
        return nota;
    }

    @Override
    public Nota update(Nota entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity " + Nota.class.getName() + " NULL!");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Nota oldValue = entities.get(entity.getId());
        oldValue.setValoare(entity.getValoare());
        oldValue.setData(entity.getData());
        oldValue.setProfesor(entity.getProfesor());
        oldValue.setFeedback(entity.getFeedback());
        saveAllToFile();
        return null;
    }

    public void saveToTextFile(NotaDTO entity) {
        String file = "./data/catalog/" + entity.getNumeStudent() + ".txt";
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write("Tema: " + entity.getIdTema());
            out.newLine();
            out.write("Nota: " + entity.getValoare());
            out.newLine();
            out.write("Predata in saptamna: " + entity.getNrSaptamana());
            out.newLine();
            out.write("Deadline: " + entity.getDeadlineWeek());
            out.newLine();
            out.write("Feedback: " + entity.getFeedback());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
