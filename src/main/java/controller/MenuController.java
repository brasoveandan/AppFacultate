package controller;

import controller.notaController.NotaController;
import controller.rapoarteController.RapoarteController;
import controller.studentController.StudentController;
import controller.temaController.TemaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.Service;

import java.io.IOException;

public class MenuController {
    Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void showStudentWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/studentView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            StudentController studentController = loader.getController();
            studentController.setService(service);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showTemaWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/temaView.fxml"));
            AnchorPane root = loader.load();
            Stage temaStage = new Stage();
            temaStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            temaStage.setScene(scene);
            TemaController temaController = loader.getController();
            temaController.setService(service);
            temaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNotaWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/notaView.fxml"));
            AnchorPane root = loader.load();
            Stage notaStage = new Stage();
            notaStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            notaStage.setScene(scene);
            NotaController notaController = loader.getController();
            notaController.setService(service);
            notaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showRapoarteWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/rapoarteView/rapoarteView.fxml"));
            AnchorPane root = loader.load();
            Stage notaStage = new Stage();
            notaStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            notaStage.setScene(scene);
            RapoarteController rapoarteController = loader.getController();
            rapoarteController.setService(service);
            notaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
