package testcases;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import rest.ApiUtil;
import rest.CustomResponse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class RestAssured_TestCases {

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	int payGradeId;
	@SuppressWarnings("unused")
	private int userIdToDelete;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\testData.xlsx";
	FileOperations excel = new FileOperations();

	/**
	 * @BeforeClass method to perform login via Selenium and retrieve session cookie
	 *              for authenticated API calls.
	 * 
	 *              Steps: 1. Setup ChromeDriver using WebDriverManager. 2. Launch
	 *              browser and open the OrangeHRM login page. 3. Perform login with
	 *              provided username and password. 4. Wait for login to complete
	 *              and extract the 'orangehrm' session cookie. 5. Store the cookie
	 *              value to be used in API requests. 6. Quit the browser session.
	 * 
	 *              Throws: - InterruptedException if thread sleep is interrupted. -
	 *              RuntimeException if the required session cookie is not found.
	 */

	@Test(priority = 0, groups = {
			"PL2" }, description = "Login to OrangeHRM using Selenium and retrieve session cookie")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(6000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
		
		 RestAssured.useRelaxedHTTPSValidation();
	}

	@Test(priority = 1, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/subunits?mode=tree' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate the request uses proper Rest Assured methods (given, cookie, get, response)\n"
					+ "4. Validate that the response contains non-empty 'id' and 'name' fields in the 'data' list\n"
					+ "5. Validate response status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full response body for verification")

	public void getSubunitsTree() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/subunits?mode=tree";

		CustomResponse customResponse = apiUtil.getSubunitsTree(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getSubunitsTree",
				List.of("given", "cookie", "get", "response"));
		List<Object> itemIds = customResponse.getIds();
		List<Object> names = customResponse.getNames();
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getSubunitsTree", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		System.out.println("getSubunitsTree API Response:");
		customResponse.getResponse().prettyPrint();
	}

