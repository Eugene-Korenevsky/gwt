package com.mycompany.mywebapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.mycompany.mywebapp.client.exceptions.StockPriceException;

import java.util.Date;

import java.util.ArrayList;

public class MyWebApp implements EntryPoint {
    private final VerticalPanel mainPanel = new VerticalPanel();
    private final FlexTable stocksFlexTable = new FlexTable();
    private final HorizontalPanel addPanel = new HorizontalPanel();
    private final TextBox newSymbolTextBox = new TextBox();
    private final Button addStockButton = new Button("Add");
    private final Label lastUpdatedLabel = new Label();
    private static final int REFRESH_INTERVAL = 5000;
    private final ArrayList<String> stocks = new ArrayList<>();
    private final StockPriceServiceAsync stockPriceService = GWT.create(StockPriceService.class);
    private final Label errorLabel = new Label();

    public void onModuleLoad() {
        stocksFlexTable.setText(0, 0, "Symbol");
        stocksFlexTable.setText(0, 1, "Price");
        stocksFlexTable.setText(0, 2, "Change");
        stocksFlexTable.setText(0, 3, "Remove");
        stocksFlexTable.setCellPadding(6);
        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        addPanel.addStyleName("addPanel");

        errorLabel.setStyleName("errorMessage");
        errorLabel.setVisible(false);

        mainPanel.add(errorLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);
        RootPanel.get("stockList").add(mainPanel);
        newSymbolTextBox.setFocus(true);
        Timer refreshTimer = new Timer() {
            @Override
            public void run() {
                refreshWatchList();
            }
        };
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
        addStockButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                addStock();
            }
        });

        newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) addStock();
            }
        });
    }

    private void refreshWatchList() {
        AsyncCallback<ArrayList<StockPrice>> callback = new AsyncCallback<ArrayList<StockPrice>>() {

            @Override
            public void onFailure(Throwable throwable) {
                String error = throwable.getMessage();
                errorLabel.setText(error);
                errorLabel.setVisible(true);
            }

            @Override
            public void onSuccess(ArrayList<StockPrice> stockPrices) {
                updateTable(stockPrices);
            }
        };
        stockPriceService.updateStocks(stocks.toArray(new String[0]), callback);

    }

    private void updateTable(ArrayList<StockPrice> prices) {
        for (StockPrice price : prices) {
            updateTable(price);
        }

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(
                DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLabel.setText("Last update : "
                + dateFormat.format(new Date()));
        errorLabel.setVisible(false);
    }

    private void updateTable(StockPrice price) {
        if (!stocks.contains(price.getSymbol())) {
            return;
        }

        int row = stocks.indexOf(price.getSymbol()) + 1;
        String priceText = NumberFormat.getFormat("#,##0.00").format(
                price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());

        stocksFlexTable.setText(row, 1, priceText);
        Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + changePercentText + "%)");
        String changeStyleName = "noChange";
        if (price.getChangePercent() < -0.1f) {
            changeStyleName = "negativeChange";
        } else if (price.getChangePercent() > 0.1f) {
            changeStyleName = "positiveChange";
        }

        changeWidget.setStyleName(changeStyleName);
    }

    private void addStock() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);

        // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
        if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert("'" + symbol + "' is not a valid symbol.");
            newSymbolTextBox.selectAll();
            return;
        }

        if (stocks.contains(symbol)) return;

        int row = stocksFlexTable.getRowCount();
        stocks.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        stocksFlexTable.setWidget(row, 2, new Label());
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");
        Button removeStockButton = new Button("x");
        removeStockButton.addStyleDependentName("remove");
        removeStockButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                int removedIndex = stocks.indexOf(symbol);
                stocks.remove(removedIndex);
                stocksFlexTable.removeRow(removedIndex + 1);
            }
        });
        stocksFlexTable.setWidget(row, 3, removeStockButton);
        refreshWatchList();

        newSymbolTextBox.setText("");
    }
}
