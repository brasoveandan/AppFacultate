package services;

import com.sun.tools.javac.util.Pair;
import domains.*;
import repository.inMemoryRepository.CrudRepository;
import repository.xmlFileRepository.XMLNotaFileRepository;
import utils.events.ChangeEventType;
import utils.events.EntityChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service implements Observable<EntityChangeEvent> {
    private static int currentWeek;
    private CrudRepository<String, Student> studentRepository;
    private CrudRepository<String, Tema> temaRepository;
    private CrudRepository<Pair<String, String>, Nota> notaRepository;

    public Service(CrudRepository<String, Student> studentFileRepository, CrudRepository<String, Tema> temaFileRepository, CrudRepository<Pair<String, String>, Nota> notaFileRepository) {
        this.studentRepository = studentFileRepository;
        this.temaRepository = temaFileRepository;
        this.notaRepository = notaFileRepository;
    }

    public static void setCurrentWeek(int currentWeek) {
        Service.currentWeek = currentWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public Student findOneStudent(String id) {
        return studentRepository.findOne(id);
    }

    public Iterable<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    public Student saveStudent(String id, String nume, String prenume, String email, String cadruDidacticLab, int grupa) {
        Student st = new Student(nume, prenume, email, cadruDidacticLab, grupa);
        st.setId(id);
        st.setNr_motivari(2);
        Student student = studentRepository.save(st);
        if (student == null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, student));
        return student;
    }

    public Student deleteStudent(String id) {
        Student student = studentRepository.delete(id);
        if (student != null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, student));
        return student;
    }

    public Student updateStudent(String id, String nume, String prenume, String email, String cadruDidacticLab, int grupa) {
        Student st = new Student(nume, prenume, email, cadruDidacticLab, grupa);
        st.setId(id);
        Student oldStudent = studentRepository.findOne(st.getId());
        if (oldStudent != null) {
            Student newStudent = studentRepository.update(st);
            notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, newStudent, oldStudent));
        }
        return oldStudent;
    }

    public Tema findOneTema(String id) {
        return temaRepository.findOne(id);
    }

    public Iterable<Tema> findAllTema() {
        return temaRepository.findAll();
    }

    public Tema saveTema(String id, String descriere, int startWeek, int deadLineWeek) {
        Tema tema = new Tema(descriere, startWeek, deadLineWeek);
        tema.setId(id);
        Tema save = temaRepository.save(tema);
        if (save == null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, save));
        return save;
    }

    public Tema deleteTema(String id) {
        Tema delete = temaRepository.delete(id);
        if (delete != null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, delete));
        return delete;
    }

    public Tema updateTema(String id, String descriere, int startWeek, int deadLineWeek) {
        Tema tema = new Tema(descriere, startWeek, deadLineWeek);
        tema.setId(id);
        Tema oldTema = temaRepository.findOne(tema.getId());
        if (oldTema != null) {
            Tema newTema = temaRepository.update(tema);
            notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, newTema, oldTema));
        }
        return oldTema;
    }

    public Nota findOneNota(Pair<String, String> id) {
        return notaRepository.findOne(id);
    }

    public Iterable<Nota> findAllNota() {
        return notaRepository.findAll();
    }

    public Nota saveNota(String idStudent, String idTema, LocalDate data, String profesor, float valoare, String feedback, String raspuns) throws IllegalAccessException, InstantiationException {
        Student student = studentRepository.findOne(idStudent);
        Tema tema = temaRepository.findOne(idTema);
        if (student == null || tema == null)
            throw new ServiceException("Nota nu se poate asigna, id invalid.");

        Student student1 = new Student(student.getNume(), student.getPrenume(), student.getEmail(), student.getCadruDidacticLab(), student.getGrupa());
        student1.setNr_motivari(student.getNr_motivari());
        student1.setId(student.getId());

        if (((currentWeek - tema.getDeadlineWeek() == 1 || currentWeek - tema.getDeadlineWeek() == 2) && raspuns.equals("NU")) ||
                (currentWeek - tema.getDeadlineWeek() >= 1 && raspuns.equals("DA") && student1.getNr_motivari() == 0))
            valoare -= currentWeek - tema.getDeadlineWeek();

        if (currentWeek - tema.getDeadlineWeek() >= 3)
            valoare = 1f;

        if (currentWeek - tema.getDeadlineWeek() == 1 && raspuns.equals("DA") && student1.getNr_motivari() >= 1) {
            int nr_motivari = student1.getNr_motivari();
            nr_motivari--;
            student1.setNr_motivari(nr_motivari);
        }

        if (currentWeek - tema.getDeadlineWeek() == 2 && raspuns.equals("DA") && student1.getNr_motivari() == 2)
            student1.setNr_motivari(0);

        if (currentWeek - tema.getDeadlineWeek() == 2 && raspuns.equals("DA") && student1.getNr_motivari() == 1) {
            valoare -= 1;
            student1.setNr_motivari(0);
        }

        studentRepository.update(student1);
        Nota nota = new Nota(data, profesor, valoare, feedback);
        nota.setId(new Pair<>(idStudent, idTema));
        int nrSaptamana = StructuraAnUniversitar.getInstance().getWeek(nota.getData(), StructuraAnUniversitar.getInstance().getSemestru(nota.getData()));
        NotaDTO notaDTO = new NotaDTO(student.getNume(), tema.getId(), nota.getValoare(), nrSaptamana, tema.getDeadlineWeek(), nota.getFeedback());
        XMLNotaFileRepository.getInstance().saveToTextFile(notaDTO);
        nota = notaRepository.save(nota);
        if (nota == null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, nota));
        return nota;
    }

    public Nota deleteNota(Pair<String, String> id) {
        Nota nota = notaRepository.delete(id);
        if (nota != null)
            notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, nota));
        return nota;
    }

    public Nota updateNota(String idStudent, String idTema, LocalDate data, String profesor, float valoare, String feedback) {
        Nota nota = new Nota(data, profesor, valoare, feedback);
        nota.setId(new Pair<>(idStudent, idTema));
        Nota oldNota = notaRepository.findOne(nota.getId());
        if (oldNota != null) {
            Nota newNota = notaRepository.update(nota);
            notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, newNota, oldNota));
        }
        return oldNota;
    }

    public <E> List<E> genericFilter(List<E> lista, Predicate<E> p) {
        return lista.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public List<Student> filtrareStudentGrupa(int grupa) {
        List<Student> listaStudenti = new ArrayList<Student>((Collection<? extends Student>) findAllStudent());
        Predicate<Student> p1 = st -> st.getGrupa() == grupa;
        List<Student> rezultat = genericFilter(listaStudenti, p1);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    public List<FitrareDTO> cautaStudent(List<Nota> lista) {
        List<FitrareDTO> rezultat = new ArrayList<>();
        lista.forEach(nota -> {
            Student student = studentRepository.findOne(nota.getId().fst);
            Tema tema = temaRepository.findOne(nota.getId().snd);
            LocalDate data = nota.getData();
            int nrSaptamana = StructuraAnUniversitar.getInstance().getWeek(data, StructuraAnUniversitar.getInstance().getSemestru(data));
            FitrareDTO entity = new FitrareDTO(student.getNume(), student.getPrenume(), nota.getProfesor(), tema.getDescriere(), nrSaptamana);
            rezultat.add(entity);
        });
        return rezultat;
    }

    public List<FitrareDTO> filtrareNotaStudentTema(String idTema) {
        List<Nota> catalog = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota -> nota.getId().snd.equals(idTema);
        List<Nota> lista = genericFilter(catalog, p1);
        List<FitrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    public List<FitrareDTO> filtrareNotaProfesor(String idTema, String profesor) {
        List<Nota> catalog = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota -> nota.getId().snd.equals(idTema) && nota.getProfesor().equals(profesor);
        List<Nota> lista = genericFilter(catalog, p1);
        List<FitrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }


    public List<FitrareDTO> filtrareNotaSaptamnaData(String idTema, int nrSaptamana) {
        List<Nota> catalog = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota -> nota.getId().snd.equals(idTema) && StructuraAnUniversitar.getInstance()
                .getWeek(nota.getData(), StructuraAnUniversitar.getInstance().getSemestru(nota.getData())) == nrSaptamana;
        List<Nota> lista = genericFilter(catalog, p1);
        List<FitrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    private List<Observer<EntityChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent t) {
        observers.stream()
                .forEach(x -> x.update(t));
    }
}
