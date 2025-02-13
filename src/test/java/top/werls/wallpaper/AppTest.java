package top.werls.wallpaper;

import static org.junit.jupiter.api.Assertions.*;
import static top.werls.wallpaper.App.JSON_NAME;
import static top.werls.wallpaper.App.JSON_SIZE;
import static top.werls.wallpaper.App.getFromSqlite;
import static top.werls.wallpaper.App.getJsonName;
import static top.werls.wallpaper.App.writeToTxt;
import static top.werls.wallpaper.App.writerJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author JiaWei Lee
 * @date 2024/2/28
 * @since on   2024/2/28
 */
class AppTest {

   @Test
  void saveToSqlite() throws Exception {
    List<Images> imagesList = getFromSqlite();
    int jsonIndex = getJsonName();
    //分割文件。
    // 按1000个存储一个json文件
    int size = imagesList.size() / JSON_SIZE;
//    for (int i = jsonIndex; i <= size; i++) {
//      int start = i * JSON_SIZE;
//      int end = (i + 1) * JSON_SIZE;
//      List<Images> images = imagesList.subList(start, Math.min(end, imagesList.size()));
//      writerJson(images, i + JSON_NAME);
//      jsonIndex = i;
//    }

    writeToTxt(jsonIndex);
    System.out.println("size: " + size + " index: " + jsonIndex);
     Calendar cal = Calendar.getInstance();

     System.out.println(cal.get(Calendar.YEAR));
  }
}