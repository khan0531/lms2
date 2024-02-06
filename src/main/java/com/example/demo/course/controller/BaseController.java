package com.example.demo.course.controller;

import com.example.demo.util.PageUtil;

public class BaseController {



  public static String getPaperHtml(long totalCount, long pageSize, long pageIndex, String queryString) {
    PageUtil pageUtil = new PageUtil(totalCount, pageSize, pageIndex, queryString);
    return pageUtil.pager();
  }


}