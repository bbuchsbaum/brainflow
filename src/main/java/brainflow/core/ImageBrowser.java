package brainflow.core;

import brainflow.app.toplevel.ImageLayerFactory;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageDataSource;
import brainflow.image.io.ImageDataSource;
import com.jidesoft.status.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

    private JTree modelTreeView;

    private JPanel controlPanel;

    private SourceList sourceList;

    private ImageViewModel currentModel;

    private StatusBar status;

    private int index=0;


    public ImageBrowser(List<List<IImageDataSource>> sources) {
        setLayout(new BorderLayout());
        sourceList = new SourceList(sources);
        currentModel = sourceList.createModel(index);
        view = new OrthoImageView(currentModel, OrthoImageView.ORIENTATION.TRIANGULAR);
        add(view, BorderLayout.CENTER);

        initControlPanel();
    }

    private void initControlPanel() {
        controlPanel = new JPanel();
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        JTextField currentLabel = new JTextField(20);
        controlPanel.add(previousButton);
        controlPanel.add(currentLabel);
        controlPanel.add(nextButton);
        add(controlPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index == (sourceList.size()-1)) {
                    index = 0;
                } else {
                    index++;
                }
                currentModel = sourceList.createModel(index);
                view.setModel(currentModel);
            }
        });
    }

    class SourceList {

        private List<List<IImageDataSource>> sourceList;

        public SourceList(List<List<IImageDataSource>> _sourceList) {
            sourceList = _sourceList;
        }

        public ImageViewModel createModel(int index) {
            List<IImageDataSource> slist = sourceList.get(index);
            List<ImageLayer3D> layers = ImageLayerFactory.createImageLayerList(slist);
            return new ImageViewModel("untitled", layers);

        }

        public int size() {
            return sourceList.size();
        }
    }


    public static void main(String[] args) {
        List<IImageDataSource> dsource1 = BrainIO.loadDataSources(new File("/home/brad/data/sub17017/anat/mprage_anonymized.nii.gz"),
                                new File("/home/brad/data/sub29158/anat/mprage_anonymized.nii.gz"));

        List<IImageDataSource> dsource2 = BrainIO.loadDataSources(new File("/home/brad/data/sub30072/anat/mprage_anonymized.nii.gz"),
                                new File("/home/brad/data/sub54329/anat/mprage_anonymized.nii.gz"));

        List<List<IImageDataSource>> sourceList = Arrays.asList(dsource1, dsource2);

        ImageBrowser browser = new ImageBrowser(sourceList);
        JFrame frame = new JFrame();
        frame.add(browser, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);



    }




}



