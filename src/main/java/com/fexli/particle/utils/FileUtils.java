package com.fexli.particle.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> getFiles(String path,String root,String endwith) {
        List<String> files = new ArrayList<String>();
        File file = new File(path+root);
        File[] tempList = file.listFiles();
        if ((tempList != null ? tempList.length : 0) == 0) return null;

        for (File value : tempList) {
            if (value.isFile() & value.getName().endsWith(endwith)) {
                files.add(root+value.getName());
            }
            if (value.isDirectory()) {
                String inner = root+value.getName()+"/";
                List<String> innerfile = getFiles(path, inner, endwith);
                if (innerfile != null) files.addAll(innerfile);
            }
        }
        return files;
    }

    public static List<String> getFiles(String path,String root) {
        List<String> files = new ArrayList<String>();
        File file = new File(path + root);
        File[] tempList = file.listFiles();
        if ((tempList != null ? tempList.length : 0) == 0) return null;

        for (File value : tempList) {
            if (value.isDirectory()) {
                String inner = root + value.getName() + "/";
                List<String> innerfile = getFiles(path, inner);
                if (innerfile != null) files.addAll(innerfile);
            } else {
                files.add(root + value.getName());
            }
        }
        return files;
    }
}
