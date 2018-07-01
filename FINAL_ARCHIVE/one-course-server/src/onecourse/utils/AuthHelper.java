package onecourse.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthHelper {
    @SuppressWarnings("unchecked")
    public static HttpSession logCheck(HttpServletRequest request, ServletContext application) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c: cookies) {
                // 如果Cookie中包含session字段（它的值为sessionID)
                if ("session".equals(c.getName())) {
                    // 存在则返回session，否则返回null
                    return (HttpSession) application.getAttribute(c.getValue());
                }
            }
        }
        return null;
    }

    public static void killAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c: cookies) {
                Cookie cookie = new Cookie(c.getName(), null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}
