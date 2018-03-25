package it.pattystrolly.wedding.service.gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import it.pattystrolly.wedding.config.EnvConstants;
import it.pattystrolly.wedding.model.Email;
import it.pattystrolly.wedding.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class GmailService {

    private Gmail gmail;

    public GmailService(Credential credential) {
        gmail = new Gmail.Builder(Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, credential)
                .setApplicationName(EnvConstants.APP_NAME)
                .build();
    }

    public Email sendEmail(Email email, String subject, String body, Map<String, String> map) throws MessagingException, IOException {
        log.info("sending {}", email);
        Message message = gmail.users().messages().send("me",
                createMessageWithEmail(createEmail(email.getEmails(), subject, body, map))).execute();
        log.info("sent {}", email);
        email.setSent(true);
        return email;
    }

    private MimeMessage createEmail(List<String> to,
                                    String subject,
                                    String bodyText,
                                    Map<String, String> attachments)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Multipart multipart = new MimeMultipart();

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(bodyText, "text/html");
        multipart.addBodyPart(bodyPart);

        for(String path: attachments.keySet()){
            multipart.addBodyPart(loadAttachment(path, attachments.get(path)));
        }

        MimeMessage email = new MimeMessage(session);
        email.setContent(multipart);
        for (String t : to) {
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(t));
        }

        email.setSubject(subject);
        return email;
    }

    private MimeBodyPart loadAttachment(String path, String fileName) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(path);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(fileName);

        return attachmentPart;
    }

    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
