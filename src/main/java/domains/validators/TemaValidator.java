package domains.validators;

import domains.Tema;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema entity) throws ValidationException {
        String message = "";
        if (entity.getDeadlineWeek() < 1 || entity.getDeadlineWeek() > 14)
            message += "DeadLineWeek invalid!\n";
        if (entity.getStartWeek() < 1 || entity.getStartWeek() > 14)
            message += "StartWeek invalid!\n";
        if (entity.getStartWeek() > entity.getDeadlineWeek())
            message += "DeadLineWeek este mai mic decat startWeek.\n";
        if (entity.getDescriere().equals(""))
            message += "Descrierea este invalida!\n";
        if (!message.equals(""))
            throw new ValidationException(message);
    }
}
