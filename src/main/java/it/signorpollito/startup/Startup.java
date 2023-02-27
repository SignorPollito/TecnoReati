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
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

public class Startup {

    private static boolean STARTED = false;
    private static final String DEFAULT_FOLDER = "Reati";

    public static void start() {
        if(STARTED) throw new IllegalStateException("The program has already started!");

        STARTED = true;
        new Startup();
    }


    private final CrimeRepository crimeRepository;

    private Startup() {
        this.crimeRepository = ServiciesManager.getInstance().getCrimeRepository();

        startApplication(Map.of(
                "Codice Civile", "https://docs.google.com/spreadsheets/d/1uzmU1XEv6RXfNO7O4Ht16Wb8Rdf1bnKqrQ4j7Ympalo/export?gid=0&format=csv",
                "Codice Stradale", "https://docs.google.com/spreadsheets/d/1uzmU1XEv6RXfNO7O4Ht16Wb8Rdf1bnKqrQ4j7Ympalo/export?gid=640355816&format=csv",
                "Codice Penale", "https://docs.google.com/spreadsheets/d/1uzmU1XEv6RXfNO7O4Ht16Wb8Rdf1bnKqrQ4j7Ympalo/export?gid=1698806507&format=csv"
        ));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    private void startApplication(Map<String, String> codes) {
        System.out.println("Aggiornando la Lista Reati...");

        File crimeFolder = new File(DEFAULT_FOLDER);
        if(!crimeFolder.exists()) crimeFolder.mkdirs();

        deleteOldFiles(DEFAULT_FOLDER);
        updateCrimeList(codes, DEFAULT_FOLDER);

        System.out.println("Registrando i reati nell'applicazione...");

        if(!loadCrimesFromFolder(crimeFolder)) System.in.read();

        registerHardcodedCrimes();
        injectCustomizations();

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

    private void updateCrimeList(Map<String, String> codes, String folderPath) {
        codes.forEach((name, url) -> {
            try(FileOutputStream fileOutputStream = new FileOutputStream("%s/ListaReati - %s.csv".formatted(folderPath, name))) {
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());

                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void loadCrimesFromFile(File file) {
        if(!file.exists() || !file.isFile()) {
            System.out.printf("File reati non trovato, assicurati che si chiami \"%s\"\n", file.getName());
            return;
        }

        new CSVReader(file).getCrimes().forEach(crimeRepository::registerCrime);
    }

    private boolean loadCrimesFromFolder(File folder) {
        if(!folder.exists() || !folder.isDirectory()) {
            System.out.printf("Cartella reati non trovata, assicurati che si chiami \"%s\"\n", folder.getName());
            return false;
        }

        for(var file : getFilesInFolder(folder))
            loadCrimesFromFile(file);

        return true;
    }

    private void registerHardcodedCrimes() {
        //REMOVING
        crimeRepository.removeCrime("stupefacenti o psicotrope");

        //DUPLICATING
        duplicateCrime("Obblighi verso funzionari, ufficiali e agenti", "Mancato fermo al controllo/blocco stradale");
        duplicateCrime("Obblighi verso funzionari, ufficiali e agenti", "Proseguimento della guida senza documenti");

        //ADDING
        crimeRepository.registerCrime(new Crime("Possesso di stupefacenti", "Art. 150", "CP", 0, 0, 0));
    }

    private void inject(String crimeName, Class<? extends Injector> injector) {
        crimeRepository.editCrime(crimeName, crime -> crime.setInjectorClass(injector));
    }

    private void duplicateCrime(String crimeName, String newName) {
        crimeRepository.editCrime(crimeName, crime -> new Crime(newName, crime.getArticle(), crime.getCode(), crime.getHours(), crime.getBail(), crime.getCharge()));
    }

    private void injectCustomizations() {
        inject("Mancato pagamento di multe", NotPaidChargeInjector.class);

        inject("Possesso di stupefacenti", DrugInjector.class);

        inject("Organizzazione di competizioni non autorizzate in velocità", CompetitionInjector.class);
        inject("Obblighi verso funzionari, ufficiali e agenti", AgentObligationsInjector.class);
        inject("Guida senza Documenti", DocumentsInjector.class);
        inject("Possesso di munizioni", AmmoInjector.class);

        inject("Evasione", EvasionInjector.class);

        inject("Atti osceni", SexInjector.class);
        inject("Frode nell'esercizio del commercio", ScamInjector.class);

        inject("Evasione fiscale per mancato scontrino", TaxEvasionNoReceiptInjector.class);
        inject("Evasione fiscale per scontrino con Importo Minore", TaxEvasionSmallerAmountInjector.class);

        inject("Associazione per delinquere", CrimeAssociationInjector.class);
        inject("Associazione di tipo mafioso", CrimeAssociationInjector.class);
        inject("Associazioni sovversive", SubversiveAssociationInjector.class);
        inject("Atto di terrorismo con ordigni micidiali o esplosivi", TerrorismInjector.class);

        inject("Mancato possesso di una Cassetta Postale", ArticleInjector.class);
        inject("Mancata registrazione di un lotto", ArticleInjector.class);
        inject("Regola del Buon Vicinato", ArticleInjector.class);
    }
}
