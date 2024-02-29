package top.werls.wallpaper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * @author leejiawei
 */
public class App {

  private static final String UHD_WIDTH = "3840";
  private static final String UHD_HEIGHT = "2160";
  private static final String YING_URL =
      "https://bing.com/HPImageArchive.aspx?format=js&n=1&uhd=1&uhdwidth="
          + UHD_WIDTH
          + "&uhdheight="
          + UHD_HEIGHT;

  private static final String BASIS_URL = "https://cn.bing.com";

  // 使用代理
  private static final String CN_BING_URL =
      "https://cn.bing.com/HPImageArchive.aspx?format=js&n=1&uhd=1&uhdwidth="
          + UHD_WIDTH
          + "&uhdheight="
          + UHD_HEIGHT;

  public static final String USER_AGENT =
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

  public static final int JSON_SIZE = 1000;

  public static final String JSON_NAME = "-images.json";

  public static final String FILE_INDEX = "index.txt";

  public static final String README = "README.md";

  public static final String CONNECT = "jdbc:sqlite:sqlite.db";
  public static final String IMAGES = "images/";



  /**
   * 获取图片信息
   *
   * @return
   * @throws Exception
   */
  public static Images getImages() throws Exception {
    Images images = new Images();

    // 创建httpclient 请求
    HttpClient client = HttpClient.newBuilder().build();

    JSONObject jsonObject = httpRe(client, CN_BING_URL, "110.242.68.66");

    DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    images.setEndDate(fmt.parse(jsonObject.getString("enddate")));
    images.setUrl(jsonObject.getString("url"));
    images.setCopyrightCN(jsonObject.getString("copyright"));
    images.setHash(jsonObject.getString("hsh"));

    // 获取英文版权 因为时间差异 可能会不一样
   JSONObject object = httpRe(client, YING_URL, "");
   images.setCopyright(object.getString("copyright"));
   images.setUrlForeign(jsonObject.getString("url"));
   images.setUtcDate(fmt.parse(jsonObject.getString("enddate")));

    // 记录文件名。
    // 添加日期
    String fileName = getUrlBase(images.getUrl()).replace("/th?id=", "");
    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    images.setFileName("bing_" + simpleDateFormat.format(images.getEndDate()) + "_" + fileName);
    images.setFileName4k("4k_" + simpleDateFormat.format(images.getEndDate()) + "_" + fileName);
    return images;
  }

