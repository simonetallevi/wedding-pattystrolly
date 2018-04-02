package it.pattystrolly.wedding.servlet.registration;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;
import it.pattystrolly.wedding.manager.SenderManager;
import it.pattystrolly.wedding.model.Email;
import it.pattystrolly.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class YesNoRegistrationServlet extends AbstractServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Preconditions.checkNotNull(parameters.get("id"), "missing parameter");
        SenderManager manager = new SenderManager();
        try {
            Email.Answer answer = Email.Answer.valueOf(parameters.get("answer"));
            manager.registerAnswer(parameters.get("id"), answer);
            resp.sendRedirect("#!/confirmation");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.sendRedirect("/");
        }
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
