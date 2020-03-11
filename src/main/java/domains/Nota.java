package domains;


import com.sun.tools.javac.util.Pair;

import java.time.LocalDate;

public class Nota extends Entity<Pair<String, String>> {
    private LocalDate data;
    private String profesor;
    private float valoare;
    private String feedback;


    public Nota(LocalDate data, String profesor, float valoare, String feedback) {
        this.data = data;
        this.profesor = profesor;
        this.valoare = valoare;
        this.feedback = feedback;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public float getValoare() {
        return valoare;
    }

    public void setValoare(float valoare) {
        this.valoare = valoare;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        if (getFeedback() != null)
            return getId().fst + "; " + getId().snd + "; " + getData().getDayOfMonth() + "/" + getData().getMonthValue() + "/" + getData().getYear()
                + "; " + getProfesor() + "; " + getValoare() + "; " + getFeedback();
        return getId().fst + "; " + getId().snd + "; " + getData().getDayOfMonth() + "/" + getData().getMonthValue() + "/" + getData().getYear()
                + "; " + getProfesor() + "; " + getValoare() + "; ";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Nota nota = (Nota) obj;
        if (!this.data.equals(nota.getData())) return false;
        if (this.valoare != nota.getValoare()) return false;
        if (!this.feedback.equals(nota.getFeedback())) return false;
        return this.profesor.equals(nota.getProfesor());
    }

    public String getDataString() {
        if (data.getDayOfMonth() >= 1 && data.getDayOfMonth() <= 9)
            return "0" + data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
        return data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
    }
}
