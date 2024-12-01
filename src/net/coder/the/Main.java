package net.coder.the;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {

        try {
            Currency cur = Currency.getCurrency();

            System.out.println(cur.convertFromUsd(2000, "EUR"));
            System.out.println(cur.convert(2000, "EUR", "GBP"));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace(System.err);
        }

    }

}
