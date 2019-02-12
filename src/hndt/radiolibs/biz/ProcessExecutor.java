/**
 * ffmpeg常用基本命令 http://www.cnblogs.com/wainiwann/p/4128154.html
 * ffmpeg命令使用详解 http://blog.csdn.net/l_yangliu/article/details/7274968
 */
package hndt.radiolibs.biz;

import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessExecutor {

    private static ProcessExecutor biz = null;

    private ProcessExecutor() {
    }

    public static ProcessExecutor getInstance() {
        if (biz == null) {
            biz = new ProcessExecutor();
        }
        return biz;
    }

    List<Process> processes = new ArrayList<>();

    public synchronized String executeFileInfo(String srcFile) {
        String command = ("ffmpeg -i " + srcFile);
        String rs = execute(command);
        exit();
        return rs;
    }

    /**
     * ffmpeg -i example.mp3 -lavfi showwavespic=colors=gray:s=600*240 out.png
     *
     * @param srcFile
     * @return
     */
    public synchronized String executeFileWave(String srcFile) {
        String data = null;
        if (Utils.isEmpty(srcFile)) {
            return null;
        }
        Path srcPath = Paths.get(AppBusiness.getInstance().realRootPath, srcFile);
        Path path = srcPath.getParent().resolve("out.png");
        String command = ("ffmpeg -i " + srcPath.toString() + " -lavfi showwavespic=colors=gray:s=600*200 " + path.toString());
        Logger.info(command);
        String rs = execute(command);
        Logger.info(rs);
        if (Files.exists(path)) {
            try {
                data = Base64.encodeBase64String(FileUtils.readFileToByteArray(path.toFile()));
                Files.delete(path);
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        exit();
        return data;
    }

    public String execute(String command) {
        String result = null;
        Process p = null;
        try {
            String os = System.getProperties().getProperty("os.name").toUpperCase();
            Logger.info("OS NAME " + os);

            if (os.startsWith("WIN")) {
                // 得到进程实例
                p = Runtime.getRuntime().exec(command);
            } else {
                List<String> commands = new ArrayList<String>();
                commands.add("/bin/sh");
                commands.add("-c");
                commands.add(command);
                ProcessBuilder builder = new ProcessBuilder(commands);
                p = builder.start();
            }
            processes.add(p);
            if (p.waitFor(1, TimeUnit.MINUTES)) {
                InputStream stdout = p.getInputStream(); //获得进程实例的标准输出
                InputStream error = p.getErrorStream();
                if (stdout.available() > 0) {
                    result = IOUtils.toString(stdout);
                }
                if (error.available() > 0) {
                    result = IOUtils.toString(error);
                }
            } else {
                p.destroyForcibly();
                processes.remove(p);
                result = "超时退出";
            }
        } catch (Exception e) {
            p.destroyForcibly();
            processes.remove(p);
            result = e.getCause().getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public void exit() {
        for (Process p : processes) {
            if (p.isAlive()) {
                p.destroyForcibly();
            }
        }
    }


}
