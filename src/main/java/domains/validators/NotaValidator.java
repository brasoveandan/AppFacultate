package domains.validators;

import domains.Nota;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota entity) throws ValidationException {
        String message = "";
        if (entity.getId() == null || entity.getId().equals("")) {
            message += "Id-ul notei este invalid.\n";
        }
        if (entity.getId().fst == null || entity.getId().fst.equals("")) {
            message += "Id-ul studentului este invalid.\n";
        }
        if (entity.getId().snd == null || entity.getId().snd.equals("")) {
            message += "Id-ul temei este invalid.\n";
        }
        if (entity.getProfesor() == null || entity.getProfesor().equals("")) {
            message += "Profesorul este invalid.\n";
        }
        if (entity.getData() == null || entity.getData().equals("")) {
            message += "Data este invalida.\n";
        }
        if (entity.getValoare() < 0 || entity.getValoare() > 10) {
            message += "Valoarea notei este invalida.\n";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
