package ru.pavel.tests;

import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class NegativeLoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        loginPage.open();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testIsPasswordErrorVisible() {
        boolean visible = loginPage.isPasswordErrorVisible("default");
        assertFalse(visible);
    }

    @Test
    public void testIsUsernameErrorVisible() {
        boolean visible = loginPage.isUsernameErrorVisible("1q2w3e");
        assertFalse(visible);
    }

}
