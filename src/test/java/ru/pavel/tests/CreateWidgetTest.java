package ru.pavel.tests;

import org.example.pages.DashboardPage;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateWidgetTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--incognito");

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createWidgetInNewDashboardTest() {

        String dashName = "Auto-Dash-" + System.currentTimeMillis();

        try {
            loginPage.open();
            loginPage.login("default", "1q2w3e");

            dashboardPage.createNewDashboard(dashName);
            dashboardPage.addWidgetToCurrentDashboard("Launch statistics chart", "Test-Widget");

            assertEquals(1, dashboardPage.getWidgetsCount());
        } finally {
            dashboardPage.deleteCurrentDashboard();
        }
    }
}