package cn.edu.zua.javacv;


import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.utils.Converters;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat mat = Imgcodecs.imread("/tmp/dog.jpg");
        opencv_core.GpuMat gpuMat = new opencv_core.GpuMat();
//        gpuMat.upload();



        HighGui.imshow("result", mat);
        HighGui.waitKey();
        System.exit(0);

    }
}
