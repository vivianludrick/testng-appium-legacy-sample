package com.browserstack;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.yaml.snakeyaml.Yaml;

public class DriverUtils {
    // Appium 8+: AndroidDriver is no longer generic. AndroidElement is gone.
    public AndroidDriver driver; 
    public static final String USER_DIR = "user.dir";
    public static final String APP_ID = "bs://791ad7f53c6a72338d23c286acb656e355ace4ae";

    @BeforeMethod(alwaysRun=true)
    public void setUp() throws Exception {
        File file = new File(System.getProperty(USER_DIR) + "/browserstack.yml");
        Map<String, Object> browserStackYamlMap = convertYamlFileToMap(file, new HashMap<>());

        String userName = System.getenv("BROWSERSTACK_USERNAME") != null 
            ? System.getenv("BROWSERSTACK_USERNAME") 
            : (String) browserStackYamlMap.get("userName");

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY") != null 
            ? System.getenv("BROWSERSTACK_ACCESS_KEY")
            : (String) browserStackYamlMap.get("accessKey");

        // Use UiAutomator2Options instead of DesiredCapabilities
        UiAutomator2Options options = new UiAutomator2Options();
        options.setApp(APP_ID);
        options.setDeviceName("Samsung Galaxy S22 Ultra");
        options.setPlatformVersion("12.0");
        options.setAutomationName("UiAutomator2");
        options.setAutoGrantPermissions(true);

        // BrowserStack specific options
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("projectName", "Testng Sample Project");
        bstackOptions.put("buildName", "UiAutomator2 Build");
        options.setCapability("bstack:options", bstackOptions);

        String remoteUrl = String.format("https://%s:%s@hub.browserstack.com/wd/hub", userName, accessKey);
        
        // Initialize driver with options
        driver = new AndroidDriver(new URL(remoteUrl), options);
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    private static Map<String, Object> convertYamlFileToMap(File yamlFile, Map<String, Object> map) {
        try (InputStream inputStream = Files.newInputStream(yamlFile.toPath())) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);
            if (config != null) {
                map.putAll(config);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Malformed browserstack.yml file - %s.", e));
        }
        return map;
    }
}
