package RestAssuredTestClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ArrayJSON extends baseClass{

	public ArrayJSON(){
		super();
	}
	
	@Test
	public void createUser() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("employee_name", "ashu");
		jsonObject.put("profile_image", "test.png");
		jsonObject.put("employee_age", "30");
		jsonObject.put("employee_salary", "11111");
		
		JSONObject data2 = new JSONObject();
		data2.put("employee_name", "MapTest");
        data2.put("profile_image", "test2.png");
        data2.put("employee_age", "20");
        data2.put("employee_salary", "99999");
        
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        
        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.put(data2);
        
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("employee1", jsonArray);
        jsonObject2.put("employee2", jsonArray2);
        
        Response resp = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(jsonObject2)
        .when().post("https://dummy.restapiexample.com/api/v1/create").then().statusCode(200).log().all().extract().response();
        
        String respString = resp.asPrettyString();
        System.out.println(respString);
	}
}
