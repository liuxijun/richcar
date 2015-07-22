package com.fortune.util;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-5-1
 * Time: 下午4:44
 * 图片处理的助手，原始作者@author yangyz
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * 对图片的操作  *   *
 *
 */
public class ImageControl {
    // 添加字体的属性值
    private Font font = new Font("", Font.PLAIN, 12);
    private Graphics2D g = null;
    private int fontsize = 0;

    /**
     * 导入本地图片到缓冲中      *
     *
     * @param imgPath *
     * @return *
     */
    public BufferedImage loadImageLocal(String imgPath) {
        try {
            return ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取网络中的图片到缓冲
     *
     * @param imgPath *
     * @return *
     */
    public BufferedImage loadImageUrl(String imgPath) {
        URL url;
        try {
            url = new URL(imgPath);
            return ImageIO.read(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void writeImageLocal(BufferedImage img,String fileName) {
        String type = FileUtils.getFileExtName(fileName);
        writeImageLocal(img,fileName,type);
    }

        /**
        * 生成新图片到本地
        *
        * @param img  *            缓冲      *
         *@param fileName       文件名
        * @param type *            类别
        */
    public void writeImageLocal(BufferedImage img,String fileName, String type) {
        if (img != null) {
            File outputFile = new File(fileName);
            try {
                ImageIO.write(img, type, outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置文字字体，大小
     *
     * @param fontStyle *
     * @param fontSize  *
     */
    public void setFont(String fontStyle, int fontSize) {
        this.fontsize = fontSize;
        this.font = new Font(fontStyle, Font.PLAIN, fontSize);
    }

    /**
     * 单行文本      *       *
     *
     * @param img     *
     * @param content *
     * @return *
     */
    public BufferedImage modifyImage(BufferedImage img, Object content) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            g.setColor(Color.RED);
            if (this.font != null) {
                g.setFont(this.font);
            }
            // 文字输出位置
            int y = h - fontsize - 10;
            int x = w - 70;
            if (content != null) {
                g.drawString(content.toString(), x, y);
            }
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    /**
     * 将两张图片合并      *       *
     *
     * @param base *
     * @param dest *            目标      *
     * @return *
     */
    public BufferedImage modifyImageTogether(BufferedImage base,
                                             BufferedImage dest) {
        try {
            int w = base.getWidth();
            int h = base.getHeight();
            int dw = dest.getWidth();
            int dh = dest.getHeight();
            g = dest.createGraphics();
            AlphaComposite cp = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.3f);
            g.setComposite(cp);
            g.drawImage(base, dw - w, dh - h, w, h, null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 改变图片大小      *       *
     *
     * @param img    *
     * @param width  *
     * @param height *
     * @return *
     */
    public BufferedImage modifySize(BufferedImage img, int width, int height) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            double wRation = (new Integer(width)).doubleValue() / w;
            double hRation = (new Integer(height)).doubleValue() / h;
            Image image = img.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            AffineTransformOp op = new AffineTransformOp(AffineTransform
                    .getScaleInstance(wRation, hRation), null);
            image = op.filter(img, null);
            img = (BufferedImage) image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public BufferedImage changeSize(BufferedImage img, int width, int height) {
        try {
            BufferedImage distin = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            g = distin.createGraphics();
            g.drawImage(img, 0, 0, width, height, null);
            return distin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 截图      *       *
     *
     * @param img    *
     * @param x      *
     * @param y      *
     * @param width  *
     * @param height *
     * @return *
     */
    public BufferedImage cutImage(BufferedImage img, int x, int y, int width, int height) {
        BufferedImage distin = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try {
            g = distin.createGraphics();             // 原图的大小，以及截图的坐标及大小
            g.drawImage(img, 0, 0, width, height, x, y,x+ width, y+height, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distin;
    }

    /**
     * 测试      *       *
     *
     * @param args *
     */
    public static void main(String[] args) {
        ImageControl ic = new ImageControl();           // 两张图片合并
        BufferedImage d = ic.loadImageLocal("E:\\temp\\0002.jpg");
        int x = d.getWidth()/4;
        int w = d.getWidth()/2;
        System.out.println("x,y,w,h="+x+",0,"+w+","+d.getHeight());
        ic.writeImageLocal(ic.cutImage(d,x,0,w,d.getHeight()), "E:\\temp\\cut.jpg");
        // 在向生成的图片添加文字
        //ic.modifyImage(d, "Troy Young");
        // // 根据坐标截图
        // BufferedImage d = ic.loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\catergory_list_right.jpg");         //
        // ic.setDestDir("f:\\temp\\");
        //ic.writeImageLocal(ic.cutImage(d, 30, 20, 200, 100), "E:\\temp\\text.jpg");
        // 修改图片大小         //
        // BufferedImage d = ic.loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\catergory_list_right.jpg");         //
        // ic.setDestDir("f:\\temp\\");
        ic.writeImageLocal(ic.modifySize(d,d.getWidth()/2, d.getHeight()/2), "E:\\temp\\resize.jpg");
    }
}