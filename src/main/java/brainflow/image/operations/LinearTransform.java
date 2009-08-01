/*
 * LinearTransform.java
 *
 * Created on May 12, 2003, 2:47 PM
 */

package brainflow.image.operations;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;
import java.io.*;
import java.util.StringTokenizer;

/**
 *
 * @author  Bradley
 */
public class LinearTransform {
    
    DoubleMatrix2D matrix4X4 = new DenseDoubleMatrix2D(4,4);
    
    DoubleMatrix1D holder = new DenseDoubleMatrix1D(4);
    
    Algebra algie = new Algebra();
    
    /** Creates a new instance of LinearTransform */
    public LinearTransform() {
    }
    
    public void setMatrix(double[][] mat) {
        if ( (mat.length != 4) || (mat[0].length != 4) )
            throw new IllegalArgumentException("Linear transformation matrix must be 4x4");
        matrix4X4 = new DenseDoubleMatrix2D(mat);
        matrix4X4 = algie.transpose(matrix4X4);

    }

    public void invertMatrix() {
        matrix4X4 = algie.inverse(matrix4X4);
    }

    public void readAsciiMatrix(String file) throws IOException {
        File f = new File(file);
        BufferedReader reader = new BufferedReader(new FileReader(f));
        double[][] matrix = new double[4][4];
        for (int i=0; i<4; i++) {
            String line = reader.readLine();
            if (line == null)
                throw new IOException("Illegal matrix file, reached end before fourth line");
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            if (tokenizer.countTokens() == 4) {
                for (int j=0; j<4; j++) {
                    String t = tokenizer.nextToken();
                    matrix[j][i] = Double.parseDouble(t);
                }
            }
            else
                throw new IOException("Illegal matrix file, must contain four values per line");
        }
        
        setMatrix(matrix);
        invertMatrix();
    }
                    
                
            
            
    
    
    public DoubleMatrix1D transform(DoubleMatrix1D input) {        
        return algie.mult(matrix4X4, input);
    }
    
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(matrix4X4.toString());
        return buffer.toString();
    }
        
    
    
    
    
    
       
    
}
