package com.mycompany.mywebapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.mywebapp.client.exceptions.StockPriceException;

import java.util.ArrayList;

public interface StockPriceServiceAsync {
    void updateStocks(String[] symbols, AsyncCallback<ArrayList<StockPrice>> callback);
}
