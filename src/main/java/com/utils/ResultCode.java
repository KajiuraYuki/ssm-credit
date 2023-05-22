package com.utils;
/**
 * @Classname Result
 * @Description 响应码实体类
 * @Date 2020/10/4 16:13
 * @Created by SHTL
 */
   public class ResultCode {
   //响应码
   private int code;

   public ResultCode(int code) {
      this.code = code;
      //System.out.println(this.code);
   }

   public int getCode() {
      return code;
   }

   public void setCode(int code) {
      this.code = code;
   }


}
