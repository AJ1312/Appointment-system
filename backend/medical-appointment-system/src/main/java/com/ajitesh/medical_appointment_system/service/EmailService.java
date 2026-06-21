package com.ajitesh.medical_appointment_system.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendAsync(SimpleMailMessage message) {
        new Thread(() -> {
            try {
                mailSender.send(message);
                System.out.println("Email sent successfully to: " + String.join(", ", message.getTo()));
            } catch (Exception e) {
                System.err.println("Failed to send email to " + String.join(", ", message.getTo()) + ": " + e.getMessage());
            }
        }).start();
    }

    public void sendBookingConfirmation(String toEmail, String patientName, String doctorName, String date, String time, int token, int queuePos) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("MediCare <no-reply@medicare.com>");
        message.setTo(toEmail);
        message.setSubject("Booking Confirmed - MediCare");
        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your appointment has been successfully booked with Dr. %s.\n\n" +
            "Details:\n" +
            "- Date: %s\n" +
            "- Time: %s\n" +
            "- Token Number: T-%02d\n" +
            "- Queue Position: %d\n\n" +
            "You can track your live queue status directly on your dashboard Overview.\n\n" +
            "Thank you for choosing MediCare!\n" +
            "Regards,\nMediCare Support Team",
            patientName, doctorName, date, time, token, queuePos
        ));
        sendAsync(message);
    }

    public void sendLoginAlert(String toEmail, String name, String role) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("MediCare <no-reply@medicare.com>");
        message.setTo(toEmail);
        message.setSubject("Security Alert: New Login - MediCare");
        message.setText(String.format(
            "Hello %s,\n\n" +
            "A new login was detected on your MediCare account.\n\n" +
            "Details:\n" +
            "- Role: %s\n" +
            "- Time: %s\n" +
            "- Location/IP: Localhost (Mac)\n\n" +
            "If this was not you, please secure your account immediately.\n\n" +
            "Regards,\nMediCare Security Team",
            name, role, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
        sendAsync(message);
    }

    public void sendRescheduleNotification(String toEmail, String patientName, String doctorName, String oldDate, String oldTime, String newDate, String newTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("MediCare <no-reply@medicare.com>");
        message.setTo(toEmail);
        message.setSubject("Appointment Rescheduled - MediCare");
        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your appointment with Dr. %s has been rescheduled.\n\n" +
            "Previous Details:\n" +
            "- Date: %s\n" +
            "- Time: %s\n\n" +
            "New Details:\n" +
            "- Date: %s\n" +
            "- Time: %s\n\n" +
            "Please check your patient dashboard Overview for the updated queue status and token information.\n\n" +
            "Regards,\nMediCare Support Team",
            patientName, doctorName, oldDate, oldTime, newDate, newTime
        ));
        sendAsync(message);
    }
}
