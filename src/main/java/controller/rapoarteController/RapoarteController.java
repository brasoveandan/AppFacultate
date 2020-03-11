package controller.rapoarteController;

import controller.ControllerAlert;
import domains.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import services.Service;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RapoarteController {
    ObservableList<NotaDtoView> modelNota = FXCollections.observableArrayList();
    private Service service;

    @FXML
    AnchorPane viewAnchorPane;

    public void setService(Service service) {
        this.service = service;
    }

    private List<Nota> getNotaList() {
        Iterable<Nota> note = service.findAllNota();
        List<Nota>  listaNote = StreamSupport.stream(note.spliterator(), false)
                .collect(Collectors.toList());
        return listaNote;
    }

    private List<Student> getStudentList() {
        Iterable<Student> studenti = service.findAllStudent();
        List<Student>  listaStudenti = StreamSupport.stream(studenti.spliterator(), false)
                .collect(Collectors.toList());
        return listaStudenti;
    }
    private List<Tema> getTemaList() {
        Iterable<Tema> teme = service.findAllTema();
        List<Tema>  temaList = StreamSupport.stream(teme.spliterator(), false)
                .collect(Collectors.toList());
        return temaList;
    }

    private List<NotaDtoView> getRaportNotaLaborator(List<Nota> notaList) {
        List<NotaDtoView> rezultat = new ArrayList<>();
        List<Student> studentList = getStudentList();
        studentList.forEach(student -> {
            final float[] pondere = {0};
            final float[] suma = {0};
            notaList.forEach(nota -> {
                Tema tema = service.findOneTema(nota.getId().snd);
                if (student.getId().equals(nota.getId().fst)) {
                        suma[0] += (nota.getValoare() * (tema.getDeadlineWeek() - tema.getStartWeek()));
                        pondere[0] += (tema.getDeadlineWeek() - tema.getStartWeek());
                }
                else {
                    suma[0] += 1;
                    pondere[0] += 1;
                }
            });
            float media = suma[0] / pondere[0];
            rezultat.add(new NotaDtoView(student.getNume(), student.getPrenume(), String.valueOf(student.getGrupa()), "", media, ""));
        });
        if (rezultat.size() != 0)
            return rezultat;
        return null;
    }

    List<NotaDtoView> getRaportTemaGrea(List<Nota> notaList) {
        List<NotaDtoView> rezultat = new ArrayList<>();
        List<Tema> temaList = getTemaList();
        temaList.forEach(tema -> {
            final int[] pondere = {0};
            final float[] suma = {0};
            notaList.forEach(nota -> {
                if (tema.getId().equals(nota.getId().snd)) {
                    suma[0] +=  nota.getValoare();
                    pondere[0] += 1;
                }
                else {
                    suma[0] += 1;
                    pondere[0] += 1;
                }
            });
            float media = suma[0] / pondere[0];
            rezultat.add(new NotaDtoView("", tema.getDescriere(), String.valueOf(tema.getStartWeek()), String.valueOf(tema.getDeadlineWeek()), media, ""));
        });
        final float[] minim = {10};
        rezultat.forEach(notaDtoView -> {
            if (notaDtoView.getValoare() < minim[0])
                minim[0] = notaDtoView.getValoare();
        });
        final int[] count = {0};
        rezultat.forEach(notaDtoView -> {
            if (notaDtoView.getValoare() == minim[0])
                count[0]++;
        });
        if (rezultat.size() != 0)
            return rezultat.subList(0, count[0]);
        return null;
    }

    private List<NotaDtoView> getRaportStundetiExamen(List<NotaDtoView> notaDtoViewList) {
        List<NotaDtoView> rezultat = new ArrayList<>();
        notaDtoViewList.forEach(notaDtoView -> {
            if (notaDtoView.getValoare() >= 4)
                rezultat.add(notaDtoView);
        });
        if (rezultat.size() != 0)
            return rezultat;
        return null;
    }

    private List<NotaDtoView> getRaportStudentiPremianti(List<Nota> notaList) {
        List<NotaDtoView> rezultat = new ArrayList<>();
        List<Student> studentList = getStudentList();
        studentList.forEach(student -> {
            final int[] count = {0};
            notaList.forEach(nota -> {
                Tema tema = service.findOneTema(nota.getId().snd);
                int nrSaptamanaPredare = StructuraAnUniversitar.getInstance().getWeek(nota.getData(),
                        StructuraAnUniversitar.getInstance().getSemestru(nota.getData()));
                if (student.getId().equals(nota.getId().fst) && nrSaptamanaPredare <= tema.getDeadlineWeek()) {
                    count[0]++;
                }
            });
            notaList.forEach(nota -> {
                Tema tema = service.findOneTema(nota.getId().snd);
                if (count[0] == getTemaList().size() && student.getId().equals(nota.getId().fst)) {
                    rezultat.add(new NotaDtoView(student.getNume(), tema.getDescriere(), String.valueOf(student.getGrupa()), "", nota.getValoare(), ""));
                }
            });
        });
        if (rezultat.size() != 0)
            return rezultat;
        return null;
    }

    public void handleNoteLaborator(ActionEvent actionEvent) throws IOException {
        List<NotaDtoView> lista = getRaportNotaLaborator(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null, "Raportul este gol!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarteView/notaLaboratorView.fxml"));
        AnchorPane notaPane = loader.load();
        NotaLaboratorController notaLaboratorController = loader.getController();
        notaLaboratorController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(notaPane);
    }

    public void handleTemaDificila(ActionEvent actionEvent) throws IOException {
        List<NotaDtoView> lista = getRaportTemaGrea(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null, "Raportul este gol!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarteView/temaGreaView.fxml"));
        AnchorPane temaPane = loader.load();
        TemaGreaController temaGreaController = loader.getController();
        temaGreaController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(temaPane);

    }

    public void handleStudentExamen(ActionEvent actionEvent) throws IOException {
        List<NotaDtoView> lista = getRaportStundetiExamen(getRaportNotaLaborator(getNotaList()));
        if (lista == null) {
            ControllerAlert.showErrorMessage(null, "Raportul este gol!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarteView/notaLaboratorView.fxml"));
        AnchorPane studentPane = loader.load();
        NotaLaboratorController notaLaboratorController = loader.getController();
        notaLaboratorController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(studentPane);
    }

    public void handleStudentPremiant(ActionEvent actionEvent) throws IOException {
        List<NotaDtoView> lista = getRaportStudentiPremianti(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null, "Raportul este gol!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarteView/studentPremiantView.fxml"));
        AnchorPane studentPane = loader.load();
        StudentPremiantController studentPremiantController = loader.getController();
        studentPremiantController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(studentPane);
    }
}