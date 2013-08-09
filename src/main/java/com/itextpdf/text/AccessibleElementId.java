package com.itextpdf.text;

public class AccessibleElementId implements Comparable<AccessibleElementId> {

    private static int id_counter = 0;
    private int id = 0;

    public AccessibleElementId() {
        id = ++id_counter;
    }

    public String toString() {
        return Integer.toString(id);
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        return (o instanceof AccessibleElementId) && (id == ((AccessibleElementId)o).id);
    }

    public int compareTo(AccessibleElementId elementId) {
        if (id < elementId.id)
            return -1;
        else if (id > elementId.id)
            return 1;
        else
            return 0;
    }


}
