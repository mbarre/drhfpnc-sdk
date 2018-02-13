package io.github.mbarre.drhfpnc.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Category {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    _9("9"),
    TOUTES("Toutes");

    private String descr;

    private Category(final String description){
        this.descr = description;
    }

    @Override
    public String toString() {
        return descr;
    }

    public static String valueOf(Category e) {
        return e.toString();
    }

    public static List<Category> listAll(){
        return Arrays.asList(values());
    }

    public static List<String> listAllDescriptions(){
        List<String> allDescr = new ArrayList<>();
        for (Category category : values()) {
            allDescr.add(category.descr);
        }
        return allDescr;
    }
}
