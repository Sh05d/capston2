package org.example.capston2.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("email");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "مرحباً بك في حِرَف";
        String body = "مرحباً " + username + " \n\n" +
                        " نرحب بك في منصة حِرَف\n\n" +
                        " يمكنك استكشاف وحجز ورش العمل الفنية\n" +
                        " استئجار الأدوات للأعمال الفنية بسهولة\n\n" +
                        "مع أطيب التحيات \n" +
                        "فريق حِرَف";
        sendEmail(to, subject, body);
    }

    public void sendNewWorkshopEmail(String to, String username, String studioName, String workshopTitle) {
        String subject = "إضافة ورشة عمل جديدة";
        String body = "مرحباً " + username + "،\n\n" +
                        "نود إعلامكم بأنه تم إضافة ورشة عمل جديدة من قبل استوديو  \"" + studioName + "\".\n\n" +
                        "عنوان الورشة: " + workshopTitle + "\n\n" +
                        "يمكنك الآن الدخول إلى التطبيق لحجز مقعدك في الورشة قبل اكتمال الحجز.\n\n" +
                        "لا تفوت الفرصة\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendEmail(to, subject, body);
    }

    public void sendBookingUpdateEmail(String to, String username, String workshopTitle) {
        String subject = "تحديث على حجزك";
        String body = "مرحباً " + username + "،\n\n" +
                        "نود إعلامكم بأنه تم تحديث مقاعد حجزك في ورشة العمل \"" + workshopTitle + "\".\n\n" +
                        "يرجى الدخول إلى التطبيق للاطلاع على تفاصيل الحجز المحدثة.\n\n" +
                        "لا تفوت أي تحديثات جديدة.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";

        sendEmail(to, subject, body);
    }

    public void sendInsuranceRefundEmail(String to, String username, Double amount) {
        String subject = "استرجاع مبلغ التأمين";
        String body = "مرحباً " + username + "،\n\n" +
                        "نود إعلامكم بأنه تم إعادة مبلغ التأمين إلى رصيدك بنجاح.\n\n" +
                        "قيمة المبلغ: " + amount + "\n\n" +
                        "نشكرك على ثقتك بخدماتنا.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendEmail(to, subject, body);
    }

    public void sendRentStatusChangeEmail(String to, String username, Integer rentId, String status) {
        String subject = "تحديث حالة طلب الاستئجار";
        String body =
                "مرحباً " + username + "،\n\n" +
                        "نود إعلامكم بأنه تم تحديث حالة طلب الاستئجار الخاص بك.\n\n" +
                        "رقم طلب الاستئجار: " + rentId + "\n" +
                        "الحالة الجديدة: " + status + "\n\n" +
                        "يمكنكم الدخول إلى المنصة للاطلاع على تفاصيل الطلب.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendEmail(to, subject, body);
    }

    public void sendBookingStatusChangeEmail(String to, String username, Integer bookingId, String status) {
        String subject = "تحديث حالة الحجز";
        String body = "مرحباً " + username + "،\n\n" +
                        "نود إعلامكم بأنه تم تحديث حالة الحجز الخاص بك.\n\n" +
                        "رقم الحجز: " + bookingId + "\n" +
                        "الحالة الجديدة: " + status + "\n\n" +
                        "يمكنكم الدخول إلى المنصة للاطلاع على تفاصيل الحجز.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendEmail(to, subject, body);
    }
}
