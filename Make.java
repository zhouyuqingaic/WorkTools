package com.ebuytech.web.qrcode.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class Make
 */
public class Make extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Make() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = null;
        ServletInputStream sis = null;
        String reqData = null;
        // 用于存放结果的数组
        byte[] dataByte = null;
        try {
            printWriter = response.getWriter();
            // 取HTTP请求流
            sis = request.getInputStream();
            // 取HTTP请求流长度
            int size = request.getContentLength();
            // 用于缓存每次读取的数据
            byte[] buffer = new byte[size];
            dataByte = new byte[size];
            int count = 0;
            int rbyte = 0;
            // 循环读取
            while (count < size) {
                // 每次实际读取长度存于rbyte中
                rbyte = sis.read(buffer);
                for (int i = 0; i < rbyte; i++) {
                    dataByte[count + i] = buffer[i];
                }
                count += rbyte;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        reqData = new String(dataByte, "UTF-8");
        
        System.out.println(reqData);
     // 4.验证数据安全性
        if (checkData(reqData)) {

        } else {
            // 验证失败
        }
        
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("message", "成功");
        printWriter.write(json.toString());
    }

    /*
     * 校验异步接收的数据的安全性 
     */
    public static boolean checkData(String reqData) {
        JSONObject reqObj = JSONObject.parseObject(reqData);
        String sign = reqObj.getString("sign");
        JSONObject dataJson = (JSONObject) reqObj.get("data");
        Set<String> headerIt = dataJson.keySet();
        TreeMap<String, String> analysisDataMap = new TreeMap<String, String>();
        for (String key : headerIt) {
            analysisDataMap.put(key, dataJson.getString(key));
        }

        StringBuffer orgin = new StringBuffer();
        for (Map.Entry<String, String> param : analysisDataMap.entrySet()) {
            orgin.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        String asynchronousData = orgin.toString();
        asynchronousData = asynchronousData.substring(0,asynchronousData.length()-1)+"密钥";
        System.out.println("待签字符串："+asynchronousData);
//        if (sign.equals(MD5.getMd5(asynchronousData))){
//            return true;
//        }
        return false;
    }

}
