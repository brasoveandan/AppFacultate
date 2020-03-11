package userinterface;

import com.sun.tools.javac.util.Pair;
import domains.FitrareDTO;
import domains.Nota;
import domains.Student;
import domains.Tema;
import domains.validators.ValidationException;
import repository.RepositoryException;
import services.Service;
import services.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Consola {
    private static int currentWeek;
    private MenuCommand mainMenu;
    private Service service;

    public Consola(Service service) {
        this.service = service;
        Service.setCurrentWeek(currentWeek);
    }

    public static void setCurrentWeek(int currentWeek) {
        Consola.currentWeek = currentWeek;
    }

    public void createMenu() {
        System.out.println("Saptamana curenta: " + currentWeek);
        mainMenu = new MenuCommand("------ MeniuPrincipal ------");
        MenuCommand crudStudent = new MenuCommand("CRUD Student: ");
        crudStudent.addCommand("1. Adauga un student nou", new AddStudentCommand());
        crudStudent.addCommand("2. Sterge un student", new DeleteStudentCommand());
        crudStudent.addCommand("3. Gaseste un student", new FindStudentCommand());
        crudStudent.addCommand("4. Modifica un student", new UpdateStudentCommand());
        crudStudent.addCommand("5. Afisare lista studenti", new tiparireStudenti());
        crudStudent.addCommand("6. --> MeniuPrincipal", mainMenu);

        MenuCommand crudTema = new MenuCommand("CRUD Tema: ");
        crudTema.addCommand("1. Adauga o noua tema", new AddTemaCommand());
        crudTema.addCommand("2. Sterge o tema", new DeleteTemaCommand());
        crudTema.addCommand("3. Gaseste o tema", new FindTemaCommand());
        crudTema.addCommand("4. Modifica o tema", new UpdateTemaCommand());
        crudTema.addCommand("5. Afisare lista teme", new tiparireTeme());
        crudTema.addCommand("6. --> MeniuPrincipal", mainMenu);

        MenuCommand crudNota = new MenuCommand("CRUD Nota: ");
        crudNota.addCommand("1. Adauga o nota", new AddNotaCommand());
        crudNota.addCommand("2. Sterge o nota", new DeleteNotaCommand());
        crudNota.addCommand("3. Gaseste o nota", new FindNotaCommand());
        crudNota.addCommand("4. Modifica o nota", new UpdateNotaCommand());
        crudNota.addCommand("5. Afisare lista note", new tiparireNote());
        crudNota.addCommand("6. --> MeniuPrincipal", mainMenu);

        MenuCommand meniuFiltrare = new MenuCommand("Meniu Filtrare:");
        meniuFiltrare.addCommand("1. Toti studentii unei grupe", new filtrareStudentGrupa());
        meniuFiltrare.addCommand("2. Toti studentii care au predat o anumita tema", new filtrareStudentTema());
        meniuFiltrare.addCommand("3. Toti studentii care au predat o anumita tema unui profesor anume", new filtrareStudentTemaProfesor());
        meniuFiltrare.addCommand("4. Toate notele la o anumita tema, dintr-o saptamana data", new filtrareNotaSaptamana());
        meniuFiltrare.addCommand("5. --> MeniuPrincipal", mainMenu);

        mainMenu.addCommand("1. CRUD Student", crudStudent);
        mainMenu.addCommand("2. CRUD Tema", crudTema);
        mainMenu.addCommand("3. CRUD Nota", crudNota);
        mainMenu.addCommand("4. Rapoarte", meniuFiltrare);
        mainMenu.addCommand("5. Exit", new ExitCommand());
    }

    public void runMenu() {
        Scanner scanner = new Scanner(System.in);
        createMenu();
        MenuCommand currentMenu = mainMenu;
        while (true) {
            System.out.println(currentMenu.getNameMenu());
            System.out.println("-----------------------");
            currentMenu.execute();
            System.out.println("Introdu o comanda: ");
            int actionNumber = scanner.nextInt();
            if (actionNumber > 0 && actionNumber <= currentMenu.getCommands().size()) {
                Command selectedCommand = currentMenu.getCommands().get(actionNumber - 1);
                if (selectedCommand instanceof MenuCommand)
                    currentMenu = (MenuCommand) selectedCommand;
                else selectedCommand.execute();
            } else System.out.println("Comanda nu este valida! Reincercati...");
        }
    }

    private class ExitCommand implements Command {
        @Override
        public void execute() {
            System.out.println("Bye! See you soon...");
            System.exit(0);
        }
    }

    private class tiparireStudenti implements Command {
        @Override
        public void execute() {
            service.findAllStudent().forEach(System.out::println);
        }
    }

    private class tiparireTeme implements Command {
        @Override
        public void execute() {
            service.findAllTema().forEach(System.out::println);
        }
    }

    private class tiparireNote implements Command {
        @Override
        public void execute() {
            service.findAllNota().forEach(System.out::println);
        }
    }

    public class AddStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Nume: ");
            String nume = scanner.nextLine();
            System.out.println("Prenume: ");
            String prenume = scanner.nextLine();
            System.out.println("E-mail: ");
            String email = scanner.nextLine();
            System.out.println("Cadru didactic laborator: ");
            String cadruDidacticLab = scanner.nextLine();
            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();
            scanner.nextLine();
            try {
                Student st = service.saveStudent(id, nume, prenume, email, cadruDidacticLab, grupa);
                if (st == null)
                    System.out.println("Studentul " + nume + " " + prenume + " a fost adaugat cu succes!");
                else
                    System.out.println("Studentul " + st.getNume() + " " + st.getPrenume() + " exista deja!");
            } catch (IllegalArgumentException | ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class DeleteStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();

            try {
                Student st = service.deleteStudent(id);
                if (st.getId() == id)
                    System.out.println("Studentul " + st.getNume() + " " + st.getPrenume() + " a fost sters cu succes!");
            } catch (IllegalArgumentException | RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class FindStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                Student student = service.findOneStudent(id);
                System.out.println("Studentul gasit: " + student.toString());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class UpdateStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Nume: ");
            String nume = scanner.nextLine();
            System.out.println("Prenume: ");
            String prenume = scanner.nextLine();
            System.out.println("E-mail: ");
            String email = scanner.nextLine();
            System.out.println("Cadru didactic laborator: ");
            String cadruDidacticLab = scanner.nextLine();
            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();
            scanner.nextLine();
            try {
                Student st = service.updateStudent(id, nume, prenume, email, cadruDidacticLab, grupa);
                if (st == null)
                    System.out.println("Studentul " + nume + " " + prenume + " este deja modificat!");
                else
                    System.out.println("Studentul " + st.getNume() + " " + st.getPrenume() + " a fost modificat!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class AddTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            System.out.println("DeadlineWeek: ");
            int deadLineWeek = scanner.nextInt();
            scanner.nextLine();
            try {
                Tema tema = service.saveTema(id, descriere, currentWeek, deadLineWeek);
                if (tema == null)
                    System.out.println("Tema cu id-ul " + id + " a fost adaugata cu succes!");
                else
                    System.out.println("Tema " + tema.getId() + " exista deja!");
            } catch (IllegalArgumentException | ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class DeleteTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID:");
            String id = scanner.nextLine();
            try {
                Tema tema = service.deleteTema(id);
                if (tema.getId() == id)
                    System.out.println("Tema cu id-ul " + id + " a fost stearsa cu succes!");
            } catch (IllegalArgumentException | RepositoryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class FindTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                Tema tema = service.findOneTema(id);
                System.out.println("Tema gasita: " + tema.toString());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class UpdateTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID:");
            String id = scanner.nextLine();
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            System.out.println("DeadlineWeek: ");
            int deadLineWeek = scanner.nextInt();
            scanner.nextLine();
            try {
                Tema old_tema = service.findOneTema(id);
                if (old_tema.getDeadlineWeek() > currentWeek) {
                    Tema tema = service.updateTema(id, descriere, old_tema.getStartWeek(), deadLineWeek);
                    if (tema == null)
                        System.out.println("Tema cu id-ul " + id + " a fost modificata deja!");
                    else
                        System.out.println("Tema " + tema.getId() + " a fost modificata cu succes!");
                }
                System.out.println("Tema nu poate fii modificata, deadlineWeek <= currentWeek");
            } catch (RepositoryException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class AddNotaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            String idStudent = scanner.nextLine();
            System.out.println("Motivat: DA/NU");
            String raspuns = scanner.nextLine();
            System.out.println("ID Tema: ");
            String idTema = scanner.nextLine();
            System.out.println("Introduceti data: ");
            System.out.println("An: ");
            int an = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Luna: ");
            int luna = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Zi: ");
            int zi = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Profesor: ");
            String profesor = scanner.nextLine();
            System.out.println("Valoare: ");
            float valoare = scanner.nextFloat();
            scanner.nextLine();
            System.out.println("Feedback: ");
            String feedback = scanner.nextLine();
            try {
                LocalDate data = LocalDate.of(an, luna, zi);
                Tema tema = service.findOneTema(idTema);
                if (currentWeek - tema.getDeadlineWeek() == 1)
                    System.out.println("Nota maxima 9!");
                if (currentWeek - tema.getDeadlineWeek() == 2)
                    System.out.println("Nota maxima 8!");
                if (data.isBefore(LocalDate.now()))
                    System.out.println("Profesorul nu a asignat notele la timp.");
                Nota notare = service.saveNota(idStudent, idTema, data, profesor, valoare, feedback, raspuns);
                if (notare == null)
                    System.out.println("Nota a fost adaugata cu succes!");
                else
                    System.out.println("Nota exista deja.");
            } catch (ValidationException | IllegalArgumentException | ServiceException e) {
                System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteNotaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            try {
                service.deleteNota(new Pair<>(idStudent, idTema));
            } catch (IllegalArgumentException | RepositoryException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private class FindNotaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            try {
                service.findOneNota(new Pair<>(idStudent, idTema));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class UpdateNotaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            System.out.println("Feedback:");
            String feedback = scanner.nextLine();
            System.out.println("Valoare: ");
            float valoare = scanner.nextFloat();
            scanner.nextLine();
            try {
                Nota nota = service.findOneNota(new Pair<>(idStudent, idTema));
                service.updateNota(idStudent, idTema, nota.getData(), nota.getProfesor(), valoare, feedback);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class filtrareStudentGrupa implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();
            scanner.nextLine();
            try {
                List<Student> lista = service.filtrareStudentGrupa(grupa);
                System.out.println("Toti studentii unei grupe: ");
                System.out.println("Nume ---- Prenume ---- Grupa");
                lista.forEach(student -> System.out.println(student.getNume() + " " + student.getPrenume() + " " + student.getGrupa()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class filtrareStudentTema implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("idTema: ");
            String idTema = scanner.nextLine();
            try {
                List<FitrareDTO> lista = service.filtrareNotaStudentTema(idTema);
                System.out.println("Toti studentii care au predat o anumita tema: ");
                System.out.println("Nume ---- Prenume ---- DescriereTema");
                lista.forEach(entity -> System.out.println(entity.getNumeStudent() + " " + entity.getPrenumeStudent() + " " + entity.getDescriereTema()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class filtrareStudentTemaProfesor implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("idTema: ");
            String idTema = scanner.nextLine();
            System.out.println("Profesor: ");
            String profesor = scanner.nextLine();
            try {
                List<FitrareDTO> lista = service.filtrareNotaProfesor(idTema, profesor);
                System.out.println("Toti studentii care au predat o anumita tema unui profesor anume: ");
                System.out.println("Nume ---- Prenume---- DescriereTema---- Profesor");
                lista.forEach(entity -> System.out.println(entity.getNumeStudent() + " " + entity.getPrenumeStudent() + " " + entity.getDescriereTema()
                        + " " + entity.getProfesor()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class filtrareNotaSaptamana implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("idTema: ");
            String idTema = scanner.nextLine();
            System.out.println("Numar saptamana: ");
            int nrSaptamana = scanner.nextInt();
            scanner.nextLine();
            try {
                List<FitrareDTO> lista = service.filtrareNotaSaptamnaData(idTema, nrSaptamana);
                System.out.println("Toate notele la o anumita tema, dintr-o saptamana data: ");
                System.out.println("DescriereTema ---- NumarSaptamana");
                lista.forEach(entity -> System.out.println(entity.getDescriereTema() + " " + entity.getNrSaptamana()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
