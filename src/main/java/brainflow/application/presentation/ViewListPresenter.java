package brainflow.application.presentation;

import brainflow.core.ImageView;
import brainflow.application.toplevel.BrainFlow;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 31, 2008
 * Time: 12:40:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewListPresenter extends ImageViewPresenter {


    private JList viewList;

    public ViewListPresenter() {
        initGUI();
    }

    private void initGUI() {
        viewList = new JList();
        viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (getSelectedView() != null) {
            populateList();

        }

        viewList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int f = e.getFirstIndex();
                int n = e.getLastIndex();
                for (int i=f; i<=n; i++) {
                    if (viewList.isSelectedIndex(i)) {
                        ImageView iview = (ImageView)viewList.getModel().getElementAt(i);
                        if (iview != getSelectedView()) {
                            BrainFlow.get().getSelectedCanvas().setSelectedView(iview);
                        }
                    }
                }
            }
        });
    }

    private void populateList() {
        List<ImageView> list = BrainFlow.get().getSelectedCanvas().getViews(getSelectedView().getModel());
        DefaultListModel model = new DefaultListModel();

        int i=0;
        for (ImageView view : list) {
            model.add(i, view);
            i++;
        }

        viewList.setModel(model);

        if (list.size() > 0) {
            viewList.setSelectedIndex(list.indexOf(getSelectedView()));
        }

    }

    public void viewSelected(ImageView imageView) {
        //System.out.println("view selected " + imageView);
        populateList();
    }

    public void allViewsDeselected() {
        viewList.setModel(new DefaultListModel());
    }

    public JComponent getComponent() {
        return viewList;
    }
}
