package cn.edu.zua.javacv.util;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.features2d.AKAZE;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.opencv.core.Core.NORM_L2;

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
    /**
     * 相似度阈值
     */
    private static final double SIMILARITY_VALVE = 60.0;

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
     * 直方图均衡化，入参必须为灰度图
     *
     * @param grayMat 灰度图
     * @return 均衡化后的Mat
     * @throws IllegalArgumentException 如果入参不是灰度图
     */
    public static Mat equalizeHist(Mat grayMat) {
        if (grayMat.channels() != 1) {
            throw new IllegalArgumentException("入参必须为灰度图");
        }
        Mat retMat = new Mat();
        Imgproc.equalizeHist(grayMat, retMat);
        return retMat;
    }

    /**
     * 判断两个mat是否符合系统设定的阈值 SIMILARITY_VALVE
     * 采用 AKAZE 算法，后续可扩展其他算法
     * 默认resize尺寸为 src.width -> 512.0 然后按照 4:3 得到src的width，height，然后dest同尺度resize
     * 若去黑边 {@code ImageUtils#removeBlackEdge}
     *
     * @param src  源
     * @param dest 目标
     * @return boolean true 实际相似度 >= SIMILARITY_VALVE 返回true
     */
    public static boolean isSimilar(Mat src, Mat dest) {
        return isSimilar(src, dest, 512.0);

    }

    /**
     * 判断两个mat是否符合系统设定的阈值 SIMILARITY_VALVE
     * 采用 AKAZE 算法，后续可扩展其他算法
     * 默认resize尺寸为 src.width -> width 然后按照 4:3 得到src的width，height，然后dest同尺度resize
     * 若去黑边 {@code ImageUtils#removeBlackEdge}
     *
     * @param src   源
     * @param dest  目标
     * @param width 宽
     * @return boolean true 实际相似度 >= SIMILARITY_VALVE 返回true
     */
    public static boolean isSimilar(Mat src, Mat dest, double width) {
        double height = width * 3 / 4;
        return isSimilar(src, dest, width, height, SIMILARITY_VALVE);

    }

    /**
     * 判断两个mat是否符合系统设定的阈值 SIMILARITY_VALVE
     * 采用 AKAZE 算法，后续可扩展其他算法
     * 给啥就比啥
     *
     * @param src             源
     * @param dest            目标
     * @param width           图片宽（px）
     * @param height          图片高（px）
     * @param similarityValue 相似度阈值
     * @return boolean true 实际相似度 >= SIMILARITY_VALVE 返回true
     */
    public static boolean isSimilar(Mat src, Mat dest, double width, double height, double similarityValue) {
        boolean isSimilar = false;
        // 缩放
        Size size = new Size(width, height);
        Mat smallSrcMat = resize(src, size);
        Mat smallDestMat = resize(dest, size);

        Mat srcGrayMat = gray(smallSrcMat);
        Mat destGrayMat = gray(smallDestMat);

        AKAZE akaze = AKAZE.create();

        MatOfKeyPoint mokp = new MatOfKeyPoint();
        Mat desc = new Mat();
        akaze.detect(srcGrayMat, mokp);
        akaze.compute(srcGrayMat, mokp, desc);

        MatOfKeyPoint mokp2 = new MatOfKeyPoint();
        Mat desc2 = new Mat();
        akaze.detect(destGrayMat, mokp2);
        akaze.compute(destGrayMat, mokp2, desc2);

        BFMatcher matcher = BFMatcher.create(NORM_L2, true);
        MatOfDMatch matOfDMatch = new MatOfDMatch();
        matcher.match(desc, desc2, matOfDMatch);
        double similarity = (double) (2 * matOfDMatch.toArray().length) / (mokp.toArray().length + mokp2.toArray().length) * 100;
        if (similarity >= similarityValue) {
            isSimilar = true;
        }
        return isSimilar;
    }

    /**
     * 画出图片中的特征点，默认采用 AKAZE 算法
     *
     * @param imageMat 源图片
     * @return 画出特征点的 Mat
     */
    public static Mat drawKeyPoints(Mat imageMat) {
        AKAZE akaze = AKAZE.create();
        MatOfKeyPoint mokp = new MatOfKeyPoint();
        akaze.detect(imageMat, mokp);
        return drawKeyPoints(imageMat, mokp);
    }

    /**
     * 画出图片中的特征点
     *
     * @param imageMat 源图片
     * @param mokp     特征点
     * @return 画出特征点的 Mat
     */
    public static Mat drawKeyPoints(Mat imageMat, MatOfKeyPoint mokp) {
        Mat retMat = new Mat();
        Features2d.drawKeypoints(imageMat, mokp, retMat);
        return retMat;
    }

    private static void drawPoint(Mat srcMat, MatOfKeyPoint mokp, double width, double height, boolean src) {
        Mat outMat = new Mat();
        Features2d.drawKeypoints(srcMat, mokp, outMat, new Scalar(0, 0, 255));
        if (src) {
            save("/tmp/tmp/" + width + " X " + height + "_src.jpg", outMat);
        } else {

            save("/tmp/tmp/" + width + " X " + height + "_dest.jpg", outMat);
        }
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
