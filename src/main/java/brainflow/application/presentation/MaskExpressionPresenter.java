package brainflow.application.presentation;

import brainflow.core.ImageView;
import brainflow.core.IImageDisplayModel;
import brainflow.core.IClipRange;
import brainflow.core.mask.*;
import brainflow.core.layer.*;
import brainflow.utils.IRange;
import brainflow.image.data.IImageData;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.dialog.ButtonPanel;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 26, 2008
 * Time: 3:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskExpressionPresenter extends ImageViewPresenter {

    private StatusBar status = new StatusBar();

    private LabelStatusBarItem statusLabel = new LabelStatusBarItem();

    private JTextField primaryField;

    private JTextArea expressionArea;

    private FormLayout layout;

    private JPanel mainPanel;

    private JPanel container;

    private JButton editThresholdButton;

    private JidePopup thresholdPopup;

    private JButton computeButton = new JButton("Compute");

    private ButtonPanel buttonPanel;

    private JButton freezeButton = new JButton("Freeze");
   
    private String lastExpression;


    public MaskExpressionPresenter() {
        buildGUI();
    }

    private void buildGUI() {
        primaryField = new JTextField(15);

        expressionArea = new JTextArea();
        expressionArea.setRows(8);
        expressionArea.setBorder(BorderFactory.createEtchedBorder());
        primaryField.setEditable(false);

        AbstractAction editAction = createPopupAction();
        editThresholdButton = new JButton(editAction);

        thresholdPopup = new JidePopup();
        thresholdPopup.getContentPane().setLayout(new BorderLayout());
        thresholdPopup.getContentPane().add(new ThresholdRangePresenter().getComponent(), BorderLayout.CENTER);


        layout = new FormLayout("3dlu, p, p:g, 3dlu, p, 3dlu", "3dlu, p, 5dlu, p, 3dlu, p, 3dlu, p, 5dlu, p, 20dlu");
        mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        CellConstraints cc = new CellConstraints();
        mainPanel.add(new JLabel("Primary Threshold:"), cc.xy(2, 2));
        mainPanel.add(primaryField, cc.xyw(2, 4, 2));
        mainPanel.add(editThresholdButton, cc.xy(5, 4));


        mainPanel.add(new JLabel("Mask Expression:"), cc.xy(2, 6));
        mainPanel.add(expressionArea, cc.xyw(2, 8, 4));


        buttonPanel = new ButtonPanel(SwingConstants.LEFT, ButtonPanel.SAME_SIZE);
        buttonPanel.addButton(computeButton);
        buttonPanel.addButton(freezeButton);

        mainPanel.add(buttonPanel, cc.xyw(2, 10, 2));
        //mainPanel.add(freezeButton, cc.xy(4, 10));


        
        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);


        container.add(status, BorderLayout.SOUTH);
        statusLabel.setText("Ready");
        status.add(statusLabel);


        initControls();


    }

    private void initControls() {
        computeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parseExpression();
            }
        });

        freezeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //todo hack cast
                                
                final ImageLayer3D layer = (ImageLayer3D)getSelectedLayer();

                if (layer != null) {
                    System.out.println("freeze action");
                    freezeButton.setEnabled(false);
                    Runnable runner = new Runnable() {
                        public void run() {
                            System.out.println("freeze started");
                            layer.getMaskProperty().reduce();
                            System.out.println("freeze finished");

                        }
                    };

                    Thread t = new Thread(runner);
                    t.start();
                }

                
            }
        });


        expressionArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                String ret = expressionArea.getText().trim();
                if (!ret.equals(lastExpression)) {
                    computeButton.setEnabled(true);
                }

            }

            public void removeUpdate(DocumentEvent e) {
                String ret = expressionArea.getText().trim();
                if (!ret.equals(lastExpression)) {
                    computeButton.setEnabled(true);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                String ret = expressionArea.getText().trim();
                if (!ret.equals(lastExpression)) {
                    computeButton.setEnabled(true);
                }
            }
        });
    }

    private AbstractAction createPopupAction() {
        AbstractAction action = new AbstractAction("...") {
            public void actionPerformed(ActionEvent e) {
                thresholdPopup.updateUI();
                thresholdPopup.setOwner(editThresholdButton);
                thresholdPopup.setResizable(false);

                if (thresholdPopup.isPopupVisible()) {
                    thresholdPopup.hidePopup();
                } else {
                    thresholdPopup.showPopup();
                }
            }
        };

        return action;
    }


    private final ThresholdListener threshListener = new ThresholdListener();

    @Override
    public void viewDeselected(ImageView view) {
        view.getModel().removeImageLayerListener(threshListener);

    }

    public void viewSelected(ImageView view) {
        updateThresholdString();

        view.getModel().addImageLayerListener(threshListener);

    }

    class ThresholdListener extends ImageLayerListenerImpl {
        public void thresholdChanged(ImageLayerEvent event) {
            updateThresholdString();

        }

    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void updateThresholdString() {
        String exprStr = convertThresholdToString(getSelectedLayer().getThreshold());
        primaryField.setText(exprStr);
    }

    @Override
    protected void layerSelected(ImageLayer layer) {
        updateThresholdString();

    }

    private String convertThresholdToString(IClipRange clip) {
        IRange range = clip.getInnerRange();

        //todo hack cast
        int index = getSelectedView().getModel().indexOf((ImageLayer3D) getSelectedLayer());
        String varName = "V" + (index + 1);

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        return "(" + varName + " < " + format.format(range.getMin()) + ")" + " and " + "(" + varName + " > " + format.format(range.getMax()) + ")";

    }

    public JComponent getComponent() {
        return container;
    }

    private BinaryExpressionParser createParser(final IImageDisplayModel model) {
        BinaryExpressionParser parser = new BinaryExpressionParser(new Context<IImageData>() {
            public IImageData getValue(String symbol) {
                int index = mapIndex(symbol);

                if (index < 0 || (index > model.getNumLayers() - 1)) {
                    throw new IllegalArgumentException("illegal layer index " + index);
                }

                return model.getLayer(index).getData();

            }


            private int mapIndex(String varName) {
                if (!varName.toUpperCase().startsWith("V")) {
                    throw new IllegalArgumentException("illegal variable name : " + varName);
                }

                String numberPart = varName.substring(1);
                return Integer.parseInt(numberPart) - 1;


            }
        });

        return parser;


    }


    public void parseExpression() {
        final IImageDisplayModel model = getSelectedView().getModel();
        BinaryExpressionParser parser = createParser(model);

        lastExpression = expressionArea.getText().trim();

        if (lastExpression.isEmpty()) {
            statusLabel.setText("Nothing to parse.");
            return;
        }

        try {


            INode node = parser.createParser().parse(lastExpression);

            RootNode root = new RootNode(node);
            VariableSubstitution varsub = new VariableSubstitution(model);
            RootNode vnode = varsub.start(root);

            MaskEvaluator masksub = new MaskEvaluator();
            RootNode res = masksub.start(vnode);

            statusLabel.setText("Parsed Expression: " + res.getChild());
            statusLabel.setToolTipText(res.getChild().toString());
            computeButton.setEnabled(false);

            MaskDataNode maskNode = (MaskDataNode) res.getChild();

            ImageLayer3D layer = (ImageLayer3D) getSelectedLayer();

            MaskProperty3D newmask = layer.getMaskProperty().setMask(IMaskProperty.MASK_KEY.EXPRESSION_MASK, maskNode.getData());
            layer.setMaskProperty(newmask);

            //ComparisonNode cnode = (ComparisonNode)node;
            //MaskDataNode masknode = (MaskDataNode)cnode.left();
            // System.out.println("cardinality " + masknode.getData().cardinality());


            statusLabel.setForeground(Color.BLACK);
            statusLabel.setText("Expression parsed successfully.");


        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(ex.getMessage());
            ex.printStackTrace();
            

        }

    }

}
