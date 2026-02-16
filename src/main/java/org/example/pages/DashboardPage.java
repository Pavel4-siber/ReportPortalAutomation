package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    //private final By dashboardsNavBtn = By.xpath("//a[contains(@href, 'dashboard')]");
    //private final By firstDashboardLink = By.xpath("//a[contains(@href, 'dashboard/')]");
    private final By addWidgetBtn = By.xpath("//button[contains(., 'Add new widget')]");
    private final By widgetTypeChart = By.xpath("//div[text()='Launch statistics chart']");
    private final By nextStepBtn = By.xpath("//button[contains(., 'Next step')]");
    private final By firstFilterRadio = By.xpath("//div[contains(@class, 'filter-item')]//label");
    private final By notificationToast = By.xpath("//div[contains(@class, 'notificationItem')]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Step("Перейти напрямую к списку дашбордов через URL")
    public void openFirstDashboardDirectly() {
        String baseUrl = driver.getCurrentUrl().split("/ui/")[0];
        driver.get(baseUrl + "/ui/#default_personal/dashboard");

        WebElement firstDash = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href, 'dashboard/')]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstDash);
    }

    @Step("Нажать кнопку 'Add New Widget'")
    public void startAddingWidget() {

        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationToast));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addWidgetBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    @Step("Выбрать тип виджета и перейти далее")
    public void selectWidgetType() {
        wait.until(ExpectedConditions.elementToBeClickable(widgetTypeChart)).click();
        driver.findElement(nextStepBtn).click();
    }

    @Step("Настроить фильтр и нажать Next")
    public void configureWidget() {

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'notificationItem')]")));

        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(firstFilterRadio));
        filter.click();

        WebElement nextBtn = wait.until(ExpectedConditions.presenceOfElementLocated(nextStepBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
    }

    @Step("Финальное сохранение виджета")
    public void finishWidgetCreation(String widgetName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Enter widget name']")
        ));

        input.clear();
        input.sendKeys(widgetName);
        input.sendKeys(Keys.TAB);

        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Add']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
    }

    @Step("Создать дашборд с именем '{name}'")
    public void createNewDashboard(String name) {

        String baseUrl = driver.getCurrentUrl().split("/ui/")[0];
        driver.get(baseUrl + "/ui/#default_personal/dashboard");

        WebElement addDashBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Add New Dashboard')]")
        ));
        addDashBtn.click();

        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'modal')]//input[@type='text']")
        ));

        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys(name);

        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'modal')]//button[text()='Add']")
        ));
        submitBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal')]")));
        wait.until(ExpectedConditions.urlContains("dashboard/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[contains(@class, 'breadcrumb')]//*[contains(text(), '" + name + "')]")
        ));
    }

    @Step("Создать виджет '{widgetName}' типа '{widgetType}'")
    public void addWidgetToCurrentDashboard(String widgetType, String widgetName) {

        WebElement addWidgetBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[.//span[contains(translate(text(), 'ADD NEW WIDGET', 'add new widget'), 'add new widget')]]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addWidgetBtn);

        WebElement typeCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'widget-type-item')]//div[text()='" + widgetType + "']")
        ));
        typeCard.click();
        clickNextStep();

        WebElement firstFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class, 'filter-item')]//span)[1] | (//span[contains(@class, 'filterName')])[1]")
        ));
        firstFilter.click();
        clickNextStep();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[contains(@placeholder, 'name')]")
        ));
        nameInput.clear();
        nameInput.sendKeys(widgetName);

        try {

            List<WebElement> closeButtons = driver.findElements(By.xpath("//div[contains(@class, 'notificationItem')]//div[contains(@class, 'close')]"));
            for (WebElement close : closeButtons) {
                if (close.isDisplayed()) close.click();
            }
            Thread.sleep(500);
        } catch (Exception ignored) {

        }
        By finalAddBtnLocator = By.xpath("//div[contains(@class, 'modal')]//button[contains(translate(., 'ADD', 'add'), 'add')]");
        WebElement finalAddBtn = wait.until(ExpectedConditions.presenceOfElementLocated(finalAddBtnLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", finalAddBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finalAddBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal-window')] | //div[contains(@class, 'modalLayout')]")));
    }

    private void clickNextStep() {
        WebElement nextBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'Next step')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);
        try { Thread.sleep(600); } catch (InterruptedException ignored) {}
    }

    @Step("Получить количество виджетов на дашборде")
    public int getWidgetsCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'notificationItem')]")
        ));

        try {
            By widgetLocator = By.xpath("//div[contains(@class, 'grid-stack-item')] | //div[contains(@class, 'widgets-grid')]//div[contains(@class, 'item')]");

            wait.until(ExpectedConditions.presenceOfElementLocated(widgetLocator));

            Thread.sleep(500);

            List<WebElement> widgets = driver.findElements(widgetLocator);
            return widgets.size();
        } catch (TimeoutException | InterruptedException e) {
            return 0;
        }
    }

    @Step("Удалить текущий дашборд через кнопку Delete")
    public void deleteCurrentDashboard() {

        WebElement deleteBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'Delete')] | //div[contains(@class, 'deleteDashboard')]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'modal')]//button[text()='Delete' or contains(., 'Delete')]")
        ));

        confirmBtn.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'modal')]")));
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

}
