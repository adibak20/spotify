package pl.adibak.contollers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    private static final String USERNAME = "adam+test@adambak.pl";

    private static final String CORRECT_PASSWORD = "zaq1@WSXcd";

    private static final String INCORRECT_PASSWORD = "bad_password";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        System.setProperty("webdriver.chrome.driver", "C:\\chrome\\chromedriver.exe");
    }

    @Test
    public void login_successful() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/login/spotify")).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String location = response.getHeader("Location");
        WebDriver driver = new ChromeDriver();
        driver.get(location);
        driver.findElement(By.id("login-username")).sendKeys(USERNAME);
        driver.findElement(By.id("login-password")).sendKeys(CORRECT_PASSWORD);
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(4000);
        driver.findElement(By.xpath("//button[@data-testid='auth-accept']")).click();
        Thread.sleep(5000);
        String callback = driver.getCurrentUrl();
        Assertions.assertTrue(callback.contains("code="));
        driver.close();
    }

    @Test
    public void login_failed() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/login/spotify")).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String location = response.getHeader("Location");
        WebDriver driver = new ChromeDriver();
        driver.get(location);
        driver.findElement(By.id("login-username")).sendKeys(USERNAME);
        driver.findElement(By.id("login-password")).sendKeys(INCORRECT_PASSWORD);
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
        String element = driver.findElement(By.cssSelector("span.Message-sc-15vkh7g-0")).getText();
        Assertions.assertEquals("Nieprawidłowa nazwa użytkownika lub błędne hasło.", element);
        driver.close();
    }

}
