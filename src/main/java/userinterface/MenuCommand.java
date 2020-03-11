package userinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MenuCommand implements Command {
    private String nameMenu;
    private Map<String, Command> mapMenu = new TreeMap<>();

    public MenuCommand(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    @Override
    public void execute() {
        mapMenu.keySet().forEach(System.out::println);
    }

    public void addCommand(String descriere_comanda, Command comanda) {
        mapMenu.put(descriere_comanda, comanda);
    }

    public List<Command> getCommands() {
        return new ArrayList<>(mapMenu.values());
    }

    public String getNameMenu() {
        return nameMenu;
    }
}
