package brainflow.app.presentation;

import brainflow.app.toplevel.DisplayManager;
import brainflow.core.ImageView;
import brainflow.core.IBrainCanvas;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;
import net.java.dev.properties.binding.swing.adapters.SwingBind;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 14, 2007
 * Time: 12:14:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkedViewsPresenter extends ImageViewPresenter {

    private IBrainCanvas canvas;

    private JComboBox viewBox;

    private CheckBoxList linkedViewList;

    private JPanel form;


    public LinkedViewsPresenter() {
        // temporary
        canvas = DisplayManager.getInstance().getSelectedCanvas();
        // temporary
        buildGUI();
        bind();


    }

    public void bind() {
        SwingBind.get().bindContent(canvas.getImageCanvasModel().imageViewList, viewBox);
        SwingBind.get().bindIndex(canvas.getImageCanvasModel().listSelection, viewBox);

        //SwingBind.get().bindContent(canvas.getImageCanvasModel().imageViewList, linkedViewList);

    }

    private void buildGUI() {
        FormLayout layout = new FormLayout("6dlu, l:p:grow, 1dlu, 6dlu", "6dlu, p, 6dlu, min(p;60dlu), 6dlu");
        CellConstraints cc = new CellConstraints();

        form = new JPanel();
        form.setLayout(layout);
        viewBox = new JComboBox();
        linkedViewList = new CheckBoxList();

        form.add(viewBox, cc.xyw(2, 2, 2));
        form.add(new JScrollPane(linkedViewList), cc.xyw(2, 4, 2));

        /*viewBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateLinkedViewList();
            }
        });  */

    }

    private void updateLinkedViewList() {
        CheckBoxListSelectionModel cmodel = new CheckBoxListSelectionModel();
        DefaultListModel lmodel = new DefaultListModel();

        Iterator<ImageView> iter = canvas.getImageCanvasModel().imageViewList.iterator();

        Set<ImageView> viewSet = DisplayManager.getInstance().getYokedViews(getSelectedView());

        int count = 0;
        while (iter.hasNext()) {
            ImageView view = iter.next();
            if (view != getSelectedView()) {
                lmodel.addElement(view);
                if (viewSet.contains(view)) {
                    cmodel.addSelectionInterval(count, count);
                }
                count++;
            }
        }


        linkedViewList.setCheckBoxListSelectionModel(cmodel);
        linkedViewList.setModel(lmodel);

        cmodel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int f1 = e.getFirstIndex();
                int f2 = e.getLastIndex();
                CheckBoxListSelectionModel model = (CheckBoxListSelectionModel) e.getSource();
                ImageView view = getSelectedView();

                for (int i = f1; i <= f2; i++) {

                    if (model.isSelectedIndex(i)) {
                        DisplayManager.getInstance().yoke(view, (ImageView) model.getModel().getElementAt(i));

                    } else {
                        DisplayManager.getInstance().unyoke(view, (ImageView) model.getModel().getElementAt(i));

                    }
                }

            }

        });
    }

    protected void layerChangeNotification() {

    }

    protected void layerChanged(ListDataEvent event) {

    }

    public void allViewsDeselected() {

    }

    public void viewSelected(ImageView view) {
        updateLinkedViewList();
        bind();

    }

    @Override
    public void viewModelChanged(ImageView view) {
        updateLinkedViewList();
        bind();
    }



    public JComponent getComponent() {
        return form;
    }
}
