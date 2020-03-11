import com.sun.tools.javac.util.Pair;
import controller.MenuController;
import domains.Nota;
import domains.Student;
import domains.Tema;
import domains.validators.NotaValidator;
import domains.validators.StudentValidator;
import domains.validators.TemaValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.inMemoryRepository.CrudRepository;
import repository.xmlFileRepository.XMLNotaFileRepository;
import repository.xmlFileRepository.XMLStudentFileRepository;
import repository.xmlFileRepository.XMLTemaFileRepository;
import services.Service;
import utils.Paths;

import java.io.IOException;

public class MainApp extends Application {

    CrudRepository<String, Student> studentFileRepository = new XMLStudentFileRepository(new StudentValidator(), Paths.STUDENT);
    CrudRepository<String, Tema> temaFileRepository = new XMLTemaFileRepository(new TemaValidator(), Paths.TEMA);
    CrudRepository<Pair<String, String>, Nota> notaFileRepository = new XMLNotaFileRepository(new NotaValidator(), Paths.NOTA);
    Service service = new Service(studentFileRepository, temaFileRepository, notaFileRepository);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initViewMenu(primaryStage);
        primaryStage.setWidth(450);
        primaryStage.show();
    }

    private void initViewMenu(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/menuView.fxml"));
        AnchorPane anchorPane = loader.load();
        primaryStage.setScene(new Scene(anchorPane));

        MenuController menuController = loader.getController();
        menuController.setService(service);
    }
}
