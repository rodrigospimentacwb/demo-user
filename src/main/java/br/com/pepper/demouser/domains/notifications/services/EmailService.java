package br.com.pepper.demouser.domains.notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String email, String subject, String body) {

        // TODO PEPPER Here you can add logic to use a client like Google to send the email and also fetch templates from the project

        try {
            Thread.sleep(2000); // TODO PEPPER I added thread sleep to demonstrate that the user's save operation does not depend on sending the email, occurring asynchronously
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        logger.info("Sending email to: {}", email);
        logger.info("Subject: {}", subject);
        logger.info("Body: {}", body);
        logger.info("Email sent successfully.");
    }
}
