package domains;

public class NotaDTO {
    private String numeStudent;
    private String idTema;
    private float valoare;
    private int nrSaptamana;
    private int deadlineWeek;
    private String feedback;

    public NotaDTO(String numeStudent, String idTema, float valoare, int nrSaptamana, int deadlineWeek, String feedback) {
        this.numeStudent = numeStudent;
        this.idTema = idTema;
        this.valoare = valoare;
        this.nrSaptamana = nrSaptamana;
        this.deadlineWeek = deadlineWeek;
        this.feedback = feedback;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public String getIdTema() {
        return idTema;
    }

    public void setIdTema(String idTema) {
        this.idTema = idTema;
    }

    public float getValoare() {
        return valoare;
    }

    public void setValoare(float valoare) {
        this.valoare = valoare;
    }

    public int getNrSaptamana() {
        return nrSaptamana;
    }

    public void setNrSaptamana(int data) {
        this.nrSaptamana = data;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
