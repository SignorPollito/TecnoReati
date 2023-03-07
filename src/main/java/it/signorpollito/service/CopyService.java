package it.signorpollito.service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class CopyService {
    public static void copy(String phrase) {
        copy(Toolkit.getDefaultToolkit().getSystemClipboard(), phrase);
    }

    public static void copy(Clipboard clipboard, String phrase) {
        clipboard.setContents(new StringSelection(phrase), null);
    }


    private final List<String> phrases;
    private final Clipboard clipboard;

    public CopyService(Collection<String> phrases) {
        this.phrases = new ArrayList<>(phrases);
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public void copyEach(Consumer<String> copyConsumer) {
        phrases.forEach(phrase -> {
            copy(clipboard, phrase);
            copyConsumer.accept(phrase);
        });
    }
}
