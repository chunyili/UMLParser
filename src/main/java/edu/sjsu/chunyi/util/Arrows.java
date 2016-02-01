package edu.sjsu.chunyi.util;

/**
 * Created by jilongsun on 9/28/15.
 */
public enum Arrows {


    association("--"),
    entends("<|--"),
    Interface("<|.."),
    uses("<..")
    ;


    private final String name;

    private Arrows(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }


}
