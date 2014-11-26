package ru.timreset.example.gxt.client;

import com.google.gwt.core.shared.GWT;

public abstract class AsyncCallback<T> implements com.google.gwt.user.client.rpc.AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        if (GWT.isClient()) {
            caught.printStackTrace();
        } else {
            throw new RuntimeException(caught);
        }
    }
}
