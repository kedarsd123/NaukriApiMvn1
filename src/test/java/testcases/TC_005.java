package testcases;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.testng.annotations.Test;

public class TC_005 extends Basic_Pre_Post_Conditions
{
  @Test
  public void SendEmail() throws Exception 
  {
	  MultiPartEmail email = new MultiPartEmail();
	  email.setHostName("smtp.gmail.com");
	  email.setSmtpPort(465);
	  email.setSSLOnConnect(true);
	  email.setAuthenticator(new DefaultAuthenticator(ConfigDct.get("GmailId"),ConfigDct.get("GmailIdPwd")));
	  email.setFrom(ConfigDct.get("GMailFrom"));
	  email.setSubject("Auto Email By Selenium : Kedar Datar");
	  email.setMsg("Please find attached summary report for your reference"+"\n"+"Note : Download the file and open it to view summary of execution of Automation Script.");
	  
	  for (Object obj : EmailDct.values())
	  {
		  email.addTo(obj.toString());
	  }
	  
	  EmailAttachment attachment = new EmailAttachment();
	  attachment.setPath(StoredJobSummaryReportPath);
	  email.attach(attachment);
	  email.send();
	  PostExecDetails("Email Sent Successfully",micPass,false);
	  
	  FlushAssert();
	}
}
