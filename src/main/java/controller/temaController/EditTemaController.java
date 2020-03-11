package controller.temaController;

import controller.ControllerAlert;
import domains.Tema;
import domains.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.Service;

public class EditTemaController {
    private Service service;
    Stage dialogStage;
    Tema tema;

    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldDescriere;
    @FXML
    private TextField textFieldStartWeek;
    @FXML
    private TextField textFieldDeadlineWeek;

    public void setService(Service service, Stage stage, Tema tema) {
        this.service = service;
        this.dialogStage = stage;
        this.tema = tema;
        if (tema != null) {
            setField(tema);
            textFieldId.setEditable(false);
        }
    }

    private void setField(Tema tema) {
        textFieldId.setText(tema.getId());
        textFieldDescriere.setText(tema.getDescriere());
        textFieldStartWeek.setText(String.valueOf(tema.getStartWeek()));
        textFieldDeadlineWeek.setText(String.valueOf(tema.getDeadlineWeek()));
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
        String descriere = textFieldDescriere.getText();
        String startWeek = textFieldStartWeek.getText();
        String deadlineWeek = textFieldDeadlineWeek.getText();
        if (!isInt(startWeek)) {
            ControllerAlert.showErrorMessage(null, "StartWeek trebuie sa fie de tipul INT!");
        }
        if (!isInt(deadlineWeek)) {
            ControllerAlert.showErrorMessage(null, "DeadlineWeek trebuie sa fie de tipul INT!");
        }
        Tema temaNoua = new Tema(descriere, Integer.parseInt(startWeek), Integer.parseInt(deadlineWeek));
        temaNoua.setId(id);
        if (tema == null)
            addTema(temaNoua);
        else
            updateTema(temaNoua);
    }

    private void addTema(Tema temaNoua) {
        try {
            Tema saveTema = service.saveTema(temaNoua.getId(), temaNoua.getDescriere(), temaNoua.getStartWeek(), temaNoua.getDeadlineWeek());
            if (saveTema == null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare Tema", "Tema a fost salvata!");
                dialogStage.close();
            }
            else
                ControllerAlert.showErrorMessage(null, "Exista o tema cu id-ul: " + saveTema.getId());
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateTema(Tema temaNoua) {
        try {
            Tema updateTema = service.updateTema(temaNoua.getId(), temaNoua.getDescriere(), temaNoua.getStartWeek(), temaNoua.getDeadlineWeek());
            if (updateTema != null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare Tema", "Tema a fost modificata!");
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
