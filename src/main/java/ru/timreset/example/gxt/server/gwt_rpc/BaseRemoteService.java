package ru.timreset.example.gxt.server.gwt_rpc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class BaseRemoteService extends RemoteServiceServlet {

  private static final long serialVersionUID = -6681353208202647775L;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    // Поддержка Autowired в сервлетах 
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
    beanFactory.autowireBean(this);
  }

  @Override
  protected void checkPermutationStrongName() throws SecurityException {
    // Ничего не делаем, чтобы корректно работал SyncProxy в тестах.
  }

}
