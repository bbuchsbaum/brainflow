package brainflow.core;

import brainflow.app.toplevel.ImageLayerFactory;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageSource;
import com.jidesoft.dialog.ButtonPanel;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.apache.commons.vfs.VFS;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 7, 2010
 * Time: 9:12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageBrowser extends JPanel {

    private ImageView view;

    private JList sourceView;

    private SourceList sourceList;

    private ImageViewModel currentModel;



    public ImageBrowser(List<IImageSource> sources) {
        setLayout(new BorderLayout());
        sourceList = new SourceList(sources);
        currentModel = sourceList.createModel(0);
        view = new OrthoImageView(currentModel, OrthoImageView.ORIENTATION.TRIANGULAR);
        add(view, BorderLayout.CENTER);


        initSourceView();
    }

    private void initSourceView() {
        sourceView = new JList();
        final DefaultListModel model = new DefaultListModel();
        for (IImageSource source : sourceList.sourceList) {
            model.addElement(source);
        }

        sourceView.setModel(model);
        sourceView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ButtonPanel panel = new ButtonPanel(SwingConstants.CENTER);

        JButton nextButton = new JButton("Next");
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/control_play_blue.png"));
        nextButton.setIcon(icon);

        JButton prevButton = new JButton("Previous");
        icon = new ImageIcon(getClass().getClassLoader().getResource("icons/control_rev_blue.png"));
        prevButton.setIcon(icon);
        panel.addButton(prevButton);
        panel.addButton(nextButton);
        panel.setSizeConstraint(ButtonPanel.SAME_SIZE);

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new TitledBorder("Image List"));
        container.add(new JScrollPane(sourceView), BorderLayout.CENTER);
        container.add(panel, BorderLayout.SOUTH);
        add(container, BorderLayout.WEST);


        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = sourceView.getSelectedIndex();
                if (index == (sourceList.size() - 1)) {
                    index = 0;
                } else {
                    index++;
                }

                updateView(index);

            }
        });

        sourceView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = sourceView.getSelectedIndex();
                if (currentModel.getSelectedLayer().getDataSource() != sourceView.getSelectedValue()) {
                    System.out.println("updating view");
                    updateView(index);
                } else {
                    System.out.println("not updating view ");
                }


            }
        });
    }


    private void updateView(int index) {
         currentModel = sourceList.createModel(index);
         view.setModel(currentModel);
         sourceView.setSelectedIndex(index);

    }




    class SourceList {

        private List<IImageSource> sourceList;

        public SourceList(List<IImageSource> _sourceList) {
            sourceList = _sourceList;
        }

        public ImageViewModel createModel(int index) {
            IImageSource source = sourceList.get(index);
            ImageLayer3D layer = ImageLayerFactory.createImageLayer(source);
            return new ImageViewModel("untitled", layer);

        }

        public int size() {
            return sourceList.size();
        }
    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        //com.jidesoft.plaf.LookAndFeelFactory.installDefaultLookAndFeel();
        //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2007_STYLE);

        URL url1 = BF.getDataURL("data/icbm452_atlas_probability_insula.hdr");
        URL url2 = BF.getDataURL("data/icbm452_atlas_probability_white.hdr");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            IImageSource dsource1 = BrainIO.loadDataSource(VFS.getManager().resolveFile(url1.toURI().toString()));
            IImageSource dsource2 = BrainIO.loadDataSource(VFS.getManager().resolveFile(url2.toURI().toString()));
            ImageBrowser browser = new ImageBrowser(Arrays.asList(dsource1, dsource2));
            JFrame frame = new JFrame();
            frame.add(browser, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }


        //List<IImageDataSource> dsource2 = BrainIO.loadDataSources(new File("/home/brad/data/sub30072/anat/mprage_anonymized.nii.gz"),
        //                         new File("/home/brad/data/sub54329/anat/mprage_anonymized.nii.gz"));

        // List<List<IImageDataSource>> sourceList = Arrays.asList(dsource1, dsource2);

        // ImageBrowser browser = new ImageBrowser(sourceList);
        //  JFrame frame = new JFrame();
        // frame.add(browser, BorderLayout.CENTER);
        //  frame.pack();
        // frame.setVisible(true);


    }


}


                                                                                                                                                                  