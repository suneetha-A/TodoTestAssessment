package ToDoAssessment;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;





public class TodoTestCases {
	WebDriver driver;
	TodoRepository todoRepository;
	
	@BeforeMethod
    public void setUp() {
       
        driver = new ChromeDriver();
        driver.get("https://todomvc.com/examples/react/dist/");
         todoRepository = new TodoRepository(driver);
        
    }
	
	@AfterMethod
    public void tearDown() {
        driver.quit();
    }

   @Test
    public void emptyTodo() {
        //WebElement todoTask = driver.findElement(By.id("todo-input"));
	   todoRepository.todoTask.sendKeys(Keys.ENTER);
       
        // Verify the todo list with out adding Task
                
        boolean elementExists = driver.findElements(By.cssSelector(".todo-count")).size() > 0;

        // Assert that the element is not present on the page
        Assert.assertFalse(elementExists, "Element found when it should not be present!");
          
    
    }
    
    @Test
    public void addTodo() {
    	
    	todoRepository.addTodoElements();   
    	
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
    	// Then check the size of the Todo list
    	Assert.assertEquals(todoList.size(), 2);
    	
        // Verify that the new todo appears in the list
         Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "2 items left!");
         //The input field should be cleared after adding the task
         Assert.assertTrue(todoRepository.todoTask.getAttribute("value").isEmpty());

    }
    
    @Test
    public void VerifyDestroyButton() {
    	 todoRepository.addTodoElements();   
         
    	 Actions a = new Actions(driver);
    	 a.moveToElement(driver.findElement(By.cssSelector(".toggle"))).perform();
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         WebElement deleteFilter = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".destroy")));
                  
         deleteFilter.click();
            

        // Verify that the new todo appears in the list
         Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "1 item left!");

    }
    
    @Test
    public void verifyCompleted() {
    	 	 
    	todoRepository.addTodoElements();
    	driver.findElement(By.xpath("//li[1]//div[1]//input[1]")).click();
    	//Check the Todo list weather or not item selected
    	//System.out.println(driver.findElement(By.xpath("//li[1]")).getAttribute("class").contains("completed"));
    	Assert.assertTrue(driver.findElement(By.xpath("//li[1]")).getAttribute("class").contains("completed"), "Todo should be marked as completed.");
    	
    	//To check Completed Tag
    	
    	driver.findElement(By.linkText("Completed")).click();
    	//Check the Todo list weather or not item selected
    	Assert.assertTrue(driver.findElement(By.cssSelector("li[class='completed'] input[type='checkbox']")).isSelected());
    	//Check the todo-count message
    	Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "1 item left!");
    	
    
    }
       
    @Test
    
    public void verifyActive()
    {
    	todoRepository.addTodoElements();
         
         //Click the Checkbox beside of Task(to make task completed)
    	todoRepository.clickCheckBox();
  	
   
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //collect the active tasks before clicking Active link
        driver.findElement(By.linkText("All")).click();
        List<WebElement> todoActiveElementsofMainList = driver.findElements(By.cssSelector(".todo-list li:not(.completed)"));
       
       
        driver.findElement(By.linkText("Active")).click();
        //Check the todo-count message
        Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "1 item left!");
        List<WebElement> todoActiveElementsofActiveLink = driver.findElements(By.cssSelector(".todo-list li:not(.completed)"));
        
        //Compart both list sizes
        Assert.assertEquals(todoActiveElementsofMainList, todoActiveElementsofActiveLink, "Both lists are equal.");
       
    }
 
    @Test
    public void verifyMainPageAfterClearCompleted() {
    	todoRepository.addTodoElements();
    	todoRepository.clickCheckBox();
    	
    	driver.findElement(By.className("clear-completed")).click();
    	//Check the todo list (check for unchecked boxes)
    	
    	Assert.assertFalse(driver.findElement(By.xpath("//li[1]")).getAttribute("class").contains("completed"), "Element deleted from Todo list.");
    	
    
    }
    
    @Test
    public void verifyCompletedPageClearCompleted() {
    	todoRepository.addTodoElements();

    	todoRepository.clickCheckBox();
    	    	
    	driver.findElement(By.className("clear-completed")).click();
    	//Check the todo list (after clicking Completed )
    	
    	driver.findElement(By.linkText("Completed")).click();
    	List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
    	
    	Assert.assertEquals(todoList.size(), 0, "todo list is empty in Completed page");
    	
      
    }
    
    

    @Test
    public void verifySelectDownArrowButton() {
    	todoRepository.addTodoElements();
    	//Press downArrow button in text field
    	driver.findElement(By.className("toggle-all")).click();
    	//Check the todo-count message 	
    	Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "0 items left!");
    	
    
    }
    
    
    @Test
    public void verifySelectDownArrowButtonTwotimes() {
    	todoRepository.addTodoElements();
    	
    	//select and unselect items
    	driver.findElement(By.className("toggle-all")).click();
    	driver.findElement(By.className("toggle-all")).click();
    	//Check the todo-count message 	    	
    	Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='todo-count'])[1]")).getText(), "2 items left!", "All items Tallyed! ");
    	
    
    }
    
	@Test
    public void verifyAll() {
		todoRepository.addTodoElements();
         
         int beforesize = driver.findElements(By.cssSelector(".todo-list li")).size();
         
         driver.findElement(By.linkText("Completed")).click();
         driver.findElement(By.linkText("All")).click();
         
         int aftersize = driver.findElements(By.cssSelector(".todo-list li")).size();
         
         Assert.assertEquals(beforesize, aftersize);


         
    }
    @Test
    public void verifyEditInput() {
    	
    	String testTask = "testTask";
      	 
    	// Create an Actions object
         Actions actions = new Actions(driver);

         // Send text to the text box and press Enter
         actions.sendKeys(todoRepository.todoTask, "Test").doubleClick().sendKeys(testTask).sendKeys(Keys.ENTER).perform();
         Assert.assertEquals(driver.findElement(By.className("view")).getText(),testTask, "Successfull");
         
    }

}
