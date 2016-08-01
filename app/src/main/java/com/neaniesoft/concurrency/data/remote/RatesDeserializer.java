package com.neaniesoft.concurrency.data.remote;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.neaniesoft.concurrency.data.model.Currency;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class RatesDeserializer implements JsonDeserializer<FixerIOResponse> {

    @Override
    public FixerIOResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (!json.isJsonObject()) {
            return null;
        }

        FixerIOResponse response = new FixerIOResponse();

        JsonElement baseElement = json.getAsJsonObject().get("base");
        if (baseElement != null && baseElement.isJsonPrimitive()) {
            response.base = baseElement.getAsString();
        }

        JsonElement ratesElement = json.getAsJsonObject().get("rates");
        if (ratesElement != null && ratesElement.isJsonObject()) {
            List<Currency> currencies = new ArrayList<>();
            JsonObject ratesObject = ratesElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = ratesObject.entrySet();

            for (Map.Entry<String, JsonElement> entry : entries) {
                String code = entry.getKey();
                double rate = 0;

                JsonElement value = entry.getValue();
                if (value != null && value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isNumber()) {
                        rate = primitive.getAsDouble();
                    }
                }

                Currency currency = new Currency(code, rate);
                currencies.add(currency);
            }
            response.rates = currencies;
        }
        return response;
    }
}
