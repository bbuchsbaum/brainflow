package brainflow.app.presentation.controls;

import brainflow.app.toplevel.ImageViewFactory;
import brainflow.core.BrainFlowException;
import brainflow.core.ImageView;
import brainflow.image.anatomy.SpatialLoc3D;
import brainflow.image.data.*;
import brainflow.image.io.BrainIO;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.operations.ComponentLabeler;
import brainflow.math.Index3D;
import com.jidesoft.grid.AbstractExpandableRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.grid.TreeTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 18, 2010
 * Time: 9:51:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterTreeView extends JPanel {


    private TreeTable clusterTable;

    private ClusterSet clusterSet;

    private ClusterTableModel clusterTableModel;

    public ClusterTreeView(ClusterSet clusterSet) {
        this.clusterSet = clusterSet;
        clusterTableModel = createTableModel();

        clusterTable = new TreeTable(clusterTableModel);
        clusterTable.setSortable(true);
        // you can also set different sortable option on each column.
        //  ((SortableTreeTableModel) clusterTable.getModel()).setSortableOption(1, SortableTreeTableModel.SORTABLE_NONE);
        //  ((SortableTreeTableModel) clusterTable.getModel()).setSortableOption(2, SortableTreeTableModel.SORTABLE_LEAF_LEVEL);

        // configure the TreeTable
        clusterTable.setExpandAllAllowed(false);
        clusterTable.setRowHeight(18);
        clusterTable.setShowTreeLines(true);
        clusterTable.setShowGrid(false);
        clusterTable.setIntercellSpacing(new Dimension(0, 0));


        // do not select row when expanding a row.
        clusterTable.setSelectRowWhenToggling(false);

        //clusterTable.getColumnModel().getColumn(0).setPreferredWidth(50);
       // clusterTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        //clusterTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        clusterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        com.jidesoft.grid.TableUtils.autoResizeAllColumns(clusterTable);
      

        JScrollPane scrollPane = new JScrollPane(clusterTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        setLayout(new BorderLayout(6, 6));
        add(scrollPane, BorderLayout.CENTER);
        add(new JLabel("Cluster Table: "), BorderLayout.BEFORE_FIRST_LINE);
        

    }

    public static void main(String[] args) {
        try {
            IImageData3D image = (IImageData3D) BrainIO.readNiftiImage("src/main/groovy/testdata/cohtrend_GLT#0_Tstat.nii");
            final double max = image.maxValue();

            System.out.println("max value = " + max);

            IMaskedData3D mask = new
                    MaskedData3D(image, new MaskPredicate() {
                @Override
                public boolean mask(double value) {
                    return value > (max / 2);

                }
            });

            ComponentLabeler labeler = new ComponentLabeler(mask, 12);
            labeler.label();

             IImageData3D labels = labeler.getLabelledComponents();
            ValueIterator iter = labels.valueIterator();
            while(iter.hasNext()) {
                double lab = iter.next();
                if (lab > 0) {
                    image.value(iter.index());
                }

            }


            ClusterSet cset = new ClusterSet(labeler.getLabelledComponents(), image);

            JFrame frame = new JFrame();
            ClusterTreeView tree = new ClusterTreeView(cset);
            frame.add(tree, BorderLayout.EAST);

            ImageView view1 = ImageViewFactory.createAxialView(labeler.getLabelledComponents());
            ImageView view2 = ImageViewFactory.createAxialView(mask);
            ImageView view3 = ImageViewFactory.createAxialView(image);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,3));
            panel.add(view1);
            panel.add(view2);
            panel.add(view3);

            frame.add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);






        } catch (BrainFlowException e) {
            e.printStackTrace();
        }


    }

    private ClusterTableModel createTableModel() {
        Collection<ClusterSet.Cluster> coll = clusterSet.getClusters();
        List<ClusterRow> rows = new ArrayList<ClusterRow>();
        for (ClusterSet.Cluster clus : coll) {
            rows.add(new ClusterRow(clus));
        }

        return new ClusterTableModel(rows);

    }


    public class ClusterRow extends AbstractExpandableRow implements Comparable<ClusterRow> {

        private ClusterSet.Cluster cluster;

        private List<?> _children;


        public ClusterRow(ClusterSet.Cluster cluster) {
            this.cluster = cluster;
        }

        @Override
        public int compareTo(ClusterRow o) {
            if (o.cluster.getSize() > this.cluster.getSize()) return 1;
            if (o.cluster.getSize() < this.cluster.getSize()) return -1;
            return 0;

        }



        @Override
        public Object getValueAt(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return this;
                case 1:
                    return cluster.getSize();
                case 2:
                    return cluster.getMinValue();
                case 3:
                    return cluster.getMaxValue();
            }

            return null;

        }

        @Override
        public List<?> getChildren() {
            return new ArrayList();
        }

        @Override
        public void setChildren(List<?> children) {
            _children = children;
            if (_children != null) {
                for (Object row : _children) {
                    if (row instanceof Row) {
                        ((Row) row).setParent(this);
                    }
                }
            }

        }

        @Override
        public boolean hasChildren() {
            return false;
        }

        @Override
        public String toString() {
            SpatialLoc3D loc = cluster.getWorldCentroid();
            return "" + Math.round(loc.getX()) + " " + Math.round(loc.getY()) + " " + Math.round(loc.getZ());
        }
    }


    static final protected String[] COLUMN_NAMES = {"Location", "Size", "Min", "Max"};

    public class ClusterTableModel extends TreeTableModel<ClusterRow> {

        public ClusterTableModel(List<? extends ClusterRow> clusterRows) {
            super(clusterRows);
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];


        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return ClusterRow.class;
                case 1:
                    return Integer.class;
                case 2:
                    return Double.class;
                case 3:
                    return Double.class;

            }

            return super.getColumnClass(columnIndex);
        }
    }
}
