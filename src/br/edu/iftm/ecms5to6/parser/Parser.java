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
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import jdk.jfr.events.FileWriteEvent;
import rinoceronte.Escodegen;
import rinoceronte.Esprima;

public class Parser {

	private String filePath;
	private String obj;


	public Parser(String filePath) {
		this.filePath = filePath;
	}


	public void parse() throws Exception {

		File fileJS = new File(filePath);
		if (fileJS.isFile() && fileJS.getPath().endsWith(".js")) {
			File fileJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + ".json");
			//Gerar arquivo json
			Esprima.parse(fileJS, fileJSON);

			//Carregar o arquivo JSON para objetos Java
			JsonReader jsonReader = Json.createReader(new FileReader(fileJSON));
			JsonObject jsonObjectMain = jsonReader.readObject();

			
			JsonObjectBuilder tree = Json.createObjectBuilder();
			tree = tree.add("type", "Program");
			JsonArray body1 = jsonObjectMain.getJsonArray("body");
			
			JsonObjectBuilder tree2 = Json.createObjectBuilder();
			JsonObject jsonObjectMember1= null;
			for(JsonValue member: body1){
				jsonObjectMember1 = (JsonObject)member;
				tree2 = buildTree (jsonObjectMember1,tree2);
			}	
			tree = tree.add("body", Json.createArrayBuilder().add(tree2));
			JsonObject jsonObjectNew = tree.build();
			System.out.println(jsonObjectNew);

			//Gerar o novo arquivo json
			File fileNewJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.json");
			JsonWriter jsonWriter = Json.createWriter(new FileWriter(fileNewJSON));
			jsonWriter.writeObject(jsonObjectNew);
			jsonWriter.close();

			//Gerar o novo arquivo.js
			//Responsabilidade do Cesar
			File fileNewJS = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.js");
			//Escodegen.generate(fileNewJSON, fileNewJS);

			System.out.println("\n\nArquivo exportado!!");
			System.out.println("\nPrograma finalizado.");
		}
	}
  

	private JsonObjectBuilder buildTree (JsonObject jsonObject,JsonObjectBuilder tree2) {
		
		Set<Entry<String,JsonValue>> myset= jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray){
				JsonObject object= null;
				JsonArray array= (JsonArray) entry.getValue();
				for(JsonValue obj : array ) {
					 object= (JsonObject) obj;
					 tree2 = tree2.add(entry.getKey(), object);
					 buildTree(object,tree2);
				}
			}else if (entry.getValue() instanceof JsonObject){
				JsonObject obj1= (JsonObject) entry.getValue();
				tree2= tree2.add(entry.getKey(), entry.getValue());
				buildTree(obj1,tree2);
			}
			else if (entry.getValue() instanceof JsonString){
				if(entry.getValue().equals("answer")) {
				  tree2 = tree2.add(entry.getKey(),"resposta");
				}else {
					tree2 = tree2.add(entry.getKey(),entry.getValue());	
				}
			}else if (entry.getValue() instanceof JsonNumber){
				if(entry.getValue().toString().equals("6")) {
					tree2 = tree2.add(entry.getKey(),8);
				}else if(entry.getValue().toString().equals("7")) {
					 tree2 = tree2.add(entry.getKey(),9);
				}
			}
		}
		return tree2;
	}
   
}

