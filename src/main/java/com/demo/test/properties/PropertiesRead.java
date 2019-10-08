package com.demo.test.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Util class for reading properties file
 *
 * @author Jack
 * @date 2019/08/18 01:30 AM
 */
public class PropertiesRead {

    public static void main(String[] args) {

        try (InputStream input = PropertiesRead.class.getClassLoader().getResourceAsStream("application.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            System.out.println(prop.getProperty("spring.profiles.active"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
