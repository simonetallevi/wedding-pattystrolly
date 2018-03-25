package it.pattystrolly.wedding.servlet.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.oauth2.model.Userinfoplus;
import it.pattystrolly.wedding.config.EnvConstants;
import it.pattystrolly.wedding.manager.Manager;
import it.pattystrolly.wedding.model.Config;
import it.pattystrolly.wedding.model.TechUser;
import it.pattystrolly.wedding.service.credential.CredentialService;
import it.pattystrolly.wedding.service.datastore.DatastoreService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OauthCallback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("error") != null) {
            resp.sendRedirect(EnvConstants.getBaseURL());
            return;
        }

        String code = req.getParameter("code");

        CredentialService oauthService = new CredentialService(
                Config.K.clientId.get(),
                Config.K.clientSecret.get(),
                Config.K.scopes.getSet());

        try{
            if (code != null){
                GoogleTokenResponse tokenResponse = oauthService.getTokenResponse(code, EnvConstants.getBaseURL() + "oauthcallback");
                String accessToken = tokenResponse.getAccessToken();
                String refreshToken = tokenResponse.getRefreshToken();
                Userinfoplus userinfoplus = oauthService.getCurrentUser(oauthService.getCredential(accessToken));

                TechUser techUser = new TechUser();
                techUser.setEmail(userinfoplus.getEmail());
                techUser.setNome(userinfoplus.getName());
                techUser.setAccessToken(accessToken);
                techUser.setRefreshToken(refreshToken);
                techUser.setExpirationTime(DateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds().intValue()).toDate());

                DatastoreService datastoreService = new DatastoreService();
                datastoreService.ofy().save().entity(techUser);

                Manager.techUserCredential.cleanUp();

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(EnvConstants.getBaseURL());
            }

        } catch (Throwable throwable) {
            throw new ServletException(throwable);
        }
    }
}
