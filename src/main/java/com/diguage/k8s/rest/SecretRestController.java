package com.diguage.k8s.rest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SecretRestController {
  @GetMapping(value = "/secret")
  public Map<String, Object> index() throws Throwable {
    Map<String, Object> data = getHostnameAndIp();
    String fileName = "/db-user.conf/db";
    File file = new File(fileName);
    Map<String, Object> secrets = new HashMap<>();
    if (file.exists()) {
      if (file.isDirectory()) {
        File[] files = file.listFiles();
        List<String> fileAbsPaths = Arrays.stream(files)
          .map(File::getAbsolutePath)
          .filter(s -> !s.contains(".."))
          .collect(Collectors.toList());
        System.out.println("fileAbsPaths: " +
          Arrays.toString(fileAbsPaths.toArray(new String[0])));
        Map<String, Object> file2content = new HashMap<>();
        for (String path : fileAbsPaths) {
          file2content.put(path, readFile(path));
        }
        secrets.put(fileName, file2content);
      } else {
        List<String> content = readFile(fileName);
        secrets.put(fileName, content);
      }
    } else {
      secrets.put(fileName, "没有该文件！");
    }
    Map<String, String> envData = new HashMap<>();
    envData.put("SECRET_DB_USERNAME", System.getenv("SECRET_DB_USERNAME"));
    envData.put("SECRET_DB_PASSWORD", System.getenv("SECRET_DB_PASSWORD"));
    secrets.put("env", envData);
    data.put("secrets", secrets);
    return data;
  }

  private List<String> readFile(String path) throws Throwable {
    File file = new File(path);
    return FileUtils.readLines(file, StandardCharsets.UTF_8);
  }

  private Map<String, Object> getHostnameAndIp() throws Throwable {
    Map<String, Object> data = new HashMap<>();
    data.put("hostname", InetAddress.getLocalHost().getHostName());
    DatagramSocket socket = new DatagramSocket();
    socket.connect(InetAddress.getByName("119.29.29.29"), 53);
    data.put("ip", socket.getLocalAddress().getHostAddress());
    socket.close();
    return data;
  }
}
