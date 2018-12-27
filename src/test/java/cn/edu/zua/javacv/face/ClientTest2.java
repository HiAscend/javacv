package cn.edu.zua.javacv.face;

import cn.edu.zua.javacv.util.ImageUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Random;

import static cn.edu.zua.javacv.util.ImageUtils.*;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

/**
 * @author ascend
 * @date 2018/12/20
 */
public class ClientTest2 {
    @BeforeClass
    public void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 各参数含义：
     * const Mat& image: 需要被检测的图像（灰度图）
     * vector<Rect>& objects: 保存被检测出的人脸位置坐标序列
     * double scaleFactor: 表示在前后两次相继的扫描中，搜索窗口的比例系数。默认为1.1即每次搜索窗口依次扩大10%;
     * int minNeighbors: 每一个人脸至少要检测到多少次才算是真的人脸
     * int flags： 决定是缩放分类器来检测，还是缩放图像
     * Size(): 表示人脸的最大最小尺寸
     * ---------------------
     * 作者：zuidao3105
     * 来源：CSDN
     * 原文：https://blog.csdn.net/zuidao3105/article/details/79346591
     * 版权声明：本文为博主原创文章，转载请附上博文链接！
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testFace() {
        String faceXml = Client.class.getClassLoader().getResource("data/lbpcascades/lbpcascade_frontalface_improved.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face8.jpg");
        Mat grayMat = ImageUtils.gray(image);

        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(grayMat, faceDetections,1.1, 5, 0, new Size(40, 40));
        MatOfInt numDetections = new MatOfInt();
        faceDetector.detectMultiScale2(grayMat, faceDetections, numDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        for (int i : numDetections.toArray()) {
            System.out.println("i = " + i);
        }

        showImg(image);
    }

    /**
     * 1
     * image	Matrix of the type CV_8U containing an image where objects are detected.
     * objects	Vector of rectangles where each rectangle contains the detected object, the rectangles may be partially outside the original image.
     * scaleFactor	Parameter specifying how much the image size is reduced at each image scale.
     * minNeighbors	Parameter specifying how many neighbors each candidate rectangle should have to retain it.
     * flags	Parameter with the same meaning for an old cascade as in the function cvHaarDetectObjects. It is not used for a new cascade.
     * minSize	Minimum possible object size. Objects smaller than that are ignored.
     * maxSize	Maximum possible object size. Objects larger than that are ignored. If maxSize == minSize model is evaluated on single scale.
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testFace2() {
        String faceXml = Client.class.getClassLoader().getResource("data/lbpcascades/lbpcascade_frontalface_improved.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face8.jpg");
        Mat grayMat = ImageUtils.gray(image);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(grayMat, faceDetections, 1.1, 5, 0, new Size(40, 40));
        MatOfInt numDetections = new MatOfInt();
        faceDetector.detectMultiScale2(grayMat, faceDetections, numDetections);
//        faceDetector.detectMultiScale3(grayMat, faceDetections, numDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (int i = 0; i < numDetections.toArray().length; i++) {
            int numDetection = numDetections.toArray()[i];
            Rect rect = faceDetections.toArray()[i];
            // 22 4 9
//            System.out.println(numDetection);
            if (numDetection > 0) {
                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
            }
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testFace3() {
        String faceXml = Client.class.getClassLoader().getResource("data/logo/logo2/cascade.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/logo2/tmp/logo8.jpg");
        Mat grayMat = ImageUtils.gray(image);

        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(grayMat, faceDetections,1.1, 3, 0);
//        faceDetector.detectMultiScale(grayMat, faceDetections, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
        faceDetector.detectMultiScale(grayMat, faceDetections, 1.1, 3, 0);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    /**
     * CV_HAAR_DO_CANNY_PRUNING =    1
     */
    @Test
    @SuppressWarnings("Duplicates")
    public void testEye() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_eye.xml").getPath().substring(1);

        CascadeClassifier detector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face6.jpg");

        MatOfRect detections = new MatOfRect();
        Mat detectorMat = ImageUtils.equalizeHist(ImageUtils.gray(image));
        detector.detectMultiScale(detectorMat, detections, 1.5, 5, CV_HAAR_DO_CANNY_PRUNING, new Size(40, 40));

        System.out.println(String.format("Detected %s eye", detections.toArray().length));
        for (Rect rect : detections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testBody() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_fullbody.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face7.jpg");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testSmile() {
        String faceXml = Client.class.getClassLoader().getResource("data/haarcascades/haarcascade_smile.xml").getPath().substring(1);

        CascadeClassifier faceDetector = new CascadeClassifier(faceXml);
        Mat image = Imgcodecs.imread("/tmp/face/face6.jpg");

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
        }

        showImg(image);
    }

    // ----------------显示图片

    private void showImg(Mat mat) {
        HighGui.imshow("结果", mat);
        HighGui.waitKey();
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogo() {
        Mat logo = Imgcodecs.imread("/tmp/logo/pos/images/logo_40_result.jpg");
        Mat grayMat = ImageUtils.gray(logo);
        int topRow = 0;
        int leftCol = 0;
        // 开始
        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < grayMat.width(); col++) {
                System.out.print(grayMat.get(row, col)[0]);
                System.out.print("\t");
            }
            System.out.println();
        }
//        showImg(logo);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoPos() {
//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(3000));
//        showImg(cut(mat, 107, 73, 386, 80));
//        Mat small = ImageUtils.cut(mat, 28, 30, 51, 51);
//        showImg(small);

