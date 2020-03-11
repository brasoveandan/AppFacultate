package domains;

import com.sun.tools.javac.util.Pair;

public class NotaDtoView extends Entity<Pair<String, String>>{
    private String numeStudent;
    private String descriereTema;
    private String dataPredare;
    private String numeProfesor;
    private float valoare;
    private String feedback;

    public NotaDtoView(String numeStudent, String descriereTema, String dataPredare, String numeProfesor, float valoare, String feedback) {
        this.numeStudent = numeStudent;
        this.descriereTema = descriereTema;
        this.dataPredare = dataPredare;
        this.numeProfesor = numeProfesor;
        this.valoare = valoare;
        this.feedback = feedback;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public String getDescriereTema() {
        return descriereTema;
    }

    public String getDataPredare() {
        return dataPredare;
    }

    public String getNumeProfesor() {
        return numeProfesor;
    }

    public float getValoare() {
        return valoare;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setValoare(float valoare) {
        this.valoare = valoare;
    }
}
