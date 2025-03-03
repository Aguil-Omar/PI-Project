package com.pi_dev.services;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailService {


    private String code;
    private  String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return this.code=String.valueOf(code);
    }


    private static final String FROM_EMAIL = "kthiriyoussef09@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "plcw yzun jzcb rfsu"; // Replace with your email password
    private static final String SMTP_HOST = "smtp.gmail.com"; // Gmail SMTP host
    private static final String SMTP_PORT = "587"; // Gmail SMTP port

    public void sendVerificationEmail(String toEmail) {

        String code =this.generateVerificationCode();
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Verify Your Email Address");
            message.setText("Your verification code is: " + code);

            // Send the email
            Transport.send(message);
            System.out.println("Verification email sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send verification email.");
        }
    }

    public String getGenertaedCode(){
        return this.code;
    }
}
