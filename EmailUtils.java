package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.io.File;
import java.util.Properties;

public class EmailUtils {

    public static void enviarEmailComAnexo(
            String destinatario,
            String assunto,
            String corpo,
            File arquivoAnexo
    ) throws Exception {

        final String usuario = "stelladd203.gmail.com";
        final String senha = "hdahtkjpjlhvgdos";

        // Configuração SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(usuario));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(assunto);

        // Corpo do e-mail
        MimeBodyPart textoBodyPart = new MimeBodyPart();
        textoBodyPart.setText(corpo);

        // Anexo
        MimeBodyPart anexoBodyPart = new MimeBodyPart();
        anexoBodyPart.attachFile(arquivoAnexo);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textoBodyPart);
        multipart.addBodyPart(anexoBodyPart);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("E-mail enviado com sucesso!");
    }
}
