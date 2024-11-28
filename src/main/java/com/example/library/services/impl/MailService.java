package com.example.library.services.impl;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The MailService class provides functionality to send emails using SMTP.
 * It supports sending emails with HTML content to multiple recipients asynchronously.
 */
public class MailService {

  private final String username = "minhhuybui044@gmail.com";
  private final String password = "cbgp qdow bmly vtzv";
  private final Properties prop;
  private final ExecutorService executor;

  /**
   * Constructs a new MailService with the specified SMTP host.
   *
   * @param host the SMTP host to use for sending emails
   */
  public MailService(String host) {
    prop = new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", host);
    prop.put("mail.smtp.port", 587);
    prop.put("mail.smtp.ssl.trust", host);

    this.executor = Executors.newSingleThreadExecutor();
  }

  /**
   * Sends an email with the specified subject to multiple recipients asynchronously.
   * Each recipient can have a unique message body.
   *
   * @param emailMessages a map where the keys are recipient email addresses and the values are
   *                      the email messages
   * @param subject the subject of the email
   */
  public void sendMail(Map<String, String> emailMessages, String subject) {
    String from = username;
    executor.submit(
        () -> {
          try {
            Session session =
                Session.getInstance(
                    prop,
                    new javax.mail.Authenticator() {
                      protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                      }
                    });

            for (Map.Entry<String, String> entry : emailMessages.entrySet()) {
              String email = entry.getKey();
              String msg = entry.getValue();

              Message message = new MimeMessage(session);
              message.setFrom(new InternetAddress(from));
              message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

              message.setSubject(subject);

              MimeBodyPart mimeBodyPart = new MimeBodyPart();
              mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

              Multipart multipart = new MimeMultipart();
              multipart.addBodyPart(mimeBodyPart);

              message.setContent(multipart);

              Transport.send(message);
            }
          } catch (MessagingException e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * Shuts down the executor service used for asynchronous email sending.
   * This method should be called to properly terminate the executor
   * and release resources when the MailService is no longer needed.
   */
  public void shutdown() {
    executor.shutdown();
  }

}
