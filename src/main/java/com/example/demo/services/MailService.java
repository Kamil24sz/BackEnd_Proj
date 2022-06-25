package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
public class MailService {

    @Value("${mail.smtp.debug")
    private static final String smtpDebug = "false";
    private static final String smtpEmail = "kamil24sz@wp.pl";    // ustawić
    private static final String smtpUsername = "kamil24sz@wp.pl"; // ustawić
    private static final String smtpPassword = "Puszek123";      // ustawić
    private static final String smtpHost = "smtp.wp.pl";      // ustawić
    private static final String smtpPort = "465";
    private static final String smtpAuth = "true";
    private static final String smtpSecurity = "ssl";

    public void sendEmail(String recipientEmail, String emailSubject, String emailBody) throws MessagingException {

        // logika wysyłająca

        Properties smtpProperties = new Properties();

        smtpProperties.put("mail.debug", smtpDebug);
        smtpProperties.put("mail.smtp.auth", smtpAuth);
        smtpProperties.put("mail.smtp." + smtpSecurity + ".enable", true);
        smtpProperties.put("mail.smtp.host", smtpHost);
        smtpProperties.put("mail.smtp.port", smtpPort);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        };

        Session session = Session.getInstance(smtpProperties, authenticator);

        InternetAddress[] replyAddresses = InternetAddress.parse(smtpEmail);
        InternetAddress[] recipientAddresses = InternetAddress.parse(recipientEmail);

        Message message = new MimeMessage(session);

        message.addFrom(replyAddresses);
        message.setRecipients(Message.RecipientType.TO, recipientAddresses);
        message.setReplyTo(replyAddresses);
        message.setSubject(emailSubject);

        Multipart multipart = new MimeMultipart();
        MimeBodyPart part = new MimeBodyPart();

        // typ wiadomości może zostać zmieniony na text/html
        part.addHeader("Content-Type", "text/plain; charset=UTF-8");
        part.setText(emailBody);

        multipart.addBodyPart(part);
        message.setContent(multipart);

        Transport.send(message);
    }
}
