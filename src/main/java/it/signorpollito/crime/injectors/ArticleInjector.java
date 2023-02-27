package it.signorpollito.crime.injectors;

import it.signorpollito.crime.Crime;

public class ArticleInjector implements Injector {

    @Override
    public String modifyName(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }

    @Override
    public String modifyCommand(Crime crime, Crime.Type crimeType) {
        return crime.getFormattedArticle();
    }
}
