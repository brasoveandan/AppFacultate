package controller.studentController;

import controller.ControllerAlert;
import domains.Student;
import domains.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.Service;


public class EditStudentController {
    private Service service;
    Stage dialogStage;
    Student student;

    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfesor;
    @FXML
    private TextField textFieldGrupa;


    public void setService(Service service, Stage stage, Student student) {
        this.service = service;
        this.dialogStage = stage;
        this.student = student;
        if (student != null) {
            setField(student);
            textFieldId.setEditable(false);
        }
    }

    private void setField(Student student) {
        textFieldId.setText(student.getId());
        textFieldNume.setText(student.getNume());
        textFieldPrenume.setText(student.getPrenume());
        textFieldEmail.setText(student.getEmail());
        textFieldProfesor.setText(student.getCadruDidacticLab());
        textFieldGrupa.setText(String.valueOf(student.getGrupa()));
    }
    private boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    public void handleEnter(ActionEvent event) {
        String id = textFieldId.getText();
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String mail = textFieldEmail.getText();
        String cadruDidacticLab = textFieldProfesor.getText();
        String grupa = textFieldGrupa.getText();
        if (!isInt(grupa)) {
            ControllerAlert.showErrorMessage(null, "Grupa trebuie sa fie de tipul INT!");
        }
        Student st = new Student(nume, prenume, mail, cadruDidacticLab, Integer.parseInt(grupa));
        st.setId(id);
        if (student == null)
            addStudent(st);
        else
            updateStudent(st);
    }

    private void addStudent(Student st) {
        try {
            Student st1 = service.saveStudent(st.getId(), st.getNume(), st.getPrenume(), st.getEmail(), st.getCadruDidacticLab(), st.getGrupa());
            if (st1 == null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare Student", "Studentul a fost salvat!");
                dialogStage.close();
            }
            else
                ControllerAlert.showErrorMessage(null, "Exista un studentul cu id-ul: " + st.getId());
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateStudent(Student st) {
        try {
            Student st1 = service.updateStudent(st.getId(), st.getNume(), st.getPrenume(), st.getEmail(), st.getCadruDidacticLab(), st.getGrupa());
            if (st1 != null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare Student", "Studentul a fost modificat!");
                dialogStage.close();
            }
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
