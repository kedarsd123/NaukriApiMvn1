package actionsteps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.testng.annotations.Test;

public class DemoTestMail {
  //@Test
  public void f() 
  {
	  String host = "smtp.gmail.com";
	  final String user = "kdatar57@gmail.com";// change accordingly
	  final String password = "gsif usxa bxns gczq";// change accordingly
	// Get the session object
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

			List<String> emails = new ArrayList<String>();
			
			   emails.add("kdatar84@gmail.com");
			   Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				});
			   
			   try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(user));
					message.setRecipients(Message.RecipientType.TO, getRecipients(emails));
					// message.addRecipient(Message.RecipientType.TO,new InternetAddress());
					message.setSubject("Naukri Dot Com");
					// message.setText("Please find attached summary report for your reference");

					BodyPart messageBodyPart1 = new MimeBodyPart();
					messageBodyPart1.setText("Please find attached summary report for your reference" + "\n"
							+ "Note : Download the file and open it to view summary of execution of Automation Script.");
					MimeBodyPart messageBodyPart2 = new MimeBodyPart();

					String filename = "E:\\SeleniumProjects\\NaukriApiMVN1\\Automation\\TestReport\\JobSummaryReport_07-01-2025 19_42_31.xlsx";// change accordingly
					DataSource source = new FileDataSource(filename);
					messageBodyPart2.setDataHandler(new DataHandler(source));
					messageBodyPart2.setFileName(filename);

					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart1);
					multipart.addBodyPart(messageBodyPart2);

					// 6) set the multiplart object to the message object
					message.setContent(multipart);
					// send the message
					Transport.send(message);

					//System.out.println("message sent successfully...");
					// driver.close();
					//System.err.println("driver closed");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			   
			   
	  
  }
  
  @Test
  public void g() throws Exception
  {
	  //SendGMail();
	  HashMap<Integer, String> EmailDct1 = new HashMap<Integer, String>();
	  EmailDct1.put(1, "Kedar");
	  EmailDct1.put(2, "Aneesh");
	  EmailDct1.put(3, "Aaresh");
	  EmailDct1.put(4, "Pulak");
	  
	  for (Object obj : EmailDct1.values())
	  {
		  System.out.println(obj.toString());
	  }
  }
  
  public static void SendGMail () throws Exception
	{
	  	MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setSSLOnConnect(true);
		email.setAuthenticator(new DefaultAuthenticator("kdatar57","gsif usxa bxns gczq"));
		email.setFrom("kdatar57@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)"+"\n"+"Shared by Kedar Datar");
		email.addTo("kdatar84@gmail.com");
		email.addTo("ksdatar@rediffmail.com");
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath("E:\\SeleniumProjects\\NaukriApiMVN1\\Automation\\TestReport\\JobSummaryReport_07-01-2025 19_42_31.xlsx");
		email.attach(attachment);
		email.send();
	}
  
  public static Address[] getRecipients ( java.util.List <String> emails) throws AddressException {

		Address[] addresses = new Address[emails.size()];

		for (int i =0;i < emails.size();i++) {

			addresses[i] = new InternetAddress(emails.get(i));

		}

		return addresses;

	}
  
}
