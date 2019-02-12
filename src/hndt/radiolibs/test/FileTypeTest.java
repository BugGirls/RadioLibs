package hndt.radiolibs.test;

import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.Utils;

import java.io.File;
import java.nio.file.Files;

/**
 * 测试
 *
 * @author Hystar
 * @date 2017/10/19
 */
public class FileTypeTest {

    /**
     * 获取指定路径下的所有文件并检查文件采样率不是44100Hz或44.1KHz的文件
     *
     * @param path
     */
    public static void getFileName(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = file.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {// 文件夹
                System.out.println(fs.getName() + " [目录]");
                getFileName(fs.getPath());
            } else {// 文件
                if (ResBusiness.getInstance().is44100HZ(fs.getPath())) {// 文件采样率是44100Hz或44.1KHz
                    System.out.println(fs.getName());
                } else {// 文件采样率是44100Hz或44.1KHz
                    System.out.println(fs.getName() + "     【淘汰】");
                    Utils.deleteFile(fs.getPath());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "E:\\RadioLibs\\out\\artifacts\\RadioLibs_war_exploded\\uploads";
        getFileName(path);
    }
}
