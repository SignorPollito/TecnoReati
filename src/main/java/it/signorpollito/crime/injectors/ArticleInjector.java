package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;

public class ArticleInjector implements Injector {

    @Override
    public String getModifiedDisplayName(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }

    @Override
    public String getCommandName(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }
}
