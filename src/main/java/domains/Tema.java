package domains;

import java.util.Objects;

public class Tema extends Entity<String> {
    private String descriere;
    private int startWeek;
    private int deadlineWeek;

    public Tema() {
    }

    public Tema(String descriere, int startWeek, int deadlineWeek) {
        this.descriere = descriere;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    @Override
    public String toString() {
        return getId() + "; " + descriere + "; " + startWeek + "; " + deadlineWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tema tema = (Tema) o;
        return startWeek == tema.startWeek &&
                deadlineWeek == tema.deadlineWeek &&
                Objects.equals(descriere, tema.descriere);
    }
}
