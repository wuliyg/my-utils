package com.wuligao.utils.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuligao
 * @description 获得请求端IP地址
 * @date 2019-11-20 16:48
 **/
public class ClientUtil {
    /**
     * 获取客户端真实ip
     * @param request  http请求
     * @return 实际ip
     */
    public static String getClientIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
