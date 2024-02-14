package pl.marek.securityjwt.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.marek.securityjwt.exception.EmailException;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Component
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;

    //put your email here and credentials in application.properties
    private static final String yourOwnEmailAddress = "";

    private static final String PERSONAL = "email.personal";
    private static final String EMAIL = "SecurityJWTApplication";

    private static final String REGISTER_MESSAGE_SUBJECT = "email.register.subject";
    private static final String REGISTER_MESSAGE = "email.register.message";

    public EmailUtil(JavaMailSender javaMailSender, MessageSource messageSource) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
    }

    public void sendRegisterEmail(String recipientEmail, Locale locale) {

        try {
            String subject = messageSource.getMessage(REGISTER_MESSAGE_SUBJECT, null, locale);
            String content = messageSource.getMessage(REGISTER_MESSAGE, null, locale);

            prepareMessage(recipientEmail, locale, subject, content);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new EmailException();
        }
    }

    private void prepareMessage(String recipientEmail, Locale locale, String subject, String content) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(yourOwnEmailAddress);
        helper.setTo(recipientEmail);

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }
}