package assignment2ecse428;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Stepdefs {

    // Constants
    // replace PATH_TO_GECKO_DRIVER value with your own driver
    private final String PATH_TO_GECKO_DRIVER = "vendor/geckodriver-v0.24.0-linux64/geckodriver";
    // Replace test emails. TARGET_EMAIL_1 must be email address being logged into confirming the tests
    private final String TARGET_EMAIL_1 = "aidssmcc@gmail.com";
    private final String TARGET_EMAIL_2 = "aidansullivan25@gmail.com";
    private final String SHADOW_PATH = ".shadow";
    private final String SUBJECT_TEXT = "Test";
    // NOTE, due to the nature of the typing bot, some paths may not work. It will recognize all characters that can
    // be typed without pressing shift. Otherwise and exception must be made. It does recognize capitals.
    // This only applies to TARGET_IMG_1 and TARGET_IMG_2
    private final String TARGET_IMG_1 = "/home/aidssmcc/git_repos/assignment2ecse428/vendor/test1.png";
    private final String TARGET_IMG_2 = "/home/aidssmcc/git_repos/assignment2ecse428/vendor/test2.png";

    // Variables
    private WebDriver driver;

    @Given("^a user is logged into their gmail$")
    public void a_user_is_logged_into_their_gmail() throws Exception {

        // Read login credentials
        ArrayList<String> creds = readFile(SHADOW_PATH);

        // set up drivers before attempting to log in
        setupSeleniumWebDrivers();

        // go to page
        driver.get("https://mail.google.com");

        // input credentials
        WebElement emailLogin = driver.findElement(By.id("identifierId"));
        emailLogin.sendKeys(creds.get(0));

        WebElement emailNext = driver.findElement(By.id("identifierNext"));
        emailNext.click();

        Thread.sleep(1500); // Pause to allow password dialog to open

        WebElement emailPassword = driver.findElement(By.name("password"));
        emailPassword.sendKeys(creds.get(1));
        WebElement passwordNext = driver.findElement(By.id("passwordNext"));
        passwordNext.click();

        Thread.sleep(3000); // Pause for page to load
        // only detected if previous message is read. Else would throw an error and fail the test
        WebElement firstRow = driver.findElement(By.className("yO"));
    }

    @When("^the compose button is selected$")
    public void the_compose_button_is_selected() throws Exception {
        WebElement compose = driver.findElement(By.className("z0"));
        compose.click();
        Thread.sleep(1000); // Pause for message dialog to open
    }

    @When("^a valid recipient is inputted$")
    public void a_valid_recipient_is_inputted() throws Exception {
        WebElement recipientsInput = driver.findElement(By.name("to"));
        recipientsInput.sendKeys(TARGET_EMAIL_1);               // type recipient
    }

    @When("^the attach files button is selected$")
    public void the_attach_files_button_is_selected() throws Exception {
        WebElement attach = driver.findElement(By.cssSelector("div[class='a1 aaA aMZ']"));
        attach.click();         // click attach box. Pause for attachment window to open
        Thread.sleep(1000);
    }

    @When("^an image is selected$")
    public void an_image_is_selected() throws Exception {
        typeText(TARGET_IMG_1, true);
        Thread.sleep(500);      // Puase for dialog close
    }

    @When("^a subject is filled in the field$")
    public void a_subject_is_filled_in_the_field() throws Exception {
        WebElement subjectBox = driver.findElement(By.name("subjectbox"));
        subjectBox.sendKeys(SUBJECT_TEXT);
        Thread.sleep(2000);
    }

    @When("^the send button is pressed$")
    public void the_send_button_is_pressed() throws Exception {
        WebElement sendBox = driver.findElement(By.cssSelector("div[class='T-I J-J5-Ji aoO T-I-atl L3']"));
        sendBox.click();
    }

    @Then("^the email will be sent$")
    public void the_email_will_be_sent() throws Exception {
        Thread.sleep(6000);     // Pause for message send
        java.util.List<WebElement> subjects = driver.findElements(By.xpath("//*[@class='bog']"));

        // Compare subject text to email that was sent
        Assert.assertTrue( subjects.get(0).getText().equals(SUBJECT_TEXT));

        // Open email so that it is no longer unread. Ensures next test has an unread email.
        subjects.get(0).click();
        Thread.sleep(2000);
        driver.quit();
    }

    @When("^2 valid recipients are given$")
    public void a_second_valid_recipient_is_given() throws Exception {
        WebElement recipientsInput = driver.findElement(By.name("to"));
        recipientsInput.sendKeys(TARGET_EMAIL_1 + " ");               // type recipient
        Thread.sleep(1000);

        // Element changes, need to update targetted element
        recipientsInput = driver.findElement(By.xpath("//textarea[@name='to' and not(@type='hidden')]"));
        recipientsInput.sendKeys(TARGET_EMAIL_2 + " ");
    }

    @When("^the attach files button is selected again$")
    public void the_attach_files_button_is_selected_again() throws Exception {
        Thread.sleep(1000);      // Puase for first image to be uploaded properly
        the_attach_files_button_is_selected();
    }

    @When("^a new image is selected$")
    public void a_new_image_is_selected() throws Exception {
        Thread.sleep(500);
        typeText(TARGET_IMG_2, true);
        Thread.sleep(500);      // Puase for dialog close
    }

    @Then("^a popup will appear warning the user$")
    public void a_popup_will_appear_warning_the_user() throws Exception {
        Thread.sleep(1000);     // Pause for message send
        WebElement errorText = driver.findElement(By.xpath("//span[@role='heading' and contains(text(), 'Error')]"));

        // ensure that alert has expected text
        Assert.assertTrue(errorText.getText().equals("Error"));
        driver.quit();
    }

    // Helpers
    /**
     * Simple helper function to read a file line by line and return it as an array list
     * @param filename path to a file and that file's name
     * @return String ArrayList that reads file line by line
     */
    private ArrayList<String> readFile(String filename) {

        ArrayList<String> credentials = new ArrayList<String>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                if (!br.ready()) break;
                credentials.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    /**
     * Sets up selenium firefox web drivers
     */
    private void setupSeleniumWebDrivers() {
        if (driver == null) {
            System.out.println("Setting up Firefox Driver... ");
            System.setProperty("webdriver.gecko.driver", PATH_TO_GECKO_DRIVER);
            driver = new FirefoxDriver();
            System.out.println("Driver set up");
        }
    }

    /**
     * Creates a robot that will type out the text provided manually. Copy pasting was problematic in this
     * environment so this is the workaround
     * @param text A string of text for the robot to type out
     * @param enter If true, will press the enter key after execution
     */
    private void typeText (String text, Boolean enter) {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // failed to create a robot, if this happens, the execution fails.
            driver.quit();
            System.out.println("Robot failed to initialize");
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            int key = KeyEvent.getExtendedKeyCodeForChar(c);

            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(key);
                robot.keyRelease(key);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '_') {
                // a bug with getExtendedKeyCodeForChar doesn't recognize underscores properly
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_MINUS);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '@') {
                // antoher exception
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
            else {
                robot.keyPress(key);
                robot.keyRelease(key);
            }
        }

        if (enter) {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
    }
}