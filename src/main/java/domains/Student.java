package domains;

/**
 *
 */
public class Student extends Entity<String> {
    private String nume;
    private String prenume;
    private String email;
    private String cadruDidacticLab;
    private int grupa;
    private int nr_motivari;

    public Student() {
    }

    public Student(String nume, String prenume, String email, String cadruDidacticLab, int grupa) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.cadruDidacticLab = cadruDidacticLab;
        this.grupa = grupa;
    }

    public int getNr_motivari() {
        return nr_motivari;
    }

    public void setNr_motivari(int nr_motivari) {
        this.nr_motivari = nr_motivari;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCadruDidacticLab() {
        return cadruDidacticLab;
    }

    public void setCadruDidacticLab(String cadruDidacticLab) {
        this.cadruDidacticLab = cadruDidacticLab;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    @Override
    public String toString() {
        return getId() + "; " + nume + "; " + prenume + "; " + email + "; " + cadruDidacticLab + "; " + grupa + "; " + nr_motivari;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Student student = (Student) obj;
        if (!this.nume.equals(student.getNume())) return false;
        if (!this.prenume.equals(student.getPrenume())) return false;
        if (!this.email.equals(student.getEmail())) return false;
        if (this.grupa != student.getGrupa()) return false;
        if (this.nr_motivari != student.getNr_motivari()) return false;
        return cadruDidacticLab.equals(student.getCadruDidacticLab());
    }
}