//
	@Test(priority = 2, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/skills?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Validate response status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the complete API response body for verification")

	public void getAdminSkills() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/skills?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminSkills(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminSkills",
				List.of("given", "cookie", "get", "response"));
		// response, statusCode, status, ids, names, descriptions
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminSkills", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		System.out.println("getAdminSkills API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 3, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/educations?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify that the status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body to the console for verification")

	public void getAdminEdu() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/educations?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminEdu(endpoint, cookieValue, null);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminEdu",
				List.of("given", "cookie", "get", "response"));
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminEdu", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		System.out.println("getAdminEdu API Response:");
		customResponse.getResponse().prettyPrint();

	}

	@Test(priority = 4, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/licenses?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body for verification")

	public void getAdminLicenses() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/licenses?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminLicenses(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminLicenses",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatus());
		Assert.assertTrue(isImplementationCorrect,
				"getAdminLicenses must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminEdu", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		System.out.println("getAdminEdu API Response:");
		customResponse.getResponse().prettyPrint();

	}


	@Test(priority = 5, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/languages?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body for verification")

	public void getAdminLanguages() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/languages?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminLanguages(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminLanguages",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatus());
		Assert.assertTrue(isImplementationCorrect,
				"getAdminLanguages must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"getAdminLanguages must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminLanguages", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		System.out.println("getAdminLanguages API Response:");
		customResponse.getResponse().prettyPrint();

	}


	@Test(priority = 6, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/memberships?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.0 200 OK'\n"
					+ "6. Print the full API response body for verification")
		public void getMembershipdetails() throws IOException{
			String endpoint = "/web/index.php/api/v2/admin/memberships?limit=50&offset=0";
			CustomResponse customResponse=apiUtil.getMembershipdetails(endpoint, cookieValue);


			boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getMembershipdetails",
				List.of("given", "cookie", "get", "response"));

			
			System.out.println("Status Code: " + customResponse.getStatus());
			Assert.assertTrue(isImplementationCorrect,
					"getAdminLanguages must be implementated using the Rest assured  methods only!");
			assertEquals(customResponse.getStatusCode(), 200);


			Assert.assertNotNull(customResponse.getIds(), " ID list is null");
			Assert.assertFalse(customResponse.getIds().isEmpty(), " ID list is empty");

			Assert.assertNotNull(customResponse.getNames(), " Name list is null");
			Assert.assertFalse(customResponse.getNames().isEmpty(), " Name list is empty");

			Assert.assertEquals(customResponse.getStatusCode(), 200, " Status code is not 200");
			Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", " Status line mismatch");			

		}


		@Test(priority = 7, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/nationalities?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.0 200 OK'\n"
					+ "6. Print the full API response body for verification")
		public void getNationalities() throws IOException{
			String endpoint = "/web/index.php/api/v2/admin/nationalities?limit=50&offset=0";
			CustomResponse customResponse=apiUtil.getNationalities(endpoint, cookieValue);


			boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getMembershipdetails",
				List.of("given", "cookie", "get", "response"));

			
			System.out.println("Status Code: " + customResponse.getStatus());
			Assert.assertTrue(isImplementationCorrect,
					"getAdminLanguages must be implementated using the Rest assured  methods only!");
			assertEquals(customResponse.getStatusCode(), 200);


			Assert.assertNotNull(customResponse.getIds(), " ID list is null");
			Assert.assertFalse(customResponse.getIds().isEmpty(), " ID list is empty");

			Assert.assertNotNull(customResponse.getNames(), " Name list is null");
			Assert.assertFalse(customResponse.getNames().isEmpty(), " Name list is empty");

			Assert.assertEquals(customResponse.getStatusCode(), 200, " Status code is not 200");
			Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", " Status line mismatch");		
			
			Object total = customResponse.getMeta().get("total");
			Assert.assertTrue(total instanceof Integer, "'total' in meta is not an Integer");
			Assert.assertTrue((Integer) total > 0, "Total value in meta is not greater than 0");

		}

		@Test(priority = 8, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/theme' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Verify the status code is 200 and the status line is 'HTTP/1.0 200 OK'\n"
					+ "5. Verify the PrimaryColor,PrimaryFontColor,Secondary color is not null")
		public void getTheme() throws IOException{
			String endpoint = "/web/index.php/api/v2/admin/theme";
			CustomResponse customResponse=apiUtil.getTheme(endpoint, cookieValue);


			boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getMembershipdetails",
				List.of("given", "cookie", "get", "response"));

			System.out.println("Status Code: " + customResponse.getStatus());
			Assert.assertTrue(isImplementationCorrect,
					"getAdminLanguages must be implementated using the Rest assured  methods only!");
			Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
			Assert.assertTrue(customResponse.getStatus().contains("OK"), "Status line should contain 'OK'");

			Assert.assertNotNull(customResponse.getPrimaryColor(), "Primary color should not be null");
			Assert.assertFalse(customResponse.getPrimaryColor().isEmpty(), "Primary color should not be empty");

			Assert.assertNotNull(customResponse.getPrimaryFontColor(), "Primary font color should not be null");
			Assert.assertFalse(customResponse.getPrimaryFontColor().isEmpty(), "Primary font color should not be empty");

			Assert.assertNotNull(customResponse.getSecondaryColor(), "Secondary color should not be null");
			Assert.assertFalse(customResponse.getSecondaryColor().isEmpty(), "Secondary color should not be empty");
			
		}

		@Test(priority = 9, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
    "PL2" }, description = "1. Send a GET request to the 'web/index.php/api/v2/admin/email-configuration' endpoint with a valid cookie\n"
            + "2. Do not pass any request body (null)\n"
            + "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
            + "4. Assert that the 'data' array in the response contains non-empty 'mailType', 'sentAs', 'smtpAuthType', 'smtpHost', 'smtpUsername'\n"
            + "5. Verify the status code is 200 and the status line is 'HTTP/1.0 200 OK'\n")
public void getemailConfiguration() throws IOException {
    String endpoint = "/web/index.php/api/v2/admin/email-configuration";
    CustomResponse customResponse = apiUtil.getemailConfiguration(endpoint, cookieValue);

    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "getemailConfiguration",
            List.of("given", "cookie", "get", "response"));

	Assert.assertTrue(isImplementationCorrect,
					"getemailConfiguration must be implementated using the Rest assured  methods only!");

    // ✅ Assertions
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
    Assert.assertTrue(isImplementationCorrect, "Implementation should use given, cookie, get, response");

    Assert.assertNotNull(customResponse.getMailType(), "'mailType' should not be null");
    Assert.assertFalse(customResponse.getMailType().isEmpty(), "'mailType' should not be empty");

    Assert.assertNotNull(customResponse.getSentAs(), "'sentAs' should not be null");
    Assert.assertFalse(customResponse.getSentAs().isEmpty(), "'sentAs' should not be empty");

    Assert.assertNotNull(customResponse.getSmtpAuthType(), "'smtpAuthType' should not be null");
    Assert.assertFalse(customResponse.getSmtpAuthType().isEmpty(), "'smtpAuthType' should not be empty");

    Assert.assertNotNull(customResponse.getSmtpHost(), "'smtpHost' should not be null");
    Assert.assertFalse(customResponse.getSmtpHost().isEmpty(), "'smtpHost' should not be empty");

    Assert.assertNotNull(customResponse.getSmtpUsername(), "'smtpUsername' should not be null");
    Assert.assertFalse(customResponse.getSmtpUsername().isEmpty(), "'smtpUsername' should not be empty");

}

