package cn.edu.zua.javacv;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

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
        // 测试
    }

    @Test
    public void test2() {
        String src = "170101001\n" +
                "170101002\n" +
                "170524001\n" +
                "170524002\n" +
                "170607001\n" +
                "170607002\n" +
                "170614001\n" +
                "170614002\n" +
                "170705001\n" +
                "170705002\n" +
                "170712001\n" +
                "170712002\n" +
                "170720001\n" +
                "170726001\n" +
                "170726002\n" +
                "170809001\n" +
                "170816001\n" +
                "170816002\n" +
                "170823001\n" +
                "170830001";
        String[] arr = src.split("\n");
        List<String> list = Arrays.asList(arr);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            String s = "-- "+(i+1)+". 加载 "+item+".csv\n" +
                    " delete from SRC_MAT;\n" +
                    " copy SRC_MAT from '/opt/data/media/csv/"+item+".csv' with csv header;\n" +
                    " insert into MAT (code,FRAME,MYCUT,POINTINDEX,ROW_VEC) select "+item+",FRAME,MYCUT,POINTINDEX,ROW_VEC from SRC_MAT;\n" +
                    "-- 显示时间\n" +
                    " select now();\n";
            sb.append(s);
        }
        System.out.println(sb.toString());
    }
}
