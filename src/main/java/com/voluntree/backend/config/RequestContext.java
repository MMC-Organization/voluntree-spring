package com.voluntree.backend.config;

public class RequestContext {
  private static final ThreadLocal<String> clientIp = new ThreadLocal<>();

  public static void setIp(String ip) {
    clientIp.set(ip);
  }

  public static void clearIp() {
    clientIp.remove();
  }

  public static String getIp() {
    return clientIp.get();
  }

}
