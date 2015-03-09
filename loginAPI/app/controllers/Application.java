package controllers;

import play.*;
import play.mvc.*;
import models.*;

import views.html.*;
import play.mvc.BodyParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import java.util.UUID;


public class Application extends Controller {

   public static Result index() {
		return ok(index.render("Rest API"));
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result authenticate() {
				  ObjectNode result = Json.newObject();

		try{
			JsonNode json = request().body().asJson();
			String userName = json.findPath("userName").textValue();
			String password = json.findPath("password").textValue();
	  
			User user = User.authenticate(userName,password);

			if(user == null) {
			result.put("status", "UNAUTHORIZED");
			result.put("message", "Invalid User Name / Password");
			return unauthorized(result);
		  } else {
			result.put("status", "OK");
			result.put("accessToken", UUID.randomUUID().toString());
			return ok(result);
		  }
		} catch (Exception e){
			result.put("status", "UNAUTHORIZED");
			result.put("message", "Invalid User Name / Password");
			return unauthorized(result);
		}
	  
	
	}
	@BodyParser.Of(BodyParser.Json.class)
	public static Result createUser() {
	String userName = null;
	String password = null;
	String name = null;
	ObjectNode result = Json.newObject();
	
	  JsonNode json = request().body().asJson();
	  if(json != null){
	  if(json.findPath("userName") != null ){
	  	  userName = json.findPath("userName").textValue();
	  } else {
			result.put("status", "OK");
			result.put("message", "userName is required");
			return ok(result);

	  }
	  if(json.findPath("password") != null ){
	  	  password = json.findPath("password").textValue();

	  } else {
	  result.put("status", "OK");
			result.put("message", "Password is required");
			return ok(result);
	  }
	  if(json.findPath("name") != null ){
	  	  name = json.findPath("name").textValue();

	  } else {
			result.put("status", "OK");
			result.put("message", "Name is required");
			return ok(result);
	  }

		new User(userName, name, password).save();

		User user = User.find.byId(userName);
		
		if(user != null){
			  return ok(Json.toJson(user));

		} else {
			result.put("status", "UNAUTHORIZED");
			result.put("message", "Access is denied");
			return unauthorized(result);
		}
	  } else {
			result.put("status", "UNAUTHORIZED");
			result.put("message", "Access is denied");
			return unauthorized(result);
			}
	  
		
	}
}
