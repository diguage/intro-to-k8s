package com.diguage.k8s.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeRestController {
  @GetMapping(value = "/")
  public Map<String, Object> index() throws Throwable {
    return getHostnameAndIp();
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
