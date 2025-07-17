package RestAssuredTestClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Users extends baseClass {

	public Users() {
		super();
	}

	@Test
	public void getUserDetail() {
		System.out.println("Printing X-Session-token" + x_session_token);
		Response response = RestAssured.given().relaxedHTTPSValidation().log().all()
				.header("x-session-token", x_session_token).contentType(ContentType.JSON).when()
				.get("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/users/1349").then().log().all().statusCode(200)
				.extract().response();
		String responseValue = response.asPrettyString();
		System.out.println(responseValue);
	}

	@Test
	public void createUser() {
		Map<String, Object> user = new HashMap<>();
		user.put("firstName", "Rudra");
		user.put("lastName", "Singh");
		user.put("emailId", "Rudra@gmail.com");
		user.put("userName", "RudraSingh");
		user.put("authType", "NATIVE");
		user.put("password", "Ashu@123");
		user.put("forcePasswordChange", true);

		Map<String, Object> roles = new HashMap<String, Object>();
		roles.put("id", 2);
		roles.put("name", "Admin");
		roles.put("description", "Tenant Administrator");
		roles.put("systemDefined", true);
		/*
		 * roles.put("lastUpdatedBy", 1); roles.put("lastUpdatedDate",
		 * System.currentTimeMillis());
		 */

		user.put("roles", List.of(roles));
		user.put("groupIds", List.of(4));

		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token", x_session_token)
				.contentType(ContentType.JSON).queryParam("ep", false).queryParam("verify", false).body(user).when()
				.post("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/users").then().statusCode(200).extract()
				.response();
		String respString = response.asPrettyString();
		System.out.println(respString);
	}
	
	@Test(priority = 3)
	public void updateUser() {
		Map<String, Object> updatedUser = new HashMap<>();
	    updatedUser.put("firstName", "Ashu");
	    updatedUser.put("lastName", "Singh");
	    updatedUser.put("emailId", "singhashu772@gmail.com"); // ✅ match existing format
	    updatedUser.put("userName", "ashu"); // ✅ usually required
	    updatedUser.put("authType", "NATIVE");
	    updatedUser.put("password", "Pass@123"); // ✅ Some APIs require this on update
	    updatedUser.put("forcePasswordChange", false);
	    Map<String, Object> role = new HashMap<>();
	    role.put("id", 2);
	    role.put("name", "Admin");
	    role.put("description", "Tenant Administrator");
	    role.put("systemDefined", true);
	    role.put("lastUpdatedBy", 1);
	    role.put("lastUpdatedDate", System.currentTimeMillis());

	    updatedUser.put("roles", List.of(role));
	    updatedUser.put("groupIds", List.of(4));
		Response response = RestAssured.given().log().all()
				.relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).queryParam("ep", "false").body(updatedUser).when()
				.put("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/users/1352").then().log().all()
				.statusCode(200).extract().response();
		String respString = response.asPrettyString();
		System.out.println(respString);
	}
	
	@Test
	public void deleteUser() {
		Response response = RestAssured.given().relaxedHTTPSValidation().header("X-Session-Token",x_session_token)
				.contentType(ContentType.JSON).when().delete("https://10.41.4.83:8443/aeengine/rest/tenants/BMC/users/1348")
				.then().log().all().statusCode(200).extract().response();
		String respString = response.asPrettyString();
		System.out.println("Response From server : "+respString);
	}
}
