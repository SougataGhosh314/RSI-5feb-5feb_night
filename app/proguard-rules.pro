# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Default PV proguard file - use it and abuse it if its useful.

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Default PV proguard file - use it and abuse it if its useful.

# -keep class !com.my.package.** { *; }


#
# This ProGuard configuration file illustrates how to process a program
# library, such that it remains usable as a library.
# Usage:
#     java -jar proguard.jar @library.pro
#

# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-printmapping out.map
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.

-keepattributes *Annotation*

# Preserve all public classes, and their public and protected fields and
# methods.

-keep public class * {
    public protected *;
}

# Preserve all .class method names.

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

# Preserve all native method names and the names of their classes.

-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve the special static methods that are required in all enumeration
# classes.

-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your library doesn't use serialization.
# If your code contains serializable classes that have to be backward
# compatible, please refer to the manual.

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Your library may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class mypackage.MyClass
# -keep public interface mypackage.MyInterface
# -keep public class * implements mypackage.MyInterface

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.
-optimizations !code/simplification/cast,!field/*,!class/merging/*,!class/unboxing/enum,!code/allocation/variable,!method/marking/private
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# ADDED
-dontshrink
-dontobfuscate

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Optimize
-optimizations !field/*,!class/merging/*,*
-mergeinterfacesaggressively

# will keep line numbers and file name obfuscation
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Apache POI
-dontwarn org.apache.**
-dontwarn org.openxmlformats.schemas.**
-dontwarn org.etsi.**
-dontwarn org.w3.**
-dontwarn com.microsoft.schemas.**
-dontwarn com.graphbuilder.**
-dontnote org.apache.**
-dontnote org.openxmlformats.schemas.**
-dontnote org.etsi.**
-dontnote org.w3.**
-dontnote com.microsoft.schemas.**
-dontnote com.graphbuilder.**

-keeppackagenames org.apache.poi.ss.formula.function

-keep class com.fasterxml.aalto.stax.InputFactoryImpl
-keep class com.fasterxml.aalto.stax.OutputFactoryImpl
-keep class com.fasterxml.aalto.stax.EventFactoryImpl

-keep class schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2.TypeSystemHolder { public final static *** typeSystem; }

-keep class org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem { public static *** get(...); public static *** getNoType(...); }
-keep class org.apache.xmlbeans.impl.schema.PathResourceLoader { public <init>(...); }
-keep class org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler { public static *** compile(...); }
-keep class org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl { public <init>(...); public static *** get(...); public static *** getNoType(...); }
-keep class org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl { public static *** getContextTypeLoader(...); public static *** build(...); }
-keep class org.apache.xmlbeans.impl.store.Locale { public static *** streamToNode(...); public static *** nodeTo*(...); }
-keep class org.apache.xmlbeans.impl.store.Path { public static *** compilePath(...); }
-keep class org.apache.xmlbeans.impl.store.Query { public static *** compileQuery(...); }

-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType$Enum { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType$Enum { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring { *; }

-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CommentsDocumentImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTAuthorsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBooleanPropertyImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBookViewImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBookViewsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBorderImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBordersImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBorderPrImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellAlignmentImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellFormulaImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellStyleXfsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellXfsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTColorImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTColImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTColsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCommentImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCommentsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCommentListImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTDrawingImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFillImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFillsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontNameImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontSchemeImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontSizeImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTIntPropertyImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTLegacyDrawingImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTNumFmtsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTPatternFillImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTPageMarginsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTPaneImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTRowImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSelectionImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetDataImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetDimensionImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetFormatPrImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetViewImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetViewsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetsImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSstImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTStylesheetImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTRstImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTWorkbookImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTWorkbookPrImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTWorksheetImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTXfImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.SstDocumentImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.StyleSheetDocumentImpl { *; }
-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.STXstringImpl { *; }

-keep class org.openxmlformats.schemas.officeDocument.x2006.customProperties.impl.CTPropertiesImpl { *; }
-keep class org.openxmlformats.schemas.officeDocument.x2006.customProperties.impl.PropertiesDocumentImpl { *; }
-keep class org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.impl.CTPropertiesImpl { *; }
-keep class org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.impl.PropertiesDocumentImpl { *; }
-keep class org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl.CTDrawingImpl { *; }
-keep class org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl.CTMarkerImpl { *; }
-keep class com.microsoft.schemas.office.office.impl.CTIdMapImpl { *; }
-keep class com.microsoft.schemas.office.office.impl.CTShapeLayoutImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTShadowImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTFillImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTPathImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTShapeImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTShapetypeImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTStrokeImpl { *; }
-keep class com.microsoft.schemas.vml.impl.CTTextboxImpl { *; }
-keep class com.microsoft.schemas.office.excel.impl.CTClientDataImpl { *; }
-keep class com.microsoft.schemas.office.excel.impl.STTrueFalseBlankImpl { *; }