package br.edu.iftm.ecms5to6.parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
			JsonObject jsonObject = jsonReader.readObject();
				
			System.out.println(jsonObject.toString());
			
	         /*
	     
	        JsonArray body= jsonObject.getJsonArray("body");
			JsonArray declarations= body.getJsonArray(0);
			JsonObject id= declarations.getJsonObject(0);
			
			id.put("name", "resposta");
			id.replace("name", "resposta");
			
			JsonObject init= declarations.getJsonObject(1);
			JsonObject left= init.getJsonObject("left");
			
			left.put("value", 8);
			left.repalce("value", 8);
			
			left.put("raw", 8);
			left.repalce("raw", 8);
			
			JsonObject right= init.getJsonObject("right");
			
			right.put("value", 9);
			right.repalce("value", 9);
			
			right.put("raw", 9);
			right.repalce("raw", 9);
	          
	         */
			
			//código para o matheus adicionar
			System.out.println(jsonObject.toString());
			
			//Gerar o novo arquivo json
			File fileNewJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.json");
			JsonWriter jsonWriter = Json.createWriter(new FileWriter(fileNewJSON));
			jsonWriter.writeObject(jsonObject);
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
