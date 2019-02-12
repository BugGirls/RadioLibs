package hndt.radiolibs.util;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {

    public static void stampWithIcon(String iconPath, String srcImgPath, String targetImgPath, int position) {
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics(); //得到画笔对象
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //设置对线段的锯齿状边缘处理
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            ImageIcon iconImg = new ImageIcon(iconPath); //水印图片应该是png的，这样可设置透明度
            Image img = iconImg.getImage();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .9f));
            int padding = 2;
            int buffImgHeight = buffImg.getHeight() - padding;
            int buffImgWidth = buffImg.getWidth() - padding;
            int x = 0;
            int y = 0;
            if (position == 4 || position == 5 || position == 6) {
                y = (buffImgHeight - iconImg.getIconHeight()) / 2;
            }
            if (position == 7 || position == 8 || position == 9) {
                y = (buffImgHeight - iconImg.getIconHeight());
            }
            if (position == 2 || position == 5 || position == 8) {
                x = (buffImgWidth - iconImg.getIconWidth()) / 2;
            }
            if (position == 3 || position == 6 || position == 9) {
                x = (buffImgWidth - iconImg.getIconWidth());
            }
            g.drawImage(img, x, y, null); // 6、水印图片的位置x,y
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();// 7、释放资源
            os = new FileOutputStream(targetImgPath);// 8、生成图片
            ImageIO.write(buffImg, "JPG", os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
