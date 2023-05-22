package com.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Classname MD5
 * @Description 生成MD5和验证MD5,暂时未用
 * @Date 2020/11/10 20:28
 * @Created by SHTL
 */
   public class MD5   {

   /**
    * @param text 明文
    * @return 密文
    */


   // 不带秘钥加密
   public static String md5(String text) throws Exception {
      // 加密后的字符串
      String md5str = DigestUtils.md5Hex(text);
      return md5str;
   }

   /**
    * MD5验证方法
    *
    * @param text 明文
    * @param md5 密文
    */
   // 根据传入的密钥进行验证
   public static boolean verify(String text, String md5) throws Exception {
      String md5str = md5(text);
      if (md5str.equalsIgnoreCase(md5)) {
         return true;
      }
      return false;
   }

   }
