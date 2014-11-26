package ru.timreset.example.gxt.server.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GwtCacheService implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestURI = httpRequest.getRequestURI();

    HttpServletResponse httpResponse = (HttpServletResponse) response;
    if (requestURI.contains(".nocache.") || requestURI.endsWith(".html") || requestURI.endsWith(".htm")) {
      Date now = new Date();
      httpResponse.setDateHeader("Date", now.getTime());
      httpResponse.setDateHeader("Expires", now.getTime() - 86400000L);
      httpResponse.setHeader("Pragma", "no-cache");
      httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
    }

    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

}
