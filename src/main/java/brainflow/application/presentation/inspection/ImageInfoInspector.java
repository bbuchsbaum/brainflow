package brainflow.application.presentation.inspection;


import brainflow.image.io.IImageDataSource;
import brainflow.image.io.ImageInfo;
import brainflow.utils.IDimension;
import brainflow.utils.Point3D;
import com.jidesoft.grid.Property;
import com.jidesoft.grid.PropertyTable;
import com.jidesoft.grid.PropertyTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 15, 2007
 * Time: 8:28:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageInfoInspector {

    private ImageInfo info;

    public ImageInfoInspector(ImageInfo info) {
        this.info = info;
    }

    public PropertyTable createPropertyTable() throws Exception {
        ArrayList list = new ArrayList();
        //list.add(new JidePropertyWrapper(info.getArrayDim(), "array dim", "Image"));
        list.add(new JidePropertyWrapper(info.getArrayDim(), "array dim", "Image"));
        list.add(new JidePropertyWrapper(info.getSpacing(), "voxel dim", "Image"));
        list.add(new JidePropertyWrapper(info.getDataType(), "data type", "Image"));
        



        PropertyTableModel model = new PropertyTableModel(list);

        PropertyTable table = new PropertyTable(model);

        return table;
    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel());

            IImageDataSource source = null; //TestUtils.quickDataSource("icbm452_atlas_probability_gray.hdr");
            ImageInfo info = source.getImageInfo();

            ImageInfoInspector inspector = new ImageInfoInspector(info);

            PropertyTable table = inspector.createPropertyTable();
            JFrame jf = new JFrame();
            jf.add(table, BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class IDimensionProperty extends Property {

        private IDimension value;

        public IDimensionProperty(String displayName, IDimension value) {
            this.value = value;
            this.setDisplayName(displayName);
        }

        public String getDisplayName() {
            return super.getDisplayName();    //To change body of overridden methods use File | Settings | File Templates.
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object o) {

        }

        public void setType(Class aClass) {

        }
    }

    class Point3DWrapper extends Property {
        private Point3D value;

        public Point3DWrapper(String displayName, Point3D value) {
            this.value = value;
            this.setDisplayName(displayName);
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object o) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setType(Class aClass) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}
