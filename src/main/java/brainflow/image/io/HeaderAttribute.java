package brainflow.image.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 7:55:59 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HeaderAttribute {

    enum HEADER_ATTRIBUTE_TYPE {
        string_attribute,
        float_attribute,
        integer_attribute,
        double_attribute;

    }


    public HeaderAttribute(AFNIAttributeKey _key, int _count, String _content) {
        name = _key.toString();
        count = _count;
        content = _content;
        key = _key;
    }

    public AFNIAttributeKey key;

    protected String name;

    protected int count;

    protected String content;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("type = " + getType());
        sb.append("\n");
        sb.append("name = " + name);
        sb.append("\n");
        sb.append("count = " + count);
        sb.append("\n");


        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public AFNIAttributeKey getKey() {
        return key;
    }

    public abstract List<?> getData();

    public abstract int size();

    public abstract HEADER_ATTRIBUTE_TYPE getType();

    public abstract void parseContent();

    public static HEADER_ATTRIBUTE_TYPE parseType(String typeStr) {
        StringTokenizer tokenizer = new StringTokenizer(typeStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return HEADER_ATTRIBUTE_TYPE.valueOf(tok);
    }

    public static int parseCount(String countStr) {
        StringTokenizer tokenizer = new StringTokenizer(countStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return Integer.parseInt(tok);

    }

    public static String parseName(String nameStr) {
        StringTokenizer tokenizer = new StringTokenizer(nameStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return tok;

    }

    public static HeaderAttribute createAttribute(HEADER_ATTRIBUTE_TYPE type, AFNIAttributeKey key, int count, String content) {

        HeaderAttribute attr = null;

        switch (type) {
            case integer_attribute:
                attr = new IntegerAttribute(key, count, content);
                break;
            case float_attribute:
                attr = new FloatAttribute(key, count, content);
                break;
            case string_attribute:
                attr = new StringAttribute(key, count, content);
                break;
            default:
                throw new RuntimeException("unrecognized attribute : " + type);

        }

        return attr;

    }

    public static class IntegerAttribute extends HeaderAttribute {

        List<Integer> data = new ArrayList<Integer>();

        public IntegerAttribute(AFNIAttributeKey key, int count, String content) {
            super(key, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content, " ");
            while (tokenizer.hasMoreTokens()) {
                data.add(Integer.parseInt(tokenizer.nextToken()));
            }
        }

        public HEADER_ATTRIBUTE_TYPE getType() {
            return HEADER_ATTRIBUTE_TYPE.integer_attribute;
        }

        public int size() {
            return data.size();
        }

        public List<Integer> getData() {
            return data;
        }


        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);

            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }

    public static class StringAttribute extends HeaderAttribute {

        List<String> data = new ArrayList<String>();

        public StringAttribute(AFNIAttributeKey key, int count, String content) {
            super(key, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content.substring(1, content.length()), "~;");

            while (tokenizer.hasMoreTokens()) {
                data.add(tokenizer.nextToken());
            }
        }

        public HEADER_ATTRIBUTE_TYPE getType() {
            return HEADER_ATTRIBUTE_TYPE.string_attribute;
        }

        public List<String> getData() {
            return data;
        }

        public int size() {
            return data.size();
        }

        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);

            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }

    public static class FloatAttribute extends HeaderAttribute {

        List<Float> data = new ArrayList<Float>();

        public FloatAttribute(AFNIAttributeKey key, int count, String content) {
            super(key, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content, " ");
            while (tokenizer.hasMoreTokens()) {
                data.add(Float.parseFloat(tokenizer.nextToken()));
            }
        }

        public HEADER_ATTRIBUTE_TYPE getType() {
            return HEADER_ATTRIBUTE_TYPE.float_attribute;
        }

        public int size() {
            return data.size();
        }

        public List<Float> getData() {
            return data;
        }

        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);

            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }


}

