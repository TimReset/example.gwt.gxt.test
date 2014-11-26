package ru.timreset.example.gxt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import ru.timreset.example.gxt.client.gin.Ginjector;

/**
 * @author averin
 * @date 25.11.2014
 */
public class GxtModule implements EntryPoint {
  @Override
  public void onModuleLoad() {
    Ginjector ginjector = GWT.create(Ginjector.class);
    ginjector.getGinEntryPoint().run();
  }
}
