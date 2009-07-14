package brainflow.image.operations;

import brainflow.core.BrainFlowException;
import brainflow.image.io.MemoryImageDataSource;
import brainflow.app.toplevel.ImageViewFactory;
import brainflow.image.data.*;
import brainflow.image.io.BrainIO;
import brainflow.image.space.Axis;
import brainflow.core.BF;
import brainflow.core.ImageViewModel;
import brainflow.core.ImageView;
import brainflow.core.layer.ImageLayer3D;
import brainflow.core.layer.LayerProps;
import brainflow.colormap.ColorTable;
import brainflow.utils.Range;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 30, 2006
 * Time: 1:53:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectedComponentsFilter extends AbstractImageFilter {

    private RunSet runs;

    private int nruns;

    private int[] rows;

    private int nx;

    private int ny;

    private int nz;

    public IImageData getOutput() {
        List<IImageData> sources = getSources();
        connect((IImageData3D) sources.get(0));
        return null;
    }

    private void flattenTree() {
        int root, rootroot;
        int nruns = runs.size();

        for (int irun = nruns - 1; irun >= 0; irun--) {
            root = runs.get(irun).root;
            if (root < 0) continue;
            if (root <= irun) {
                System.err.println("error in flattenTree");
            }
            rootroot = runs.get(root).root;
            if (rootroot < 0) continue;

            int root3 = runs.get(rootroot).root;
            if (root3 < 0) {
                runs.get(irun).root = rootroot;
                continue;
            }

        }
    }

    private final int findRoot(int run) {
        int r = run;
        //nfindroot++;

        Run crun = runs.get(r);
        while (crun.root >= 0) {
            r = crun.root;
            //nfollrptr++;
            //lfindroot++;
            if (r >= runs.size()) {
                throw new RuntimeException("Illegal root in FindRoot");
            }

            crun = runs.get(r);
        }

        return r;
    }

    private void updateRoot(int run, int newroot) {
        int r = run;
        int i;

        Run crun = runs.get(r);

        while (crun.root >= 0) {
            i = crun.root;
            crun.root = newroot;
            r = i;
            crun = runs.get(r);
        }

        crun.root = newroot;
    }

    private void processAdjacency(int run1, int run2, boolean diag) {

        Run r1 = runs.get(run1);
        Run r2 = runs.get(run2);

        int lap1 = r1.zhigh - r2.zlo;
        int lap2 = r2.zhigh - r1.zlo;

        if (lap1 < 1 || lap2 < 1) {
            return;
        }
        int lap = Math.min(r1.zhigh, r2.zhigh) -
                Math.max(r1.zlo, r2.zlo) + 1;

        int root1 = findRoot(run1);
        int root2 = findRoot(run2);

        if (root1 == root2) {
            int area;
            if (diag) {
                area = -runs.get(root1).root;
            } else {
                area = -runs.get(root1).root - 2 * lap;
            }

            if (area < 0) {
                throw new RuntimeException("Illegal Area in processAdjacency");
            }

            updateRoot(run1, root1);
            runs.get(root1).root = -area;    // preventing an infinite loop
            updateRoot(run2, root1);
            runs.get(root1).root = -area;
        } else {
            int root = Math.max(root1, root2);
            // Make root to be the root of the combined tree.  This maintains an
            // invariant that a run's father is higher numbered than it.
            int area1 = -runs.get(root1).root;   // surface area
            int area2 = -runs.get(root2).root;
            int area;
            if (diag) {
                area = area1 + area2;
            } else {
                area = area1 + area2 - 2 * lap;
            }

            // Update root even if the root of this run is already the new root,
            // since this compresses the tree.  If the root is already the new
            // root, then this will leave the root's root positive, which is an
            // infinite loop.  However, that is fixed by replacing the root with
            // the negative of the size.

            updateRoot(run1, root);
            updateRoot(run2, root);

            runs.get(root).root = -area;
            if (area < 0) {
                throw new RuntimeException("Illegal area");
            }

        }

    }


    private final void connectRow(int x1, int y1, int x2, int y2, boolean diag) {
        if (y1 < 0 || y1 >= ny || x1 < 0 || x1 >= nx || y2 < 0 || y2 >= ny || x2 < 0
                || x2 >= nx) return;
        // In the following, the 2nd subscript of rows might be -1.  This is
        // OK since rows is mapped to the 1-D rows1.

        int row1lorun = runs.getRowStart(y1 * nx + x1); // + 1;
        int row1hirun = runs.getRowEnd(y1 * nx + x1);
        int row2lorun = runs.getRowStart(y2 * nx + x2); // + 1;
        int row2hirun = runs.getRowEnd(y2 * nx + x2);


        if (row1lorun > row1hirun || row2lorun > row2hirun) return;

        int row1currun = row1lorun;
        int row2currun = row2lorun;

        for (; ;) {
            processAdjacency(row1currun, row2currun, diag);

            // In the row whose current run has the lower zhi, go to the next run.
            if (runs.get(row1currun).zhigh < runs.get(row2currun).zhigh) {
                if (++row1currun > row1hirun)
                    break;
            } else {
                if (++row2currun > row2hirun)
                    break;
            }
        }
    }


    private final boolean runnifyRow(IImageData3D data, int x, int y, int nz) {
        int runCount = 0;
        int rownum = y * nx + x;

        for (int iz = 0; iz < nz; iz++) {
            if (data.value(x, y, iz) > 0) {
                if (runCount == 0) {
                    runs.setStartIndex(rownum);
                }

                runCount++;
                Run run = new Run(x, y, iz);
                int jz = iz + 1;
                while (data.value(x, y, jz) > 0) {
                    jz++;
                }
                run.zhigh = jz - 1;
                int area = 4 * (jz - iz) + 2;
                int vol = jz - iz;
                run.root = -area;

                runs.addRun(run);
                iz = jz;
            }
        }

        if (runCount > 0) {
            runs.setEndIndex(rownum);
            return true;
        }

        return false;
        //rows[zero * nx + zero] = runs.size() - 1;

    }

    private final void processRow(IImageData3D data, int x, int y, int nz) {

        boolean found = runnifyRow(data, x, y, nz);
        if (!found) return;

        connectRow(x, y, x, y - 1, false);        // to left
        connectRow(x, y, x - 1, y, false);        // to below
        //if (conn26) {
        connectRow(x, y, x - 1, y - 1, true); // to below and left.
        connectRow(x, y, x - 1, y + 1, true); // to below and right.
        //}
    }

    private ArrayList<ClusterComponent> assembleComponents() {
        int root, comp;
        int nruns = runs.size();
        int ncomps = 0;

        ArrayList<ClusterComponent> comps = new ArrayList<ClusterComponent>(100);

        for (int irun = nruns - 1; irun >= 0; irun--) {
            root = runs.get(irun).root;
            if (root < 0) {
                ClusterComponent cc = new ClusterComponent();
                cc.area = -root;
                cc.runs = irun;
                cc.nruns = 1;
                runs.get(irun).root = -comps.size();
                comps.add(cc);
            } else {
                comp = -runs.get(root).root;
                //if (comp >= ncomps) die("illegal root");
                comps.get(comp).nruns++;
                runs.get(irun).root = comps.get(comp).runs;
                comps.get(comp).runs = irun;
            }
        }
        return comps;


    }


    public static void main(String[] args) {
        try {

            IImageData3D dat = (IImageData3D) BrainIO.readNiftiImage(BF.getDataURL("cohtrend_GLT#0_Tstat.nii"));
            MaskedData3D mask = new MaskedData3D(dat, new MaskPredicate() {
                public final boolean mask(double value) {
                    return value > 8;
                }
            });


            System.out.println("cardinality = " + mask.cardinality());

            ConnectedComponentsFilter filter = null;
            long btime = System.currentTimeMillis();

            filter = new ConnectedComponentsFilter();
            filter.addInput(mask);
            IImageData3D dat3d = (IImageData3D) filter.getOutput();
            ImageViewModel model = new ImageViewModel("test",
                    new ImageLayer3D(new MemoryImageDataSource(dat3d), new LayerProps(ColorTable.SPECTRUM, new Range(0, dat3d.maxValue()))));


            ImageView view = ImageViewFactory.createAxialView(model);
            JFrame frame = new JFrame();
            frame.add(view, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        } catch (BrainFlowException e) {
            e.printStackTrace();
        }

    }


    private void connect(IImageData3D data) {
        nz = data.getImageSpace().getDimension(Axis.Z_AXIS);
        nx = data.getImageSpace().getDimension(Axis.X_AXIS);
        ny = data.getImageSpace().getDimension(Axis.Y_AXIS);

        //allruns = new RunList[nx*ny];

        runs = new RunSet(nx * ny);
        //rows = new int[nx * ny];


        long startTime = System.currentTimeMillis();
        for (int x = 0; x < nx; x++) {
            for (int y = 0; y < ny; y++) {
                processRow(data, x, y, nz);
            }
        }

        flattenTree();
        List<ClusterComponent> comps = assembleComponents();

        long endTime = System.currentTimeMillis();
        System.out.println("ncomps = " + comps.size());
        System.out.println("time = " + (endTime - startTime));

        for (Object crun : runs) {
            //System.out.println(crun.toString());
        }
    }

    class ClusterComponent {
        int nruns;          // # runs in each component.
        int runs;           // Pointer to first run of this component.
        int vol;            // Volume
        int area;           // Surface area.

    }

    class Run {
        int x;
        int y;
        int zlo;
        int zhigh;
        int root;

        public Run(int x, int y, int zlo) {
            this.x = x;
            this.y = y;
            this.zlo = zlo;

        }

        public String toString() {
            return "(" + x + ", " + y + ", " + zlo + ", " + zhigh + ": " + root;

        }

    }

    class RunSet implements Iterable {
        private ArrayList<Run> runs = new ArrayList<Run>();
        private int[] rowStart;
        private int[] rowEnd;


        public RunSet(int nrows) {
            rowStart = new int[nrows];
            rowEnd = new int[nrows];
        }

        public void setStartIndex(int rownum) {
            //System.out.println("adding row: " + rownum);
            rowStart[rownum] = runs.size();
        }

        public void setEndIndex(int rownum) {
            //System.out.println("end row: " + rownum);
            rowEnd[rownum] = runs.size() - 1;
        }

        public void addRun(Run run) {
            runs.add(run);
        }

        public Run get(int ridx) {
            return runs.get(ridx);
        }

        public int getRowStart(int row) {
            return rowStart[row];
        }

        public int getRowEnd(int row) {
            return rowEnd[row];
        }

        public final int size() {
            return runs.size();
        }

        public Iterator iterator() {
            return runs.iterator();
        }
    }


}

/*final class RunList {
  private ArrayList<Run> runs = new ArrayList<Run>();

  public void addRun(Run run) {
      runs.add(run);
  }

  public int size() {
      return runs.size();
  }

  public ArrayList<Run> getRuns() {
      return runs;
  }

  public boolean isEmpty() {
      return size() == 0;
  }
}  */





        





