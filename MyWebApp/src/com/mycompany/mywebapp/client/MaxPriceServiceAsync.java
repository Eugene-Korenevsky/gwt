package com.mycompany.mywebapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MaxPriceServiceAsync {
    void getPrice(AsyncCallback<Double> callback);
}
