package controller.notaController;

import com.sun.tools.javac.util.Pair;
import controller.ControllerAlert;
import domains.Nota;
import domains.NotaDtoView;
import domains.Student;
import domains.Tema;
import domains.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import services.Service;
import utils.controller.ComboBoxAutoComplete;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static utils.Constants.DATE_FORMAT;

public class EditNotaController {
    private Service service;
    Stage dialogStage;
    NotaDtoView nota;

    @FXML
    TextField textFieldNumeStudent;
    @FXML
    TextField textFieldMotivare;
    @FXML
    TextField textFieldDataPredare;
    @FXML
    TextField textFieldNumeProfesor;
    @FXML
    TextField textFieldValoare;
    @FXML
    ComboBox comboBoxTema;
    @FXML
    TextArea textAreaFeedback;

    List<String> descrieriTeme = new ArrayList<>();
    String descriereTemaDefault = new String();

    public void setService(Service service, Stage stage, NotaDtoView nota) {
        this.service = service;
        this.dialogStage = stage;
        this.nota = nota;
        if (nota != null) {
            setField(nota);
            textFieldNumeStudent.setEditable(false);
            textFieldMotivare.setEditable(false);
            textFieldDataPredare.setEditable(false);
            comboBoxTema.setEditable(false);
            descrieriTeme.addAll(getTemaDescriere());
        }
        else {
            descrieriTeme.addAll(getTemaDescriere());
            if (this.getTemaSaptamanaCurenta() != null)
                descriereTemaDefault = this.getTemaSaptamanaCurenta().getDescriere();
            initialize();
        }
    }

    @FXML
    public void initialize() {
        comboBoxTema.getItems().setAll(descrieriTeme);
        comboBoxTema.setValue(descriereTemaDefault);
        new ComboBoxAutoComplete<>(comboBoxTema);
    }


    private void setField(NotaDtoView nota) {
        textFieldNumeStudent.setText(nota.getNumeStudent());
        textFieldMotivare.setText("Nerelevant");
        comboBoxTema.setValue(nota.getDescriereTema());
        textFieldDataPredare.setText(nota.getDataPredare());
        textFieldNumeProfesor.setText(nota.getNumeProfesor());
        textFieldValoare.setText(String.valueOf(nota.getValoare()));
        textAreaFeedback.setText(nota.getFeedback());
    }

    private List<String> getTemaDescriere() {
        List<Tema> temaList = getTemaList();
        List<String> rezultat = new ArrayList<>();
        temaList.forEach(tema -> {
            if (!rezultat.contains(tema.getDescriere()))
                rezultat.add(tema.getDescriere());
        });
        if (rezultat.size() != 0)
            return rezultat;
        return null;
    }

    private Tema getTemaSaptamanaCurenta() {
            List<Tema> temaList = getTemaList();
            temaList = temaList.stream()
                    .filter(tema -> tema.getDeadlineWeek() == service.getCurrentWeek())
                    .collect(Collectors.toList());
           if (temaList.size() != 0)
               return temaList.get(0);
           return null;
    }

