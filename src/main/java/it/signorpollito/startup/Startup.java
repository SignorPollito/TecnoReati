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

public class Startup {

    private static boolean started = false;

    public static Startup start() {
        if(started) throw new IllegalStateException("The program has already started!");

        started = true;
        return new Startup();
    }

    private final CrimeRepository crimeRepository;

    private Startup() {
        this.crimeRepository = ServiciesManager.getInstance().getCrimeRepository();

        startApplication();
    }

    @SneakyThrows
    private void startApplication() {
        System.out.println("Aggiornando la Lista Reati...");
        updateCrimeList();

        System.out.println("Registrando i reati nell'applicazione...");

        if(!loadCrimes()) System.in.read();

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

    private void updateCrimeList() {
        new File("ListaReati.csv").delete();

        try(FileOutputStream fileOutputStream = new FileOutputStream("ListaReati.csv")) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(
                    "https://docs.google.com/spreadsheets/d/1CzQoZbGMeN9Z59ZeZU9No8actcZneGEIgevOao9zEFs/export?format=csv"
            ).openStream());

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean loadCrimes() {
        File file = new File("ListaReati.csv");
        if(!file.exists()) {
            System.out.println("File reati non trovato, assicurati che si chiami \"ListaReati.csv\"");
            return false;
        }

        new CSVReader(file).getCrimes().forEach(crimeRepository::registerCrime);
        return true;
    }

    private void registerHardcodedCrimes() {
        //REMOVING
        crimeRepository.removeCrime("Mancato scontrino ( Evasione Fiscale )");
        crimeRepository.removeCrime("Scontrino con importo minore (Evasione Fiscale)");

        crimeRepository.removeCrime("Possesso Stup Catg. 1");
        crimeRepository.removeCrime("Possesso Stup Catg. 2");
        crimeRepository.removeCrime("Possesso Stup Catg. 3");
        crimeRepository.removeCrime("Possesso Stup Catg. 4");


        //ADDING
        crimeRepository.registerCrime(new Crime("Art. 27 CC", 0, 0, 3500));
        crimeRepository.registerCrime(new Crime("Guida senza casco", 0, 0, 500));
        crimeRepository.registerCrime(new Crime("Mancato fermo al controllo/blocco stradale", 0, 0, 1000));
        crimeRepository.registerCrime(new Crime("Possesso di stupefacenti", 0, 0, 0));
    }

    private void inject(String crimeName, Class<? extends Injector> injector) {
        crimeRepository.editCrime(crimeName, crime -> crime.setInjectorClass(injector));
    }

    private void setCustomName(String crimeName, String newName) {
        crimeRepository.editCrime(crimeName, crime -> crime.setName(newName));
    }

    private void injectCustomizations() {
        inject("Mancato pagamento di una multa", NotPaidChargeInjector.class);

        inject("Possesso di stupefacenti", DrugInjector.class);

        inject("Guida senza documenti", DocumentsInjector.class);
        inject("Possesso di munizioni", AmmoInjector.class);

        inject("Evasione", EvasionInjector.class);

        inject("Atti osceni in pubblico", SexInjector.class);
        inject("Frode nell'esercizio del commercio", ScamInjector.class);
        inject("Diffamazione", DefamationInjector.class);

        inject("Evasione fiscale ( mancato scontrino )", TaxEvasionNoReceiptInjector.class);
        inject("Evasione fiscale ( scontrino con importo minore )", TaxEvasionSmallerAmountInjector.class);

        setCustomName("Mancata registrazione di un Plot", "Art. 28 CC");
        setCustomName("Mancata cassetta postale", "Art. 29 CC");
    }
}
