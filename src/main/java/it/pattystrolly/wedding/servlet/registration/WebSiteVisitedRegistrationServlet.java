package it.pattystrolly.wedding.servlet.registration;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import it.pattystrolly.wedding.servlet.AbstractServlet;
import it.pattystrolly.wedding.manager.SenderManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class WebSiteVisitedRegistrationServlet extends AbstractServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Preconditions.checkNotNull(parameters.get("id"), "missing parameter");
        SenderManager manager = new SenderManager();
        manager.registerWeb(parameters.get("id"));
        resp.sendRedirect("/");
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
