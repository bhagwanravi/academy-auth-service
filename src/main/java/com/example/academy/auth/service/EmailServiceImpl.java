package com.example.academy.auth.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    
    @Value("${email.from}")
    private String fromEmail;

    @Override
    public void sendAdminRegistrationNotification(String superAdminEmail, String adminName, String adminEmail, String tenantId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(superAdminEmail);
            helper.setSubject("New Admin Registration Request - " + tenantId);
            helper.setFrom(fromEmail);
            
            Map<String, Object> model = new HashMap<>();
            model.put("adminName", adminName);
            model.put("adminEmail", adminEmail);
            model.put("tenantId", tenantId);
            
            String content = processTemplate("admin-registration-notification.ftl", model);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("Admin registration notification sent to super admin: {}", superAdminEmail);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send admin registration notification", e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public void sendAdminApprovalNotification(String adminEmail, String adminName, boolean approved, String superAdminName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(adminEmail);
            helper.setSubject(approved ? "Your Admin Account Has Been Approved" : "Your Admin Registration Request Has Been Rejected");
            helper.setFrom(fromEmail);
            
            Map<String, Object> model = new HashMap<>();
            model.put("adminName", adminName);
            model.put("approved", approved);
            model.put("superAdminName", superAdminName);
            
            String templateName = approved ? "admin-approved-notification.ftl" : "admin-rejected-notification.ftl";
            String content = processTemplate(templateName, model);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("Admin approval notification sent to admin: {}", adminEmail);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send admin approval notification", e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public void sendAdminApprovedNotificationToSuperAdmin(String superAdminEmail, String adminName, String adminEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(superAdminEmail);
            helper.setSubject("Admin Account Approved - " + adminName);
            helper.setFrom(fromEmail);
            
            Map<String, Object> model = new HashMap<>();
            model.put("adminName", adminName);
            model.put("adminEmail", adminEmail);
            
            String content = processTemplate("admin-approved-to-super-admin.ftl", model);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("Admin approved notification sent to super admin: {}", superAdminEmail);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send admin approved notification to super admin", e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> model) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}
