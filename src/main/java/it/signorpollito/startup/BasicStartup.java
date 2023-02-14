package it.signorpollito.startup;

import it.signorpollito.commands.CommandService;
import it.signorpollito.commands.impl.CrimeCommand;
import it.signorpollito.commands.impl.HistoryCommand;
import it.signorpollito.commands.impl.PreventiveCommand;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.crime.Crime;
import it.signorpollito.crime.injectors.*;
import it.signorpollito.reader.CSVReader;
import it.signorpollito.repository.CrimeRepository;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class BasicStartup {

    private static boolean started = false;

    public static BasicStartup start() {
        if(started) throw new IllegalStateException("The program has already started!");

        started = true;
        return new BasicStartup();
    }

    private final CrimeRepository crimeRepository;

    private BasicStartup() {
        this.crimeRepository = ServiciesManager.getInstance().getCrimeRepository();

        startApplication();
    }

    @SneakyThrows
    private void startApplication() {
        System.out.println("Aggiornando la Lista Reati...");
        updateCrimeList();

        System.out.println("Registrando i reati nell'applicazione...");

        if(!loadCrimes())
            System.in.read();

        registerHardcodedCrimes();
        injectCustomizations();

        registerCommands();
    }

    private void registerCommands() {
        CommandService commandService = ServiciesManager.getInstance().getCommandService();
        commandService.registerCommand(new CrimeCommand());
        commandService.registerCommand(new PreventiveCommand());
        commandService.registerCommand(new HistoryCommand());
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
        crimeRepository.registerCrime(new Crime("Art. 27 CC", 0, 0, 3500));
        crimeRepository.registerCrime(new Crime("Guida senza casco", 0, 0, 500));

        crimeRepository.removeCrime("Mancato scontrino ( Evasione Fiscale )");
        crimeRepository.removeCrime("Scontrino con importo minore (Evasione Fiscale)");
    }

    private void inject(String crimeName, Class<? extends Injector> injector) {
        crimeRepository.editCrime(crimeName, crime -> crime.setInjectorClass(injector));
    }

    private void setCustomName(String crimeName, String newName) {
        crimeRepository.editCrime(crimeName, crime -> crime.setName(newName));
    }

    private void injectCustomizations() {
        inject("Mancato pagamento di una multa", NotPaidChargeInjector.class);

        inject("Possesso Stup Catg. 1 ( Da 1 a 10 pz )", DrugInjector.class);
        inject("Possesso Stup Catg. 2 ( Da 11 a 32 pz )", DrugInjector.class);
        inject("Possesso Stup Catg. 3 ( Da 33 a 127pz )", DrugInjector.class);
        inject("Possesso Stup Catg. 4 ( +128pz )", DrugInjector.class);

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
