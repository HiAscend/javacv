package cn.edu.zua.javacv.face;

import cn.edu.zua.mytool.core.util.CharsetUtils;

import org.apache.commons.io.FileUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static cn.edu.zua.javacv.util.ImageUtils.*;

/**
 * @author ascend
 * @date 2018/12/29
 */
public class LogoTagTest {
    @BeforeClass
    public void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 24, 24, 128, 88
     */
    @Test
    public void testChongqing() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 100;
        int index = 1;
        // 640X480  new Rect(24, 25, 104, 62)
        // 320X240  new Rect(12, 12, 52, 31)
        Rect rect = new Rect(12, 12, 52, 31);
//        Mat mat = findMat(frameIndex, new File(basePath, "video/chongqing3.mp4").getAbsolutePath());
//        mat = resize(mat, new Size(320, 240));
//        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
//        showImg(mat);
        for (int i = 0; i < 500; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/chongqing" + videoIndex + ".mp4").getAbsolutePath());
                Mat small = resize(mat, new Size(320, 240));
                String imageName = "chongqing_" + index + ".jpg";
                save(new File(basePath, "images/chongqing/" + imageName).getAbsolutePath(), small);
                File xmlFile = new File(basePath, "xml/chongqing/chongqing_" + index + ".xml");
                writeXml(small, "chongqing", imageName, xmlFile.getAbsolutePath(), rect);
                index++;
                frameIndex += 50;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 150;
                e.printStackTrace();
            }
            if (videoIndex > 3) {
                break;
            }
        }
    }

    @Test
    public void testBeijing() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 2;
        int index = 1;
        // 1920X1080  new Rect(105, 70, 390, 80)
        // 384X216    new Rect(20, 13, 80, 20)
        /*Rect rect = new Rect(20, 12, 80, 20);
        Mat mat = findMat(frameIndex, new File(basePath, "video/beijing2.mp4").getAbsolutePath());
        mat = resize(mat, new Size(384,216));
        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
        showImg(mat);*/

        Rect rect = new Rect(28, 18, 129, 42);
        Mat mat = findMat(frameIndex, new File(basePath, "video/beijing2.mp4").getAbsolutePath());
        mat = resize(mat, new Size(500,281));
        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
        showImg(mat);

        /*for (int i = 0; i < 500; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/beijing" + videoIndex + ".mp4").getAbsolutePath());
                Mat small = resize(mat, new Size(384, 216));
                String imageName = "beijing_" + index + ".jpg";
                save(new File(basePath, "images/beijing/" + imageName).getAbsolutePath(), small);
                File xmlFile = new File(basePath, "xml/beijing/beijing_" + index + ".xml");
                writeXml(small, "beijing", imageName, xmlFile.getAbsolutePath(), rect);
                index++;
                frameIndex += 70;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 2;
                e.printStackTrace();
            }
            if (videoIndex > 2) {
                break;
            }
        }*/
    }

    @Test
    public void testShanghai() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 2;
        int index = 1;
        // 1920X1080  new Rect(135, 65, 120, 120)
        // 384X216    new Rect(25, 11, 26, 26)
        Rect rect = new Rect(25, 11, 26, 26);
        Mat mat = findMat(frameIndex, new File(basePath, "video/shanghai3.mp4").getAbsolutePath());
        mat = resize(mat, new Size(384,216));
        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
        showImg(mat);

        /*for (int i = 0; i < 500; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/shanghai" + videoIndex + ".mp4").getAbsolutePath());
                Mat small = resize(mat, new Size(384, 216));
                String imageName = "shanghai_" + index + ".jpg";
                save(new File(basePath, "images/shanghai/" + imageName).getAbsolutePath(), small);
                File xmlFile = new File(basePath, "xml/shanghai/shanghai_" + index + ".xml");
                writeXml(small, "shanghai", imageName, xmlFile.getAbsolutePath(), rect);
                index++;
                frameIndex += 150;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 2;
                e.printStackTrace();
            }
            if (videoIndex > 3) {
                break;
            }
        }*/
    }


    private void writeXml(Mat mat, String name, String imageName, String xmlFilePath, Rect rect) throws IOException {
        String sb = "<annotation>\n" +
                "\t<folder>images</folder>\n" +
                "\t<filename>" + imageName + "</filename>\n" +
                "\t<path>" + "Z:/adeng/work/logo/images/" + imageName + "</path>\n" +
                "\t<source>\n" +
                "\t\t<database>Unknown</database>\n" +
                "\t</source>\n" +
                "\t<size>\n" +
                "\t\t<width>" + mat.width() + "</width>\n" +
                "\t\t<height>" + mat.height() + "</height>\n" +
                "\t\t<depth>" + 3 + "</depth>\n" +
                "\t</size>\n" +
                "\t<segmented>0</segmented>\n" +
                "\t<object>\n" +
                "\t\t<name>" + name + "</name>\n" +
                "\t\t<pose>Unspecified</pose>\n" +
                "\t\t<truncated>0</truncated>\n" +
                "\t\t<difficult>0</difficult>\n" +
                "\t\t<bndbox>\n" +
                "\t\t\t<xmin>" + rect.x + "</xmin>\n" +
                "\t\t\t<ymin>" + rect.y + "</ymin>\n" +
                "\t\t\t<xmax>" + rect.width + "</xmax>\n" +
                "\t\t\t<ymax>" + rect.height + "</ymax>\n" +
                "\t\t</bndbox>\n" +
                "\t</object>\n" +
                "</annotation>\n";
        FileUtils.writeByteArrayToFile(new File(xmlFilePath), sb.getBytes(CharsetUtils.UTF_8), false);
    }

    @Test
    public void test() throws IOException {
        String str = "hello world";
        File file = new File("/tmp/tf/res/xml/test.xml");
        FileUtils.writeByteArrayToFile(file, str.getBytes(CharsetUtils.UTF_8), false);

    }


    @Test
    public void saveBeijing() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 2;
        int index = 1;
        // 1920X1080  new Rect(105, 70, 390, 80)
        // 384X216    new Rect(20, 13, 80, 20)
