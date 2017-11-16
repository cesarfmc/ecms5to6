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

			String typeOut= (String ) jsonObjectMain.getString("type");
			String typeIn= null;
			String typeMiddle1= null;
			String typeMiddle2= null;
			String typeMiddle3= null;
			String typeMiddle4= null;
			String last= null;
			String kind= null;
			
			String name= null,operator=null; int valueL=0,valueR=0;
			
			JsonObjectBuilder tree = Json.createObjectBuilder();
			tree = tree.add("type", "Program");
			JsonArray body1 = jsonObjectMain.getJsonArray("body");
			JsonObjectBuilder tree2 = Json.createObjectBuilder();
			for(JsonValue member: body1){
				JsonObject jsonObjectMember1 = (JsonObject)member;
				Set<Entry<String,JsonValue>> myset = jsonObjectMember1.entrySet();
				for (Entry<String, JsonValue> entry : myset) {
					if (entry.getValue() instanceof JsonArray){
						System.out.println("JsonArray");
					}else if (entry.getValue() instanceof JsonObject){
						System.out.println("JsonObject"); 
					}
					else if (entry.getValue() instanceof JsonString){
						System.out.println("JsonString"); 
					}else{
						System.out.println("error");
					}
					tree2 = tree2.add(entry.getKey(),entry.getValue());
				}
			}	
			
			tree = tree.add("body", Json.createArrayBuilder().add(tree2));
			JsonObject jsonObjectNew = tree.build();
			System.out.println(jsonObjectNew);
		
			/*
			 * Para alterar o valor da esquerda altere a variável "valueL";
			 * Para alterar o valor da direita altere a variável "valueR";
			 * Para alterar o operador altere a variável "operator";
			 * Para alterar o nome da variável altere a variável "name";
			 * 
			 * Realizar a alteração dos valores abaixo do comentário!!!
			 */
			
			// Exemplo;
			
			valueL= 100;
			valueR= 12;
			name= "Expressão";
			operator= "+";
			
			// criando a árvore.

			/*JsonObject tree3 = Json.createObjectBuilder().add("type", typeOut)
					.add("body", Json.createArrayBuilder().add(Json.createObjectBuilder()
							.add("type", typeIn).add("declarations",Json.createArrayBuilder().add(Json.createObjectBuilder()
									.add("type",typeMiddle1).add("id", Json.createObjectBuilder()
											.add("type",typeMiddle2).add("name",name))
									.add("init",Json.createObjectBuilder()
											.add("type", typeMiddle3)
											.add("operator",operator).add("left",Json.createObjectBuilder()
													.add("type",typeMiddle4)
													.add("Value",valueL)
													.add("raw", valueL)).add("right",Json.createObjectBuilder()
															.add("type",last).add("value",valueR).add("raw",valueR))))).add("kind",kind))).build();
			System.out.println("\n\ntree: "+tree);
			*/
			//código para o matheus adicionar

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


}

