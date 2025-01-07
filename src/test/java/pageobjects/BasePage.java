package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public class BasePage
{
	WebDriver d;
	Actions action;
	
	public BasePage (WebDriver d)
	{
		this.d = d;
		PageFactory.initElements(d, this);
		action = new Actions(d);
	}

}
