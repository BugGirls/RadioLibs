package hndt.radiolibs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author Hystar
 * @date 2017/11/29
 */
public class FileUtil {

    /**
     * 文件复制：使用NIO中的管道到管道传输
     * 在第一种实现方法基础上对输入输出流获得其管道,然后分批次的从f1的管道中像f2的管道中输入数据每次输入的数据最大为2MB
     *
     * @param f1
     * @param f2
     * @return
     * @throws Exception
     */
    public static long forTransfer(File f1, File f2) throws Exception {
        long time = System.currentTimeMillis();
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        int i = 0;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return System.currentTimeMillis() - time;
            }
            if ((inC.size() - inC.position()) < 20971520) {
                length = (int) (inC.size() - inC.position());
            } else {
                length = 20971520;
            }
            inC.transferTo(inC.position(), length, outC);
            inC.position(inC.position() + length);
            i++;
        }
    }
}
