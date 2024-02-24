package it.signorpollito.startup;

import it.signorpollito.commands.CommandService;
import it.signorpollito.commands.impl.CrimeCommand;
import it.signorpollito.commands.impl.HistoryCommand;
import it.signorpollito.commands.impl.PreventiveCommand;
import it.signorpollito.commands.impl.WebCommand;
import it.signorpollito.crime.Crime;
import it.signorpollito.crime.injectors.*;
import it.signorpollito.reader.CSVReader;
import it.signorpollito.repository.CrimeRepository;
import it.signorpollito.service.ServiciesManager;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Collection;

public class Startup {

    private static boolean STARTED = false;
    private static final String DEFAULT_FOLDER = "Reati";

    /**
     * TODO
     *
     * Aggiungere check per Flagranza di Reato (TRUE/FALSE)
     * Se FdR è TRUE allora vuol dire che si può arrestare direttamente.
     * Se c'è anche solo un reato FALSE allora mandare avviso nel programma.
     *
     */

    public static void start() {
        if(STARTED) throw new IllegalStateException("The program has already started!");

        STARTED = true;
        new Startup();
    }


    private final CrimeRepository crimeRepository;

    private Startup() {
        this.crimeRepository = ServiciesManager.getInstance().getCrimeRepository();

        startApplication(Arrays.asList(
                new Code("Codice Civile", "CC", "https://docs.google.com/spreadsheets/d/1nGH7mVfH-a-i8h5E91X6cib0zZLMH6BDOHbK69VJdpE/export?gid=1782618121&format=csv"),
                new Code("Codice Stradale", "CS", "https://docs.google.com/spreadsheets/d/1nGH7mVfH-a-i8h5E91X6cib0zZLMH6BDOHbK69VJdpE/export?gid=369345429&format=csv"),
                new Code("Codice Penale", "CP", "https://docs.google.com/spreadsheets/d/1nGH7mVfH-a-i8h5E91X6cib0zZLMH6BDOHbK69VJdpE/export?gid=0&format=csv")
        ));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    private void startApplication(Collection<Code> codes) {
        System.out.println("Aggiornando la Lista Reati...");

        File crimeFolder = new File(DEFAULT_FOLDER);
        if(!crimeFolder.exists()) crimeFolder.mkdirs();

        deleteOldFiles(DEFAULT_FOLDER);
        updateCrimeList(codes);

        System.out.println("Registrando i reati nell'applicazione...");

        codes.forEach(code -> loadCrimesFromFile(new File(code.getPath()), code.getAcronym()));

        registerHardcodedCrimes();
        injectCustomizations();
        registerAliases();

        registerCommands();
    }

    private void registerCommands() {
        CommandService commandService = ServiciesManager.getInstance().getCommandService();
        commandService.registerCommand(new CrimeCommand());
        commandService.registerCommand(new PreventiveCommand());
        commandService.registerCommand(new HistoryCommand());
        commandService.registerCommand(new WebCommand());
    }

    private File[] getFilesInFolder(File folder) {
        if(!folder.isDirectory()) return new File[0];

        File[] contents = folder.listFiles();
        return contents==null ? new File[0] : contents;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteOldFiles(String path) {
        File file = new File(path);
        if(!file.exists()) return;

        if(file.isFile()) {
            file.delete();
            return;
        }

        for(var content : getFilesInFolder(file))
            content.delete();
    }

    private void updateCrimeList(Collection<Code> codes) {
        codes.forEach(code-> {
            try(FileOutputStream fileOutputStream = new FileOutputStream(code.getPath())) {
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(code.getUrl()).openStream());

                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void loadCrimesFromFile(File file, String code) {
        if(!file.exists() || !file.isFile()) {
            System.out.printf("File reati non trovato, assicurati che si chiami \"%s\"\n", file.getName());
            return;
        }

        new CSVReader(file).getCrimes(code).forEach(crimeRepository::registerCrime);
    }

    private void registerHardcodedCrimes() {
        //REMOVING
        crimeRepository.removeCrime("stupefacenti o psicotrope");
        crimeRepository.removeCrime("Evasione", true);
        crimeRepository.removeCrime("Evasione (si costituisce)", true);

        //ADDING
        crimeRepository.registerCrime(new Crime("Possesso di stupefacenti", "Art. 150", "CP", 0, 0, true));
        //crimeRepository.registerCrime(new Crime("Evasione", "Art. 93", "CP", 0, 0, true));
    }

    private void registerAliases() {
        setAlias("Obblighi verso funzionari, ufficiali e agenti",
                "Mancato fermo al controllo/blocco stradale Proseguimento della guida senza documenti", false
        );

        setAlias("Detenzione abusiva di armi", "arma coltello pistola", false);
        setAlias("Omessa consegna di armi", "arma coltello pistola", false);
        setAlias("Mancato pagamento di multe", "multa sanzione", false);
        setAlias("Possesso di stupefacenti", "droga droghe stupefacente traffico", false);
        setAlias("Inosservanza dei provvedimenti", "cinturamento", false);
        setAlias("Introduzione clandestina in luoghi militari", "zona militare", false);
        setAlias("Immondizia nelle Strade", "inquinamento", false);
        setAlias("Parcheggio in luoghi inopportuni", "luogo non opportuno inopportuno", false);
        setAlias("Disturbo del Lavoro", "chiamate futili", false);
        setAlias("Procurata inosservanza di pena", "complicità in fuga", false);
    }

    private void inject(String crimeName, Class<? extends Injector> injector, boolean fullMatch) {
        crimeRepository.editCrime(crimeName, crime -> crime.setInjectorClass(injector), fullMatch);
    }

    private void setAlias(String crimeName, String alias, boolean fullMatch) {
        crimeRepository.editCrime(crimeName, crime -> crime.setAlias(alias), fullMatch);
    }

    private void injectCustomizations() {
        inject("Mancato pagamento di multe", NotPaidChargeInjector.class, false);

        inject("Possesso di stupefacenti", DrugInjector.class, false);

        inject("Guida senza Documenti", DocumentsInjector.class, false);
        inject("Possesso di munizioni", AmmoInjector.class, false);

        //inject("Evasione", EvasionInjector.class, true);

        //inject("Atti osceni", SexInjector.class, false);
        inject("Frode nell'esercizio del commercio", ScamInjector.class, false);

        inject("Evasione fiscale per mancato scontrino", TaxEvasionNoReceiptInjector.class, false);
        inject("Evasione fiscale per scontrino con importo minore", TaxEvasionSmallerAmountInjector.class, true);

        inject("Mancata cassetta postale", ArticleInjector.class, false);
        inject("Mancata registrazione di un lotto", ArticleInjector.class, false);
        inject("Regola del Buon Vicinato", ArticleInjector.class, false);
    }


    @Getter
    private static class Code {

        private final String name;
        private final String acronym;
        private final String path;
        private final String url;

        public Code(String name, String acronym, String url) {
            this.name = name;
            this.acronym = acronym;
            this.url = url;
            this.path = "%s/ListaReati - %s.csv".formatted(DEFAULT_FOLDER, name);
        }
    }
}
