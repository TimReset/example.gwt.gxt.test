package ru.timreset.example.gxt.client.gin;

import com.google.gwt.inject.client.GinModules;
import ru.timreset.example.gxt.client.GinEntryPoint;

/**
 * @author averin
 * @date 25.11.2014
 */
@GinModules(GinModule.class)
public interface Ginjector extends com.google.gwt.inject.client.Ginjector {
    GinEntryPoint getGinEntryPoint();
}
