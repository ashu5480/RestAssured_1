package RestAssuredTestClass;

import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import base.authentication.baseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class hamcrestAssertion extends baseClass{

	public hamcrestAssertion() {
		super();
	}
	
	public String AEndpoint = "https://restful-booker.herokuapp.com/booking/1";
	
	@Test
	public void numberAssertion() {
	     RestAssured.given().contentType(ContentType.JSON).when().get(AEndpoint).then().body("totalPrice",equalTo(164));	
	}
}
