package com.demo.test.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Jack
 */
public class FileId {

    public static int nextId() {
        try {
            String file = FileId.class.getResource("/id.txt").getFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String idStr = br.readLine();
            if (idStr == null) {
                idStr = "0";
            }
            Integer id = Integer.valueOf(idStr) + 1;
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(id + "");
            bw.flush();
            bw.close();
            br.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
