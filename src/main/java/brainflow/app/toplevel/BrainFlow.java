package brainflow.app.toplevel;

import brainflow.app.*;
import brainflow.app.dnd.BrainCanvasTransferHandler;
import brainflow.app.actions.*;
import brainflow.app.presentation.*;
import brainflow.core.services.ImageViewMousePointerEvent;
import brainflow.colormap.ColorTable;
import brainflow.colormap.IColorMap;
import brainflow.core.*;
import brainflow.core.layer.ImageLayer3D;
import brainflow.image.anatomy.Anatomy;
import brainflow.image.anatomy.Anatomy3D;
import brainflow.image.anatomy.GridPoint3D;
import brainflow.image.io.IImageDataSource;
import brainflow.gui.ExceptionDialog;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandMenuBar;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.GuiCommands;
import com.pietschy.command.configuration.ParseException;
import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.group.ExpansionPointBuilder;
import com.pietschy.command.toggle.ToggleGroup;
import com.pietschy.command.ActionCommand;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

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

    private SelectedViewStatus viewStatus;

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
            System.out.println(
                    "Error: no splash image specified on the command line");
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


    public void launch() throws Throwable {


        try {
            openSplash();
            drawSplashProgress("loading look and feel");


            String osname = System.getProperty("os.name");
            System.out.println("os name is : " + osname);
            if (osname.toUpperCase().contains("WINDOWS")) {
                UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel());
                //UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());
                //UIManager.setLookAndFeel(new WindowsLookAndFeel());
                LookAndFeelFactory.installJideExtension();

            } else if (osname.toUpperCase().contains("LINUX")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
                //UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel());
                //UIManager.setLookAndFeel(new WindowsLookAndFeel());
                LookAndFeelFactory.installJideExtension();

            } else if (osname.toUpperCase().contains("MAC")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BrainFlow");

                //System.setProperty("Quaqua.tabLayoutPolicy","wrap");
                UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel());
                //UIManager.setLookAndFeel(new ch.randelshofer.quaqua.QuaquaLookAndFeel());
                LookAndFeelFactory.installJideExtension();
            }

        } catch (UnsupportedLookAndFeelException e) {
            log.severe("could not load look and feel");
        } catch (Throwable ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                LookAndFeelFactory.installJideExtension();

            } catch (Throwable ex2) {
                log.severe("could not load look and feel");
                throw new RuntimeException("failed to initialize look and feel", ex2);
            }

        }


        drawSplashProgress("creating frame ...");
        brainFrame = new BrainFrame();
        statusBar = new StatusBar();

        brainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        drawSplashProgress("initializing DisplayManager ...");
        DisplayManager.get().newCanvas();


        drawSplashProgress("initializing resources ...");
        initializeResources();


        drawSplashProgress("loading commands ...");
        loadCommands();


        drawSplashProgress("initializing IO ...");

        initImageIO();


        drawSplashProgress("initializing status bar ...");
        initializeStatusBar();


        drawSplashProgress("initializing work space ...");
        initializeWorkspace();


        drawSplashProgress("binding container ...");
        bindContainer();


        drawSplashProgress("initializing tool bar ...");
        initializeToolBar();


        drawSplashProgress("initializing menu ...");
        initializeMenu();


        initExceptionHandler();


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


    private void initializeMenu() {
        log.info("initializing Menu");


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

        menuBar.setStretch(true);
        menuBar.setPaintBackground(false);

        menuBar.add(fileMenuGroup.createMenuItem());
        menuBar.add(viewMenuGroup.createMenuItem());
        menuBar.add(gotoMenuGroup.createMenuItem());

        brainFrame.setJMenuBar(menuBar);

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
        viewStatus = new SelectedViewStatus();
        log.info("initialzing status bar");
        statusBar.setAutoAddSeparator(false);

        statusBar.add(viewStatus.getComponent(), JideBoxLayout.FIX);
        statusBar.add(new com.jidesoft.status.StatusBarSeparator(), JideBoxLayout.FIX);

        LabelStatusBarItem crossLabel = new LabelStatusBarItem();
        crossLabel.setText("Cross: ");
        statusBar.add(crossLabel, JideBoxLayout.FIX);

        CursorCoordinates cursorCoordinates = new CursorCoordinates();

        CrosshairCoordinates crosshairCoordinates = new CrosshairCoordinates();

        statusBar.add(crosshairCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getZaxisLabel(), JideBoxLayout.FIX);
        statusBar.addSeparator();

        LabelStatusBarItem cursorLabel = new LabelStatusBarItem();
        cursorLabel.setText("Cursor: ");
        statusBar.add(cursorLabel, JideBoxLayout.FIX);

        statusBar.add(cursorCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getZaxisLabel(), JideBoxLayout.FIX);

        statusBar.addSeparator();

        statusBar.add(new ValueStatusItem(), JideBoxLayout.FIX);

        statusBar.add(new LabelStatusBarItem(), JideBoxLayout.VARY);
        //statusBar.add(new com.jidesoft.status.ProgressStatusBarItem(), JideBoxLayout.FIX);

        //statusBar.add(new com.jidesoft.status.MemoryStatusBarItem());
        brainFrame.getContentPane().add(statusBar, "South");


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

        JToolBar mainToolbar = mainToolbarGroup.createToolBar();


        brainFrame.getContentPane().add(mainToolbar, BorderLayout.NORTH);


        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    KeyEvent ke = (KeyEvent) event;
                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                        previousSliceCommand.execute();
                    } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                        nextSliceCommand.execute();
                    }

                }

            }
        }, AWTEvent.KEY_EVENT_MASK);


    }

    private void initCanvas() {
        BrainCanvasTransferHandler handler = new BrainCanvasTransferHandler();
        DisplayManager.get().getSelectedCanvas().getComponent().setTransferHandler(handler);

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
        documentPane.openDocument(new DocumentComponent(new JScrollPane(comp), canvasName));
        documentPane.setActiveDocument(canvasName);
        DisplayManager.get().setSelectedCanvas(canvas);
    }


    private void initializeWorkspace() throws Exception {
        log.info("initializing workspace");
        brainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        brainFrame.getDockingManager().getWorkspace().add(documentPane, "Center");

        JComponent canvas = DisplayManager.get().getSelectedCanvas().getComponent();
        canvas.setRequestFocusEnabled(true);
        canvas.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));


        documentPane.setTabPlacement(DocumentPane.BOTTOM);
        documentPane.openDocument(new DocumentComponent(new JScrollPane(canvas), "Canvas-1"));
        documentPane.setActiveDocument("Canvas-1");

        log.info("initializing loading dock");
        initLoadingDock();
        log.info("initializing project view");
        initProjectView();
        log.info("initializing image table view");
        initLoadableImageTableView();
        log.info("initializing control panel");
        initControlPanel();
        log.info("initializing event monitor");
        initEventBusMonitor();
        log.info("initializing log monitor");
        initLogMonitor();


        brainFrame.getDockingManager().beginLoadLayoutData();
        brainFrame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);
        brainFrame.getDockingManager().loadLayoutData();

        brainFrame.toFront();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        brainFrame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 50);
        brainFrame.setVisible(true);


        CanvasBar cbar = new CanvasBar();

        /// TODO add automatic updating of canvas to Canvas Bar via EventBus
        //cbar.setImageCanvas(canvas);
        ////////////////////////////////////////////////////////////////////
        canvas.add(cbar.getComponent(), BorderLayout.NORTH);


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
            dframe.setPreferredSize(new Dimension(275, 200));

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


    private void initControlPanel() {

        JideTabbedPane tabbedPane = new JideTabbedPane();

        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Tool Box",
                "icons/types.gif",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_EAST);


        ColorAdjustmentControl colorAdjustmentControl = new ColorAdjustmentControl();

        CoordinateControls coordinateControls = new CoordinateControls();

        LayerInfoControl layerInfoControl = new LayerInfoControl();

        MaskControl maskControl = new MaskControl();

        //ColorMapTablePresenter tablePresenter = new ColorMapTablePresenter();

        //MaskTablePresenter maskPresenter = new MaskTablePresenter();

        tabbedPane.addTab("Adjustment", new JScrollPane(colorAdjustmentControl.getComponent()));
        tabbedPane.addTab("Image Mask", maskControl.getComponent());
        tabbedPane.addTab("Layer Info", new JScrollPane(layerInfoControl.getComponent()));
        //tabbedPane.addTab("Color Table", tablePresenter.getComponent());
        //tabbedPane.addTab("Mask Table", maskPresenter.getComponent());
        tabbedPane.addTab("Coordinates", new JScrollPane(coordinateControls.getComponent()));


        dframe.getContentPane().add(tabbedPane);

        dframe.setPreferredSize(new Dimension(300, 500));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    boolean initImageIO() {
        log.info("initializing imageio");
        try {
            ImageIOManager.getInstance().initialize();
        } catch (BrainFlowException e) {
            log.severe("Could not initialize IO facilities, aborting");
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean initializeResources() {
        log.info("initializing resources");
        ResourceManager.getInstance().getColorMaps();
        return true;
    }


    private void register(IImageDataSource dataSource) {
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

    private IImageDataSource specialHandling(IImageDataSource dataSource) {


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


    public void loadAndDisplay(final IImageDataSource dataSource) {
        //this whole set of methods is a horror
        //todo need a set of related methods that allow
        // 1. loading image in background
        // 2. registering image
        // 3. creating model if necessary
        // 4. creating layer if necessary
        // 5. creating view if necessary

        log.info("loading and displaying : " + dataSource);

        final IImageDataSource checkedDataSource = specialHandling(dataSource);
        register(checkedDataSource);

        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());
        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageViewModel displayModel = ProjectManager.get().createViewModel(checkedDataSource, true);
                ImageView iview = ImageViewFactory.createAxialView(displayModel);
                DisplayManager.get().displayView(iview);
                //DisplayManager.get().getSelectedCanvas().addImageView(iview);
            }
        });


    }


    public void load(final IImageDataSource dataSource) {
        final IImageDataSource checkedDataSource = specialHandling(dataSource);
        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());

        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register(checkedDataSource);
            }
        });


    }


    public void loadAndDisplay(final IImageDataSource dataSource, final ImageView view) {
        final IImageDataSource checkedDataSource = specialHandling(dataSource);
        register(checkedDataSource);

        ImageProgressMonitor monitor = new ImageProgressMonitor(checkedDataSource, brainFrame.getContentPane());
        monitor.loadImage(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageViewModel displayModel = view.getModel();
                ImageLayer3D layer = ImageLayerFactory.createImageLayer(dataSource);
                displayModel.add(layer);
                

            }
        });


    }


    public IImageDataSource createDataSource(URI uri) throws BrainFlowException {
        File file = new File(uri);
        return createDataSource(file.getAbsolutePath());
    }

    public IImageDataSource createDataSource(String path) throws BrainFlowException {
        File file = new File(path);

        if (!file.exists()) {
            throw new BrainFlowException("argument " + path + " not found");
        }

        if (!ImageIOManager.getInstance().isLoadableImage(path)) {
            throw new BrainFlowException("argument " + path + "is not a valid image path");
        }

        IImageDataSource[] sources = ImageIOManager.getInstance().findLoadableImages(new File[]{file});

        assert sources.length != 0;

        if (sources.length > 1) {
            log.warning("mulitple matching files for path " + path + "... using first match.");
        }

        return sources[0];


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

    public boolean isShowing(IImageDataSource dsource) {
        return DisplayManager.get().isShowing(dsource);

    }

    public void displayView(ImageView view) {
        DisplayManager.get().displayView(view);
    }

    public void updateViews(ImageViewModel oldModel, ImageViewModel newModel) {
        DisplayManager.get().updateViews(oldModel, newModel);
    }

    public IBrainCanvas newCanvas() {
        return DisplayManager.get().newCanvas();
    }


    public java.util.List<IImageDataSource> getSelectedLoadableImages() {
        IImageDataSource[] limg = loadingDock.requestLoadableImages();
        return Arrays.asList(limg);

    }


    class ValueStatusItem extends LabelStatusBarItem implements EventSubscriber {

        private Map<Color, ImageIcon> colorMap = new HashMap<Color, ImageIcon>();
        private NumberFormat format = NumberFormat.getNumberInstance();

        public ValueStatusItem() {
            EventBus.subscribeExactly(ImageViewMousePointerEvent.class, this);
            setIcon(ColorTable.createImageIcon(Color.GRAY, 40, 15));
            setText("Value :");
        }

        private boolean validEvent(ImageViewMousePointerEvent event) {
            //todo only publish events when cursorPos is over valid view
            ImageView view = event.getImageView();

            if (view == null) {
                //todo this should be impossible
                return false;
            }
            if (view.getModel().getSelectedIndex() < 0) {
                // an empty view ... hmmm
                return false;
            }

            if (event.getLocation() == null) {
                // well, shouldn't realy happen but ...
                return false;
            }

            return true;

        }

        public void onEvent(Object evt) {
            ImageViewMousePointerEvent event = (ImageViewMousePointerEvent) evt;

            //todo only publish events when cursorPos is over valid view
            ImageView view = event.getImageView();
            if (!validEvent(event)) return;

            GridPoint3D gpoint = event.getLocation();
            ImageLayer3D layer = view.getSelectedLayer();


            double value = layer.getValue(gpoint);


            IColorMap cmap = layer.getImageLayerProperties().getColorMap();
            Color c = null;
            try {
                c = cmap.getColor(value);
            } catch (Exception e) {

                e.printStackTrace();
            }
            ImageIcon icon = colorMap.get(c);
            if (icon == null) {
                icon = ColorTable.createImageIcon(c, 40, 15);
                colorMap.put(c, icon);
                if (colorMap.size() > 256) {
                    colorMap.clear();

                }
            }

            setText("Value: " + format.format(value));
            this.setIcon(icon);

        }
    }


    class SelectedViewStatus extends ImageViewPresenter {


        private LabelStatusBarItem anatomyLabel;

        public SelectedViewStatus() {
            anatomyLabel = new LabelStatusBarItem();
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setMinimumSize(new Dimension(100, 0));
        }


        public void allViewsDeselected() {
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setEnabled(false);

        }

        public void viewSelected(ImageView view) {
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
