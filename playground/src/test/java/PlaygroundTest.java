/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;


class PlaygroundTest {

    private static final String PLAYGROUND_URL = "https://finos.github.io/datahelix/playground/";
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void before() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 30);
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
    }

    @BeforeEach
    void beforeEach() {
        driver.get(PLAYGROUND_URL);

        wait.until(ExpectedConditions.attributeContains(By.id("output-panel"), "textContent", "Click Run"));
        driver.findElement(By.id("run")).click();
        wait.until(invisibilityOfElementLocated(By.id("spinner")));
    }

    @Test
    void clickRun_showsContentInOutputPanel() {
        boolean outputExists = driver.findElements(By.id("output")).size() > 0;
        assertTrue(outputExists);
    }

    @Test
    void clickRun_doesNotShowErrorMessage() {
        boolean errorExists = driver.findElements(By.id("error-popup")).size() > 0;
        assertFalse(errorExists);
    }
}