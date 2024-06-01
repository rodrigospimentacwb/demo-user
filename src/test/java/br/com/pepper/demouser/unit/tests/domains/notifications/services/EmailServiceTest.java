package br.com.pepper.demouser.unit.tests.domains.notifications.services;

import br.com.pepper.demouser.commons.AbstractUnitTest;
import br.com.pepper.demouser.domains.notifications.services.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
public class EmailServiceTest extends AbstractUnitTest {
    private final EmailService emailService = new EmailService();

    @Test
    public void testSendEmail(CapturedOutput output) {
        String email = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        emailService.sendEmail(email, subject, body);

        Assertions.assertTrue(output.toString().contains("test@example.com"));
        Assertions.assertTrue(output.toString().contains("Test Subject"));
        Assertions.assertTrue(output.toString().contains("Test Body"));
        Assertions.assertTrue(output.toString().contains("Email sent successfully."));
    }
}