@Test(priority = 10, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
    "PL2" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/email-subscriptions?limit=50&offset=0' endpoint with a valid cookie\n"
            + "2. Do not pass any request body (null)\n"
            + "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
            + "4. Assert that the 'data' array in the response contains non-empty 'id', 'name', 'isEnable'is boolean,meta is not empty\n"
            + "5. Verify the status code is 200 and the status line is 'HTTP/1.0 200 OK'\n")
public void getEmailSubscription() throws IOException {
	String endpoint = "/web/index.php/api/v2/admin/email-subscriptions?limit=50&offset=0";
    CustomResponse customResponse = apiUtil.getEmailSubscription(endpoint, cookieValue);

	boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "getEmailSubscription",
            List.of("given", "cookie", "get", "response"));

	Assert.assertTrue(isImplementationCorrect,
					"getEmailSubscription must be implementated using the Rest assured  methods only!");

	// ✅ Status checks
    Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");
    Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status line should be HTTP/1.0 200 OK");

    // ✅ Data array checks
    List<Integer> idList = customResponse.getIdList();
    List<String> nameList = customResponse.getNameList();
    List<Boolean> isEnabledList = customResponse.getIsEnabledList();

    Assert.assertNotNull(idList, "'id' list should not be null");
    Assert.assertFalse(idList.isEmpty(), "'id' list should not be empty");
    idList.forEach(id -> Assert.assertNotNull(id, "Each 'id' should not be null"));

    Assert.assertNotNull(nameList, "'name' list should not be null");
    Assert.assertFalse(nameList.isEmpty(), "'name' list should not be empty");
    nameList.forEach(name -> Assert.assertFalse(name.trim().isEmpty(), "Each 'name' should not be empty"));

    Assert.assertNotNull(isEnabledList, "'isEnabled' list should not be null");
    Assert.assertFalse(isEnabledList.isEmpty(), "'isEnabled' list should not be empty");
    isEnabledList.forEach(flag -> Assert.assertTrue(flag instanceof Boolean, "'isEnabled' should be boolean"));

    // ✅ Meta check
    Assert.assertNotNull(customResponse.getMetaMap(), "'meta' map should not be null");
    Assert.assertFalse(customResponse.getMetaMap().isEmpty(), "'meta' map should not be empty");

}
	

@Test(priority = 11, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = { "PL2" }, 
    description = "1. Create a requestbody as \n"
        + "2. Fetch the ID using getFirstUserId and pass it in request body '{\"ids\": [%d]}'\n"
        + "3. Post the String using '/web/index.php/api/v2/buzz/posts'\n"
        + "4. Verify the user is deleted by comparing IDs before and after")
public void PostCaption() throws IOException {
    // 1. Create a request body
    String randomText = UUID.randomUUID().toString().substring(0, 8);

    String requestBody = "{"
		+ "\"text\": \"" + randomText + "\","
        + "\"type\": \"text\""
        + "}";

    String endpoint = "/web/index.php/api/v2/buzz/posts";

    CustomResponse customResponse = apiUtil.PostCaption(endpoint, cookieValue, requestBody);

    // Validate that implementation is correct
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "PostCaption",
            List.of("given", "cookie", "post", "response"));

    Assert.assertTrue(isImplementationCorrect, "Implementation should use the correct Rest Assured methods");

    // Fetch actual status line
    String actualStatusLine = customResponse.getStatusLine();

    System.out.println("Actual Status Line from server: " + actualStatusLine);

    // Assert protocol-independent 200 OK
    Assert.assertTrue(
        actualStatusLine != null && actualStatusLine.contains("200 OK"),
        "Expected HTTP 200 OK but got: " + actualStatusLine
    );

    // Parse JSON from the raw response
    JsonPath jsonPath = new JsonPath(customResponse.getRawResponse());

    // Assert non-null values from the response
    Assert.assertNotNull(jsonPath.get("data.post.id"), "Post ID should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.empNumber"), "Employee Number should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.lastName"), "Last Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.firstName"), "First Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.employeeId"), "Employee ID should not be null");
}



@Test(priority = 12, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = { "PL2" },
description = "1. Upload a photo via JSON payload with base64\n"
    + "2. Call '/web/index.php/api/v2/buzz/posts' with type 'photo'\n"
    + "3. Verify status code and response fields")
