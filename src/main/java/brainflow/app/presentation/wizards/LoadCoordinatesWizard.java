package brainflow.app.presentation.wizards;

import com.jidesoft.wizard.*;
import com.jidesoft.dialog.PageList;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.swing.JideSwingUtilities;
import brainflow.app.presentation.ProjectListView;
import brainflow.app.toplevel.ProjectManager;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 14, 2007
 * Time: 6:20:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadCoordinatesWizard {

    private static final String WELCOME_TITLE = "Welcome to the Coordinates Wizard";

    private static final String REFERENCE_SPACE_TITLE = "Choose Reference Layer";
    private static final String DATASOURCE_TITLE = "Select Data Source";
    private static final String SETUPTABLE_TITLE = "Table Properties";
    private static final String SHOWTABLE_TITLE = "Table Entry";
    private static final String COMPLETION_TITLE = "Completing Coordinates Wizard";

    private WizardDialog wizard;

    private CoordinateTableSetupPresenter setupPresenter;

    private ProjectListView projectListView;

    private CoordinateTableView tableView;

    public LoadCoordinatesWizard() {
    }

    public WizardDialog showWizard(JFrame frame) {
        wizard = new WizardDialog(frame, "Coordinates Wizard");

        final WizardDialog wizard = new WizardDialog(frame, "JIDE Wizard Demo");
        wizard.setSize(550, 450);
        wizard.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        wizard.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                wizard.dispose();

            }
        });

        setupPresenter = new CoordinateTableSetupPresenter();
        projectListView = new ProjectListView(ProjectManager.getInstance().getActiveProject());

        // setup model
        PageList model = new PageList();

        model.append(new WelcomePage(WELCOME_TITLE, "A wizard for loading a set of 3D image coordinates"));
        model.append(new DataSourcePage(DATASOURCE_TITLE, ""));
        model.append(new ChooseReferencePage(REFERENCE_SPACE_TITLE, "Select reference space for coordinate table"));
        model.append(new SetupTablePage(SETUPTABLE_TITLE, "Set up coordinate table"));
        model.append(new ShowTablePage(SHOWTABLE_TITLE, "View and edit coordinate table"));
        CompletionPage cpage = new CompletionPage(COMPLETION_TITLE, "You have successfully loaded a set of image coordinates");


        JideSwingUtilities.setOpaqueRecursively(cpage, false);
        model.append(cpage);
        wizard.setPageList(model);


        wizard.setFinishAction(new AbstractAction("Finish") {
            public void actionPerformed(ActionEvent e) {
                if (wizard.closeCurrentPage()) {
                    wizard.dispose();

                }
            }
        });
        wizard.setCancelAction(new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent e) {
                if (wizard.closeCurrentPage()) {
                    wizard.dispose();

                }
            }
        });

        //wizard.pack();
        
        wizard.setResizable(true); // for wizard, it's better to make it not resizable.
        JideSwingUtilities.globalCenterWindow(wizard);
        wizard.setVisible(true);
        return wizard;


    }


    public static class WelcomePage extends WelcomeWizardPage {
        public WelcomePage(String title, String description) {
            super(title, description);


        }

        protected void initContentPane() {
            super.initContentPane();
            addText("This wizard will help you load or enter a set of coodinates for visualization");
            addSpace();
            addText("To continue, click Next.");
        }
    }


    public static class CompletionPage extends CompletionWizardPage {
        public CompletionPage(String title, String description) {
            super(title, description);
        }

        protected void initContentPane() {
            super.initContentPane();
            addSpace();
            addText("To close this wizard, click Finish.");
        }
    }

    public class ShowTablePage extends DefaultWizardPage {

        public ShowTablePage(String title, String description) {
            super(title, description);


        }

        public int getSelectedStepIndex() {
            return 4;
        }

        protected void initContentPane() {
            super.initContentPane();
            tableView = new CoordinateTableView(setupPresenter.getInfo());
            addComponent(new JScrollPane(tableView.getComponent()));
            JideSwingUtilities.setOpaqueRecursively(tableView.getComponent(), false);
        }

        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextPage();
        }

        private void updateNextPage() {

            getOwner().setNextPage(getOwner().getPageByTitle(COMPLETION_TITLE));

        }

    }


    public class ChooseReferencePage extends DefaultWizardPage {

        public ChooseReferencePage(String title, String description) {
            super(title, description);


        }



        protected void initContentPane() {
            super.initContentPane();

            addComponent(projectListView.getComponent());

        }

        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextPage();
        }

        private void updateNextPage() {

            getOwner().setNextPage(getOwner().getPageByTitle(SETUPTABLE_TITLE));

        }



    }


    public class SetupTablePage extends DefaultWizardPage {

        public SetupTablePage(String title, String description) {
            super(title, description);


        }

        public int getSelectedStepIndex() {
            return 3;
        }

        protected void initContentPane() {
            super.initContentPane();
            setupPresenter.getInfo().setCoordinateSpace(projectListView.getSelectedLayer().getCoordinateSpace());
            addComponent(setupPresenter.getComponent());
            
        }

        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextPage();
        }

        private void updateNextPage() {

            getOwner().setNextPage(getOwner().getPageByTitle(SHOWTABLE_TITLE));

        }

    }

    public static class DataSourcePage extends DefaultWizardPage {
        private JRadioButton _button1;
        private JRadioButton _button2;

        public DataSourcePage(String title, String description) {
            super(title, description);
        }

        /*public List getSteps() {
            List list = new ArrayList();
            list.add("Welcome to Coordinates Wizard");
            list.add("License Agreement");
            list.add("Change Next Page");
            list.add("...");
            return list;
        } */

        public int getSelectedStepIndex() {
            return 2;
        }

        protected void initContentPane() {
            super.initContentPane();
            addSpace();
            _button1 = new JRadioButton("Load coordinates from external file");
            _button2 = new JRadioButton("Enter coordinates into table by hand");
            _button1.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextPage();
                    }
                }
            });
            _button2.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextPage();
                    }
                }
            });
            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.add(_button1);
            _button1.setSelected(true);
            panel.add(_button2);
            ButtonGroup group = new ButtonGroup();
            group.add(_button1);
            group.add(_button2);
            addComponent(panel);
            JideSwingUtilities.setOpaqueRecursively(panel, false);
        }

        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextPage();
        }

        private void updateNextPage() {
            if (_button1.isSelected() && getOwner() != null) {
                getOwner().setNextPage(getOwner().getPageByTitle(REFERENCE_SPACE_TITLE));
            } else if (_button2.isSelected() && getOwner() != null) {
                getOwner().setNextPage(getOwner().getPageByTitle(REFERENCE_SPACE_TITLE));
            }


        }
    }


}
