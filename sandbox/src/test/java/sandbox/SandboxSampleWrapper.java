package sandbox;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class SandboxSampleWrapper extends GenericTest {

    /** The logger class */
    private final static Logger LOGGER = LoggerFactory.getLogger(GenericTest.class.getName());

    public SandboxSampleWrapper(SandboxWrapperParam param) {
        setKlass(param.getClassName());
        setCompareRenders(param.isCompareRenders());
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() throws UnsupportedEncodingException {
        String classes = new File("").getAbsolutePath() + "/target/classes/";
        List<SandboxWrapperParam> sandboxWrapperParams = getClassNamesRecursively(classes, "");
        List<Object[]> params = new ArrayList<Object[]>();
        for (SandboxWrapperParam p : sandboxWrapperParams) {
            params.add(new SandboxWrapperParam[] {new SandboxWrapperParam(p.getClassName(), p.isCompareRenders())});
        }
        return params;
    }

    private static List<SandboxWrapperParam> getClassNamesRecursively(String path, String currentPackage) {
        List<SandboxWrapperParam> sandboxWrapperParams = new ArrayList<SandboxWrapperParam>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String[] splitted = file.getAbsolutePath().replace("\\", "/").split("/");
                String packageName = splitted[splitted.length - 1];
                sandboxWrapperParams.addAll(getClassNamesRecursively(file.getAbsolutePath(), currentPackage + packageName + "."));
            } else {
                String fileName = file.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    String className = currentPackage + fileName.replace(".class", "");
                    Class<?> c = null;
                    try {
                        c = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        LOGGER.error(e.getLocalizedMessage(), e);
                    }
                    if (c != null) {
                        if (c.isAnnotationPresent(WrapToTest.class)) {
                            Annotation annotation = c.getAnnotation(WrapToTest.class);
                            WrapToTest wrapToTest = (WrapToTest) annotation;
                            sandboxWrapperParams.add(new SandboxWrapperParam(className, wrapToTest.compareRenders()));
                        }
                    }
                }
            }
        }
        return sandboxWrapperParams;
    }

    private static class SandboxWrapperParam {
        private String className;
        private boolean compareRenders = false;

        SandboxWrapperParam(String className, boolean compareRenders) {
            this.className = className;
            this.compareRenders = compareRenders;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public boolean isCompareRenders() {
            return compareRenders;
        }

        public void setCompareRenders(boolean compareRenders) {
            this.compareRenders = compareRenders;
        }

        @Override
        public String toString() {
            return "SandboxWrapperParam{" +
                    "className='" + className + '\'' +
                    ", compareRenders=" + compareRenders +
                    '}';
        }
    }


}
