package it.pattystrolly.wedding.servlet.task;

import com.google.gson.JsonObject;
import it.pattystrolly.wedding.manager.SenderManager;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class SendEmailsTaskServlet extends AbstractTaskServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SenderManager senderManager = new SenderManager();
        String action = parameters.get("action");
        String requestId = parameters.get("id");
        try {
            switch (action){
                case "STORE":
                    senderManager.storeEmails(requestId);
                    break;
                case "SEND":
                    senderManager.sendEmails();
                    break;
                default:
                    throw new IllegalStateException("Method not supported");
            }
        } catch (MessagingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
