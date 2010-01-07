package brainflow.image.io;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 6, 2010
 * Time: 7:05:45 AM
 * To change this template use File | Settings | File Templates.
 */
public enum BinaryEncoding {

    RAW(""),
    GZIP("gz");


    private String extension;

    BinaryEncoding(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