//        Rect rect = new Rect(20, 12, 80, 20);
//        Mat mat = findMat(frameIndex, new File(basePath, "video/beijing2.mp4").getAbsolutePath());
//        mat = resize(mat, new Size(384,216));
//        Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 1);
//        showImg(mat);

        for (int i = 0; i < 5; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/beijing" + videoIndex + ".mp4").getAbsolutePath());
                String imageName = "logo_" + index + ".jpg";
                save(new File(basePath, "test/"+imageName).getAbsolutePath(), mat);
                index++;
                frameIndex += 200;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 2;
                e.printStackTrace();
            }
            if (videoIndex > 2) {
                break;
            }
        }
    }

    @Test
    public void saveShanghai() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 2;
        int index = 6;


        for (int i = 0; i < 5; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/shanghai" + videoIndex + ".mp4").getAbsolutePath());
                String imageName = "logo_" + index + ".jpg";
                save(new File(basePath, "test/"+imageName).getAbsolutePath(), mat);
                index++;
                frameIndex += 200;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 2;
                e.printStackTrace();
            }
            if (videoIndex > 2) {
                break;
            }
        }
    }

    @Test
    public void saveChongqing() throws IOException {
        String basePath = "/tmp/tf/res/";
        int videoIndex = 1;
        int frameIndex = 2;
        int index = 11;


        for (int i = 0; i < 5; i++) {
            try {
                Mat mat = findMat(frameIndex, new File(basePath, "video/chongqing" + videoIndex + ".mp4").getAbsolutePath());
                String imageName = "logo_" + index + ".jpg";
                save(new File(basePath, "test/"+imageName).getAbsolutePath(), mat);
                index++;
                frameIndex += 200;
                System.out.println("*******************************************************************index:" + index);
            } catch (Exception e) {
                videoIndex++;
                frameIndex = 2;
                e.printStackTrace();
            }
            if (videoIndex > 2) {
                break;
            }
        }
    }
}
