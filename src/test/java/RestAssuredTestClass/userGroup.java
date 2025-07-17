package RestAssuredTestClass;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class userGroup extends baseClass {

	public userGroup() {
		super();
	}

	private static int createdGroupId;
	
	@Test(groups = { "getDetails" },priority = 1)
	public void getGroups() {
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).when()
				.get("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/users/usergroup").then().log().all()
				.statusCode(200).extract().response();
		String resString = response.asPrettyString();
		System.out.println("Response is : " + resString);
	}

	@Test(priority = 2)
	public void createUserGroup() {
		/*
		 * JSONObject jsonData = new JSONObject(); jsonData.put("groupName",
		 * "RestAssured1"); jsonData.put("description", "test");
		 */

		Map<String, Object> datajson = new HashMap<String, Object>();
		datajson.put("groupName", "RestAssured4");
		datajson.put("description", "RestAssured1");

		Response response = RestAssured.given().log().all().relaxedHTTPSValidation()
				.headers("X-Session-Token", x_session_token).contentType(ContentType.JSON).body(datajson).when()
				.post("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/groups").then().log().all().statusCode(200)
				.extract().response();
		String resString = response.asPrettyString();
		createdGroupId = response.jsonPath().getInt("id");
		System.out.println("Group Id : "+createdGroupId);
		System.out.println("Response Body is : " + resString);
	}

	@Test(dependsOnMethods = {"createUserGroup"},priority = 3)
	public void updateGroup() {
		// RestAssured.basePath=prop.getProperty("basePath");
		Map<String, Object> dataJson = new HashMap<String, Object>();
		dataJson.put("groupName", "RestAssured3");
		dataJson.put("description", "updated description 1");
		dataJson.put("id", createdGroupId);
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).body(dataJson).when()
				.put("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/groups/"+createdGroupId).then().log().all().statusCode(200)
				.extract().response();
		String respString = response.asPrettyString();
		System.out.println("Response Body is : " + respString);
	}

	@Test(priority = 4)
	public void deleteGroup() {
		// RestAssured.basePath=prop.getProperty("basePath");
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).when()
				.delete("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/groups/"+createdGroupId).then().log().all()
				.statusCode(200).extract().response();
		String respString = response.asPrettyString();
		System.out.println("Response : " + respString);
	}

	@Test(priority = 5)
	public void assignUserToGroup() {
		String requestBody = "[5,9]";
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).body(requestBody).when()
				.put("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/usergroups/3").then().log().all()
				.statusCode(200).extract().response();

		String resString = response.asPrettyString();
		System.out.println("Response : " + resString);
	}

	@Test(dependsOnMethods = {"assignUserToGroup"},priority = 6 )
	public void unassignGroup() {
		String requestBody = "[5,9]";
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).body(requestBody).when()
				.delete("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/usergroups/3").then().statusCode(200)
				.extract().response();
		String resString = response.asPrettyString();
		System.out.println("Response : " + resString);
	}
}
