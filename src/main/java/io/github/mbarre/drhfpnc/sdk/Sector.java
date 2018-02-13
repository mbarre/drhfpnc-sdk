package io.github.mbarre.drhfpnc.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Sector {

    EMPLOIS_FONCTIONNELS("EMPLOIS FONCTIONNELS"),
    FILIERE_ADMINISTRATIVE("FILIERE ADMINISTRATIVE"),
    FILIERE_CULTURE_ET_SPORTS("FILIERE CULTURE ET SPORTS"),
    FILIERE_ENSEIGNEMENT("FILIERE ENSEIGNEMENT"),
    FILIERE_INCENDIE("FILIERE INCENDIE"),
    FILIERE_SANTE_SOCIALE("FILIERE SANTE-SOCIALE"),
    FILIERE_TECHNIQUE("FILIERE TECHNIQUE"),
    SECURITE("SECURITE"),
    TOUTES("Toutes");

    private String descr;

    private Sector(final String description){
        this.descr = description;
    }

    @Override
    public String toString() {
        return descr;
    }

    public static String valueOf(Sector e) {
        return e.toString();
    }

    public static List<Sector> listAll(){
        return Arrays.asList(values());
    }

    public static List<String> listAllDescriptions(){
        List<String> allDescr = new ArrayList<>();
        for (Sector sector : values()) {
            allDescr.add(sector.descr);
        }
        return allDescr;
    }




}
