package cn.edu.zua.javacv;

import cn.edu.zua.javacv.util.ImageUtils;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat horse = Imgcodecs.imread("/tmp/test.jpg");
//        Mat horse2 = ImageUtils.resize(horse, new Size(horse.width(), horse.height()));

        System.out.println(horse);
        Mat dest = new Mat(new Size(horse.height(), horse.width()), CvType.CV_8UC3);
        Point center = new Point(horse.width() / 2.0, horse.height() / 2.0);
        Mat affineTrans = Imgproc.getRotationMatrix2D(center, 90, 0.5);
        Imgproc.warpAffine(horse, dest, affineTrans, dest.size());

        Imgcodecs.imwrite("/tmp/90.jpg", dest);

        HighGui.imshow("result", dest);
        HighGui.waitKey();
        System.exit(0);

    }
}
