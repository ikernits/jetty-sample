package org.ikernits.sample.jetty;



import org.eclipse.jetty.servlet.ErrorPageErrorHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by ikernits on 11/10/15.
 */
public class JettyErrorHandler extends ErrorPageErrorHandler {
//    @Override
//    protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
//        writer.write("error " + code);
//    }

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        writer.write(
                "<h2>" + "HTTP error: " + code + "</h2>" +
                "<h2>" + message + "</h2>"
        );
    }
}
