package com.hashjosh.agripro.user.services;

import com.hashjosh.agripro.rsbsa.RsbsaModel;
import com.hashjosh.agripro.user.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class UserEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sendFrom;

    public UserEmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendStaffRegistrationMail(User staff) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();

        context.setVariable("staff", staff);
        String html = templateEngine.process("staff-registration", context);

        helper.setText(html, true);
        helper.setTo(staff.getEmail());
        helper.setSubject(staff.getUsername());
        mailSender.send(message);

    }

    public void sendFarmerRegistrationMail(User user, RsbsaModel rsbsa, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();

        context.setVariable("user", user);
        context.setVariable("dateOfBirth", rsbsa.getDateOfBirth().toString());
        String registerHtml = templateEngine.process("register", context);

        helper.setTo(user.getEmail());
        helper.setFrom(sendFrom);
        helper.setSubject(subject);
        helper.setText(registerHtml, true);

        mailSender.send(message);

    }
}
