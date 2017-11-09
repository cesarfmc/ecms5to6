package br.edu.iftm.ecms5to6.parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import jdk.jfr.events.FileWriteEvent;
import rinoceronte.Escodegen;
import rinoceronte.Esprima;

public class Parser {

	private String filePath;
	

	public Parser(String filePath) {
		this.filePath = filePath;
	}

	
	public void parse() throws Exception {
		
		File fileJS = new File(filePath);
		if (fileJS.isFile() && fileJS.getPath().endsWith(".js")) {
			File fileJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + ".json");
			//Gerar arquivo json
			Esprima.parse(fileJS, fileJSON);
			
			//Carregar a arquivo JSON para objetos Java
			JsonReader jsonReader = Json.createReader(new FileReader(fileJSON));
			JsonObject jsonObjectMain = jsonReader.readObject();
				
			System.out.println(jsonObjectMain.toString());
			
	        //
	     
			
	        JsonArray body = jsonObjectMain.getJsonArray("body");
	        for(JsonValue member: body){
	        	JsonObject jsonObjectMember1 = (JsonObject)member;
	        	JsonArray declarations = jsonObjectMember1.getJsonArray("declarations");
	        	 for(JsonValue declaration: declarations){
	        		 JsonObject jsonObjectDeclaration1 = (JsonObject)declaration;
	        		 JsonObject jsonObjectInit = jsonObjectDeclaration1.getJsonObject("init");
	        		 JsonObject jsonObjectLeft = jsonObjectInit.getJsonObject("left");
	        		 JsonObject jsonObjectRight = jsonObjectInit.getJsonObject("right");
	        		 JsonString six = jsonObjectLeft.getJsonString("value");
	        		 if (six.equals("6")){
	        			 jsonObjectLeft.replace("value",member);
	        		 }
	        	 }
	        }

	        
	       
			
			//código para o matheus adicionar
			System.out.println(jsonObjectMain.toString());
			
			//Gerar o novo arquivo json
			File fileNewJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.json");
			JsonWriter jsonWriter = Json.createWriter(new FileWriter(fileNewJSON));
			jsonWriter.writeObject(jsonObjectMain);
			jsonWriter.close();
			
			//Gerar o novo arquivo.js
			//Responsabilidade do Cesar
			File fileNewJS = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.js");
			//Escodegen.generate(fileNewJSON, fileNewJS);
			
			System.out.println("Arquivo exportado!!");
			System.out.println("\nPrograma finalizado.");
		}
	}

	
}
