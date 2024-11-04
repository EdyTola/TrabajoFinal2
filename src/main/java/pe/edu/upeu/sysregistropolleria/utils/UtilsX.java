package pe.edu.upeu.sysregistropolleria.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class UtilsX {

    public URL getFile(String ruta) {
        return this.getClass().getResource("/" + ruta);
    }

    public File getFileExterno(String carpeta, String filex) {
        File newFolder = new File(carpeta);
        String ruta = newFolder.getAbsolutePath();
        Path CAMINO = Paths.get(ruta + "/" + filex);
        return CAMINO.toFile();
    }

    public File getFolderExterno(String carpeta) {
        File newFolder = new File(carpeta);
        String ruta = newFolder.getAbsolutePath();
        Path CAMINO = Paths.get(ruta + "/");
        return CAMINO.toFile();
    }

    public Properties detectLanguage(String idioma) {
        Properties myresourcesx = new Properties();
        // Intento cargar el archivo desde el recurso (ruta relativa dentro del JAR o carpeta de recursos)
        try (InputStream in = getClass().getResourceAsStream("/language/idiomas-" + idioma + ".properties")) {
            if (in != null) {
                myresourcesx.load(in);
            } else {
                System.err.println("Archivo de idioma no encontrado en el recurso: /language/idiomas-" + idioma + ".properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myresourcesx;
    }

    public String readLanguageFile() {
        Properties myresourcesx = new Properties();
        String idioma = "";
        try {
            FileInputStream in = new FileInputStream(
                    getFileExterno("language", "SysCenterLife.properties").getAbsolutePath());
            try {
                myresourcesx.load(in);
                idioma = myresourcesx.getProperty("syscenterlife.idioma");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return idioma;
    }
}