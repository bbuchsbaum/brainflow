package brainflow.app.toplevel;

import brainflow.app.*;
import brainflow.app.dnd.BrainCanvasTransferHandler;
import brainflow.app.actions.*;
import brainflow.app.presentation.*;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.gui.IActionProvider;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.data.IImageData;
import brainflow.image.io.BrainIO;
import brainflow.image.io.IImageSource;
import brainflow.gui.ExceptionDialog;
import brainflow.utils.AbstractBuilder;
import brainflow.utils.StopWatch;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentComponentAdapter;
import com.jidesoft.document.DocumentComponentEvent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.pane.FloorTabbedPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.StatusBarItem;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideToggleButton;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.GuiCommands;
import com.pietschy.command.configuration.ParseException;
import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.group.ExpansionPointBuilder;
import com.pietschy.command.group.GroupVisitor;
import com.pietschy.command.toggle.ToggleGroup;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.factory.ButtonFactory;
import com.pietschy.command.factory.MenuFactory;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.net.URI;


/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 9:00:59 AM
 * To change this template use File | Settings | File Templates.
 */

public class BrainFlow {

    static {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
    }


    private SplashScreen splash;

    private Graphics2D splashGraphics;


    private final static Logger log = Logger.getLogger(BrainFlow.class.getCanonicalName());

    private BrainFrame brainFrame = null;


    private RecentPathMenu pathMenu = new RecentPathMenu();

    private FavoritesMenu favoritesMenu = new FavoritesMenu();

    private DocumentPane documentPane = new DocumentPane();

    private ImageFileExplorer loadingDock = null;

    private StatusBar statusBar;

    private CommandContainer commandContainer;

    private ExecutorService threadService = Executors.newCachedThreadPool();

    protected BrainFlow() {
        // Exists only to thwart instantiation.
    }

    public static BrainFlow get() {
        return (BrainFlow) SingletonRegistry.REGISTRY.getInstance("brainflow.app.toplevel.BrainFlow");
    }


