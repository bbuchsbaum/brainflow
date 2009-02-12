package org.boxwood.data;

/**
 * Title:        LCBR Home Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * @author Brad Buchsbaum
 * @version 1.0
 */

public class Level {

    private final String label;

    private final int code;

    public Level(String _label, int _code) {
        label = _label;
        code = _code;
    }

    public int code() {
        return code;
    }

    public String label() {
        return new String(label);
    }
 

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        if (code != level.code) return false;
        if (label != null ? !label.equals(level.label) : level.label != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (label != null ? label.hashCode() : 0);
        result = 31 * result + code;
        return result;
    }




}