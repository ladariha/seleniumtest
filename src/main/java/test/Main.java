package test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vriha
 */
public class Main {

    public static void main(String[] args) {

        WebDriver browser;
        browser = new ChromeDriver(getCapabilities("chrome", DesiredCapabilities.chrome()));
        browser.get("http://ladariha.cz/testik/index.html");

        waitFor(4);
        System.out.println("LOADED");
        new Actions(browser)
                .clickAndHold(findElement(browser, "//*[@id='aaa']"))
                .moveToElement(findElement(browser, "//*[@id='drop-target-one']"))
                .release()
                .build()
                .perform();
        System.out.println("DROPPED");
        waitFor(6);
        browser.quit();
    }

    public static WebElement findElement(SearchContext parent, String xpath) {
        final By xp = By.xpath(xpath);
        FluentWait<SearchContext> wait = new FluentWait<>(parent);
        wait.pollingEvery(500, TimeUnit.MILLISECONDS).withTimeout(15000, TimeUnit.MILLISECONDS);
        wait.withMessage(xp.toString());
        return wait.ignoring(NoSuchElementException.class).until(new Function<SearchContext, WebElement>() {

            @Override
            public WebElement apply(SearchContext context) {
                return context.findElement(xp);
            }
        });
    }

    public static DesiredCapabilities getCapabilities(String browser, DesiredCapabilities defaults) {

        LoggingPreferences prefs = new LoggingPreferences();
        prefs.enable(LogType.BROWSER, Level.ALL);

        switch (browser) {
            case "chrome":
            case "firefox":
            case "phantomjs":
                defaults.setCapability(CapabilityType.LOGGING_PREFS, prefs);
                break;
            case "ie": // IE does not support it
            default:
                break;
        }

        return defaults;
    }

    public static void waitFor(final int seconds) {
        final long start = System.currentTimeMillis();
        FluentWait<By> fluentWait = new FluentWait<>(By.tagName("body"));
        final long timeout = 1000 * seconds;
        fluentWait.pollingEvery(500, TimeUnit.MILLISECONDS).withTimeout(15000, TimeUnit.MILLISECONDS).until(new Predicate<By>() {

            @Override
            public boolean apply(By by) {
                return System.currentTimeMillis() - start >= timeout;
            }
        });
    }
}
