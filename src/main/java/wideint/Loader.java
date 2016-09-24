package wideint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Sergey Melnikov
 */
public class Loader {
    private static void LoadFromResource(String libName) throws IOException {
        String fileName = System.mapLibraryName(libName);
        File tempFile;
        try (InputStream if_ = Loader.class.getClassLoader().getResourceAsStream(fileName)) {
            int last_dot_pos = fileName.lastIndexOf('.');
            tempFile = File.createTempFile(
                    fileName.substring(0, last_dot_pos),
                    fileName.substring(last_dot_pos));
            tempFile.deleteOnExit();
            
            try (OutputStream of = new FileOutputStream(tempFile)) {
                byte[] buf = new byte[2048];
                
                int read = if_.read(buf);
                while (read > 0) {
                    of.write(buf, 0, read);
                    read = if_.read(buf);
                }
            }
        }
        System.load(tempFile.getAbsolutePath());
    }
    
    public static void LoadLibrary(String libName) {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e) {
            try {
                LoadFromResource(libName);
            } catch (IOException ee) {
                throw new RuntimeException("Can't load library: " + libName, ee);
            }
        }
    }
}
