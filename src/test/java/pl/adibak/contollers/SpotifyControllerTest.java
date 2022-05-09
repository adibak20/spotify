package pl.adibak.contollers;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpotifyControllerTest {

    private static final String USERNAME = "adam+test@adambak.pl";

    private static final String CORRECT_PASSWORD = "zaq1@WSXcd";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        System.setProperty("webdriver.chrome.driver", "C:\\chrome\\chromedriver.exe");
    }

    @Test
    public void show_artists_list() throws Exception {
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
        driver.close();

        Map<String, String> queryMap = getParamsFromURL(callback.substring(callback.indexOf("?") + 1));
        this.mockMvc.perform(get("/callback/spotify?code=" + queryMap.get("code")))
                .andExpect(status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/spotify/main"));

        this.mockMvc.perform(get("/spotify/main"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.view().name("main"));
    }

    private static Map<String, String> getParamsFromURL(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

}
