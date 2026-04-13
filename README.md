To get your project running with the **BrowserStack Java SDK** for accessibility testing, follow this setup guide.

---
## Step 1: Update your Dependencies

Open your `pom.xml` file and add the BrowserStack SDK to your `<dependencies>` section. This allows Maven to pull the necessary libraries to intercept your test execution.
```xml
<dependency>
    <groupId>com.browserstack</groupId>
    <artifactId>browserstack-java-sdk</artifactId>
    <version>LATEST</version>
    <scope>compile</scope>
</dependency>
```

## Step 2: Configure the Java Agent

The SDK works as a Java Agent to wrap your tests. You need to configure the `maven-surefire-plugin` within a profile in your `pom.xml` so it knows where to find the SDK JAR during runtime.
```xml
<profiles>
    <profile>
        <id>{{ test-profile }}</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <argLine>-javaagent:${com.browserstack:browserstack-java-sdk:jar}</argLine>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

## Step 3: Create the Configuration File

Create a file named `browserstack.yml` in your project's **root directory**. This file handles your credentials and specific accessibility flags.
```yml
userName: <BROWSERSTACK_USERNAME>
accessKey: <BROWSERSTACK_ACCESS_KEY>
framework: testng
projectName: MyAccessibilityProject
buildName: Build-01

# Flags required to run a11y in legacy mode
app: bs://<app-id>
# The dummy ID is can be used because the actual App ID is pulled from the legacy driver.
browserstackAutomation: false
accessibility: true
```

---
## Step 4: Run Your Tests

Since the configuration is wrapped inside a Maven profile, you must activate that profile when running your commands.

### Option A: Using the Terminal

Run the following command from your project root:

```sh
mvn test -P {{ test-profile }}
```
### Option B: Using an IDE (IntelliJ/Eclipse)

1. Open the **Maven** tool window.

2. Under **Profiles**, check the box for your test profile.

3. Run the `test` lifecycle goal.

---

> [!Note]
> - Replace `<BROWSERSTACK_USERNAME>` and `<BROWSERSTACK_ACCESS_KEY>` in [#Step 3](#step-3-create-the-configuration-file) with your actual keys found in the [BrowserStack Dashboard](https://www.browserstack.com/accounts/settings).
> - Replace the `{{ test-profile }}` with the profile you run in pom.xml [#Step 2](#step-2-configure-the-java-agent) and [#Step 4](#step-4-run-your-tests)
> - **Legacy Mode:** The flags `app: bs://<app-id>` and `browserstackAutomation: false` are specific to running **App Accessibility** in legacy mode. If you transition to the standard App Automate flow later, these may need to be adjusted.
> - **SDK Path:** The `${com.browserstack:browserstack-java-sdk:jar}` syntax in the `argLine` is a Maven property that automatically resolves to the local path of the downloaded JAR file.
