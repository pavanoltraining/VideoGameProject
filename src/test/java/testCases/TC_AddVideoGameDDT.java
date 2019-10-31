package testCases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import utilities.XLUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.HashMap;

public class TC_AddVideoGameDDT {

	@Test(priority=1,dataProvider="Data")
	public void test_addNewVideoGame(String id,String name,String releaseDate,String reviewScore,String category,String rating)
	{
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("name", name);
		data.put("releaseDate", releaseDate);
		data.put("reviewScore", reviewScore);
		data.put("category", category);
		data.put("rating", rating);
		
		Response res=
			given()
				.contentType("application/json")
				.body(data)
			.when()
				.post("http://localhost:8081/app/videogames")
				
			.then()
				.statusCode(200)
				.log().body()
				.extract().response();
			
		String jsonString=res.asString();
		Assert.assertEquals(jsonString.contains("Record Added Successfully"), true);
		
	}
	
	@DataProvider(name="Data")
	public String[][] getData() throws IOException
	{
		String path=System.getProperty("user.dir")+"/VideoGameAPIData.xlsx";
		
		int rownum=XLUtils.getRowCount(path, "Sheet1");	
		int colcount=XLUtils.getCellCount(path,"Sheet1",1);
		
		String apidata[][]=new String[rownum][colcount];
		
		for(int i=1;i<=rownum;i++)
		{		
			for(int j=0;j<colcount;j++)
			{
				apidata[i-1][j]= XLUtils.getCellData(path, "Sheet1",i, j);  //1,0
			}
		}
	
		return apidata;
	}
	
	
	@Test(priority=2,dataProvider="Data")
	public void test_getVideoGame(String id,String name,String releaseDate,String reviewScore,String category,String rating)
	{
		given()
			.pathParam("id",id)
		.when()
			.get("http://localhost:8081/app/videogames/{id}")
		.then()
			.statusCode(200)
			.log().body()
			.body("videoGame.id", equalTo(id))
			.body("videoGame.name", equalTo(name));
			
	}
	
}


