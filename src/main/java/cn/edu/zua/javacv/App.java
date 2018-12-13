package cn.edu.zua.javacv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat horse = Imgcodecs.imread("/tmp/shine0.jpg");
        HighGui.imshow("result", horse);
        HighGui.waitKey();
        System.exit(0);

    }
}
