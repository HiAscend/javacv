package cn.edu.zua.javacv;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @BeforeClass
    public void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

   @Test
    public void test() {
       Mat horse = Imgcodecs.imread("/tmp/test.jpg");
       Mat tmp = new Mat();
       Core.transpose(horse, tmp);
       Mat result = new Mat();
       Core.flip(tmp, result, 0);
       Imgcodecs.imwrite("/tmp/90.jpg", result);

       HighGui.imshow("result", result);
       HighGui.waitKey();
   }
}
