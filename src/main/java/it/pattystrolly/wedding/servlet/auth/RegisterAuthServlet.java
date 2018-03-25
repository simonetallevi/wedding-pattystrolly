package it.pattystrolly.wedding.servlet.auth;

import com.google.gson.JsonObject;
import it.pattystrolly.wedding.servlet.AbstractServlet;
import it.pattystrolly.wedding.config.EnvConstants;
import it.pattystrolly.wedding.model.Config;
import it.pattystrolly.wedding.service.credential.CredentialService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class RegisterAuthServlet extends AbstractServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CredentialService oauthService = new CredentialService(
                Config.K.clientId.get(),
                Config.K.clientSecret.get(),
                Config.K.scopes.getSet());
        resp.sendRedirect(oauthService.getAuthURL(EnvConstants.getBaseURL() + "oauthcallback"));
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
