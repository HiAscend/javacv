package cn.edu.zua.javacv.util;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * javacv opencv 图片操作工具类
 *
 * @author ascend
 * @date 2018/12/14
 */
public class ImageUtils {
    /**
     * 去黑边"全黑"阈值
     */
    private static final int BLACK_VALUE = 5;

    private ImageUtils() {
    }

    /**
     * 保存图片
     *
     * @param fileName 绝对路径
     * @param mat      源Mat
     */
    public static void save(String fileName, Mat mat) {
        Imgcodecs.imwrite(fileName, mat);
    }

    /**
     * 按照指定的尺寸截取Mat，截取宽高自动计算（对称）。坐标原点为左上角
     *
     * @param src 源Mat
     * @param x   x
     * @param y   y
     * @return 截取后的Mat
     */
    public static Mat cut(Mat src, int x, int y) {
        // 截取尺寸
        int width = src.width() - 2 * x;
        int height = src.height() - 2 * y;
        return cut(src, x, y, width, height);
    }

    /**
     * 按照指定的尺寸截取Mat，坐标原点为左上角
     *
     * @param src    源Mat
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     * @return 截取后的Mat
     */
    public static Mat cut(Mat src, int x, int y, int width, int height) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (width > src.width()) {
            width = src.width();
        }
        if (height > src.height()) {
            height = src.height();
        }
        // 截取尺寸
        Rect rect = new Rect(x, y, width, height);
        return new Mat(src, rect);
    }

    /**
     * 缩放
     *
     * @param srcMat 源Mat
     * @param dSize  目标大小
     * @return 缩放结果Mat
     */
    public static Mat resize(Mat srcMat, Size dSize) {
        Mat retMat = new Mat();
        Imgproc.resize(srcMat, retMat, dSize);
        return retMat;
    }

    /**
     * 逆时针旋转,但是图片宽高不用变,此方法与rotateLeft和rotateRight不兼容
     *
     * @param src    源
     * @param angele 旋转的角度
     * @return 旋转后的对象
     */
    public static Mat rotate(Mat src, double angele) {
        Mat dst = src.clone();
        Point center = new Point(src.width() / 2.0, src.height() / 2.0);
        Mat affineTrans = Imgproc.getRotationMatrix2D(center, angele, 1.0);
        Imgproc.warpAffine(src, dst, affineTrans, dst.size(), Imgproc.INTER_NEAREST);
        return dst;
    }

    /**
     * 图像整体向左旋转90度
     *
     * @param src Mat
     * @return 旋转后的Mat
     */
    public static Mat rotateLeft(Mat src) {
        Mat tmp = new Mat();
        // 此函数是转置、（即将图像逆时针旋转90度，然后再关于x轴对称）
        Core.transpose(src, tmp);
        Mat result = new Mat();
        // flipCode = 0 绕x轴旋转180， 也就是关于x轴对称
        // flipCode = 1 绕y轴旋转180， 也就是关于y轴对称
        // flipCode = -1 此函数关于原点对称
        Core.flip(tmp, result, 0);
        return result;
    }

    /**
     * 图像整体向左旋转90度
     *
     * @param src Mat
     * @return 旋转后的Mat
     */
    public static Mat rotateRight(Mat src) {
        Mat tmp = new Mat();
        // 此函数是转置、（即将图像逆时针旋转90度，然后再关于x轴对称）
        Core.transpose(src, tmp);
        Mat result = new Mat();
        // flipCode = 0 绕x轴旋转180， 也就是关于x轴对称
        // flipCode = 1 绕y轴旋转180， 也就是关于y轴对称
        // flipCode = -1 此函数关于原点对称
        Core.flip(tmp, result, 1);
        return result;
    }

    /**
     * 灰度处理 BGR灰度处理
     *
     * @param src 原图Mat
     * @return Mat 灰度后的Mat
     */
    public static Mat gray(Mat src) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    /**
     * 去除图片黑边，若无黑边，则原图返回。默认“全黑”阈值为 {@code BLACK_VALUE}
     *
     * @param srcMat 预去除黑边的Mat
     * @return 去除黑边之后的Mat
     */
    public static Mat removeBlackEdge(Mat srcMat) {
        return removeBlackEdge(srcMat, BLACK_VALUE);
    }

    /**
     * 去除图片黑边，若无黑边，则原图返回。
     *
     * @param blackValue 一般低于5的已经是很黑的颜色了
     * @param srcMat     源Mat对象
     * @return Mat对象
     */
    public static Mat removeBlackEdge(Mat srcMat, int blackValue) {
        // 预截取，默认播放条等情况的处理
        Mat smallMat = cut(srcMat, (int) (srcMat.width() * 0.02), (int) (srcMat.height() * 0.02));
        // 灰度
        Mat grayMat = gray(smallMat);
        int topRow = 0;
        int leftCol = 0;
        int rightCol = grayMat.width() - 1;
        int bottomRow = grayMat.height() - 1;

        // 上方黑边判断
        for (int row = 0; row < grayMat.height(); row++) {
            // 判断当前行是否基本“全黑”，阈值自定义；
            if (sum(grayMat.row(row)) / grayMat.width() < blackValue) {
                // 更新截取条件
                topRow = row;
            } else {
                break;
            }
        }
        // 左边黑边判断
        for (int col = 0; col < grayMat.width(); col++) {
            // 判断当前列是否基本“全黑”，阈值自定义；
            if (sum(grayMat.col(col)) / grayMat.height() < blackValue) {
                // 更新截取条件
                leftCol = col;
            } else {
                break;
            }
        }
        // 右边黑边判断
        for (int col = grayMat.width() - 1; col > 0; col--) {
            // 判断当前列是否基本“全黑”，阈值自定义；
            if (sum(grayMat.col(col)) / grayMat.height() < blackValue) {
                // 更新截取条件
                rightCol = col;
            } else {
                break;
            }
        }
        // 下方黑边判断
        for (int row = grayMat.height() - 1; row > 0; row--) {
            // 判断当前行是否基本“全黑”，阈值自定义；
            if (sum(grayMat.row(row)) / grayMat.width() < blackValue) {
                // 更新截取条件
                bottomRow = row;
            } else {
                break;
            }
        }

        int x = leftCol;
        int y = topRow;
        int width = rightCol - leftCol;
        int height = bottomRow - topRow;

        if (leftCol == 0 && rightCol == grayMat.width() - 1 && topRow == 0 && bottomRow == grayMat.height() - 1) {
            return srcMat;
        }
        return cut(smallMat, x, y, width, height);
    }

    /**
     * 求和
     *
     * @param mat mat
     * @return sum
     */
    private static int sum(Mat mat) {
        int sum = 0;
        for (int row = 0; row < mat.height(); row++) {
            for (int col = 0; col < mat.width(); col++) {
                sum += mat.get(row, col)[0];
            }
        }
        return sum;
    }

    /**
     * 未处理
     * 反色处理
     *
     * @param image
     * @return
     */
    public static Mat inverse(Mat image) {
        int width = image.cols();
        int height = image.rows();
        int dims = image.channels();
        byte[] data = new byte[width * height * dims];
        image.get(0, 0, data);
        int index = 0;
        int r = 0, g = 0, b = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width * dims; col += dims) {
                index = row * width * dims + col;
                b = data[index] & 0xff;
                g = data[index + 1] & 0xff;
                r = data[index + 2] & 0xff;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                data[index] = (byte) b;
                data[index + 1] = (byte) g;
                data[index + 2] = (byte) r;
            }
        }
        image.put(0, 0, data);
        return image;
    }


    /**
     * Mat转换成BufferedImage
     *
     * @param img     要转换的Mat
     * @param extName 格式为 ".jpg", ".png", etc
     * @return BufferedImage
     */
    public static BufferedImage mat2BufferedImage(Mat img, String extName) {
        // 将矩阵转换为适合此文件扩展名的字节矩阵
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(extName, img, mob);
        // 将字节矩阵转换为字节数组
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try (InputStream in = new ByteArrayInputStream(byteArray)) {
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    /**
     * BufferedImage转换成Mat
     *
     * @param original 要转换的BufferedImage
     * @param imgType  bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType  转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat BufImg2Mat(BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType) {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

}
