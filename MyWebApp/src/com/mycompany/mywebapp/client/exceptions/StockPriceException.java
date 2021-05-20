package com.mycompany.mywebapp.client.exceptions;

import java.io.Serializable;

public class StockPriceException extends Exception implements Serializable {
    private String symbol;

    public StockPriceException() {
    }

    public StockPriceException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
