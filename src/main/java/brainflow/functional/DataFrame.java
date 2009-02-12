package brainflow.functional;

import java.util.*;



import java.io.*;

import cern.colt.list.IntArrayList;
import cern.colt.list.DoubleArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 26, 2005
 * Time: 2:23:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataFrame {


    private LinkedHashMap variableMap = new LinkedHashMap();
    private LinkedHashMap<String, VariableType> variableTypes = new LinkedHashMap<String, VariableType>();


    private boolean containsData = false;
    private int nrows = 0;

    //


    public DataFrame() {
    }

    public DataFrame(String[] names, VariableType[] types) {
        if (names.length != types.length)
            throw new IllegalArgumentException("DataFrame(): number of variable names must equal number of variables types");

        for (int i = 0; i < names.length; i++) {
            variableTypes.put(names[i], types[i]);

        }
    }


    public synchronized void addRow(String[] values) {
        if (values.length != getNColumns()) {
            throw new IllegalArgumentException("length of values must equal number of columns/variables in DataFrame");
        }

        Iterator iter = variableTypes.keySet().iterator();

        int count = 0;
        while (iter.hasNext()) {
            String name = (String) iter.next();
            addValue(name, values[count]);
            count++;
        }

        nrows = nrows + 1;

    }


    public void addEmptyVariable(String name, VariableType type) {
        if (containsData) {
            throw new IllegalArgumentException("Cannot add empty variable to populated DataFrame");
        }

        variableTypes.put(name, type);

    }

    private void addValue(String name, String value) {
        VariableType vtype = variableTypes.get(name);
        if (vtype == null) {
            throw new IllegalArgumentException("variable " + name + " not found in data frame");
        }

        switch (vtype) {
            case STRING:
                addStringValue(name, (String) value);
                break;
            case INTEGER:
                addIntValue(name, Integer.parseInt(value));
                break;
            case DOUBLE:
                addDoubleValue(name, Double.parseDouble(value));
                break;
            case FACTOR:
                addFactorValue(name, value);
                break;

            default:
                throw new AssertionError("Unrecognized VariableType " + vtype);
        }
    }

    private void addStringValue(String name, String value) {
        List<String> var = (List<String>) variableMap.get(name);
        var.add(value);

    }

    private void addIntValue(String name, int value) {
        IntArrayList alist = (IntArrayList) variableMap.get(name);
        alist.add(value);
    }

    private void addDoubleValue(String name, double value) {
        DoubleArrayList alist = (DoubleArrayList) variableMap.get(name);
        alist.add(value);
    }

    private void addFactorValue(String name, String level) {
        Factor factor = (Factor) variableMap.get(name);
        factor.addValue(level);
    }




    public void addVariable(String name, List<String> var) {
        if (!containsData) {
            variableTypes.put(name, VariableType.STRING);
            variableMap.put(name, var);
            containsData = true;
            nrows = var.size();
        } else if (var.size() != nrows) {
            throw new IllegalArgumentException("variable length " + var.size() + " exceeds number of rows in dataframe " + nrows);
        } else {
            variableMap.put(name, var);
            variableTypes.put(name, VariableType.STRING);
        }

    }

    public void addVariable(Factor var) {
        if (!containsData) {
            variableMap.put(var.getName(), var);
            variableTypes.put(var.getName(), VariableType.FACTOR);
            containsData = true;
            nrows = var.length();
        } else if (var.length() != nrows) {
            throw new IllegalArgumentException("variable length " + var.length() + " exceeds number of rows in dataframe " + nrows);
        } else {
            variableMap.put(var.getName(), var);
            variableTypes.put(var.getName(), VariableType.FACTOR);

        }
    }

    public void addVariable(String name, int[] var) {
        if (!containsData) {
            IntArrayList alist = new IntArrayList(var.length);
            for (int i = 0; i < var.length; i++) {
                alist.add(var[i]);
            }
            variableMap.put(name, alist);
            variableTypes.put(name, VariableType.INTEGER);
            containsData = true;
            nrows = var.length;
        } else if (var.length != nrows) {
            throw new IllegalArgumentException("variable length " + var.length + " exceeds number of rows in dataframe " + nrows);
        } else {
            variableMap.put(name, var);
            variableTypes.put(name, VariableType.INTEGER);
        }
    }

    public void addVariable(String name, double[] var) {
        if (!containsData) {
            DoubleArrayList alist = new DoubleArrayList(var.length);
            for (int i = 0; i < var.length; i++) {
                alist.add(var[i]);
            }
            variableMap.put(name, alist);
            variableTypes.put(name, VariableType.DOUBLE);
            containsData = true;
            nrows = var.length;
        } else if (var.length != nrows) {
            throw new IllegalArgumentException("variable length " + var.length + " exceeds number of rows in dataframe " + nrows);
        } else {
            variableMap.put(name, var);
            variableTypes.put(name, VariableType.DOUBLE);
        }
    }

    public double[] getNumericVariable(String name) {
        Object tmp = variableTypes.get(name);
        if (tmp == null) throw new IllegalArgumentException("DataFrame does not contain variable named " + name);

        VariableType vtype = (VariableType) tmp;
        if (vtype != VariableType.DOUBLE) {
            throw new IllegalArgumentException("Variable " + name + "is not a numeric variable " + vtype);
        }

        double[] var = (double[]) variableMap.get(name);
        return var.clone();
    }

    public int[] getIntegerVariable(String name) {
        Object tmp = variableTypes.get(name);
        if (tmp == null) throw new IllegalArgumentException("DataFrame does not contain variable named " + name);

        VariableType vtype = (VariableType) tmp;
        if (vtype != VariableType.INTEGER) {
            throw new IllegalArgumentException("Variable " + name + "is not an integer variable " + vtype);
        }

        int[] var = (int[]) variableMap.get(name);
        return var.clone();
    }

    public String[] getStringVariable(String name) {
        Object tmp = variableTypes.get(name);
        if (tmp == null) throw new IllegalArgumentException("DataFrame does not contain variable named " + name);

        VariableType vtype = (VariableType) tmp;
        if (vtype != VariableType.STRING) {
            throw new IllegalArgumentException("Variable " + name + "is not a String variable " + vtype);
        }

        List<String> var = (List<String>) variableMap.get(name);
        String[] vals = new String[var.size()];
        var.toArray(vals);
        return vals;

    }

    public Factor getFactorVariable(String name) {
        Object tmp = variableTypes.get(name);
        if (tmp == null) throw new IllegalArgumentException("DataFrame does not contain variable named " + name);

        VariableType vtype = (VariableType) tmp;
        if (vtype != VariableType.FACTOR) {
            throw new IllegalArgumentException("Variable " + name + "is not a Factor variable " + vtype);
        }

        Factor var = (Factor) variableMap.get(name);
        return new Factor(var.getName(), var.getValues());

    }


    public int getNColumns() {
        return variableTypes.size();
    }

    public int getNRows() {
        return nrows;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<getNRows(); i++) {

        }

        return "";
    }


    public static DataFrame read(File ipfile, VariableType[] vtypes) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(ipfile);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        tokenizer.eolIsSignificant(true);


        DataFrame dframe = new DataFrame();

        try {

            int vcount = 0;
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOL) {
                dframe.addEmptyVariable(tokenizer.sval, vtypes[vcount]);
                vcount++;
            }


            int varNum = 0;


            Object[] vals = new Object[vtypes.length];
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (tokenizer.ttype == StreamTokenizer.TT_EOL) {
                    varNum = 0;

                    vals = new Object[vtypes.length];
                    continue;
                }

                vals[varNum] = tokenizer.sval;
                varNum++;
            }

        } catch (IOException e) {
            throw e;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw e;
            }
        }

        return dframe;

    }


}
