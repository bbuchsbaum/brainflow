package brainflow.functional;

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

    private String label;

    private int code = 0;

    public Level(String _label, int _code) {
        label = _label;
        code = _code;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return new String(label);
    }

    public void setLabel(String _label) {
        label = _label;
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