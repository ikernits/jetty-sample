package org.ikernits.sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by ikernits on 04/10/15.
 */
public class GsonView implements View {

    private static final DateTimeFormatter instantFormatter = DateTimeFormat.forPattern("YYYY-MM-DD'T'HH:mm:ss'Z'");

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> new JsonPrimitive(instant.toString(instantFormatter)))
            .create();

    private static final GsonView view = new GsonView();


    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Output output = Output.createForSuccess(model.get("data"));
        response.getOutputStream().print(gson.toJson(output));
    }

    public static ModelAndView createModelAndView(Object response) {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("data", response);
        return modelAndView;
    }

    @SuppressWarnings("unused")
    private static class Output {
        private boolean success;
        private Object data;

        public static Output createForSuccess(Object data) {
            Output output = new Output();
            output.data = data;
            output.success = true;
            return output;
        }
    }
}
