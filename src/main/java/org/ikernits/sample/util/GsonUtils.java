package org.ikernits.sample.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.concurrent.atomic.AtomicLong;


public class GsonUtils {
    private static GsonBuilder registerTypeAdapters(GsonBuilder builder) {
        return builder
            .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context)
                -> new JsonPrimitive(instant.getMillis()))
            .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (elem, type, context)
                -> new Instant(elem.getAsLong()))

            .registerTypeAdapter(Duration.class, (JsonSerializer<Duration>) (duration, type, context)
                -> new JsonPrimitive(duration.getStandardSeconds()))
            .registerTypeAdapter(Duration.class, (JsonDeserializer<Duration>) (elem, type, context)
                -> Duration.standardSeconds(elem.getAsLong()))

            .registerTypeAdapter(AtomicLong.class, (JsonSerializer<AtomicLong>) (value, type, context)
                -> new JsonPrimitive(value.get()))
            .registerTypeAdapter(AtomicLong.class, (JsonDeserializer<AtomicLong>) (elem, type, context)
                -> new AtomicLong(elem.getAsLong()));
    }


    public static final Gson gson = registerTypeAdapters(new GsonBuilder())
        .disableHtmlEscaping()
        .create();

    public static final Gson gsonPretty = registerTypeAdapters(new GsonBuilder())
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();
}
