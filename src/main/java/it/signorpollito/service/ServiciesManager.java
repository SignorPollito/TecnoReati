package it.signorpollito.service;

import it.signorpollito.commands.CommandService;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.repository.CrimeRepository;
import lombok.Getter;

public class ServiciesManager {
    private static ServiciesManager instance;

    public static ServiciesManager getInstance() {
        if(instance==null) instance = new ServiciesManager();
        return instance;
    }


    @Getter private final CrimeRepository crimeRepository;
    @Getter private final CommandService commandService;
    @Getter private final CommandHistory commandHistory;

    public ServiciesManager() {
        this.crimeRepository = new CrimeRepository();
        this.commandService = new CommandService();
        this.commandHistory = new CommandHistory();
    }

}
