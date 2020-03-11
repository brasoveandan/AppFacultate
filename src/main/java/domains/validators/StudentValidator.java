package domains.validators;

import domains.Student;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student entity) throws ValidationException {
        String message = "";
        if (entity.getId() == null || entity.getId().equals("")) {
            message += "Id-ul studentului este invalid.\n";
        }
        if (entity.getNume().equals("")) {
            message += "Numele studentului este invalid.\n";
        }
        if (entity.getPrenume().equals("")) {
            message += "Prenumele studentului este invalid.\n";
        }
        if (entity.getEmail().equals("")) {
            message += "Email-ul studentului este invalid.\n";
        }
        if (entity.getCadruDidacticLab().equals("")) {
            message += "Numele cadrului didactic este invalid.\n";
        }
        if (entity.getGrupa() <= 0) {
            message += "Grupa studentului este invalida.\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
