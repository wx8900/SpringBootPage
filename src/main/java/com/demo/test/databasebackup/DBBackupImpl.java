package com.demo.test.databasebackup;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 备份数据库的操作、恢复、删除工具类
 */
@Slf4j
public class DBBackupImpl extends DBBackupUtils {

    /**
     * 备份操作工具
     */
    public boolean dbBackUp() {
        //最终文件名
        String finalFileName = System.currentTimeMillis() + fileName;
        String pathSql = backPath + finalFileName;
        File fileSql = new File(pathSql);
        //创建备份sql文件
        if (!fileSql.exists()) {
            try {
                fileSql.createNewFile();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(toolPath + "mysqldump");
        sb.append(" -h" + host);
        sb.append(" -u" + root);
        sb.append(" -p" + pwd);
        sb.append(" " + dbName + " >");
        sb.append(pathSql);
        System.out.println("cmd命令为：" + sb.toString());
        System.out.println("开始备份：" + dbName);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("cmd /c " + sb.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 数据恢复操作工具
     */
    public boolean dbRestore(String fileName) throws IOException {
        String filePath = backPath + fileName;
        StringBuilder sb = new StringBuilder();
        sb.append(toolPath + "mysql");
        sb.append(" -h" + host);
        sb.append(" -u" + root);
        sb.append(" -p" + pwd);
        sb.append(" " + dbName + " <");
        sb.append(filePath);
        System.out.println("cmd命令为：" + sb.toString());
        System.out.println("开始还原数据");
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /c" + sb.toString());
        //插入数据库备份恢复记录表
        try (InputStream is = process.getInputStream();
             BufferedReader bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除数据备份
     */
    public String remove(String fileName) {
        final String file = DBBackupUtils.backPath + fileName;
        File del_file = new File(file);
        if (del_file.isFile()) {
            if (del_file.delete() == false) {
                return "删除失败";
            }
        }
        return "删除成功";
    }

}
