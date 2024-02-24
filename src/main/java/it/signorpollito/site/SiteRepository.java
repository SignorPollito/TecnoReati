package it.signorpollito.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SiteRepository {

    private final List<Site> sites;

    public SiteRepository() {
        this.sites = new ArrayList<>();

        sites.add(new Site("Codice penale", "https://tcnrp.co/OG2X3"));
        sites.add(new Site("Codice civile", "https://tcnrp.co/6s79w"));
        sites.add(new Site("Codice stradale", "https://tcnrp.co/YO371"));
        sites.add(new Site("Lista reati", "https://docs.google.com/spreadsheets/d/1uzmU1XEv6RXfNO7O4Ht16Wb8Rdf1bnKqrQ4j7Ympalo/edit#gid=1698806507"));
    }

    public List<Site> getSites() {
        return Collections.unmodifiableList(sites);
    }
}
