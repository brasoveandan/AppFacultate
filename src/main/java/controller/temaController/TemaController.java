package controller.temaController;

import controller.ControllerAlert;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TemaController implements Observer<EntityChangeEvent> {
    ObservableList<Tema> modelTema = FXCollections.observableArrayList();
    @FXML
    TableColumn<Tema, String> tableColumnID;
    @FXML
    TableColumn<Tema, String> tableColumnDescriere;
    @FXML
    TableColumn<Tema, String> tableColumnStartWeek;
    @FXML
    TableColumn<Tema, String> tableColumnDeadlineWeek;
    @FXML
    TableView<Tema> tableViewTeme;
    @FXML
    TextField textFieldDescriereTema;
    //----------------------end TableView fx:id----------------
    private Service service;

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnDescriere.setCellValueFactory(new PropertyValueFactory("descriere"));
        tableColumnStartWeek.setCellValueFactory(new PropertyValueFactory("startWeek"));
        tableColumnDeadlineWeek.setCellValueFactory(new PropertyValueFactory("deadlineWeek"));
        tableViewTeme.setItems(modelTema);

        textFieldDescriereTema.textProperty().addListener(((observableValue, s, t1) -> handleFilter()));
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        modelTema.setAll(getTemaList());
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        modelTema.setAll(getTemaList());
    }

    private List<Tema> getTemaList() {
        Iterable<Tema> teme = service.findAllTema();
        List<Tema>  listaTeme = StreamSupport.stream(teme.spliterator(), false)
                .collect(Collectors.toList());
        return listaTeme;
    }

    @FXML
    private void handleFilter() {
        Predicate<Tema> p1 = tema -> tema.getDescriere().startsWith(textFieldDescriereTema.getText());
        modelTema.setAll(getTemaList()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    private void showEditTemaDialog(Tema tema) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editTemaView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Tema");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditTemaController editTemaController = loader.getController();
            editTemaController.setService(service, dialogStage, tema);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTema(ActionEvent event) {
        showEditTemaDialog(null);
    }

    @FXML
    public void handleDeleteTema(ActionEvent event) {
        Tema tema = tableViewTeme.getSelectionModel().getSelectedItem();
        if (tema != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa stergi tema?");
            alert.setTitle("Delete");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                service.deleteTema(tema.getId());
            }
        } else ControllerAlert.showErrorMessage(null, "Mai intai selectati o tema!");
    }

    @FXML
    private void handleUpdateTema(ActionEvent event) {
        Tema tema = tableViewTeme.getSelectionModel().getSelectedItem();
        if (tema != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa modifici tema?");
            alert.setTitle("Update");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                showEditTemaDialog(tema);
        } else
            ControllerAlert.showErrorMessage(null, "Mai intai selecteaza o tema!");
    }
}