  private static JSONObject httpRe(HttpClient client, String yingUrl, String xfor)
      throws IOException, InterruptedException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .header("x-forwarded-for", xfor)
            .header("User-Agent", USER_AGENT)
            .uri(URI.create(yingUrl))
            .build();
    HttpResponse<String> httpResponse =
        client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    JSONArray jsonArray = JSON.parseObject(httpResponse.body()).getJSONArray("images");
    return jsonArray.getJSONObject(0);
  }

  /**
   * 下载文件
   *
   * @param images
   * @throws Exception
   */
  public static void downloadFile(Images images) throws Exception {
    HttpClient client = HttpClient.newBuilder().build();

    // 4k
    HttpRequest request =
        HttpRequest.newBuilder()
            .header("User-Agent", USER_AGENT)
            .uri(URI.create(BASIS_URL + images.getUrl()))
            .build();

    // 原图
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .header("User-Agent", USER_AGENT)
            .uri(URI.create(BASIS_URL + getUrlBase(images.getUrl())))
            .build();

    Path path = Paths.get(IMAGES + images.getFileName4k());

    HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(path));

    Path filePath = Paths.get(IMAGES + images.getFileName());

    HttpResponse<Path> httpResponse =
        client.send(httpRequest, HttpResponse.BodyHandlers.ofFile(filePath));
  }

  /**
   * 追加写入readme
   *
   * @param images images object
   * @throws Exception writer
   */
  public static void writeMd(Images images) throws Exception {
    //
    File file = new File(README);
    FileWriter fileWriter = new FileWriter(file);

    String readme =
        """
            # wallpaper
            [![Java CI with Gradle](https://github.com/gsdukbh/wallpaper/actions/workflows/gradle.yml/badge.svg)](https://github.com/gsdukbh/wallpaper/actions/workflows/gradle.yml)

            bing wallpaper 4k download

            """;
    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    String url = getUrlBase(images.getUrl());

    String path = IMAGES;
    String md =
        """
            ![%1$s](%2$s%3$s) [%4$s](%7$s%5$s) %6$s
            """
            .formatted(
                images.getCopyright(),
                path,
                images.getFileName(),
                images.getCopyrightCN(),
                url,
                fmt.format(images.getEndDate()),
                BASIS_URL
            );

    fileWriter.write(readme);
    fileWriter.write("### 今天 today :");
    fileWriter.write("\n");
    fileWriter.write(md);
    List<Images> imagesList = getFromSqlite();

    // 倒序
    imagesList =
        imagesList.stream()
            .sorted(Comparator.comparing(Images::getEndDate).reversed())
            .toList();
    var monthFmt = new SimpleDateFormat("MM");
    var yearMonthFmt = new SimpleDateFormat("yyyy-MM");
    String from = "|     |    |\n" + "| ---- | ---- |";
    if (!imagesList.isEmpty()) {
      int count = 0;
      var month = "";
      for (Images i : imagesList) {
        if (!month.equals(monthFmt.format(i.getEndDate()))) {
          if (count % 2 == 1) {
            fileWriter.write("|");
          }
          fileWriter.write("\n");
          fileWriter.write(
              """
                  ### %1$s
                  """
                  .formatted(yearMonthFmt.format(i.getEndDate())));
          fileWriter.write("\n");
          fileWriter.write(from);
          fileWriter.write("\n");
        }
        month = monthFmt.format(i.getEndDate());
        String tem =
            " |![%1$s](%2$s%3$s) [%4$s](%6$s%3$s) %5$s"
                .formatted(
                    i.getCopyrightCN(),
                    path,
                    i.getFileName(),
                    i.getCopyrightCN(),
                    fmt.format(i.getEndDate()),
                    BASIS_URL
                );
        fileWriter.write(tem);
        count++;
        if (count % 2 == 0) {
          fileWriter.write("|");
          fileWriter.write("\n");
        }
      }
      if (count % 2 == 1) {
        fileWriter.write("|");
      }
    }
    fileWriter.close();
  }


  public static String getUrlBase(String url) {
    return url.substring(0, url.indexOf("&"));
  }

  /**
   * 读取 json
   *
   * @param filePath file
   * @return List<Images>
   * @throws Exception
   */
  public static List<Images> readerJson(String filePath) throws Exception {
    File file = new File(filePath);
    if (!Files.exists(Path.of(filePath))) {
      // Files.createFile(Path.of(filePath));
      return new ArrayList<>();
    }
    FileReader reader = new FileReader(file);
    JSONReader jsonReader = new JSONReader(reader);
    List<Images> images = new ArrayList<>();
    jsonReader.startArray();
    while (jsonReader.hasNext()) {
      Images temp = jsonReader.readObject(Images.class);
      images.add(temp);
    }
    jsonReader.endArray();
    jsonReader.close();
    reader.close();
    return images;
  }

  /**
   * 写入 json
   *
   * @param images   list
   * @param filePath file
   * @throws Exception
   */
  public static void writerJson(List<Images> images, String filePath) throws Exception {
    File file = new File(filePath);
    Path path = Path.of(filePath);
    if (!Files.exists(path)) {
      Files.createFile(path);
    }
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write(JSON.toJSONString(images));
    fileWriter.close();
  }

  /**
   * 保存到sqlite
   *
   * @param images images
   * @throws Exception
   */
  public static void saveToSqlite(Images images) throws Exception {
    Connection connection = DriverManager.getConnection(CONNECT);

    // 创建数据库
    String dateBase =
        "CREATE TABLE IF NOT EXISTS images (\n"
            + "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
            + "  \"hash\" TEXT,\n"
            + "  \"url\" TEXT,\n"
            + "  \"urlForeign\" TEXT,\n"
            + "  \"copyright\" TEXT,\n "
            + "   \"copyrightCN\" TEXT,\n "
            + "  \"file_name_4k\" TEXT,\n"
            + " \"file_name\" TEXT,\n"
            + "  \"endDate\" DATE\n,"
            + "  \"utcDate\" DATE\n"
            + ")";
    Statement statement = connection.createStatement();
    statement.execute(dateBase);
    statement.close();
    // 保存
    String sql =
        "INSERT INTO images(hash,url,copyright,endDate,urlForeign,copyrightCN,utcDate,file_name_4k,file_name)"
            + " VALUES(?,?,?,?,?,?,?,?,?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, images.getHash());
    preparedStatement.setString(2, images.getUrl());
    preparedStatement.setString(3, images.getCopyright());
    preparedStatement.setDate(4, new java.sql.Date(images.getEndDate().getTime()));
    preparedStatement.setString(5, images.getUrlForeign());
    preparedStatement.setString(6, images.getCopyrightCN());
    preparedStatement.setDate(7, new java.sql.Date(images.getUtcDate().getTime()));
    preparedStatement.setString(8, images.getFileName4k());
    preparedStatement.setString(9, images.getFileName());
    preparedStatement.execute();
    preparedStatement.close();
  }

  /**
   * 读取 sqlite
   *
   * @return
   * @throws Exception
   */
  public static List<Images> getFromSqlite() throws Exception {
    Connection connection = DriverManager.getConnection(CONNECT);
    String sql = "SELECT * FROM images";
    List<Images> list = new ArrayList<>();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);
    while (resultSet.next()) {
      Images images = new Images();
      images.setHash(resultSet.getString("hash"));
      images.setUrl(resultSet.getString("url"));
      images.setUrlForeign(resultSet.getString("urlForeign"));
      images.setCopyright(resultSet.getString("copyright"));
      images.setEndDate(new Date(resultSet.getDate("endDate").getTime()));
      images.setUtcDate(new Date(resultSet.getDate("utcDate").getTime()));
      images.setCopyrightCN(resultSet.getString("copyrightCN"));
      images.setFileName(resultSet.getString("file_name"));
      images.setFileName4k(resultSet.getString("file_name_4k"));
      list.add(images);
    }
    return list;
  }

  public static void writeToTxt(int newFile) throws Exception {
    File file = new File(FILE_INDEX);
    FileWriter writer = new FileWriter(file);
    writer.write(String.valueOf(newFile));
    writer.close();
  }

  public static String readerTxt() throws Exception {
    File file = new File(FILE_INDEX);
    Path path = Path.of(FILE_INDEX);
    if (!Files.exists(path)) {
      Files.createFile(path);
    }
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String res = reader.readLine();
    reader.close();
    return res;
  }

  public static int getJsonName() {
    int res = 0;
    try {
      res = Integer.parseInt(readerTxt());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  public static void save2Json() {
    try {
      List<Images> imagesList = getFromSqlite();
      int jsonIndex = getJsonName();
      //分割文件。
      // 按1000个存储一个json文件
      int size = imagesList.size() / JSON_SIZE;
      for (int i = jsonIndex; i <= size; i++) {
        int start = i * JSON_SIZE;
        int end = (i + 1) * JSON_SIZE;
        List<Images> images = imagesList.subList(start, Math.min(end, imagesList.size()));
        writerJson(images, i + JSON_NAME);
        jsonIndex = i;
      }
      writeToTxt(jsonIndex);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    Images images = getImages();
    downloadFile(images);
    saveToSqlite(images);
    writeMd(images);
    save2Json();
  }
}
