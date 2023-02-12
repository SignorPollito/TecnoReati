# TecnoReati

Applicazione per PC (dovrebbe andare per su tutti gli OS) scritta in Java.
Il suo scopo principale è quello di fornire uno strumento per il calcolo veloce delle pene, cauzioni e multe.

L'applicazione genera sia il comando di arresto/multa, sia la dichiarazione da scrivere pre-arresto.

I reati si aggiornano in automatico, in base ad una lista online, ogni qualvolta verrà aperto il software, sarà eseguito un controllo su eventuali modifiche dei reati e otterrete l'aggiornamento senza dover muovere un dito!


## Disclaimer

Il codice principale è stato scritto in qualche ora, successivamente sono stati fatti bug fix e migliorie estetiche o delle funzionalità.

Non ho puntato a scrivere un codice perfetto, infatti non lo è, per un progetto così piccolo e fine a se stesso, è piuttosto inutile scrivere il doppio o il triplo del codice, solo per seguire le regole fondamentali della programmazione.

- Non faccio infatti uso di Dependency Injection, ma mi affido ad un singleton a cui posso richiedere i vari servizi (code smell).
- Non utilizzo interface o classi astratte per ogni oggetto, se non in un paio di casi dove si sono resi necessari, anche questa una cosa generalmente errata, anche se farebbe perdere troppo tempo per un progetto simile.
- In generale alcuni parti di codice sono migliorabili (come sempre), ma ripeto, scritto per essere funzionale e leggibile.

## Installazione

1. Scaricate il file "TecnoReati.zip" dell'ultima release e mettete i file al suo interno dentro una cartella a vostro piacere.
2. Assicuratevi di avere Java 17 sul vostro PC, per controllare vi basterà aprire il Prompt dei Comandi ed eseguire "java --version" e controllare che ci sia il numero 17.
3. Avviate il programma con doppio-click sull'eseguibile "start.bat"
4. Assicuratevi che parta correttamente e che spunti nella stessa cartella un file denominato "ListaReati.csv"
5. Godetevi la sua bellezza!
