package com.estatica.servicos.service;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.estatica.servicos.model.Produto;

public class ConsultaMailService {

	public void sendMailReport(Produto produto, File file) {
		Properties props = new Properties();

		// Parâmetros de conexão com servidor Gmail
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("zct.automacao@gmail.com", "engenheiro31");
			}
		});

		session.setDebug(true);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zct.automacao@gmail.com"));
			message.setSubject("Lote " + produto.getLote());

			Address[] toUser = InternetAddress.parse("z_cittadin@hotmail.com");
			// .parse("seuamigo@gmail.com, seucolega@hotmail.com,
			// seuparente@yahoo.com.br");
			message.setRecipients(Message.RecipientType.TO, toUser);

			BodyPart messageBodyPart = new MimeBodyPart();
			StringBuilder builder = new StringBuilder();
			builder.append("<p>Olá, Zander.</p>");
			builder.append("<p>Segue em anexo o relatório do lote de produção " + produto.getLote() + ".</p>");
			String msg = builder.toString();
			messageBodyPart.setText(msg);
			messageBodyPart.setContent(msg, "text/html; charset=utf-8");

			DataSource fds = new FileDataSource(file.getAbsolutePath());

			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			attachmentBodyPart.setDisposition(Part.ATTACHMENT);
			attachmentBodyPart.setDataHandler(new DataHandler(fds));
			attachmentBodyPart.setFileName(fds.getName());

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(messageBodyPart);
			mp.addBodyPart(attachmentBodyPart);
			message.setContent(mp);

			/** Método para enviar a mensagem criada */
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