    private List<Nota> getNoteList() {
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

    private Nota getNotaDetails(String numeStudent, String descriereTema) {
        List<Nota> notaList = getNoteList();
        final Nota[] rezultat = new Nota[1];
        notaList.stream()
                .forEach(nota -> {
                    Student student = service.findOneStudent(nota.getId().fst);
                    Tema tema = service.findOneTema(nota.getId().snd);
                    if (student.getNume().equals(numeStudent) && tema.getDescriere().equals(descriereTema)) {
                        Pair id = Pair.of(student.getId(), tema.getId());
                        if (nota.getId().equals(id))
                            rezultat[0] = nota;
                    }
                });
        return rezultat[0];
    }

    private Student getStudentDetails(String numeStudent, String idStudent){
        List<Student> listaStudenti = getStudentList();
        if (idStudent == null)
            listaStudenti = listaStudenti.stream()
            .filter(student -> student.getNume().equals(numeStudent))
            .collect(Collectors.toList());
        else {
            listaStudenti = listaStudenti.stream()
                    .filter(student -> student.getNume().equals(numeStudent) && student.getId().equals(idStudent))
                    .collect(Collectors.toList());
        }
        if (listaStudenti.size() != 0)
            return listaStudenti.get(0);
        return null;
    }

    private Tema getTemaDetails(String descriereTema, String idTema) {
        List<Tema> listaTeme = getTemaList();
        if (idTema == null)
            listaTeme =  listaTeme.stream()
            .filter(tema -> tema.getDescriere().equals(descriereTema))
            .collect(Collectors.toList());
        else {
            listaTeme = listaTeme.stream()
                    .filter(tema -> tema.getDescriere().equals(descriereTema) && tema.getId().equals(idTema))
                    .collect(Collectors.toList());
        }
        if (listaTeme.size() != 0)
            return listaTeme.get(0);
        return null;
    }

    private boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean dataFormat(String input) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate.from(formatter.parse(input));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public void handleEnter(ActionEvent event) {
        String numeStudent = textFieldNumeStudent.getText();
        String raspuns = textFieldMotivare.getText();
        String descriereTema = String.valueOf(comboBoxTema.getValue());
        String data = textFieldDataPredare.getText();
        String profesor = textFieldNumeProfesor.getText();
        String valoare = textFieldValoare.getText();
        String feedback = textAreaFeedback.getText();

        Student student = this.getStudentDetails(numeStudent, null);
        Tema tema = this.getTemaDetails(descriereTema, null);

        if (student == null) {
            ControllerAlert.showErrorMessage(null, "Studentul " + numeStudent + " nu exista!");
            return;
        }
        if (tema == null) {
            ControllerAlert.showErrorMessage(null, "Tema " + descriereTema + " nu exista!");
            return;
        }
        if (!dataFormat(data)) {
            ControllerAlert.showErrorMessage(null, "Formatul datei trebuie sa fie: dd/MM/yyyy");
            return;
        }
        if (!isFloat(valoare)) {
            ControllerAlert.showErrorMessage(null, "Valoarea trebuie sa fie numerica!");
            return;
        }
        if (profesor == null || profesor.equals("")) {
            ControllerAlert.showErrorMessage(null, "Numele profesorul este invalid!");
            return;
        }
        
        NotaDtoView notaDtoView = new NotaDtoView(numeStudent, descriereTema, data, profesor, Float.parseFloat(valoare), feedback);
        Nota notaDetails = this.getNotaDetails(numeStudent, descriereTema);
        if (notaDetails != null)
            notaDtoView.setId(notaDetails.getId());
        else
            notaDtoView.setId(Pair.of(student.getId(), tema.getId()));
        if (nota == null)
            addNota(notaDtoView, raspuns);
        else
            updateNota(notaDtoView);
    }

    private void addNota(NotaDtoView notaDtoView, String raspuns) {
        try {
            Student student = this.getStudentDetails(notaDtoView.getNumeStudent(), notaDtoView.getId().fst);
            Tema tema = this.getTemaDetails(notaDtoView.getDescriereTema(), notaDtoView.getId().snd);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate data = LocalDate.from(formatter.parse(notaDtoView.getDataPredare()));
            Nota notaAdaugata = service.saveNota(student.getId(), tema.getId(), data, notaDtoView.getNumeProfesor(), notaDtoView.getValoare(), notaDtoView.getFeedback(), raspuns);
            Nota notaAdaugataDetails = this.getNotaDetails(student.getNume(), tema.getDescriere());
            String contentText = "Nume Student: " + notaDtoView.getNumeStudent() + "\n";
            if (raspuns.equals("DA"))
                contentText += "Motivare: DA!\n";
            contentText += "Descriete Tema: " + notaDtoView.getDescriereTema() +"\n";
            contentText += "Valoare Nota: " + notaDtoView.getValoare() + "\n";
            if ((notaDtoView.getValoare() - notaAdaugataDetails.getValoare() > 0) && notaAdaugata == null) {
                contentText += "Penalizare: " + (notaDtoView.getValoare() - notaAdaugataDetails.getValoare()) + " puncte\n";
                String feedbackText = "\nNota a fost diminuata cu " + (notaDtoView.getValoare() - notaAdaugataDetails.getValoare()) + " puncte datorita intarzierilor.";
                String feedbackNotaAdaugata = notaAdaugataDetails.getFeedback();
                feedbackNotaAdaugata += feedbackText;
                service.updateNota(notaAdaugataDetails.getId().fst, notaAdaugataDetails.getId().snd, notaAdaugataDetails.getData(), notaAdaugataDetails.getProfesor(),
                        notaAdaugataDetails.getValoare(), feedbackNotaAdaugata);
                textAreaFeedback.appendText(feedbackText);
            }
            if (notaAdaugataDetails.getData().isBefore(LocalDate.now()))
                contentText += "\n OBSERVATIE: Profesorul nu a asignat notele la timp.\n";
            if (notaAdaugata == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText);
                alert.setTitle("Adaugare Nota");
                alert.setHeaderText("Detalii Nota");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                    if (notaAdaugata == null) {
                        dialogStage.close();
                    }
                else
                    service.deleteNota(notaAdaugataDetails.getId());
            }
            else
                ControllerAlert.showErrorMessage(null, "Deja a fost atribuita o nota studentului " + notaDtoView.getNumeStudent() + " la tema " +
                        notaDtoView.getDescriereTema());
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void updateNota(NotaDtoView notaDtoView) {
        try {
            Student student = this.getStudentDetails(notaDtoView.getNumeStudent(), notaDtoView.getId().fst);
            Tema tema = this.getTemaDetails(notaDtoView.getDescriereTema(), notaDtoView.getId().snd);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate data = LocalDate.from(formatter.parse(notaDtoView.getDataPredare()));
            Nota nota = service.updateNota(student.getId(), tema.getId(), data, notaDtoView.getNumeProfesor(), notaDtoView.getValoare(), notaDtoView.getFeedback());
            if (nota != null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare Nota", "Nota a fost modificata!");
                dialogStage.close();
            }
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleCancel(ActionEvent event) {
        dialogStage.close();
    }
}
