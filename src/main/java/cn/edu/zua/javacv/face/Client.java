package cn.edu.zua.javacv.face;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * @author ascend
 * @date 2018/12/20
 */
public class Client {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Running FaceDetector");

        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_frontalface_alt.xml").getPath().substring(1);
        System.out.println("faceXml = " + faceXml);
        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face2.jpg");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, 0, new Size(40, 40));

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }



        HighGui.imshow("结果", image);
        HighGui.waitKey();
        System.exit(0);
    }
}
