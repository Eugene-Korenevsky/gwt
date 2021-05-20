package com.mycompany.mywebapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.mywebapp.client.StockPrice;
import com.mycompany.mywebapp.client.StockPriceService;
import com.mycompany.mywebapp.client.exceptions.StockPriceException;

import java.util.ArrayList;
import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
    private static final double MAX_PRICE = 100.0;
    private static final double MAX_PRICE_CHANGE = 0.02;
    @Override
    public ArrayList<StockPrice> updateStocks(String[] symbols) throws StockPriceException {
        Random rnd = new Random();
        ArrayList<StockPrice> prices = new ArrayList<>();

        //StockPrice[] prices = new StockPrice[symbols.length];
        for (String symbol : symbols) {
            if (symbol.equals("ERR")) throw new StockPriceException("ERROR !!!");
            double price = rnd.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);
            prices.add(new StockPrice(symbol, price, change));
        }

        return prices;
    }
}
