package controller.studentController;

import controller.ControllerAlert;
import controller.studentController.EditStudentController;
import domains.Student;
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

public class StudentController implements Observer<EntityChangeEvent> {
    ObservableList<Student> modelStudent = FXCollections.observableArrayList();
    @FXML
    TableColumn<Student, String> tableColumnID;
    @FXML
    TableColumn<Student, String> tableColumnNume;
    @FXML
    TableColumn<Student, String> tableColumnPrenume;
    @FXML
    TableColumn<Student, String> tableColumnMail;
    @FXML
    TableColumn<Student, String> tableColumnProfesor;
    @FXML
    TableColumn<Student, Integer> tableColumnGrupa;
    @FXML
    TableView<Student> tableViewStudenti;
    @FXML
    TextField textFieldNumeStudent;
    //----------------------end TableView fx:id----------------
    private Service service;

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory("nume"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory("prenume"));
        tableColumnMail.setCellValueFactory(new PropertyValueFactory("email"));
        tableColumnProfesor.setCellValueFactory(new PropertyValueFactory("cadruDidacticLab"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory("grupa"));
        tableViewStudenti.setItems(modelStudent);

        textFieldNumeStudent.textProperty().addListener(((observableValue, s, t1) -> handleFilter()));
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        modelStudent.setAll(getStudentList());
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        modelStudent.setAll(getStudentList());
    }

    private List<Student> getStudentList() {
        Iterable<Student> studenti = service.findAllStudent();
        List<Student>  listaStudenti = StreamSupport.stream(studenti.spliterator(), false)
                .collect(Collectors.toList());
        return listaStudenti;
    }

    @FXML
    private void handleFilter() {
        Predicate<Student> p1 = student -> student.getNume().startsWith(textFieldNumeStudent.getText());
        modelStudent.setAll(getStudentList()
                            .stream()
                            .filter(p1)
                            .collect(Collectors.toList()));
    }

    private void showEditStudentDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editStudentView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditStudentController editStudentController = loader.getController();
            editStudentController.setService(service, dialogStage, student);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        showEditStudentDialog(null);
    }

    @FXML
    public void handleDeleteStudent(ActionEvent event) {
        Student student = tableViewStudenti.getSelectionModel().getSelectedItem();
        if (student != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa stergi studentul?");
            alert.setTitle("Delete");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
               service.deleteStudent(student.getId());
            }
        } else ControllerAlert.showErrorMessage(null, "Mai intai selectati un student!");
    }

    @FXML
    private void handleUpdateStudent(ActionEvent event) {
        Student student = tableViewStudenti.getSelectionModel().getSelectedItem();
        if (student != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Esti sigur ca vrei sa modifici studentul?");
            alert.setTitle("Update");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                showEditStudentDialog(student);
        } else
            ControllerAlert.showErrorMessage(null, "Mai intai selecteaza un student!");
    }
}