public void PostPhoto() throws IOException {
    // 1. Load a test image from resources
    File testImage = new File("src/main/resources/433736.jpg"); 
    Assert.assertTrue(testImage.exists(), "Test image file not found at: " + testImage.getAbsolutePath());

    String endpoint = "/web/index.php/api/v2/buzz/posts";

    // 2. Call API
    CustomResponse customResponse = apiUtil.PostPhoto(endpoint, cookieValue, testImage, "Automation Test Photo Post");

    // 3. Assert status
    String actualStatusLine = customResponse.getStatusLine();
    System.out.println("Actual Status Line from server: " + actualStatusLine);
    Assert.assertTrue(
        actualStatusLine != null && actualStatusLine.contains("200 OK"),
        "Expected HTTP 200 OK but got: " + actualStatusLine
    );

    // 4. Parse JSON
    JsonPath jsonPath = new JsonPath(customResponse.getRawResponse());
	System.out.println(jsonPath);
    Assert.assertNotNull(jsonPath.get("data.post.id"), "Post ID should not be null");
    //Assert.assertEquals(jsonPath.get("data.post.type"), "photo", "Post type should be 'photo'");
    //Assert.assertNotNull(jsonPath.get("data.post.photo"), "Photo data should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.empNumber"), "Employee Number should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.lastName"), "Last Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.firstName"), "First Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.employee.employeeId"), "Employee ID should not be null");
}

@Test(priority = 13, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = { "PL2" },
description = "1. Call '/web/index.php/api/v2/buzz/shares/19/likes' to like a share\n"
    + "2. Verify status code and response fields")
public void LikeShare() throws IOException {
	
	Response res = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)  
            .when()
            .get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/buzz/feed");
	
	int id = res.jsonPath().getInt("data[0].id");
    System.out.println(res.jsonPath().getInt("data[0].id"));

    String endpoint = "/web/index.php/api/v2/buzz/shares/" + id + "/likes";

    // 1. Call API via apiUtil method for liking a share
    CustomResponse customResponse = apiUtil.GetLike(endpoint, cookieValue);

    // 2. Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "GetLike",
            List.of("given", "cookie", "get", "response"));

    Assert.assertTrue(isImplementationCorrect, 
        "Implementation should use correct Rest Assured methods for POST like request");

    // 3. Assert status
    String actualStatusLine = customResponse.getStatusLine();
    System.out.println("Actual Status Line from server: " + actualStatusLine);
    Assert.assertTrue(
        actualStatusLine != null && actualStatusLine.contains("200 OK"),
        "Expected HTTP 200 OK but got: " + actualStatusLine
    );

    // 4. Parse JSON response
//    JsonPath jsonPath = new JsonPath(customResponse.getRawResponse());
    JsonPath jsonPath = new JsonPath(customResponse.getResponse().asString());
    
    System.out.println("This is the raw response: " + customResponse.getRawResponse());

    Assert.assertNotNull(jsonPath.get("data.id"), "Like ID should not be null");
    Assert.assertNotNull(jsonPath.get("data.shareId"), "Share ID should not be null");
    Assert.assertNotNull(jsonPath.get("data.likedBy.empNumber"), "Employee Number should not be null");
    Assert.assertNotNull(jsonPath.get("data.likedBy.lastName"), "Last Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.likedBy.firstName"), "First Name should not be null");
    Assert.assertNotNull(jsonPath.get("data.likedBy.employeeId"), "Employee ID should not be null");
}





@Test(priority = 14, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = { "PL2" },
description = "1. Call '/web/index.php/api/v2/buzz/shares/{id}' with DELETE method\n"
    + "2. Verify correct Rest Assured delete implementation\n"
    + "3. Verify status code and response fields")
public void DeleteShare() throws IOException {
	
	Response res = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)  
            .when()
            .get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/buzz/feed");
	
	int id = res.jsonPath().getInt("data[0].id");
    System.out.println(res.jsonPath().getInt("data[0].id"));
    String endpoint = "/web/index.php/api/v2/buzz/shares/"+ id +" ";

    // 1. Call API via apiUtil method for delete
    CustomResponse customResponse = apiUtil.DeleteShare(endpoint, cookieValue);

    // 2. Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "DeleteShare",
            List.of("given", "cookie", "delete", "response"));

    Assert.assertTrue(isImplementationCorrect, "Implementation should use correct Rest Assured delete methods");
 
    // 3. Assert status
    String actualStatusLine = customResponse.getStatusLine();
    System.out.println("Actual Status Line from server: " + actualStatusLine);
    Assert.assertTrue(
        actualStatusLine != null && actualStatusLine.contains("200 OK"),
        "Expected HTTP 200 OK but got: " + actualStatusLine
    );


}

