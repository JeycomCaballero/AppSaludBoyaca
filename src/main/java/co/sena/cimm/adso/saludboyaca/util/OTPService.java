package co.sena.cimm.adso.saludboyaca.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Properties;

public class OTPService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String EMAIL_REMIT = "jeycomgonzales@gmail.com";
    private static final String EMAIL_PASS = "pbpi hjol rhrr eqcb";
    private static final int OTP_LONGITUD = 6;
    private static final long OTP_EXPIRA_MS = 5 * 60 * 1000;

    public static String generarOTP() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LONGITUD);
        for (int i = 0; i < OTP_LONGITUD; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }

    public static boolean esValido(String ingresado, String guardado, long timestamp) {
        if (ingresado == null || guardado == null) {
            return false;
        }
        long ahora = Instant.now().toEpochMilli();
        boolean noExpirado = (ahora - timestamp) <= OTP_EXPIRA_MS;
        boolean coincide = ingresado.trim().equals(guardado);
        return noExpirado && coincide;
    }

    public static void enviarOTP(String destinatario, String asunto, String cuerpo)
            throws MessagingException, UnsupportedEncodingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMIT, EMAIL_PASS);
            }
        });

        Message mensaje = new MimeMessage(mailSession);

        mensaje.setFrom(new InternetAddress(EMAIL_REMIT, "Salud Boyacá"));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));

        mensaje.setSubject(asunto);

        mensaje.setHeader("Content-Type", "text/html; charset=UTF-8");
        mensaje.setContent(cuerpo, "text/html; charset=UTF-8");

        Transport.send(mensaje);
    }
}
