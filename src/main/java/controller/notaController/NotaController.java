package controller.notaController;

import controller.ControllerAlert;
import domains.Nota;
import domains.NotaDtoView;
import domains.Student;
import domains.Tema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.Service;
import utils.events.EntityChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotaController implements Observer<EntityChangeEvent> {
    private Service service;

    ObservableList<NotaDtoView> modelNota = FXCollections.observableArrayList();
    @FXML
    TableColumn<Nota, String> tableColumnID;
    @FXML
    TableColumn<Nota, String> tableColumnNumeStudent;
    @FXML
    TableColumn<Nota, String> tableColumnDescriereTema;
    @FXML
    TableColumn<Nota, String> tableColumnDataPredare;
    @FXML
    TableColumn<Nota, String> tableColumnNumeProfesor;
    @FXML
    TableColumn<Nota, Integer> tableColumnValoare;
    @FXML
    TableView<NotaDtoView> tableViewNote;
    @FXML
    TextField textFieldNumeStudent;
    @FXML
    TextArea textAreaFeedbackNota;


    //----------------------end TableView fx:id----------------

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        modelNota.setAll(getNoteList());
    }

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory("numeStudent"));
        tableColumnDescriereTema.setCellValueFactory(new PropertyValueFactory("descriereTema"));
        tableColumnDataPredare.setCellValueFactory(new PropertyValueFactory("dataPredare"));
        tableColumnNumeProfesor.setCellValueFactory(new PropertyValueFactory("numeProfesor"));
        tableColumnValoare.setCellValueFactory(new PropertyValueFactory("valoare"));
        tableViewNote.setItems(modelNota);

        textFieldNumeStudent.textProperty().addListener(((observableValue, s, t1) -> handleFilter()));
        tableViewNote.getSelectionModel().selectedItemProperty().addListener(((observableValue, notaDtoView, t1) -> {
                if (tableViewNote.getSelectionModel().getSelectedItem() != null)
                    textAreaFeedbackNota.setText(tableViewNote.getSelectionModel().getSelectedItem().getFeedback());
                }));
        textAreaFeedbackNota.setEditable(false);
    }

    private List<NotaDtoView> getNoteList() {
        List<NotaDtoView> listaNote = new ArrayList<>();
        Iterable<Nota> note = service.findAllNota();
        note.forEach(nota -> {
            Student student = service.findOneStudent(nota.getId().fst);
            Tema tema = service.findOneTema(nota.getId().snd);
            NotaDtoView notaDtoView = new NotaDtoView(student.getNume(), tema.getDescriere(), nota.getDataString(), nota.getProfesor(), nota.getValoare(), nota.getFeedback());
            notaDtoView.setId(nota.getId());
            listaNote.add(notaDtoView);
        });
        return listaNote;
    }

    @FXML
    private void handleFilter() {
        Predicate<NotaDtoView> p1 = nota -> nota.getNumeStudent().startsWith(textFieldNumeStudent.getText());
        modelNota.setAll(getNoteList()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        modelNota.setAll(getNoteList());
    }

    private void showEditNotaDialog(NotaDtoView nota) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editNotaView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Nota");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditNotaController editNotaController = loader.getController();
            editNotaController.setService(service, dialogStage, nota);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAddNota(ActionEvent event) {
        showEditNotaDialog(null);
    }

    public void handleDeleteNota(ActionEvent event) {
       NotaDtoView nota = tableViewNote.getSelectionModel().getSelectedItem();
        if (nota != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa stergi nota?");
            alert.setTitle("Delete");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                service.deleteNota(nota.getId());
            }
        } else ControllerAlert.showErrorMessage(null, "Mai intai selectati o nota!");
    }

    public void handleUpdateNota(ActionEvent event) {
        NotaDtoView nota = tableViewNote.getSelectionModel().getSelectedItem();
        if (nota != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa modifici nota?");
            alert.setTitle("Update");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                showEditNotaDialog(nota);
        } else
            ControllerAlert.showErrorMessage(null, "Mai intai selecteaza o nota!");
    }
}
