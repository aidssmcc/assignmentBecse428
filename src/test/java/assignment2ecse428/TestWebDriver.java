package assignment2ecse428;

public class TestWebDriver {

    private final String PATH_TO_GECKO_DRIVER = "/home/aidssmcc/git_repos/assignment2ecse428/vendor/geckodriver-v0.24.0-linux64/geckodriver";

    private static WebDriver driver;
    private static String targetEmail1;
    private static String targetEmail2;
    private static ArrayList<String> creds;

    public static void main(String[] args) throws InterruptedException {


        creds = readFile(".shadow");

        targetEmail1 = "aidssmcc@gmail.com";
        targetEmail2 = "aidnsullivan25@gmail.com";

//        driver = new FirefoxDriver();
//        TestWebDriver.testEmail1Sende/home/aidssmcc/Documents/school/semester6/ECSE428/asgB/test1.png
//        r1Attachment("/home/aidssmcc/Documents/school/semester6/ECSE428/asgB/test1.png");

        driver = new FirefoxDriver();
        TestWebDriver.testEmail2Sender1Attachment("/home/aidssmcc/Documents/school/semester6/ECSE428/asgB/test1.png");

    }

//    private static void parseJSON(String filename) {
//        try {
//
//            JSONParser parser = new JSONParser();
//            Object obj = (Object) parser.parse();
//
//            JSONObject jsonObject = (JSONObject) obj;
//
//            String name = (String) jsonObject.get("Name");
//            String author = (String) jsonObject.get("Author");
//            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
//
//            System.out.println("Name: " + name);
//            System.out.println("Author: " + author);
//            System.out.println("\nCompany List:");
//            Iterator<String> iterator = companyList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Given a valid robot, it will type out the text provided manually. Copy pasting was problematic in this
     * environment so this is the workaround
     * @param robot an initialized robot
     * @param text A string of text for the robot to type out
     */
    private static void typeText (Robot robot, String text) {
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            int key = KeyEvent.getExtendedKeyCodeForChar(c);

            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(key);
                robot.keyRelease(key);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                robot.keyPress(key);
                robot.keyRelease(key);
            }
        }
    }

    /**
     * Helper function used to confirm that a test was sucessful. Reads subject of first item in inbox
     * @return returns the text read from the first subject
     */
    private static String readFirstEmailSubject () {
        WebElement firstSubject = driver.findElement(By.className("bqe"));
        return firstSubject.getText();
    }

    /**
     * Performs base test, send an email to 1 person with 1 attachment
     * @param filepath path to image attachment
     * @return success state returns 0, else 1
     * @throws InterruptedException
     */
    private static int testEmail1Sender1Attachment(String filepath) throws InterruptedException {
        driver.get("https://mail.google.com");

        String testSubject = "Test1";

        TestWebDriver.login();  // login helper

        // click compose button
        WebElement compose = driver.findElement(By.className("z0"));
        compose.click();
        Thread.sleep(1000);

        // find new screen elements
        WebElement attach = driver.findElement(By.cssSelector("div[class='a1 aaA aMZ']"));
        WebElement recipientsInput = driver.findElement(By.name("to"));

        recipientsInput.sendKeys(targetEmail1);    // type recipient
        attach.click();                                                 // click attach box
        Thread.sleep(1000);

        // Use a robot to enter keys, since a target can't be found easily
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // failed to create a robot, if this happens, the execution fails.
            driver.quit();
            e.printStackTrace();
            return 1;
        }
        typeText(robot, filepath);          // helper function
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);

        // write subject
        WebElement subjectBox = driver.findElement(By.name("subjectbox"));
        subjectBox.sendKeys(testSubject);
        Thread.sleep(100);

        WebElement sendBox = driver.findElement(By.cssSelector("div[class='T-I J-J5-Ji aoO T-I-atl L3']"));
        sendBox.click();

        Thread.sleep(5000);

        // Confirm email was received
        System.out.println(readFirstEmailSubject());

        // close screen
        driver.quit();

        return 0;
    }

    /**
     * Second test to be performed. Emails 2 recipients an email with an attachment
     * @param filepath path to image attachment
     * @return success state returns 0, else 1/home/aidssmcc/Documents/school/semester6/ECSE428/asgB/test1.png
     *
     * @throws InterruptedException
     */
    private static int testEmail2Sender1Attachment(String filepath) throws InterruptedException {
        driver.get("https://mail.google.com");
        String testSubject = "Test2";
        TestWebDriver.login(creds.get(0), creds.get(1));  // login helper

        // click compose button
        WebElement compose = driver.findElement(By.className("z0"));
        compose.click();
        Thread.sleep(1000);

        // find new screen elements
        WebElement attach = driver.findElement(By.cssSelector("div[class='a1 aaA aMZ']"));
        WebElement recipientsInput = driver.findElement(By.name("to"));

        recipientsInput.sendKeys(targetEmail1 + " ");    // type recipients
        Thread.sleep(500);
        recipientsInput = driver.findElement(By.cssSelector("textarea[name='to' tabindex='1']"));
        recipientsInput.sendKeys(targetEmail2);
        Thread.sleep(5000);
        attach.click();                                                 // click attach box
        Thread.sleep(1000);

        // Use a robot to enter keys, since a target can't be found easily
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // failed to create a robot, if this happens, the execution fails.
            driver.quit();
            e.printStackTrace();
            return 1;
        }
        typeText(robot, filepath);          // helper function
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(1000);

        // write subject
        WebElement subjectBox = driver.findElement(By.name("subjectbox"));
        subjectBox.sendKeys(testSubject);
        Thread.sleep(100);

        WebElement sendBox = driver.findElement(By.cssSelector("div[class='T-I J-J5-Ji aoO T-I-atl L3']"));
        sendBox.click();

        Thread.sleep(5000);

        // Confirm email was received
        System.out.println(readFirstEmailSubject());

        // close screen
        driver.quit();

        return 0;
    }

    // Helpers
    public void setupSeleniumWebDrivers() {
        try {
            if (driver == null) {
                System.out.println("Setting up GeckoDriver... ");
                System.setProperty("webdriver.gecko.driver", PATH_TO_GECKO_DRIVER);
                driver = new FirefoxDriver();
                System.out.print("Done!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        creds = readFile(".shadow");
    }

    public void goTo(String url) {
        if (driver != null) {
            System.out.println("Go to url " + url);
            driver.get(url);
        }
    }

    /**
     * Helper function to login to email with given credentials
     * @throws InterruptedException
     */
    public void login() throws InterruptedException {
        WebElement emailLogin = driver.findElement(By.id("identifierId"));
        emailLogin.sendKeys(creds.get(0));

        WebElement emailNext = driver.findElement(By.id("identifierNext"));
        emailNext.click();

        Thread.sleep(2000);

        WebElement emailPassword = driver.findElement(By.name("password"));
        emailPassword.sendKeys(creds.get(1));
        WebElement passwordNext = driver.findElement(By.id("passwordNext"));
        passwordNext.click();


        Thread.sleep(5000);
    }

    /**
     * Simple helper function to read a file line by line and return it as an array list
     * @param filename path to a file and that file's name
     * @return String ArrayList that reads file line by line
     */
    private static ArrayList<String> readFile(String filename) {

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
}
