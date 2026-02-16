package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;

    private final By usernameField = By.name("login");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//button[@type='submit' and text()='Login']");
    private final By errorMessage = By.xpath("//*[text()='Password should contain at least 4 characters; a special symbol; upper-case (A - Z); lower-case']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Открытие страницы логина")
    public void open() {
        driver.get("https://demo.reportportal.io/ui/#login");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    @Step("Ввод логина '{username}' и пароля '{password}'")
    public void login(String username, String password) {
        WebElement userField = driver.findElement(usernameField);
        userField.clear();
        userField.sendKeys(username);

        WebElement passField = driver.findElement(passwordField);
        passField.clear();
        passField.sendKeys(password);

        driver.findElement(loginButton).click();

    }

    @Step("Проверка появления ошибки пароля при логине '{username}'")
    public boolean isPasswordErrorVisible(String username) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(loginButton).click();

        WebDriverWait wait = getWebDriverWait();
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Проверка появления ошибки логина при пароле '{password}'")
    public boolean isUsernameErrorVisible(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        WebDriverWait wait = getWebDriverWait();
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebDriverWait getWebDriverWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

}
