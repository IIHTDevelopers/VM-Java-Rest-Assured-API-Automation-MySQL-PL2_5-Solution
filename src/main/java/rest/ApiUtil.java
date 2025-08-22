package rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.nio.file.Files;
import java.util.Base64;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Collections;
import io.restassured.http.ContentType;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	public static int generateUniqueID(int base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	
	
	
	public CustomResponse getSubunitsTree(String endpoint, String cookieValue, Map<String, String> body) {

	    RequestSpecification request = RestAssured.given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> ids = new ArrayList<>();
	    List<Object> unitIds = new ArrayList<>();
	    List<Object> names = new ArrayList<>();
	    List<Object> levels = new ArrayList<>();
	    List<Object> children = new ArrayList<>();

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> data = jsonPath.getList("data");

	    if (data != null) {
	        for (Map<String, Object> unit : data) {
	            ids.add(unit.get("id"));
	            unitIds.add(unit.get("unitId"));
	            names.add(unit.get("name"));
	            levels.add(unit.get("level"));
	            children.add(unit.get("children")); // This can be nested list
	        }
	    } else {
	        System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, ids, unitIds, names, levels, children);
	}

	
	public CustomResponse getAdminSkills(String endpoint, String cookieValue, Map<String, String> body) {

	    RequestSpecification request = RestAssured.given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> ids = new ArrayList<>();
	    List<Object> names = new ArrayList<>();
	    List<Object> descriptions = new ArrayList<>();

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> data = jsonPath.getList("data");

	    if (data != null) {
	        for (Map<String, Object> skill : data) {
	            ids.add(skill.get("id"));
	            names.add(skill.get("name"));
	            descriptions.add(skill.get("description"));
	        }
	    } else {
	        System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, ids, names, descriptions);
	}
	
	
	public CustomResponse getAdminEdu(String endpoint, String cookieValue, Map<String, String> body) {

	    RequestSpecification request = RestAssured.given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> ids = new ArrayList<>();
	    List<Object> names = new ArrayList<>();

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> data = jsonPath.getList("data");

	    if (data != null) {
	        for (Map<String, Object> item : data) {
	            ids.add(item.get("id"));
	            names.add(item.get("name"));
	        }
	    } else {
	        System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, ids, names);
	}
	
	
	
		public CustomResponse getAdminLanguages(String endpoint, String cookieValue, Map<String, String> body) {

			RequestSpecification request = RestAssured.given()
				.cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json");

			if (body != null) {
				request.body(body);
			}

			Response response = request.get(BASE_URL + endpoint).then().extract().response();

			int statusCode = response.getStatusCode();
			String status = response.getStatusLine();

			List<Object> ids = new ArrayList<>();
			List<Object> names = new ArrayList<>();

			JsonPath jsonPath = response.jsonPath();
			List<Map<String, Object>> data = jsonPath.getList("data");

			if (data != null) {
				for (Map<String, Object> item : data) {
					ids.add(item.get("id"));
					names.add(item.get("name"));
				}
			} else {
				System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
			}

			return new CustomResponse(response, statusCode, status, ids, names);
		}


	public CustomResponse getAdminLicenses(String endpoint, String cookieValue, Map<String, String> body) {

	    RequestSpecification request = RestAssured.given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> ids = new ArrayList<>();
	    List<Object> names = new ArrayList<>();

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> data = jsonPath.getList("data");

	    if (data != null) {
	        for (Map<String, Object> item : data) {
	            ids.add(item.get("id"));
	            names.add(item.get("name"));
	        }
	    } else {
	        System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, ids, names);
	}
	
	public CustomResponse getAdminLicense(String endpoint, String cookieValue, Map<String, String> body) {

	    RequestSpecification request = RestAssured.given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> ids = new ArrayList<>();
	    List<Object> names = new ArrayList<>();

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> data = jsonPath.getList("data");

	    if (data != null) {
	        for (Map<String, Object> item : data) {
	            ids.add(item.get("id"));
	            names.add(item.get("name"));
	        }
	    } else {
	        System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, ids, names);
	}

	public CustomResponse GetUsageReport(String endpoint, String cookieValue, Map<String, String> body) {
	    RequestSpecification request = RestAssured
	            .given()
	            .cookie("orangehrm", cookieValue)
	            .header("Content-Type", "application/json");

	    if (body != null) {
	        request.body(body);
	    }

	    Response response = request.get(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    List<Object> names = new ArrayList<>();
	    List<Object> props = new ArrayList<>();
	    List<Object> sizes = new ArrayList<>();
	    List<Object> pins = new ArrayList<>();
	    List<Object> cellProperties = new ArrayList<>(); // Keep it List<Object> for flexibility in CustomResponse

	    JsonPath jsonPath = response.jsonPath();
	    List<Map<String, Object>> headers = jsonPath.getList("data.headers");

	    if (headers != null) {
	        for (Map<String, Object> header : headers) {
	            names.add(header.get("name"));
	            props.add(header.get("prop"));
	            sizes.add(header.get("size"));
	            pins.add(header.get("pin"));

	            Object cellProp = header.get("cellProperties");
	            if (cellProp instanceof Map || cellProp == null) {
	                cellProperties.add(cellProp); // add map or null as-is
	            } else {
	                System.out.println("⚠️ Unexpected type for cellProperties: " + cellProp.getClass().getSimpleName());
	                cellProperties.add(null);
	            }
	        }
	    } else {
	        System.out.println("❌ 'data.headers' is missing or empty in the response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, props, names, sizes, pins, cellProperties);
	}



	public CustomResponse PutVimEmp(String endpoint, String cookieValue, String requestBody) {
	    RequestSpecification request = RestAssured
	            .given()
	            .cookie("orangehrm", cookieValue)
	            .header("Content-Type", "application/json");

	    if (requestBody != null) {
	        request.body(requestBody);
	    }

	    Response response = request.put(BASE_URL + endpoint).then().extract().response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    JsonPath jsonPath = response.jsonPath();
	    Map<String, Object> data = jsonPath.getMap("data");

	    List<Object> id = null;
	    List<Object> name = null;
	    List<Object> lastName = null;

	    if (data != null) {
	        id.add(data.get("empNumber"));
	        name.add(data.get("firstName"));
	        lastName.add(data.get("lastName"));
	    } else {
	        System.out.println("'data' object is missing or empty in the response. Status code: " + statusCode);
	    }

	    return new CustomResponse(response, statusCode, status, id, name, lastName);
	}

	


	 public CustomResponse getMembershipdetails(String endpoint, String cookieValue) {

    Response response = RestAssured.given()
            .cookie("orangehrm", cookieValue) // Replace with your method to get the cookie
            .get(BASE_URL + endpoint)
            .then()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = response.getStatusLine();

    JsonPath jsonPath = response.jsonPath();
    List<Object> ids = jsonPath.getList("data.id");
    List<Object> names = jsonPath.getList("data.name");

    // Optional: Print full response for verification
    System.out.println("📥 Full API Response:\n" + response.asPrettyString());

    return new CustomResponse(response, statusCode, status, ids, names);
}

public CustomResponse getNationalities(String endpoint,String cookieValue) {


    Response response = RestAssured.given()
            .cookie("orangehrm", cookieValue) // Replace with your cookie retrieval logic
            .get(BASE_URL + endpoint)
            .then()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = response.getStatusLine();

    JsonPath jsonPath = response.jsonPath();

    List<Object> ids = jsonPath.getList("data.id");
    List<Object> names = jsonPath.getList("data.name");
    Map<String, Object> meta = jsonPath.getMap("meta");

    System.out.println("Full API Response:\n" + response.asPrettyString());

    return new CustomResponse(response, statusCode, status, ids, names, meta);
}

public CustomResponse getTheme(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .get(BASE_URL + endpoint);

    System.out.println("Status Code: " + response.getStatusCode());
    System.out.println("Status Line: " + response.getStatusLine());
    System.out.println("Raw Response Body: " + response.asString());

    // Check content type
    String contentType = response.getContentType();
    if (contentType == null || !contentType.contains("application/json")) {
        throw new IllegalStateException("Expected JSON response but got: " + contentType);
    }

    JsonPath jsonPath = response.jsonPath();
    Map<String, Object> variables = jsonPath.getMap("data.variables");

    String primaryColor = variables != null ? (String) variables.get("primaryColor") : null;
    String primaryFontColor = variables != null ? (String) variables.get("primaryFontColor") : null;
    String secondaryColor = variables != null ? (String) variables.get("secondaryColor") : null;

    return new CustomResponse(response.getStatusLine(), response.getStatusCode(),
                              primaryColor, primaryFontColor, secondaryColor);
}

public CustomResponse getemailConfiguration(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .when()
            .get(BASE_URL + endpoint)
            .then()
            .extract()
            .response(); // ✅ Now "response" exists in code

    int statusCode = response.getStatusCode();
    String status = response.getStatusLine();
    JsonPath jsonPath = response.jsonPath();
    String mailType = jsonPath.getString("data.mailType");
    String sentAs = jsonPath.getString("data.sentAs");
    String smtpAuthType = jsonPath.getString("data.smtpAuthType");
    String smtpHost = jsonPath.getString("data.smtpHost");
    String smtpUsername = jsonPath.getString("data.smtpUsername");

    return new CustomResponse(status, statusCode, mailType, sentAs, smtpAuthType, smtpHost, smtpUsername);
}

public CustomResponse getEmailSubscription(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .when()
            .get(BASE_URL + endpoint)
            .then()
            .extract()
            .response(); // ✅ ensures "response" keyword is present for validator

    int statusCode = response.getStatusCode();
    String status = response.getStatusLine();

    JsonPath jsonPath = response.jsonPath();

    // Extract data array
    List<Integer> idList = jsonPath.getList("data.id");
    List<String> nameList = jsonPath.getList("data.name");
    List<Boolean> isEnabledList = jsonPath.getList("data.isEnabled");

    // Extract meta
    Map<String, Object> metaMap = jsonPath.getMap("meta");

    return new CustomResponse(status, statusCode, idList, nameList, isEnabledList, metaMap);
}

public CustomResponse deleteAdminUser(String endpoint, String cookieValue, int userId, String requestBody) {
    

    Response response = RestAssured.given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .delete(BASE_URL + endpoint);

    return new CustomResponse(response, response.getStatusCode(), response.getStatusLine());
}

public CustomResponse PostCaption(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured
    .given()
    .relaxedHTTPSValidation()
    .cookie("orangehrm", cookieValue)
    .header("Content-Type", "application/json")
    .body(requestBody)
    .when()
    .post(BASE_URL + endpoint)
    .then()
    .extract()
    .response();
    
    int statusCode = response.getStatusCode();
    String statusLine = response.getStatusLine(); // Should now be non-null

    JsonPath jsonPath = response.jsonPath();
    int postId = jsonPath.getInt("data.post.id");
    Map<String, Object> employeeDetails = jsonPath.getMap("data.employee");

    return new CustomResponse(statusCode, statusLine, postId, employeeDetails, response.asString());
}

public CustomResponse PostPhoto(String endpoint, String cookieValue, File photoFile, String caption) throws IOException {
    // Read file into Base64
    byte[] fileBytes = Files.readAllBytes(photoFile.toPath());
    String base64Image = Base64.getEncoder().encodeToString(fileBytes);

    // Detect MIME type
    String mimeType = Files.probeContentType(photoFile.toPath());
    if (mimeType == null) {
        mimeType = "image/jpeg"; // fallback
    }

    // Build payload
    Map<String, Object> photoMeta = new HashMap<>();
    photoMeta.put("name", photoFile.getName());
    photoMeta.put("type", mimeType);
    photoMeta.put("size", photoFile.length());
    photoMeta.put("base64", base64Image);

    Map<String, Object> payload = new HashMap<>();
    payload.put("type", "photo");
    payload.put("text", caption);
    payload.put("photos", Collections.singletonList(photoMeta));

    // Send request
    Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .cookie("orangehrm", cookieValue)
            .contentType(ContentType.JSON)
            .body(payload)
			.log().all()
            .post(BASE_URL + endpoint)
            .then()
            .extract()
            .response();
	System.out.println("Raw Response: " + response.asString());
    System.out.println("Status: " + response.getStatusLine());
    System.out.println("Raw Response: " + response.asString());

    if (response.getStatusCode() != 200) {
        throw new IllegalStateException("Upload failed: " + response.asString());
    }

    return new CustomResponse(
        response.getStatusCode(),
        response.getStatusLine(),
        response.jsonPath().getString("data.post.id"),
        response.jsonPath().getMap("data.employee"),
        response.asString()
    );
}


public CustomResponse GetLike(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
    .relaxedHTTPSValidation()
    .cookie("orangehrm", cookieValue)
    .header("Content-Type", "application/json")
    .body("{}")
    .when()
    .get(BASE_URL + endpoint)
    .then()
    .extract()
    .response();


    // Pass actual status code and status line from the HTTP response
    return new CustomResponse(
        response,
        response.getStatusCode(),
        response.getStatusLine()
    );
}



public CustomResponse DeleteShare(String endpoint, String cookieValue) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
        .when()
            .delete(endpoint)
        .then()
            .extract().response();

    return new CustomResponse(
            response,
            response.getStatusCode(),
            response.getStatusLine()
    );
}


public CustomResponse updatePost(String endpoint, String cookieValue, String requestBody) {
    Response response = RestAssured.given()
            .baseUri(BASE_URL)
            .cookie("orangehrm", cookieValue)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .put(endpoint)
        .then()
            .extract().response();

    String rawBody = response.asString(); 
    System.out.println("------------------------------------");
    System.out.println(rawBody);// capture raw response

    // Debug: print what you actually got back
    System.out.println("PUT " + endpoint);
    System.out.println("Status: " + response.getStatusLine());
    System.out.println("Body: " + rawBody);

    return new CustomResponse(
            response,                    
            response.getStatusCode(),
            response.getStatusLine()
    );
}





	


}