package com.mycompany.mywebapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mycompany.mywebapp.client.exceptions.StockPriceException;

import java.util.ArrayList;

@RemoteServiceRelativePath("stocks")
public interface StockPriceService extends RemoteService {
    ArrayList<StockPrice> updateStocks(String[] symbols) throws StockPriceException;
}
