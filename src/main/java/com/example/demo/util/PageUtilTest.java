package com.example.demo.util;

public class PageUtilTest {
  public static void main(String[] args) {
    PageUtil pageUtil = new PageUtil(151, 3, "");
    String htmlPage = pageUtil.pager();
    System.out.println(htmlPage);
  }
}
