package RestAssuredTestClass;

import java.io.FileNotFoundException;

import org.testng.annotations.Test;

import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class verifyResponseHeader extends baseClass{

	@Test
	public void getLicense() throws FileNotFoundException {
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).when().filter(RequestLoggingFilter.logRequestTo(ps))
				.filter(ResponseLoggingFilter.logResponseTo(ps)).get("https://10.41.4.83/aeengine/rest/licenses/usage?orgCode=BMC")
				.then().header("content-type", "application/json")
				.extract().response();
		System.out.println("Response : "+response.asPrettyString());
		JsonPath responseData = response.jsonPath();
		int license_Version = responseData.getInt("licenseVersion");
		System.out.println(license_Version);
		
	}
}
