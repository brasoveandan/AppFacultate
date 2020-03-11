package repository.fileRepository;

import domains.Student;
import domains.validators.Validator;

public class StudentFileRepository extends FileRepository<Student, String> {
    public StudentFileRepository(Validator<Student> validator, String nameFile) {
        super(validator, nameFile);
    }

    @Override
    public Student createEntity(String[] fields) {
        try {
            Student student = new Student(fields[1], fields[2], fields[3], fields[4], Integer.parseInt(fields[5]));
            student.setId(fields[0]);
            student.setNr_motivari(Integer.parseInt(fields[6]));
            return student;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
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
