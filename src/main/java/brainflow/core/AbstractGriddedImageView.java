package brainflow.core;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 1, 2007
 * Time: 11:50:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGriddedImageView extends ImageView  {

   // private ArrayListModel plots = new ArrayListModel();

   // private SelectionInList plotSelection = new SelectionInList((ListModel) plots);

    private int NRows = 3;

    private int NCols = 3;

    private GridLayout layout;

    private JPanel gridPanel;


    public AbstractGriddedImageView(IImageDisplayModel imodel) {
        super(imodel);
        setLayout(new BorderLayout());
      
    }


    protected AbstractGriddedImageView(IImageDisplayModel imodel, int NRows, int NCols) {
        super(imodel);
        this.NRows = NRows;
        this.NCols = NCols;
        setLayout(new BorderLayout());
    }

    protected void layoutGrid() {
        //plots.clear();
        if (gridPanel == null) {
            gridPanel = new JPanel();
        } else {
            gridPanel.removeAll();
            remove(gridPanel);
        }

        layout = new GridLayout(getNRows(), getNCols());
        gridPanel.setLayout(layout);

        int count = 0;
        for (int i=0; i<getNRows(); i++) {
            for (int j=0; j<getNCols(); j++) {
                IImagePlot plot = makePlot(count++, i,j);
                //plots.add(plot);
                gridPanel.add(plot.getComponent());
            }

        }

        //plotSelection.setSelection(plots.get(0));
        add(gridPanel, BorderLayout.CENTER);
       

    }

    //public SelectionInList getPlotSelection() {
    //    return plotSelection;
   // }

    protected abstract IImagePlot makePlot(int index, int row, int column);


    public int getNRows() {
        return NRows;
    }

   

    public int getNCols() {
        return NCols;
    }


   // public int getNumPlots() {
   //     return plots.size();
   // }

   // public int getSelectedPlotIndex() {
   //     return plotSelection.getSelectionIndex();
   // }

  //  public IImagePlot whichPlot(Point p) {
  //     for (int i = 0; i < plots.size(); i++) {
   //         IImagePlot plot = (IImagePlot) plots.get(i);
  //
  //          Point plotPoint = SwingUtilities.convertPoint(this, p, plot.getComponent());
  //          if (plot.getComponent().contains(plotPoint)) {
  //              return plot;
   //         }

  //      }

  //      return null;
  //  }

   // public List<IImagePlot> getPlots() {
   //     List<IImagePlot> ret = new ArrayList();
   //     for (int i = 0; i < plots.size(); i++) {
    //        ret.add((IImagePlot) plots.get(i));
   //     }

   //     return ret;

    }




   



