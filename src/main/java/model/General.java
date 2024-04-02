package model;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class General {
	public static String getResourcesImagesPath() {
		return "resources/images";
	}
	
	public static String getProjectFolderPath() {
		return "D:/tal/JAVA/AuctionJavax/src/main/webapp";
	}
	
	public static void sendMail(String title, String msg, String receiving) {
    	final String username = "auctionhousejava@gmail.com";
        final String password = "mxjb hint zyzf fbsn";
        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("auctionhousejava@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiving));
            message.setSubject(title);
            message.setText(msg);

            Transport.send(message);

            System.out.println("Sent email successfully");
            
        } catch (MessagingException e) {
        	System.out.println("Failed to send email");
            e.printStackTrace();
        }
    }
}
