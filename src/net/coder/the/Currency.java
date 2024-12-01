package net.coder.the;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.coder.Constants;
import net.coder.the.util.UnregisteredCurrencyException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Currency {

    private final double CAD;
    private final double NZD;
    private final double ZAR;
    private final double EUR;
    private final double GBP;
    private final double JPY;

    private Currency() throws IOException, URISyntaxException {
        final String END_POINT = Constants.END_POINT;

        URL url = new URI(END_POINT).toURL();

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream());
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JsonObject object = JsonParser.parseString(response.toString()).getAsJsonObject();

            JsonObject currencies = object.get("data").getAsJsonObject();

            NZD = currencies.get("NZD").getAsDouble();
            CAD = currencies.get("CAD").getAsDouble();
            GBP = currencies.get("GBP").getAsDouble();
            JPY = currencies.get("JPY").getAsDouble();
            ZAR = currencies.get("ZAR").getAsDouble();
            EUR = currencies.get("EUR").getAsDouble();
        }
    }

    public static Currency getCurrency() throws IOException, URISyntaxException {
        return new Currency();
    }

    public double convertFromUsd(double amount, String toCode) {
        return getCurCode(toCode) * amount;
    }

    public double convert(double amount, String fromCode, String toCode) {
        double usd = amount / getCurCode(fromCode);

        return convertFromUsd(usd, toCode);
    }

    private double getCurCode(String currencyCode) {
        return switch (currencyCode.toUpperCase()) {
            case "NZD" -> NZD;
            case "CAD" -> CAD;
            case "GBP" -> GBP;
            case "JPY" -> JPY;
            case "ZAR" -> ZAR;
            case "EUR" -> EUR;
            default -> throw new UnregisteredCurrencyException("Enter valid currency");
        };
    }
}