    public static void main(String[] args) {


        final BrainFlow bflow = get();

        //Class myClass = BrainFlow.class;
        //URL url = myClass.getResource("BrainFlow.class");
        //System.out.println("class located: " + url);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    log.info("Launching BrainFlow ...");
                    bflow.launch();
                } catch (Throwable e) {
                    Logger.getAnonymousLogger().severe("Error Launching BrainFlow, exiting");
                    e.printStackTrace();
                    System.exit(-1);

                }

            }
        });

    }

    private void openSplash() {
        splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            log.warning("Error: no splash image specified on the command line");
            return;
        }

        // compute base positions for text and progress bar
        Dimension splashSize = splash.getSize();
        textY = splashSize.height - 20;
        barY = splashSize.height - 30;

        splashGraphics = splash.createGraphics();
    }


    private static final int X = 20, W = 250;
    private static final int TEXT_H = 10, BAR_H = 20;
    private static final int NUM_BUBBLES = 10;

    private int textY, barY;
    private int barPos = 0;


    public void closeSplash() {
        if (splash != null) {
            splash.close();
        }
    }

    public void drawSplashProgress(String msg) {
        if (splashGraphics == null) return;

        // clear what we don't need from previous state
        splashGraphics.setComposite(AlphaComposite.Clear);
        splashGraphics.fillRect(X, textY, W, TEXT_H + 20);
        splashGraphics.setPaintMode();

        // draw message
        splashGraphics.setColor(Color.WHITE);
        splashGraphics.drawString(msg, X, textY + TEXT_H);


        if (splash.isVisible())
            splash.update();

    }


    public JFrame getApplicationFrame() {
        return brainFrame;
    }


    private void initLookAndFeel() {
        try {


            String osname = System.getProperty("os.name");
            System.out.println("os name is : " + osname);
            if (osname.toUpperCase().contains("WINDOWS")) {
                log.info("windows");

                com.jidesoft.plaf.LookAndFeelFactory.installDefaultLookAndFeel();
                LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2007_STYLE);
                //((Office2003Painter) Office2003Painter.getInstance()).setColorName("Metallic");

                //UIManager.setLookAndFeel(new NimbusLookAndFeel());
                //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);




            } else if (osname.toUpperCase().contains("LINUX")) {
                //UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);

            } else if (osname.toUpperCase().contains("MAC")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BrainFlow");
                System.setProperty("com.apple.macos.useScreenMenuBar", "true");
                System.setProperty("apple.awt.graphics.UseQuartz", "true");
                System.setProperty("apple.awt.brushMetalLook", "true");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                //UIManager.setLookAndFeel(new NimbusLookAndFeel());
                //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
                LookAndFeelFactory.installJideExtension(1);

            }

        } catch (UnsupportedLookAndFeelException e) {
            log.severe("could not createSource look and feel");
        } catch (Throwable ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                LookAndFeelFactory.installJideExtension();

            } catch (Throwable ex2) {
                log.severe("could not createSource look and feel");
                throw new RuntimeException("failed to initialize look and feel", ex2);
            }

        }

    }


    public void launch() throws Throwable {
        launch(new LaunchConfiguration().build());
    }


    public void launch(LaunchConfiguration config) throws Throwable {
        openSplash();
        drawSplashProgress("loading look and feel");
        initLookAndFeel();

        StopWatch clock = new StopWatch();
        clock.start("launch");
        clock.start("construct brainframe");
        drawSplashProgress("creating frame ...");


        brainFrame = new BrainFrame();
        statusBar = new StatusBar();

        brainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clock.stopAndReport("construct brainframe");


        drawSplashProgress("initializing DisplayManager ...");
        DisplayManager.get().newCanvas();


        clock.start("createSource commands");
        drawSplashProgress("loading commands ...");
        loadCommands();
        clock.stopAndReport("createSource commands");

        drawSplashProgress("binding container ...");
        bindContainer();

        //drawSplashProgress("initializing IO ...");
        //clock.start("init io");
        //initImageIO();
        //clock.stopAndReport("init io");


        clock.start("workspace");
        drawSplashProgress("initializing work space ...");
        initializeWorkspace();
        clock.stopAndReport("workspace");


        clock.start("toolbar");
        drawSplashProgress("initializing tool bar ...");
        initializeToolBar();
        clock.stopAndReport("toolbar");

        clock.start("menu");
        drawSplashProgress("initializing menu ...");
        initializeMenu();
        clock.stopAndReport("menu");

        initExceptionHandler();
        clock.stopAndReport("launch");

        clock.start("initialize resources");
        drawSplashProgress("initializing resources ...");
        initializeResources();
        clock.stopAndReport("initialize resources");

        drawSplashProgress("initializing status bar ...");
        clock.start("status bar");
        initializeStatusBar();
        clock.stopAndReport("status bar");



        brainFrame.setVisible(true);

        mountInitialDirectories(config.mountPoints);

        for(List<FileObject> flist : config.datasets) {
            List<IImageSource<IImageData>> sourceList = BrainIO.loadDataSources(flist);
            ImageViewModel model = ImageViewFactory.createModel("untitled", sourceList);
            displayView(ImageViewFactory.createAxialView(model));

        }



    }

    private void mountInitialDirectories(List<FileObject> mountPoints) {
        for (FileObject mp : mountPoints) {
            DirectoryManager.getInstance().mountFileSystem(mp);
        }

    }


    private boolean loadCommands() {
        try {

            GuiCommands.load("commands/ImageViewCommands");
            GuiCommands.load("commands/BrainFlowCommands");


        } catch (ParseException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }

        return true;
    }

    public CommandContainer getCommandContainer() {
        return commandContainer;
    }

    private void bindContainer() {
        commandContainer = new CommandContainer();
        commandContainer.bind(brainFrame);

        CommandGroup imageViewGroup = new CommandGroup("image-view-menu");
        imageViewGroup.bind(brainFrame);
    }

    private void initializeLogging() {

    }

    private MenuFactory createMenuFactory() {
        return new MenuFactory() {
            @Override
            public JMenu createMenu() {
                return new JideMenu();
            }

            @Override
            public JMenuItem createMenuItem() {
                return new JMenuItem();
            }

            @Override
            public JCheckBoxMenuItem createCheckBoxMenuItem() {
                return new JCheckBoxMenuItem();
            }

            @Override
            public JRadioButtonMenuItem createRadioButtonMenuItem() {
                return new JRadioButtonMenuItem();
            }

            @Override
            public JPopupMenu createPopupMenu() {
                return new JPopupMenu();
            }

            @Override
            public JMenuBar createMenuBar() {
                return new CommandMenuBar();
            }
        };

    }


    private ButtonFactory createToolBarButtonFactory() {
        return new ButtonFactory() {
            @Override
            public JButton createButton() {
                JideButton button = new JideButton();
                button.setButtonStyle(JideButton.TOOLBAR_STYLE);
                button.setText("");
                return button;
            }

            @Override
            public AbstractButton createToggleButton() {
                return new JideToggleButton();

            }

            @Override
            public JCheckBox createCheckBox() {
                return new JCheckBox();
            }

            @Override
            public JRadioButton createRadioButton() {
                return new JRadioButton();
            }
        };


    }


    private void initializeMenu() {
        log.info("initializing Menu");

        GuiCommands.defaults().setButtonFactory(createToolBarButtonFactory());
        GuiCommands.defaults().setMenuFactory(createMenuFactory());
        //GuiCommands.defaults().

        GoToVoxelCommand gotoVoxelCommand = new GoToVoxelCommand();
        gotoVoxelCommand.bind(getApplicationFrame());
        gotoVoxelCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CommandGroup fileMenuGroup = new CommandGroup("file-menu");
        fileMenuGroup.bind(getApplicationFrame());

        CommandGroup viewMenuGroup = new CommandGroup("view-menu");
        viewMenuGroup.bind(getApplicationFrame());

        CommandGroup gotoMenuGroup = new CommandGroup("goto-menu");
        gotoMenuGroup.bind(getApplicationFrame());


        CommandBar menuBar = new CommandMenuBar();
        menuBar.setBorder(new EmptyBorder(2, 2, 2, 2));
        //menuBar.setBorder(new LineBorder(Color.black, 1));
        menuBar.setStretch(true);

        // for nimbus look and feel
        menuBar.setPaintBackground(false);
        // for nimbus look and feel

        menuBar.add(fileMenuGroup.createMenuItem());
        menuBar.add(viewMenuGroup.createMenuItem());
        menuBar.add(gotoMenuGroup.createMenuItem());
        menuBar.setKey("menu");
        brainFrame.getDockableBarManager().addDockableBar(menuBar);

        MountFileSystemCommand mountFileSystemCommand = new MountFileSystemCommand();
        mountFileSystemCommand.bind(getApplicationFrame());

        ExitApplicationCommand exitCommand = new ExitApplicationCommand();
        exitCommand.bind(getApplicationFrame());

        ExpansionPointBuilder builder = fileMenuGroup.getExpansionPointBuilder();
        builder.add(pathMenu.getCommandGroup());
        builder.applyChanges();

        menuBar.add(DockWindowManager.getInstance().getDockMenu());

        JMenuItem favMenu = favoritesMenu.getCommandGroup().createMenuItem();
        favMenu.setMnemonic('F');
        menuBar.add(favMenu);


    }

    private void initializeStatusBar() {
        SelectedViewStatus viewStatus = new SelectedViewStatus();
        log.info("initializing status bar");
        statusBar.setAutoAddSeparator(false);
        statusBar.setChildrenOpaque(true);

        statusBar.add(viewStatus.getComponent(), JideBoxLayout.FIX);
        statusBar.add(new com.jidesoft.status.StatusBarSeparator(), JideBoxLayout.FIX);

        LabelStatusBarItem crossLabel = new LabelStatusBarItem();
        crossLabel.setText("Cross: ");
        crossLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        statusBar.add(crossLabel, JideBoxLayout.FIX);

        CursorCoordinates cursorCoordinates = new CursorCoordinates();
        CrosshairCoordinates crosshairCoordinates = new CrosshairCoordinates();

        statusBar.add(crosshairCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getZaxisLabel(), JideBoxLayout.FIX);
        statusBar.addSeparator();
        statusBar.add(new ValueStatusItem.Crosshair(), JideBoxLayout.FIX);


        LabelStatusBarItem cursorLabel = new LabelStatusBarItem();
        cursorLabel.setText("Cursor: ");
        cursorLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        statusBar.add(cursorLabel, JideBoxLayout.FIX);


        statusBar.addSeparator();
        statusBar.add(cursorCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getZaxisLabel(), JideBoxLayout.FIX);

        statusBar.addSeparator();

        statusBar.add(new ValueStatusItem.Mouse(), JideBoxLayout.FIX);
        statusBar.addSeparator();

        LabelStatusBarItem zoomLabel = new LabelStatusBarItem();
        zoomLabel.setText("Magnify: ");
        zoomLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        statusBar.add(zoomLabel, JideBoxLayout.FIX);

        ImageViewZoomer zoomer = new ImageViewZoomer();
        zoomer.getComponent().setOpaque(false);
        zoomer.getComponent().setBorder(new EmptyBorder(0, 0, 0, 0));

        statusBar.add(new SliderStatusBarItem((JSlider) zoomer.getComponent()), JideBoxLayout.FIX);

        statusBar.add(new LabelStatusBarItem(), JideBoxLayout.VARY);
        //statusBar.add(new com.jidesoft.status.ProgressStatusBarItem(), JideBoxLayout.FIX);

        statusBar.add(new com.jidesoft.status.MemoryStatusBarItem(), JideBoxLayout.FIX);
        statusBar.setMinimumSize(new Dimension(10, 100));
        brainFrame.getDockableBarManager().getMainContainer().add(statusBar, "South");
        statusBar.resetToPreferredSizes();

    }

    private void bindCommand(ActionCommand command, boolean installShortCut) {
        command.bind(getApplicationFrame());
        if (installShortCut) {
            command.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        }
    }


    private void initializeToolBar() {


        CommandGroup mainToolbarGroup = new CommandGroup("main-toolbar");
      

        mainToolbarGroup.bind(getApplicationFrame());


        ToggleGroup interpToggleGroup = new ToggleGroup("toggle-interp-group");
        interpToggleGroup.bind(getApplicationFrame());

        bindCommand(new OpenImageCommand(), true);
        bindCommand(new SnapshotCommand(), true);
        bindCommand(new NewCanvasCommand(), true);

        bindCommand(new CreateAxialViewCommand(), true);
        bindCommand(new CreateSagittalViewCommand(), true);

        bindCommand(new CreateCoronalViewCommand(), true);
        bindCommand(new CreateMontageViewCommand(), true);
        bindCommand(new CreateVerticalOrthogonalCommand(), true);

        bindCommand(new CreateHorizontalOrthogonalCommand(), true);
        bindCommand(new CreateTriangularOrthogonalCommand(), true);


        CommandGroup orthoGroup = new CommandGroup("ortho-view-group");
        orthoGroup.bind(getApplicationFrame());


        final NextSliceCommand nextSliceCommand = new NextSliceCommand();
        bindCommand(nextSliceCommand, false);

        final PreviousSliceCommand previousSliceCommand = new PreviousSliceCommand();
        bindCommand(previousSliceCommand, false);


        bindCommand(new PageBackSliceCommand(), true);
        bindCommand(new PageForwardSliceCommand(), true);

        bindCommand(new IncreaseContrastCommand(), true);
        bindCommand(new DecreaseContrastCommand(), true);


        bindCommand(new NearestInterpolationToggleCommand(), true);
        bindCommand(new LinearInterpolationToggleCommand(), true);
        bindCommand(new CubicInterpolationToggleCommand(), true);
        bindCommand(new ToggleAxisLabelCommand(), true);
        bindCommand(new ToggleCrossCommand(), true);

        //JToolBar mainToolbar = mainToolbarGroup.createToolBar();
        final CommandBar mainToolbar = new CommandBar();
        // for nimbus look and feel
               mainToolbar.setPaintBackground(false);
               // for nimbus look and feel

        mainToolbar.setBorder(new EmptyBorder(0, 0, 0, 0));
        final ButtonFactory buttonFactory = createToolBarButtonFactory();

        mainToolbarGroup.visitMembers(new GroupVisitor() {
            @Override
            public void visit(ActionCommand actionCommand) {
                JideButton jb = new JideButton(actionCommand.getActionAdapter());
                jb.setButtonStyle(JideButton.TOOLBAR_STYLE);
                jb.setText("");
                mainToolbar.add(jb);
            }

            @Override
            public void visit(CommandGroup commandGroup) {
                JComponent jc = commandGroup.createButton(buttonFactory);
                mainToolbar.add(jc);
            }
        });

        mainToolbar.setKey("toolbar");
        brainFrame.getDockableBarManager().addDockableBar(mainToolbar);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {

                if (event.getID() == KeyEvent.KEY_PRESSED) {

                    KeyEvent ke = (KeyEvent) event;
                    Component comp = ke.getComponent();
                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                        ImageView view = BrainFlow.get().getSelectedView();

                        if (/*view.hasFocus() || */ parentIsImageView(comp)) {
                            previousSliceCommand.execute();
                        }
                    } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                        ImageView view = BrainFlow.get().getSelectedView();

                        if ( /*view.hasFocus() */ parentIsImageView(comp)) {
                            nextSliceCommand.execute();
                        } else {
                            System.out.println("no focus");
                        }
                    }

                }


            }
        }, AWTEvent.KEY_EVENT_MASK);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == MouseEvent.MOUSE_RELEASED) {
                    MouseEvent me = (MouseEvent) event;
                    if (me.isPopupTrigger()) {
                        showActionMenu(me);
                    }
                }

            }
        }, AWTEvent.MOUSE_EVENT_MASK);


    }

    private boolean parentIsImageView(Component c) {
        if (c instanceof ImageView) {
            return true;
        }

        while (c != null) {
            Container comp = c.getParent();
            if (comp instanceof ImageView) {
                return true;
            }

            c = c.getParent();
        }

        return false;

    }

    private JPopupMenu createPopup(java.util.List<Action> actionList) {
        JPopupMenu menu = new JPopupMenu();
        for (Action action : actionList) {
            menu.add(action);
        }

        return menu;
    }

    private void showActionMenu(MouseEvent e) {
        Component c = SwingUtilities.getDeepestComponentAt(e.getComponent(), e.getX(), e.getY());
        java.util.List<Action> actionList = new ArrayList<Action>();
        while (true) {
            if (c instanceof IActionProvider) {
                IActionProvider provider = (IActionProvider) c;
                provider.addActions(e, actionList);
            } else if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;

                Object provider = jc.getClientProperty(IActionProvider.KEY);
                if (provider != null) {
                    ((IActionProvider) provider).addActions(e, actionList);
                }
            }


            Component p = c.getParent();
            if (p != null) {
                c = p;
            } else {
                break;
            }


        }

        if (actionList.size() > 0) {
            createPopup(actionList).setVisible(true);
        }


    }

    private void initCanvas() {
        BrainCanvasTransferHandler handler = new BrainCanvasTransferHandler();
        DisplayManager.get().getSelectedCanvas().getComponent().setTransferHandler(handler);

    }


    public void mountDirectory(FileObject dir) {
        try {
            if (dir.getType() != FileType.FOLDER) {
                throw new IllegalArgumentException("argument " + dir + " is not a directory");
            }
            DirectoryManager.getInstance().mountFileSystem(dir);

        } catch(FileSystemException e) {
            throw new RuntimeException(e);
        }
    }


    public void addCanvas(IBrainCanvas canvas) {
        //todo sync with DisplayManager?
        JComponent comp = canvas.getComponent();
        comp.setRequestFocusEnabled(true);
        comp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BrainCanvasTransferHandler handler = new BrainCanvasTransferHandler();
        comp.setTransferHandler(handler);
        CanvasBar cbar = new CanvasBar();


        canvas.getComponent().add(cbar.getComponent(), BorderLayout.NORTH);
        String canvasName = "Canvas-" + (documentPane.getDocumentCount() + 1);
        documentPane.openDocument(createCanvasDocument(canvas, canvasName));
        documentPane.setActiveDocument(canvasName);
        DisplayManager.get().setSelectedCanvas(canvas);

    }

    private DocumentComponent createCanvasDocument(final IBrainCanvas canvas, String name) {
        final DocumentComponent doc = new DocumentComponent(new JScrollPane(canvas.getComponent()), name);
        doc.addDocumentComponentListener(new DocumentComponentAdapter() {
            @Override
            public void documentComponentActivated(DocumentComponentEvent documentComponentEvent) {
                DisplayManager.get().setSelectedCanvas(canvas);
            }
        });

        return doc;
    }


    private void initializeWorkspace() throws Exception {
        StopWatch watch = new StopWatch();

        watch.start("laying out workspace");
        log.info("initializing workspace");

        //_frame.getDockableBarManager().getMainContainer().setLayout(new BorderLayout());
        //_frame.getDockableBarManager().getMainContainer().add(_tabbedPane, BorderLayout.CENTER);


        //brainFrame.getMainPanel().add(documentPane, BorderLayout.CENTER);
        brainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        brainFrame.getDockingManager().getWorkspace().add(documentPane, BorderLayout.CENTER);


        brainFrame.getDockingManager().beginLoadLayoutData();
        brainFrame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);


        JComponent canvas = DisplayManager.get().getSelectedCanvas().getComponent();
        canvas.setRequestFocusEnabled(true);
        canvas.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        watch.stopAndReport("laying out workspace");
        watch.start("opening document");
        documentPane.setTabPlacement(DocumentPane.BOTTOM);
        documentPane.openDocument(createCanvasDocument(DisplayManager.get().getSelectedCanvas(), "Canvas-1"));
        documentPane.setActiveDocument("Canvas-1");
        watch.stopAndReport("opening document");


        log.info("initializing loading dock");
        watch.start("init loading dock");
        initLoadingDock();
        watch.stopAndReport("init loading dock");
        log.info("initializing project view");
        watch.start("init project view");
        initProjectView();
        watch.stopAndReport("init project view");
        log.info("initializing image table view");
        watch.start("init ploadable image table view");
        initLoadableImageTableView();
        watch.stopAndReport("init ploadable image table view");
        log.info("initializing control panel");
        watch.start("init control panel");
        initControlPanel();
        initCoordinatePanel();
        watch.stopAndReport("init control panel");
        log.info("initializing event monitor");
        watch.start("event bus monitor");
        initEventBusMonitor();
        log.info("initializing log monitor");
        watch.stopAndReport("event bus monitor");

        watch.start("log monitor");
        initLogMonitor();
        watch.stopAndReport("log monitor");

        watch.start("layout docks");


        brainFrame.getDockableBarManager().loadLayoutData();
        // brainFrame.toFront();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        brainFrame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 50);

        watch.stopAndReport("layout docks");

        watch.start("canvas bar");
        CanvasBar cbar = new CanvasBar();
        canvas.add(cbar.getComponent(), BorderLayout.NORTH);
        watch.stopAndReport("canvas bar");

        brainFrame.getDockingManager().loadLayoutData();

    }


    private void initLoadingDock() {
        try {


            loadingDock = new ImageFileExplorer(VFS.getManager().resolveFile(DirectoryManager.getInstance().getCurrentLocalDirectory().getAbsolutePath()));
            BrainCanvasTransferHandler handler = new BrainCanvasTransferHandler();

            loadingDock.getJTree().setDragEnabled(true);
            //loadingDock.setTransferHandler(handler);
            DisplayManager.get().getSelectedCanvas().getComponent().setTransferHandler(handler);

            DirectoryManager.getInstance().addFileSystemEventListener(new FileSystemEventListener() {
                public void eventOccurred(FileSystemEvent event) {
                    final FileObject fobj = event.getFileObject();

                    if (fobj != null) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                loadingDock.addFileRoot(fobj);
                            }
                        });

                    }

                }
            });

            SearchableImageFileExplorer explorer = new SearchableImageFileExplorer(loadingDock);

            DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("File Manager",
                    "icons/fldr_obj.gif",
                    DockContext.STATE_FRAMEDOCKED,
                    DockContext.DOCK_SIDE_WEST);

            //explorer.getComponent().setPreferredSize(new Dimension(400,200));
            dframe.setPreferredSize(new Dimension(275, 400));

            //JScrollPane jsp = new JScrollPane(explorer.getComponent());
            //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            dframe.getContentPane().add(explorer.getComponent());
            brainFrame.getDockingManager().addFrame(dframe);

        } catch (FileSystemException e) {
            // this would be more or less fatal, no?
            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    private void initProjectView() throws IOException {

        ViewListPresenter viewListPresenter = new ViewListPresenter();

        ProjectTreeView projectTreeView = new ProjectTreeView(ProjectManager.get().getActiveProject());
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Project",
                "icons/folder_page.png",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_WEST, 2);


        JideSplitPane splitPane = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);
        splitPane.addPane(new JScrollPane(projectTreeView.getComponent()));

        viewListPresenter.getComponent().setPreferredSize(new Dimension(275, 80));
        splitPane.addPane(new JScrollPane(viewListPresenter.getComponent()));

        dframe.getContentPane().add(splitPane);


        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initLoadableImageTableView() throws IOException {

        LoadableImageTableView loadView = new LoadableImageTableView();
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Loaded Images",
                "icons/det_pane_hide.gif",
                DockContext.STATE_AUTOHIDE_SHOWING,
                DockContext.DOCK_SIDE_WEST,
                1);

        dframe.getContentPane().add(new JScrollPane(loadView.getComponent()));
        dframe.setPreferredSize(new Dimension(275, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initLogMonitor() {
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Log Monitor",
                "icons/console_view.gif",
                DockContext.STATE_AUTOHIDE,
                DockContext.DOCK_SIDE_SOUTH,
                1);


        LogMonitor monitor = new LogMonitor();
        monitor.setLevel(Level.FINEST);
        LogManager.getLogManager().getLogger("").addHandler(monitor);
        dframe.getContentPane().add(new JScrollPane(monitor.getComponent()));

        dframe.setPreferredSize(new Dimension(800, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initEventBusMonitor() {
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Event Monitor",
                "icons/console_view.gif",
                DockContext.STATE_AUTOHIDE,
                DockContext.DOCK_SIDE_SOUTH,
                1);


        dframe.getContentPane().add(new JScrollPane(new EventBusMonitor().getComponent()));

        dframe.setPreferredSize(new Dimension(800, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private void initCoordinatePanel() {
        CoordinateControls coordinateControls = new CoordinateControls();
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Coordinates",
                //"icons/types.gif",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_EAST);

        FloorTabbedPane floorPane = new FloorTabbedPane();
        floorPane.addTab("Navigation", coordinateControls.getComponent());

        dframe.getContentPane().add(floorPane);

        dframe.setPreferredSize(new Dimension(300, 500));
        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initControlPanel() {

        
        //JideTabbedPane tabbedPane = new JideTabbedPane();
        JTabbedPane tabbedPane = new JTabbedPane();

        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Tool Box",
                "icons/types.gif",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_EAST);


        ColorAdjustmentControl colorAdjustmentControl = new ColorAdjustmentControl();

        LayerInfoControl layerInfoControl = new LayerInfoControl();

        MaskControl maskControl = new MaskControl();

        tabbedPane.addTab("Adjust", new JScrollPane(colorAdjustmentControl.getComponent()));
        tabbedPane.addTab("Mask", maskControl.getComponent());
        tabbedPane.addTab("Info", new JScrollPane(layerInfoControl.getComponent()));
        tabbedPane.addTab("Clustering", new ClusterPresenter().getComponent());

        dframe.getContentPane().add(tabbedPane);

        dframe.setPreferredSize(new Dimension(300, 500));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private boolean initializeResources() {
        log.info("initializing resources");
        ResourceManager.getInstance().getColorMaps();
        return true;
    }


    private void register(IImageSource dataSource) {
        DataSourceManager manager = DataSourceManager.get();
        boolean alreadyRegistered = manager.isRegistered(dataSource);

        try {
            if (alreadyRegistered) {
                long lastModified = dataSource.getDataFile().getContent().getLastModifiedTime();
                long lastRead = dataSource.whenRead();

                if (lastModified > lastRead) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Image " + dataSource.getDataFile().getName().getBaseName());
                    sb.append("has already been loaded in BrainFlow, but has changed on disk.  Would you like to reload?");

                    Integer ret = JOptionPane.showConfirmDialog(brainFrame, sb.toString(), "Image has been modified", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (ret == JOptionPane.YES_OPTION) {
                        dataSource.releaseData();
                    }
                } else {
                    log.info(dataSource.getStem() + " is already registered ...");
                }

            } else {
                manager.register(dataSource);
            }
        } catch (FileSystemException e) {
            JOptionPane.showMessageDialog(brainFrame, "Error accessing file information: " + e.getMessage());
        }

    }

    //public IBrainCanvas getSelectedCanvas() {
    //    return DisplayManager.get().getSelectedCanvas();
    //}

    private IImageSource specialHandling(IImageSource dataSource) {


        if (dataSource.getFileFormat().equals("Analyze7.5")) {
            JPanel panel = new JPanel();
            JLabel messageLabel = new JLabel("Please select correct image orientation from menu: ");
            java.util.List<Anatomy3D> choices = Anatomy3D.getInstanceList();
            JComboBox choiceBox = new JComboBox(choices.toArray());

            //todo hackery alert
            Anatomy anatomy = dataSource.getImageInfo().getAnatomy();
            choiceBox.setSelectedItem(anatomy);

            FormLayout layout = new FormLayout("4dlu, l:p, p:g, 4dlu", "6dlu, p, 10dlu, p, 6dlu");
            CellConstraints cc = new CellConstraints();
            panel.setLayout(layout);
            panel.add(messageLabel, cc.xyw(2, 2, 2));
            panel.add(choiceBox, cc.xyw(2, 4, 2));

            JOptionPane.showMessageDialog(brainFrame, panel, "Analyze 7.5 image format ...", JOptionPane.WARNING_MESSAGE);
            Anatomy selectedAnatomy = (Anatomy) choiceBox.getSelectedItem();
            if (selectedAnatomy != anatomy) {
                //todo hackery alert
                dataSource.getImageInfo().setAnatomy((Anatomy3D) selectedAnatomy);
                dataSource.releaseData();
            }
        }

        return dataSource;

    }


    public void loadAndDisplay(final IImageSource dataSource) {
        //this whole set of methods is a horror
        //todo need a set of related methods that allow
        // 1. loading image in background
        // 2. registering image
        // 3. creating model if necessary
        // 4. creating layer if necessary
        // 5. creating view if necessary

        log.info("loading and displaying : " + dataSource);

        final Cursor cursor = brainFrame.getCursor();
        brainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        final IImageSource checkedDataSource = specialHandling(dataSource);
        register(checkedDataSource);

        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());
        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageViewModel displayModel = ProjectManager.get().createViewModel(checkedDataSource, true);
                ImageView iview = ImageViewFactory.createAxialView(displayModel);
                DisplayManager.get().display(iview);
                brainFrame.setCursor(cursor);
            }
        });


    }


    public void load(final IImageSource dataSource) {
        final Cursor cursor = brainFrame.getCursor();
        brainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final IImageSource checkedDataSource = specialHandling(dataSource);
        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());

        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register(checkedDataSource);
                brainFrame.setCursor(cursor);
            }
        });


    }


    public void loadAndDisplay(final IImageSource dataSource, final ImageView view) {
        final IImageSource checkedDataSource = specialHandling(dataSource);
        register(checkedDataSource);

        final Cursor cursor = brainFrame.getCursor();
        brainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());
        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageViewModel model = view.getModel();
                ImageLayer3D layer = ImageLayerFactory.createImageLayer(dataSource);
                model.add(layer);
                model.layerSelection.set(model.indexOf(layer));
                brainFrame.setCursor(cursor);

            }
        });


    }


    public IImageSource createDataSource(URI uri) throws BrainFlowException {
        try {
            FileObject fobj = VFS.getManager().resolveFile(uri.getPath());

            java.util.List<IImageSource<IImageData>> sources = BrainIO.loadDataSources(new FileObject[]{fobj});
            return sources.get(0);
        } catch (FileSystemException e) {
            throw new BrainFlowException(e);
        }
    }


    public IImageSource createDataSource(String path) throws BrainFlowException {


        try {
            FileObject fobj = VFS.getManager().resolveFile(path);
            
            if (!BrainIO.isSupportedImageFile(fobj.getName().getBaseName())) {
                throw new BrainFlowException("argument " + fobj.getName().getBaseName() + " is not a valid image path");
            }

            java.util.List<IImageSource<IImageData>> sources = BrainIO.loadDataSources(new FileObject[]{fobj});
            if (sources.size() > 1) {
                log.warning("multiple matching files for path " + path + "... using first match.");
            }

            return sources.get(0);

        } catch (FileSystemException e) {
            throw new BrainFlowException(e.getMessage(), e);
        }


    }


    public void replaceLayer(ImageLayer3D oldLayer, ImageLayer3D newLayer, ImageView view) {
        DisplayManager.get().replaceLayer(oldLayer, newLayer, view);
    }


    public ImageView getSelectedView() {
        return DisplayManager.get().getSelectedCanvas().getSelectedView();
    }

    public IBrainCanvas getSelectedCanvas() {
        return DisplayManager.get().getSelectedCanvas();

    }

    public boolean isShowing(IImageSource dsource) {
        return DisplayManager.get().isShowing(dsource);

    }

    public void displayView(ImageView view) {
        DisplayManager.get().display(view);
    }

    public IBrainCanvas newCanvas() {
        return DisplayManager.get().newCanvas();
    }


    public java.util.List<IImageSource> getSelectedLoadableImages() {
        IImageSource[] limg = loadingDock.requestLoadableImages();
        return Arrays.asList(limg);

    }


    public static class LaunchConfiguration extends AbstractBuilder {

        public final List<FileObject> mountPoints = new ArrayList<FileObject>();

        public final List<List<FileObject>> datasets = new ArrayList<List<FileObject>>();

        public LaunchConfiguration() {}

        public LaunchConfiguration addMountPoint(FileObject dir) {
            mountPoints.add(dir);
            return this;
        }

        public LaunchConfiguration addDataset(List<FileObject> dset) {
            datasets.add(dset);
            return this;
        }

        public LaunchConfiguration build() {
            this.checkBuilt();
            this.isBuilt = true;
            return this;
        }
    }


    class SliderStatusBarItem extends StatusBarItem {

        JSlider slider;

        SliderStatusBarItem(JSlider slider) {
            setLayout(new BorderLayout());
            this.slider = slider;

            add(slider, BorderLayout.CENTER);
        }

        @Override
        public String getItemName() {
            return "slider";
        }

        @Override
        public Dimension getPreferredSize() {
            return slider.getPreferredSize();
        }
    }


    class SelectedViewStatus extends BrainFlowPresenter {


        private LabelStatusBarItem anatomyLabel;

        public SelectedViewStatus() {
            anatomyLabel = new LabelStatusBarItem();
            anatomyLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setMinimumSize(new Dimension(200, 0));
        }

        @Override
        public void allViewsDeselected() {
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setEnabled(false);
        }

        @Override
        protected void layerSelected(ImageLayer3D layer) {
            updateLabel(getSelectedView());

        }

        //@Override
        //protected void layerChangeNotification() {
        //     updateLabel(getSelectedView());
        //}

        private void updateLabel(ImageView view) {
            int i = view.getSelectedLayerIndex();
            if (i >= 0) {
                anatomyLabel.setText("Layer: " + view.getModel().get(i).getName());
                anatomyLabel.setEnabled(true);
            } else {
                anatomyLabel.setText("Layer: None Selected");
                anatomyLabel.setEnabled(false);

            }

        }

        @Override
        public void viewSelected(ImageView view) {
            updateLabel(view);

        }


        @Override
        public void viewModelChanged(ImageView view, ImageViewModel oldModel, ImageViewModel newModel) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public JComponent getComponent() {
            return anatomyLabel;
        }
    }


    private void initExceptionHandler() {
        LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
                //defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                ExceptionDialog ed = new ExceptionDialog(e);
                JDialog dialog = ed.createDialog(brainFrame);
                dialog.setVisible(true);
            }
        });

    }

    /*public static class NimbusInitializer implements LookAndFeelFactory.UIDefaultsInitializer {
      public void initialize(UIDefaults defaults) {
          Object marginBorder = new SwingLazyValue(
                  "javax.swing.plaf.basic.BasicBorders$MarginBorder");

          Object[] uiDefaults = {
                  "textHighlight", new ColorUIResource(197, 218, 233),
                  "controlText", new ColorUIResource(Color.BLACK),
                  "activeCaptionText", new ColorUIResource(Color.BLACK),
                  "MenuItem.acceleratorFont", new FontUIResource("Arial", Font.PLAIN, 12),
                  "ComboBox.background", new ColorUIResource(Color.WHITE),
                  "ComboBox.disabledForeground", new ColorUIResource(Color.DARK_GRAY),
                  "ComboBox.disabledBackground", new ColorUIResource(Color.GRAY),

                  "activeCaption", new ColorUIResource(197, 218, 233),
                  "inactiveCaption", new ColorUIResource(Color.DARK_GRAY),
                  "control", new ColorUIResource(220, 223, 228),
                  "controlLtHighlight", new ColorUIResource(Color.WHITE),
                  "controlHighlight", new ColorUIResource(Color.LIGHT_GRAY),
                  "controlShadow", new ColorUIResource(133, 137, 144),
                  "controlDkShadow", new ColorUIResource(Color.BLACK),
                  "MenuItem.background", new ColorUIResource(237, 239, 242),
                  "SplitPane.background", new ColorUIResource(220, 223, 228),
                  "Tree.hash", new ColorUIResource(Color.GRAY),

                  "TextField.foreground", new ColorUIResource(Color.BLACK),
                  "TextField.inactiveForeground", new ColorUIResource(Color.BLACK),
                  "TextField.selectionForeground", new ColorUIResource(Color.WHITE),
                  "TextField.selectionBackground", new ColorUIResource(197, 218, 233),
                  "Table.gridColor", new ColorUIResource(Color.BLACK),
                  "TextField.background", new ColorUIResource(Color.WHITE),

                  "Menu.border", marginBorder,
                  "MenuItem.border", marginBorder,
                  "CheckBoxMenuItem.border", marginBorder,
                  "RadioButtonMenuItem.border", marginBorder,
                  "Table.selectionBackground", new ColorUIResource(237, 239, 242),
                  "Table.selectionForeground", new ColorUIResource(12, 239, 242)
          };
          LookAndFeelFactory.putDefaults(defaults, uiDefaults);
      }
  }  */


}
