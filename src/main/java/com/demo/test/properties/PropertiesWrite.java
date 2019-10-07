package com.demo.test.properties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesWrite {

    public static void main(String[] args) {

        /*final File file = new File("application.properties");
        try (final Writer writer = new BufferedWriter(new FileWriter(file, true))) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("db.url", "localhost");
            prop.setProperty("db.user", "mkyong");
            prop.setProperty("db.password", "password");
            // save properties to project root folder
            prop.store(writer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            Properties grades = new Properties();

            grades.setProperty("Geometry", "2000");
            grades.setProperty("Algebra", "20");
            grades.setProperty("Physics", "18");
            grades.setProperty("Chemistry", "17");
            grades.setProperty("Biology", "19");

            // Save the grades properties using store() and an output stream
            String desktop = System.getProperty ("user.home") + "/Desktop/";
            FileOutputStream out = new FileOutputStream(
                    //desktop +
                    "/src/main/resources/example.properties");
            grades.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
