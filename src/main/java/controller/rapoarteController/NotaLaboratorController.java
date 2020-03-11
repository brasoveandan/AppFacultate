package controller.rapoarteController;

import domains.NotaDtoView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.Service;


public class NotaLaboratorController {
    ObservableList<NotaDtoView> model = FXCollections.observableArrayList();
    private Service service;

    @FXML
    TableColumn tableColumnNumeStudent;
    @FXML
    TableColumn tableColumnPrenumeStudent;
    @FXML
    TableColumn tableColumnGrupaStudent;
    @FXML
    TableColumn tableColumnNotaFinala;
    @FXML
    TableView tableViewNoteLaborator;

    public void setService(Service service, ObservableList<NotaDtoView> model) {
        this.service = service;
        this.model = model;
        initialize();
    }

    @FXML
    public void initialize() {
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory("numeStudent"));
        tableColumnPrenumeStudent.setCellValueFactory(new PropertyValueFactory("descriereTema"));
        tableColumnGrupaStudent.setCellValueFactory(new PropertyValueFactory("dataPredare"));
        tableColumnNotaFinala.setCellValueFactory(new PropertyValueFactory("valoare"));
        tableViewNoteLaborator.setItems(model);
    }
}
