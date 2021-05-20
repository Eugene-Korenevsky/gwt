package com.mycompany.mywebapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.mywebapp.client.MaxPriceService;

public class MaxPriceServiceImpl extends RemoteServiceServlet implements MaxPriceService {
    @Override
    public Double getPrice() {
        return (double) 20;
    }
}
