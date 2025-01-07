package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NaukriHomePage extends BasePage
{
	public NaukriHomePage (WebDriver d)
	{
		super(d);
	}
	
	@FindBy(xpath="//input[@placeholder='Enter skills / designations / companies']")
	WebElement SkillNameEdit;
	
	@FindBy(xpath="//input[@placeholder='Enter location']")
	WebElement JobLocation;
	
	@FindBy(xpath="//div[@class='qsbSubmit']")
	WebElement SubmitBtn;
	
	public void SetSkillName (String param)
	{
		SkillNameEdit.click();
		SkillNameEdit.sendKeys(param);
		SkillNameEdit.click();
	}
	
	public void SetJobLocation (String param)
	{
		JobLocation.sendKeys(param);
		JobLocation.click();
	}
	
	public void ClickSubmitButton()
	{
		SubmitBtn.click();
	}

}
