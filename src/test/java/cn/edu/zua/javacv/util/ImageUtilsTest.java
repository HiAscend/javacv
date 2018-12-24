package cn.edu.zua.javacv.util;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SURF;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.math.BigDecimal;

/**
 * @author ascend
 * @date 2018/12/14
 */
public class ImageUtilsTest {
    private static final Logger LOG = Logger.getLogger(ImageUtilsTest.class);

    @BeforeClass
    public void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void testSave() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        ImageUtils.save("/tmp/dog666.jpg", dog);
    }

    @Test
    public void testCut() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat smallDog = ImageUtils.cut(dog, 0, 0, 5000, 5000);
        showImg(smallDog);
    }

    @Test
    public void testCut2() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat smallDog = ImageUtils.cut(dog, 200, 200);
        showImg(smallDog);
    }

    @Test
    public void testResize() {
        Mat horse = Imgcodecs.imread("/tmp/dog.jpg");
        Size dSize = new Size(horse.width() * 2, horse.height() * 2);
        Mat resultMat = ImageUtils.resize(horse, dSize);
        HighGui.imshow("结果", resultMat);
        HighGui.waitKey();
    }

    @Test
    public void testRotate() {
        Mat dog = Imgcodecs.imread("/tmp/face/face3.jpg");
        Mat result = ImageUtils.rotate(dog, 30);
        Imgcodecs.imwrite("/tmp/face/face330.jpg", result);
//        showImg(result);
    }

    @Test
    public void testRotateLeft() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat result = ImageUtils.rotateLeft(dog);
        showImg(result);
    }

    @Test
    public void testRotateRight() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat result = ImageUtils.rotateRight(dog);
        showImg(result);
    }

    @Test
    public void testGray() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat result = ImageUtils.gray(dog);
        showImg(result);
    }

    @Test
    public void testRemoveBlackEdge() {
        Mat mat = Imgcodecs.imread("/tmp/test.jpg");
        Mat result = ImageUtils.removeBlackEdge(mat);
        showImg(result);
    }

    @Test
    public void testRemoveBlackEdge2() {
        Mat mat = Imgcodecs.imread("/tmp/999.jpg");
        Mat result = ImageUtils.removeBlackEdge(mat, 5);
        SURF surf = SURF.create(100.0, 4, 3, true, true);
        MatOfKeyPoint kpts = new MatOfKeyPoint();
        surf.detect(mat, kpts);
        System.out.println(kpts.toArray().length);
//        LOG.debug(kpts.toArray().length);
        showImg(result);
    }

    @Test
    public void testRemoveWhiteEdge() {
//        Mat mat = Imgcodecs.imread("/tmp/logo/pos/images/logo_40.jpg");
//        Mat result = ImageUtils.removeWhiteEdge(mat);
//        ImageUtils.save("/tmp/logo/pos/images/logo_40_result.jpg", result);
        for (int i = 0; i < 13; i++) {
            Mat mat = Imgcodecs.imread("/tmp/logo/pos/images/logo_(" + (i + 1) + ").jpg");
            Mat result = ImageUtils.removeWhiteEdge(mat);
            ImageUtils.save("/tmp/logo/pos/images/result_" + (i + 1) + ".jpg", result);
        }
    }

    @Test
    public void testEqualizeHist() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat result = ImageUtils.equalizeHist(ImageUtils.gray(dog));

        showImg(result);
    }

    @Test
    public void testIsSimilar() {
        Mat horse1 = Imgcodecs.imread("/tmp/horse3.jpg");
        Mat srcMat = ImageUtils.removeBlackEdge(horse1);

        Mat horse2 = Imgcodecs.imread("/tmp/horse2.jpg");
        Mat destMat = ImageUtils.removeBlackEdge(horse2);

        boolean result1 = ImageUtils.isSimilar(srcMat, destMat);
        Assert.assertFalse(result1);

        boolean result2 = ImageUtils.isSimilar(srcMat, destMat, 512);
        Assert.assertFalse(result2);

        boolean result3 = ImageUtils.isSimilar(srcMat, destMat, 512, 512, 60.0);
        Assert.assertFalse(result3);
    }

    @Test
    public void testIsSimilar2() {
        Mat horse1 = Imgcodecs.imread("/tmp/horse3.jpg");
        Mat srcMat = ImageUtils.removeBlackEdge(horse1);

        Mat horse2 = Imgcodecs.imread("/tmp/horse2.jpg");
        Mat destMat = ImageUtils.removeBlackEdge(horse2);
        double width = 512;
        System.out.println("//\t\t\t\t\t\t\t width\t\theight\t\tsimilar\t\tperiod");
        for (int i = 0; i < 100; i++) {
            if (width > 100) {
                try {
//                    execute(srcMat, destMat, width);
                } catch (Exception e) {
                    // ignore
                }
            }
            width -= 8;
        }
//        execute(srcMat, destMat, 168);

    }

  /*  private void execute(Mat src, Mat dest, double width) {
        long start = System.currentTimeMillis();
        double height = width * 3 / 4;
//        double height = width;
        double similarity = ImageUtils.isSimilar(src, dest, width, height);
//        BigDecimal bd = new BigDecimal(similarity);
//        bd.setScale(2, BigDecimal.ROUND_FLOOR);
        long period = (System.currentTimeMillis() - start);
        if (!Double.isNaN(similarity)) {
            System.out.println("// " + width + "\t\t" + height + "\t\t" + String.format("%.2f", similarity) + "%\t\t" + period);
            System.out.println();
        }
    }*/

    @Test
    public void testDrawKeyPoints() {
        Mat dog = Imgcodecs.imread("/tmp/ship.jpg");
        Mat result = ImageUtils.drawKeyPoints(dog);

        showImg(result);
    }


    // ----------------显示图片

    private void showImg(Mat mat) {
        HighGui.imshow("结果", mat);
        HighGui.waitKey();
    }


    @Test
    public void test() {
        System.out.println(780 / 512.0);

    }

}
