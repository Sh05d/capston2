package org.example.capston2.Service;


import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WhatsappService {

    private final String fromWhatsAppNumber;

    public WhatsappService(@Value("${twilio.account.sid}") String accountSid, @Value("${twilio.auth.token}") String authToken, @Value("${twilio.whatsapp.number}") String fromWhatsAppNumber) {
        this.fromWhatsAppNumber = fromWhatsAppNumber;
        Twilio.init(accountSid, authToken);
    }

    public String sendMessage(String to, String messageBody) {
        Message message = Message.creator(new PhoneNumber("whatsapp:+" + to), new PhoneNumber("whatsapp:" + fromWhatsAppNumber), messageBody).create();

        return message.getSid();
    }

    public void sendWorkshopCancellationMessage(String to, Double refundAmount, String workshopTitle) {
        String message ="عزيزي العميل،\n\n" +
                        "نأسف لإبلاغك بأنه تم إلغاء ورشة العمل '" + workshopTitle + "'.\n\n" +
                        "تمت إعادة مبلغ الحجز وقدره " + refundAmount + " إلى رصيدك في المنصة بنجاح.\n\n" +
                        "نشكر لك تفهمك.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendMessage(to, message);
    }

    public void sendBookingConfirmationMessage(String to, String workshopTitle, Double totalPrice) {
        String message = "مرحباً،\n\n" +
                        "تم تأكيد حجزك بنجاح لورشة العمل '" + workshopTitle + "'.\n\n" +
                        "قيمة الحجز: " + totalPrice + "\n\n" +
                        "نتمنى لك تجربة ممتعة.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendMessage(to, message);
    }

    public void sendRentToolkitMessage(String to, String toolkitName, Double totalPrice, LocalDate endDate) {
        String message = "مرحباً،\n\n" +
                        "تم تأكيد عملية استئجار الأدوات.\n\n" +
                        "اسم الأدوات: " + toolkitName + "\n" +
                        "قيمة الاجمالية: " + totalPrice + "\n" +
                        "تاريخ الإرجاع: " + endDate + "\n\n" +
                        "يرجى الالتزام بموعد الإرجاع واعادة الادوات بحالتها السليمة لاسترداد رسوم التأمين.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendMessage(to, message);
    }

    public void sendCancelBookingMessage(String to, String workshopTitle, Double refundAmount) {
        String message = "مرحباً،\n\n" +
                        "تم إلغاء حجزك لورشة العمل '" + workshopTitle + "' بنجاح.\n\n" +
                        "تمت إعادة مبلغ وقدره " + refundAmount + " إلى رصيدك في المنصة.\n\n" +
                        "نشكرك على استخدامك لخدماتنا.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendMessage(to, message);
    }

    public void sendCancelRentMessage(String to, Double refundAmount) {
        String message = "مرحباً،\n\n" +
                        "تم إلغاء طلب استئجار الأدوات '" +
                        "تمت إعادة مبلغ وقدره " + refundAmount + " إلى رصيدك في المنصة.\n\n" +
                        "نشكرك على استخدامك لخدماتنا.\n\n" +
                        "مع أطيب التحيات،\n" +
                        "فريق منصة حِرَف";
        sendMessage(to, message);
    }
}
