package com.neaniesoft.concurrency.data.remote;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.neaniesoft.concurrency.data.Currency;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mdpearce on 23/07/2016.
 */

public class RatesDeserializer implements JsonDeserializer<List<Currency>> {

    @Override
    public List<Currency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (!json.isJsonObject() && !json.isJsonArray()) {
            return null;
        }

        List<Currency> currencies = new ArrayList<>();

        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

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

        return currencies;
    }
}
