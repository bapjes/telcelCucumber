package com.Telcel;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;



import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class TelcelStepDefinitions {

    WebDriver driver;
    WebDriverWait wait;
    Celular phoneinfo;

    @Before
    public void loadBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("start-maximized");
        options.addArguments("incognito");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver,10);


    }

    @After
    public void closeBrowser() {
        driver.quit();
    }

    @Given("I am on telcel Page")
    public void i_am_on_telcel_Page() throws Exception {
        String url="https://www.telcel.com/";
        driver.get(url);
        isTelcelHomePage();
    }


    @When("I click on teléfonos y smart phones option")
    public void iClickOnTeléfonosYSmartPhonesOption() throws Exception {
        selectSmartPhones();
    }

    @And("a modal window is opened")
    public void aModalWindowIsOpened() throws Exception {

        By stateLocator = By.cssSelector(".chosen-single span");
        assertTrue("The modal window is failing", isElementpresent(stateLocator));

        By buttonLocator = By.cssSelector("#entrarPerfilador");
        assertTrue("The modal window is failing", isElementpresent(buttonLocator));

        System.out.println("The modal window is opened");
    }

    @Then("I could select {string}")
    public void iCouldSelect(String arg0) throws Exception {

        By dropboxlocator = By.cssSelector(".chosen-single span");
        assertTrue("Fails to select a state",clickonElement(dropboxlocator));

        By stateLocator = By.cssSelector(".chosen-search input");
        assertTrue("The state was not enter",enterText(stateLocator,arg0));

        By liststateLocator= By.cssSelector(".chosen-results li");
        assertTrue("Fails to select a state",clickonElement(liststateLocator));

        if (driver.findElement(liststateLocator).getText().length() >= 20) {
              assertNotEquals(driver.findElement(liststateLocator).getText(), "No hay coincidencias", driver.findElement(liststateLocator).getText().substring(0, 20));
        }
        By buttonLocator = By.cssSelector("#entrarPerfilador");
        assertTrue("Fails to select a state",clickonElement(buttonLocator));

        System.out.println("The state " + arg0 + " was selected");
    }

    @And("A list of cellphones is displayed")
    public void aListOfCellphonesIsDisplayed() throws Exception {
        By listlocator = By.cssSelector("[ng-repeat*='devices']");
        assertTrue("The list of phones is not displayed",isElementpresent(listlocator));
        System.out.println("There are " +  driver.findElements(listlocator).size() + " cellpones displayed");
    }

    @And("Select a {int} of the list")
    public void selectAPhoneOfTheList(int phone) throws Exception {

        By listlocator = By.cssSelector("[ng-repeat*='devices']");
        assertTrue("The list of phones is not displayed",isElementpresent(listlocator));

        List<WebElement> list_telephones = driver.findElements(listlocator);
        WebElement cellphone = list_telephones.get(phone);

        phoneinfo = saveInfo(cellphone);
        cellphone.click();

        System.out.println("Telephone selected was : " + phone);
    }


    @And("validate the cellphone selected")
    public void validateTheCellphoneSelected() throws Exception {
        By namelocator = By.cssSelector("header>#ecommerce-ficha-tecnica-nombre");
        isElementpresent(namelocator);

        By capacitylocator = By.cssSelector(".ecommerce-ficha-tecnica-opciones-compra-caracteristicas-etiqueta");
        isElementpresent(capacitylocator);

        By pricelocator = By.cssSelector(".ecommerce-ficha-tecnica-precio-pagos>#ecommerce-ficha-tecnica-precio-obj");
        isElementpresent(pricelocator);

        String namephone = driver.findElement(namelocator).getText().trim();
        String capacityphone = driver.findElement(capacitylocator).getText().trim();
        String pricephone = driver.findElement(pricelocator).getText().trim();

        int capac = Integer.parseInt(capacityphone.split(" ")[0]);
        double price = Double.parseDouble((pricephone.replace("$", "").replace(",", "")));

        if(phoneinfo.getNombre().equals(namephone)
                &&  phoneinfo.getPrecio() == price
                &&  phoneinfo.getCapacidad() == capac) {
            System.out.println("The information of the telephone selected is : ");
            System.out.println("Name : " + phoneinfo.getNombre());
            System.out.println("Price : " + phoneinfo.getPrecio());
            System.out.println("Capacity: " + phoneinfo.getCapacidad());
        } else {
            System.out.println("Information of the telephone is incorrect");
            throw new Exception("Information of the telephone is incorrect");
        }
      }


    public  Celular  saveInfo(WebElement element) throws Exception {
        try {

            By namelocator = By.cssSelector(".telcel-mosaico-equipos-nombre-equipo");
            isElementpresent(namelocator);

            By capacitylocator = By.cssSelector(".telcel-mosaico-equipos-capacidad-numero");
            isElementpresent(capacitylocator);

            By pricelocator = By.cssSelector(".telcel-mosaico-equipos-precio");
            isElementpresent(pricelocator);

            String namephone = element.findElement(namelocator).getText().trim();
            String capacityphone = element.findElement(capacitylocator).getText().trim();
            String pricephone = element.findElement(pricelocator).getText().trim();

            int capac = Integer.parseInt(capacityphone.split(" ")[0]);
            double price = Double.parseDouble((pricephone.replace("$", "").replace(",", "")));

            Celular cellphone = new Celular(namephone, capac, price);
            System.out.println("The information of the cell phone was saved");
            return cellphone;
        } catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Information of the cellphone was not saved");
            return null;
        }

    }


    public boolean enterText(By locator,String state) throws Exception {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            scrollToWebElement(driver.findElement(locator));
            driver.findElement(locator).clear();
            driver.findElement(locator).sendKeys(state);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }


    public boolean isElementpresent (By locator) throws Exception
    {
        try {
              wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
              return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }



    public void scrollToWebElement(WebElement webElement) throws Exception {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clickonElement(By locator) throws Exception
    {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            scrollToWebElement(driver.findElement(locator));
            driver.findElement(locator).click();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public void isTelcelHomePage() throws Exception {
        By locator = By.cssSelector("[title=\"Telcel\"]");
        assertTrue("You are not on telcel page",isElementpresent(locator));
        System.out.println("You are on telcel page");
    }

   public void selectSmartPhones() throws Exception {

        By tiendaenlineaLocator = By.cssSelector("[data-nombreboton='Tienda en linea superior']");
        clickonElement(tiendaenlineaLocator);

        By smartphonelocator = By.cssSelector("[data-nombreboton=\"Tienda en linea superior\"]+ul a[data-submenu=\"Telefonos y smartphones\"]");
        clickonElement(smartphonelocator);

        System.out.println("'Telefonos y Smart Phones' option was selected ");
    }





}
