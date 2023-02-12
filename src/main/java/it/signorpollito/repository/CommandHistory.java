package it.signorpollito.repository;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandHistory {

    private final List<Group> historyGroups;

    public CommandHistory() {
        this.historyGroups = new ArrayList<>();
    }

    public void addHistory(Group group) {
        this.historyGroups.add(group);
    }

    public List<Group> getGroups() {
        return new ArrayList<>(historyGroups);
    }


    public static class Group {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


        @Getter private final LocalDateTime date;
        @Getter private final Collection<String> commands;
        @Getter private final String declaration;
        @Getter private final String criminal;

        public Group(String criminal, Collection<String> commands, String declaration) {
            this.commands = Collections.unmodifiableCollection(commands);
            this.declaration = declaration;
            this.criminal = criminal;

            date = LocalDateTime.now();
        }

        public String getFormattedTime() {
            return FORMATTER.format(date);
        }
    }
}
