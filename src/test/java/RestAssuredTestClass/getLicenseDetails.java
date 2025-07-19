package RestAssuredTestClass;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.annotations.Test;

import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class getLicenseDetails extends baseClass {

	public getLicenseDetails() {
		super();
	}
	
	@Test(priority = 1)
	public void getLicense() throws FileNotFoundException {
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).when().filter(RequestLoggingFilter.logRequestTo(ps))
				.filter(ResponseLoggingFilter.logResponseTo(ps)).get("https://10.41.4.83/aeengine/rest/licenses/usage?orgCode=BMC")
				.then().extract().response();
		System.out.println("Response : "+response.asPrettyString());
		JsonPath responseData = response.jsonPath();
		int license_Version = responseData.getInt("licenseVersion");
		System.out.println(license_Version);
		
	}
	
	@Test(priority = 2)
	public void sendJson() {
		String jsonFile = "src/main/resource/inputFiles/dummy.json";
		File JsonFile = new File(jsonFile);
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).queryParam("entityId", 109).queryParam("category","WORKFLOW")
				.body(JsonFile).post("https://10.41.4.83:8443/aeengine/rest/file/v2/upload").then().extract()
				.response();
		System.out.println("Response : "+response);
	}
	
	@Test(priority = 3)
	public void sendXml() {
		String xmlFile = "src/main/resource/inputFiles/dummy.xml";
		File xmlFiles = new File(xmlFile);
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).queryParam("entityId", 109).queryParam("category","WORKFLOW")
				.body(xmlFiles).post("https://10.41.4.83:8443/aeengine/rest/file/v2/upload").then().extract()
				.response();
		System.out.println("Response : "+response);
	}
}
