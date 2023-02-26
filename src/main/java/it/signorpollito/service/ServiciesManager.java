package it.signorpollito.service;

import it.signorpollito.commands.CommandService;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.repository.CrimeRepository;
import it.signorpollito.site.SiteRepository;
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
    @Getter private final SiteRepository siteRepository;

    public ServiciesManager() {
        this.crimeRepository = new CrimeRepository();
        this.commandService = new CommandService();
        this.commandHistory = new CommandHistory();
        this.siteRepository = new SiteRepository();
    }

}
