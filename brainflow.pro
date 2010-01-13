-injars build\libs\brainflow-lean-0.1.1.jar
-injars build\runtimeLibs\jide-common.jar
-injars build\runtimeLibs\jide-components.jar
-injars build\runtimeLibs\jide-dock.jar
-injars build\runtimeLibs\jide-dialogs.jar
-injars build\runtimeLibs\jide-action.jar
-injars build\runtimeLibs\jide-grids.jar(!com/jidesoft/hssf/*.class,!com/jidesoft/lucene/*.class)
-injars build\runtimeLibs\colt.jar
-injars build\runtimeLibs\balloontip.jar
-injars build\runtimeLibs\datatips.jar
-injars build\runtimeLibs\xercesImpl.jar
-injars build\runtimeLibs\bean-properties.jar
-injars build\runtimeLibs\forms-1.1.0.jar
-injars build\runtimeLibs\gui-commands-2.1.jar
-injars build\runtimeLibs\EventBus-1.3beta.jar
-injars build\runtimeLibs\miglayout-3.7-swing.jar
-injars build\runtimeLibs\jfreechart-1.0.9.jar(!org/jfree/chart/servlet/*.class)
-injars build\runtimeLibs\jxlayer.jar
-injars build\runtimeLibs\jcommon-1.0.12.jar
-injars build\runtimeLibs\commons-logging-1.1.1.jar
-injars build\runtimeLibs\jparsec-2.0.jar(!org/codehaus/jparsec/misc/Curry.class,!org/codehaus/jparsec/misc/Invokables*.class)
-injars build\runtimeLibs\commons-vfs-1.1-SNAPSHOT.jar(!org/apache/commons/vfs/tasks/*.class,!org/apache/commons/vfs/provider/sftp/*.class,!org/apache/commons/vfs/provider/http/*.class,!org/apache/commons/vfs/provider/ftp/*.class,!org/apache/commons/vfs/provider/https/*.class)
-injars build\runtimeLibs\commons-pipeline-snapshot.jar
-injars build\runtimeLibs\args4j-2.0.1.jar
-injars build\runtimeLibs\jdom.jar


-outjars onejar\brainflow-app.jar
-libraryjars  "c:\Program Files\Java\jdk1.6.0_17\jre\lib\rt.jar"



-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-ignorewarnings
-dontoptimize
-dontobfuscate

-keep public class brainflow.app.toplevel.BrainFlow {
    public static void main(java.lang.String[]);
}

-keep public class brainflow.** {
    public protected *;
}

-keep public class com.pietschy.** {
    public protected *;
}

-keep public class org.apache.xerces.** {
    public protected *;
}

-keep public class com.jidesoft.plaf.** {
    public protected *;
}

-keep public class org.apache.commons.vfs.provider.** {
    public protected *;
}

-keep public class org.apache.commons.vfs.impl.** {
    public protected *;
}


-keep public class com.jidesoft.utils.** {
    public protected *;
}



-keep public class org.apache.commons.logging.impl.** {
    public protected *;
}










