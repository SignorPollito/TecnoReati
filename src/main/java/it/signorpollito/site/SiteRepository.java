package it.signorpollito.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SiteRepository {

    private final List<Site> sites;

    public SiteRepository() {
        this.sites = new ArrayList<>();

        sites.add(new Site("Codice penale", "https://docs.google.com/document/d/11n607oa4jbNTADLznxBjV80b3Kuaj4dr0y4MxDakvBE"));
        sites.add(new Site("Codice civile", "https://docs.google.com/document/d/1JRMVVJn5JRsmwF3gl0zHjlhu6gUMsqAh5Q4cCFQdvCM"));
        sites.add(new Site("Codice stradale", "https://docs.google.com/document/d/148aM4VqIpN3jfodK8Dr6pExzYMUaB_byy0S17w_YzcE"));
        sites.add(new Site("Lista reati", "https://docs.google.com/spreadsheets/d/1uzmU1XEv6RXfNO7O4Ht16Wb8Rdf1bnKqrQ4j7Ympalo/edit#gid=1698806507"));
    }

    public List<Site> getSites() {
        return Collections.unmodifiableList(sites);
    }
}
