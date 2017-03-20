package org.ethereum.service;

import java.text.MessageFormat;

public class BusinessException extends Exception {
   private static final long serialVersionUID = 1L;

//    ## 100 Common Error Message
    public static String errorHttpFailGet ="{0} 获取HTTP失败，返回{1}, 返回内容:{2}";
    public static String errorHttpRpcFail = "RPC调用失败: {0}";

//   private static ResourceBundle _bundle = PropertyResourceBundle
//           .getBundle("i18n.ErrorMessage");

   private String error_code;

   public String getCode() {
       return error_code;
   }

   private BusinessException(String code, String message) {
       super(message);
       this.error_code = code;
   }

   public static BusinessException error(String code, Object... args) {
       String message = code;//_bundle.getString(code);
       if (message == null) {
           message = code;
           code = "UNKNOWN";
       }
       return new BusinessException(code, MessageFormat.format(message, args));
   }

   public static BusinessException httpGetFail(String business, int status, String content) {
       return error(errorHttpFailGet,new Object[]{business, status, content} );
   }
}
