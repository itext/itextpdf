package sandbox;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sandbox.WrapToTest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class SandboxSampleWrapper extends GenericTest {

    /** The logger class */
    private final static Logger LOGGER = LoggerFactory.getLogger(GenericTest.class.getName());

    public SandboxSampleWrapper(String className) {
        setKlass(className);
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() throws UnsupportedEncodingException {
        String classes = new File("").getAbsolutePath() + "/target/classes/";
        List<String> classNames = getClassNamesRecursively(classes, "");
        List<Object[]> params = new ArrayList<Object[]>();
        for (String s1 : classNames) {
            params.add(new String[] {s1});
        }
        return params;
    }

    private static List<String> getClassNamesRecursively(String path, String currentPackage) {
        List<String> classNames = new ArrayList<String>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String[] splitted = file.getAbsolutePath().replace("\\", "/").split("/");
                String packageName = splitted[splitted.length - 1];
                classNames.addAll(getClassNamesRecursively(file.getAbsolutePath(), currentPackage + packageName + "."));
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
                        if (c.isAnnotationPresent(WrapToTest.class))
                            classNames.add(className);
                    }
                }
            }
        }
        return classNames;
    }


}
