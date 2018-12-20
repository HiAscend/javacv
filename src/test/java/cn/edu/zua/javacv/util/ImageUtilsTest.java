package cn.edu.zua.javacv.util;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SURF;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

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
    public void testEqualizeHist() {
        Mat dog = Imgcodecs.imread("/tmp/dog.jpg");
        Mat result = ImageUtils.equalizeHist(ImageUtils.gray(dog));

        showImg(result);
    }



    // ----------------显示图片

    private void showImg(Mat mat) {
        HighGui.imshow("结果", mat);
        HighGui.waitKey();
    }


    @Test
    public void test() {
        System.out.println();

        Mat horse = Imgcodecs.imread("/tmp/dog.jpg");
        Mat tmp = new Mat();
        // 此函数是转置、（即将图像逆时针旋转90度，然后再关于x轴对称）
        Core.transpose(horse, tmp);
        Mat result = new Mat();
        // flipCode = 0 绕x轴旋转180， 也就是关于x轴对称
        // flipCode = 1 绕y轴旋转180， 也就是关于y轴对称
        // flipCode = -1 此函数关于原点对称
//        Core.flip(tmp, result, -1);
//        HighGui.imshow("result", result);
//        HighGui.waitKey();
//        ImageUtils.save("/tmp/dogtranspose.jpg", tmp);
    }

}
