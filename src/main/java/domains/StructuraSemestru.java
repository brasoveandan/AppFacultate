package domains;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static utils.Constants.DATE_FORMAT;

public class StructuraSemestru extends Entity<String> {
    String fileName;
    Integer anUniversitar;
    Integer semestru;
    private LocalDate startSemester;
    private LocalDate beginHolyday;
    private LocalDate endHolyday;
    private LocalDate endSemester;
    private DateTimeFormatter formatter;

    public StructuraSemestru(String fileName, int semestru) {
        this.fileName = fileName;
        this.anUniversitar = Calendar.getInstance().get(Calendar.YEAR);
        this.semestru = semestru;
        formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        loadData();
    }

    public void loadData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            switch (semestru) {
                case 1:
                    line = bufferedReader.readLine();
                    break;
                case 2:
                    bufferedReader.readLine();
                    line = bufferedReader.readLine();
                    break;
                default:
                    line = "";
            }
            String[] fields = line.split("; ");
            startSemester = LocalDate.from(formatter.parse(fields[0]));
            beginHolyday = LocalDate.from(formatter.parse(fields[1]));
            endHolyday = LocalDate.from(formatter.parse(fields[2]));
            endSemester = LocalDate.from(formatter.parse(fields[3]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getAnUniversitar() {
        return anUniversitar;
    }

    public void setAnUniversitar(Integer anUniversitar) {
        this.anUniversitar = anUniversitar;
    }

    public Integer getSemestru() {
        return semestru;
    }

    public void setSemestru(Integer semestru) {
        this.semestru = semestru;
    }

    public LocalDate getStartSemester() {
        return startSemester;
    }

    public void setStartSemester(LocalDate startSemester) {
        this.startSemester = startSemester;
    }

    public LocalDate getBeginHolyday() {
        return beginHolyday;
    }

    public void setBeginHolyday(LocalDate beginHolyday) {
        this.beginHolyday = beginHolyday;
    }

    public LocalDate getEndHolyday() {
        return endHolyday;
    }

    public void setEndHolyday(LocalDate endHolyday) {
        this.endHolyday = endHolyday;
    }

    public LocalDate getEndSemester() {
        return endSemester;
    }

    public void setEndSemester(LocalDate endSemester) {
        this.endSemester = endSemester;
    }
}