        int index = 1;
        int frame = 100;

        for (int i = 0; i < 200; i++) {
            try {
                Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));
                Mat logo = ImageUtils.cut(mat, 107, 73, 386, 80);
                ImageUtils.save("/tmp/logo2/pos/" + (index++) + ".jpg", gray(resize(logo, new Size(75, 15))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame += 50;
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoNeg() {
//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(2400));
//        Mat smallMat = ImageUtils.removeBlackEdge(mat);
//        Mat neg1 = ImageUtils.cut(smallMat, 600, 260, 200, 100);
//        showImg(ImageUtils.gray(neg1));

        int index = 1;
        int frame = 2200;

        for (int i = 0; i < 100; i++) {
            Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));
            Mat smallMat = ImageUtils.removeBlackEdge(mat);

            Mat neg1 = ImageUtils.cut(smallMat, 600, 160, 200, 100);
            Mat neg2 = ImageUtils.cut(smallMat, 600, 260, 200, 100);
            Mat neg3 = ImageUtils.cut(smallMat, 600, 360, 200, 100);


            ImageUtils.save("/tmp/logo2/neg/" + (index++) + ".jpg", ImageUtils.gray(neg1));
            ImageUtils.save("/tmp/logo2/neg/" + (index++) + ".jpg", ImageUtils.gray(neg2));
            ImageUtils.save("/tmp/logo2/neg/" + (index++) + ".jpg", ImageUtils.gray(neg3));
            frame += 100;
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCutLogoNeg2() {
//        Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(2400));
//        Mat smallMat = ImageUtils.removeBlackEdge(mat);
//        Mat neg1 = ImageUtils.cut(smallMat, 600, 260, 200, 100);
//        showImg(ImageUtils.gray(neg1));

        Random random = new Random();
        int index = 1;
        int frame = 1000;
        int base = 80;
        int inc = 200;
        boolean flag = true;

        for (int i = 0; i < 500; i++) {
            try {
                Mat mat = ImageUtils.convertToOrgOpenCvCoreMat(findFrame(frame));
                Mat smallMat = ImageUtils.removeBlackEdge(mat);
                Mat grayMat = gray(smallMat);
                flag = !flag;
                Mat sunMat1;
                Mat sunMat2;
                if (flag) {
                    sunMat1 = cut(grayMat, 200 + random.nextInt(100), 50 + random.nextInt(100), 720, 720);
                    sunMat2 = cut(grayMat, 800 + random.nextInt(100), 150 + random.nextInt(100), 720, 720);
                }else {
                    sunMat1 = cut(grayMat, 200 + random.nextInt(100), 250 + random.nextInt(100), 520, 520);
                    sunMat2 = cut(grayMat, 800 + random.nextInt(100), 50 + random.nextInt(100), 720, 720);
                }

                int value = base + random.nextInt(inc);
                int value2 = base + random.nextInt(inc);
                Mat neg1 = resize(sunMat1, new Size(value, value));
                Mat neg2 = resize(sunMat2, new Size(value2, value2));

//                ImageUtils.save("/tmp/logo2/neg/" + (index++) + ".jpg", neg1);
//                ImageUtils.save("/tmp/logo2/neg/" + (index++) + ".jpg", neg2);
                ImageUtils.save("/tmp/neg/dajiangdahe_" + (index++) + ".jpg", neg2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame += 100;
        }

    }


    /**
     * 从视频中找出指定帧
     *
     * @return 指定帧
     */
    @SuppressWarnings("Duplicates")
    private Frame findFrame(int frameIndex) {
        Frame frame = null;
        String fileName = "Z:/tmp/logo2/test.mp4";
        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(fileName)) {
            fFmpegFrameGrabber.start();
            fFmpegFrameGrabber.setFrameNumber(frameIndex);
            Frame srcFrame = fFmpegFrameGrabber.grabImage();
            if (srcFrame != null) {
                frame = srcFrame.clone();
            }
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return frame;
    }


    @Test
    @SuppressWarnings("Duplicates")
    public void createNegTxt() {
        String path = "Z:\\tmp\\logo2\\neg\\";
        File txtFile = new File("Z:\\tmp\\logo2\\neg.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos, true);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            pw.println("neg/" + file.getName());
        }

    }

    @Test
    @SuppressWarnings("Duplicates")
    public void createPosTxt() {
        String path = "Z:\\tmp\\logo2\\pos\\";
        File txtFile = new File("Z:\\tmp\\logo2\\pos.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos, true);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            Mat mat = Imgcodecs.imread(file.getAbsolutePath());
            pw.println("pos/" + file.getName() + " 1 0 0 " + mat.width() + " " + mat.height());
        }

    }

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 11; i++) {
            try {
                Mat mat = Imgcodecs.imread("/tmp/logo2/pos/" + (i + 1) + ".jpg");
                ImageUtils.save("/tmp/logo2/pos/" + (i + 1) + ".jpg", resize(mat, new Size(50, 10)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() {
        File dir = new File("/tmp/neg/car/");
        int index = 1853;
        int base = 90;
        int inc = 150;
        Random rand = new Random();
        for (File file : dir.listFiles()) {
            Mat mat = Imgcodecs.imread(file.getAbsolutePath());
            int value = base + rand.nextInt(inc);
//            save("/tmp/neg/tmp/neg_"+(index++)+".jpg", gray(resize(mat, new Size(value, value))));
            save("/tmp/neg/tmp2/neg_"+(index++)+".jpg", mat);
        }

    }
}