@Test(priority = 15, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = { "PL2" },
description = "1. Call '/web/index.php/api/v2/buzz/posts/{id}?model=detailed' with PUT method\n"
    + "2. Verify correct Rest Assured PUT implementation\n"
    + "3. Verify status code and updated text in response")
public void UpdateBuzzPost() throws IOException {
	Response res = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)  
            .when()
            .get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/buzz/feed?limit=10&offset=0&sortOrder=DESC&sortField=share.createdAtUtc");
	
	int postId = res.jsonPath().getInt("data[0].post.id");

	System.out.println("Post ID: " + postId);
	String endpoint = "/web/index.php/api/v2/buzz/posts/"+postId +"?model=detailed";

    String requestBody = "{\n" +
            "    \"text\": \"Updated post content via automation\",\n" +
            "    \"type\": \"text\",\n" +
			"    \"deletedPhotos\": []\n" +
            "}";

    // 1. Call API via apiUtil method for PUT
    CustomResponse customResponse = apiUtil.updatePost(endpoint, cookieValue, requestBody);

    // 2. Validate implementation
    boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(
            apiUtilPath, "updatePost",
            List.of("given", "cookie", "body", "put", "response"));

    Assert.assertTrue(isImplementationCorrect, "Implementation should use correct Rest Assured PUT methods");

    // 3. Assert status
    String actualStatusLine = customResponse.getStatusLine();
    System.out.println("Actual Status Line from server: " + actualStatusLine);
    Assert.assertTrue(
        actualStatusLine != null && actualStatusLine.contains("200 OK"),
        "Expected HTTP 200 OK but got: " + actualStatusLine
    );

    // 4. Parse JSON response and validate updated text
    JsonPath jsonPath = new JsonPath(customResponse.getResponse().asString());
    String updatedText = jsonPath.getString("data.text");
    Assert.assertNotNull(updatedText, "'text' field should not be null");
    Assert.assertEquals(updatedText, "Updated post content via automation", "Post text not updated correctly");
}

















	/*----------------------------------------Helper Functions-----------------------------------------*/

	public Response getUsersID(String endpoint, String cookieValue, Map<String, String> body) {
		baseUrl = apiUtil.getBaseUrl();
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}
	
	public int getFirstUserId(String cookieValue) {
	    String baseUrl = "https://yakshahrm.makemylabs.in/orangehrm-5.7";

	    RestAssured.useRelaxedHTTPSValidation(); // Ignore SSL issues

	    Response response = RestAssured
	        .given()
	        .cookie("orangehrm", cookieValue) // Auth cookie
	        .get(baseUrl + "/web/index.php/api/v2/admin/users?limit=50&offset=0&sortField=u.userName&sortOrder=ASC")
	        .then()
	        .extract()
	        .response();

	    // Log full response for debugging
	    System.out.println("Response Code: " + response.getStatusCode());
	    System.out.println("Response Body: " + response.asString());

	    // Parse JSON and get first ID
	    JsonPath jsonPath = response.jsonPath();
	    int firstId = jsonPath.getInt("data[1].id");

	    System.out.println("First User ID: " + firstId);
	    return firstId;
	}
	

	public void createUserWithUniquePassword() {

	    // Generate a unique password using your method
	    String uniquePassword = "Admin@" + generateUniqueName(8);

	    // Create the request body
	    String requestBody = "{ \"password\": \"" + uniquePassword + "\" }";

	    // Send POST request
	    RestAssured.useRelaxedHTTPSValidation(); // Ignore SSL for now

	    Response response = RestAssured.given()
	        .header("Content-Type", "application/json")
	        .body(requestBody)
	        .when()
	        .post(baseUrl + "/web/index.php/api/v2/auth/public/validation/password")
	        .then()
	        .extract().response();

	    // Print the response for verification
	    System.out.println("Request Body: " + requestBody);
	    System.out.println("Response Code: " + response.getStatusCode());
	    System.out.println("Response Body: " + response.asString());
	}
	
	public String generateUniqueName(int length) {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder sb = new StringBuilder();
	    java.util.Random random = new java.util.Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    return sb.toString();
	}

	public void createUser(String cookieValue) {
        // Generate random username
        String randomUsername = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // Prepare request body
        String requestBody = String.format(
            "{\"username\": \"%s\", \"password\": \"Admin@1234\", \"status\": true, \"userRoleId\": 1, \"empNumber\": 1}",
            randomUsername
        );

        // Send POST request
        Response response = RestAssured.given()
                .relaxedHTTPSValidation()
                .cookie("orangehrm", cookieValue)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseUrl + "/web/index.php/api/v2/admin/users");

	}

	

	
}
