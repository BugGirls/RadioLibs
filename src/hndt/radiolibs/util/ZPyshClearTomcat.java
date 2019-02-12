package hndt.radiolibs.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ZPyshClearTomcat {

    public static void main(String[] args) {
        ZPyshClearTomcat delete_log = new ZPyshClearTomcat("E:\\tomcat\\apache-tomcat-7.0.42\\logs");
        delete_log.execute();
        ZPyshClearTomcat delete_work = new ZPyshClearTomcat("E:\\tomcat\\apache-tomcat-7.0.42\\work");
        delete_work.execute();
    }

    String path;

    public ZPyshClearTomcat(String path) {
        this.path = path;
    }

    public void execute() {
        try {
            File file = new File(path);
            FileUtils.cleanDirectory(file);
        } catch (IOException ex) {
            Logger.getLogger(ZPyshClearTomcat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
