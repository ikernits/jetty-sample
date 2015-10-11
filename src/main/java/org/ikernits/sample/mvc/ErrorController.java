package org.ikernits.sample.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ikernits on 11/10/15.
 */
public class ErrorController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        response.sendError(Integer.parseInt(code));
        return null;
    }

}
