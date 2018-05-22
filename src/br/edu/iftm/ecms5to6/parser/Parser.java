package br.edu.iftm.ecms5to6.parser;

import java.io.File;


import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.swing.JOptionPane;

//import jdk.jfr.events.FileWriteEvent;
import rinoceronte.Escodegen;
import rinoceronte.Esprima;

public class Parser {

	private String filePath;
	private ArrayList<Classe> classes;
	private ArrayList<JsonValue> change;
	private int i;
	private boolean funcDct,flag;

	public Parser(String filePath) {
		this.filePath = filePath;
		this.change = new ArrayList<>();
		this.funcDct = false;
		this.i = 0;
	}

	public Parser(String filePath, ArrayList<Classe> classes) {
		this.filePath = filePath;
		this.classes = classes;
		this.change = new ArrayList<>();
		this.funcDct = false;
		this.i = 0;
	}

	public void parse() throws Exception {

		File fileJS = new File(filePath);
		if (fileJS.isFile() && fileJS.getPath().endsWith(".js")) {
			File fileJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + ".json");
			// Gerar arquivo json
			Esprima.parse(fileJS, fileJSON);

			// Carregar o arquivo JSON para objetos Java
			JsonReader jsonReader = Json.createReader(new FileReader(fileJSON));
			JsonObject jsonObjectMain = jsonReader.readObject();

			JsonObjectBuilder tree = Json.createObjectBuilder();

			System.out.println("Im:  " + jsonObjectMain);

			JsonObjectBuilder tree2 = Json.createObjectBuilder();
			JsonArrayBuilder tree3 = Json.createArrayBuilder();

			Set<Entry<String, JsonValue>> myset = jsonObjectMain.entrySet();

			for (Entry<String, JsonValue> entry : myset) {
				if(entry.getValue() instanceof JsonArray) {
					if(entry.getKey().toString().equals("body")) {
						JsonObject object = null;
						JsonArray array = (JsonArray) entry.getValue();
						for (JsonValue obj : array) {
							object = (JsonObject) obj;
							Set<Entry<String, JsonValue>> myset2 = object.entrySet();
							for (Entry<String, JsonValue> entry2 : myset2) {
								if (entry2.getValue().toString().equals("\"ExpressionStatement\"")) {
									if(checkHeritage(object)) {
									}else {
										tree3 = tree3.add(object);
										break;
									}
								}else if(entry2.getValue().toString().equals("\"FunctionDeclaration\"")) {
									if(isClass(object)) {
										flag = true;
										tree3 = tree3.add(convert(object, tree2));
										break;
									}
									else {
										tree3 = tree3.add(object);
										break;
									}
								}else if(entry2.getKey().equals("range")) {

								}else {
									tree3 = tree3.add(object);
									break;
								}
							}	
						}
						tree = tree.add("body", tree3);
					}else {
						tree = tree.add(entry.getKey(), entry.getValue());
					}
				}else {
					tree = tree.add(entry.getKey(), entry.getValue());
				}
			}
			//tree = tree.add("sourceType", "script");
			JsonObject jsonObjectNew = tree.build();
			System.out.println("Out: " + jsonObjectNew);

			// Gerar o novo arquivo json
			File fileNewJSON = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_Alterado_6.json");
			JsonWriter jsonWriter = Json.createWriter(new FileWriter(fileNewJSON));
			jsonWriter.writeObject(jsonObjectNew);
			jsonWriter.close();

			// Gerar o novo arquivo.js
			File fileNewJS = new File(fileJS.getPath().substring(0, fileJS.getPath().length() - 3) + "_6.js");
			Escodegen.generate(fileNewJSON, fileNewJS);

			System.out.println("\n\nArquivo exportado!");
			System.out.println("\nPrograma finalizado.");
		}
	}

	private JsonObjectBuilder convert(JsonObject jsonObject, JsonObjectBuilder tree2) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				if (entry.getKey().equals("params")) {
					change.add(entry.getValue());
				} else if (entry.getKey().equals("defaults")) {
					tree2 = tree2.addNull("superClass");
				} else if(entry.getKey().toString().equals("range")) {
					tree2= tree2.add(entry.getKey(), entry.getValue());	
				}else{
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						Set<Entry<String, JsonValue>> set = object.entrySet();
						for (Entry<String, JsonValue> each : set) {
							if(each.getValue().toString().equals("\"FunctionDeclaration\"")) {
								if(isClass(object)) {
									flag = true;
									aryAux = aryAux.add(convert(object, treeAux));
									break;
								}else {
									aryAux = aryAux.add(buildAction(object, treeAux));
									break;
								}
							}else if(each.getValue().toString().equals("\"ExpressionStatement\"") && (flag)){
								aryAux = aryAux.add(convert(object, treeAux));
								flag = false;
								break;
							}else if(each.getKey().equals("range")){
								
							}else  if(each.getValue().toString().equals("\"ExpressionStatement\"")){
								break;
							}else {
								aryAux = aryAux.add(object);
								break;
							}
						}
					}
					tree2 = tree2.add(entry.getKey(), aryAux);
				}
			} else if (entry.getValue() instanceof JsonObject) {
				JsonObject obj1 = (JsonObject) entry.getValue();
				if (entry.getKey().toString().equals("body")) {
					change.add(entry.getValue());
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1 = convert(obj1, treeAux1);
					tree2 = tree2.add(entry.getKey(), treeAux1);
				} else if (entry.getKey().equals("right")) {
					JsonObject obj = (JsonObject) change.get(i);
					i++;
					JsonObjectBuilder tree = Json.createObjectBuilder();
					tree = buildBody(obj, tree);
					tree2 = tree2.add("body", tree);
					if (funcDct) {
						tree2.add("generator", change.get(++i));
						tree2.add("expression", change.get(++i)).add("async", false);
					} else {
						tree2.add("generator", false);
						tree2.add("expression", false).add("async", false);
					}
				} else if (entry.getKey().equals("left")) {
					if (i < change.size()) {
						tree2 = tree2.add("params", change.get(i));
						i++;
					}
				} else if (entry.getKey().toString().equals("expression")) {
					JsonObjectBuilder key = Json.createObjectBuilder();
					key = key.add("type", "Identifier").add("name", "constructor");
					tree2 = tree2.add("key", key).add("computed", false);
					JsonObjectBuilder tree = Json.createObjectBuilder();
					JsonObject obj = (JsonObject) entry.getValue();
					tree2 = tree2.add("value", convert(obj, tree));
					tree2 = tree2.add("kind", "constructor").add("static", false);
				} else {
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1 = convert(obj1, treeAux1);
					tree2 = tree2.add(entry.getKey(), treeAux1);
				}
			} else if (entry.getValue() instanceof JsonString) {
				if (entry.getKey().equals("operator")) {
					tree2 = tree2.addNull("id");
				} else if (entry.getValue().toString().equals("\"AssignmentExpression\"")) {
					tree2 = tree2.add(entry.getKey(), "FunctionExpression");
				} else if (entry.getValue().toString().equals("\"FunctionDeclaration\"")) {
					tree2 = tree2.add(entry.getKey(), "ClassDeclaration");
				} else if (entry.getValue().toString().equals("\"BlockStatement\"")) {
					tree2 = tree2.add(entry.getKey(), "ClassBody");
				} else if (entry.getValue().toString().equals("\"ExpressionStatement\"")) {
					tree2 = tree2.add(entry.getKey(), "MethodDefinition");
				} else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if (entry.getKey().toString().equals("rest")) {
			} else if ((entry.getKey().equals("generator")) || (entry.getKey().equals("expression"))) {
			} else {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
			}
		}
		return tree2;
	}

	private JsonObjectBuilder buildBody(JsonObject jsonObject, JsonObjectBuilder tree2) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				if (entry.getKey().toString().equals("params")) {
				}else if (entry.getKey().toString().equals("range")) {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
				}else if (entry.getKey().toString().equals("defaults")) {
				} else if (entry.getKey().toString().equals("arguments")) {
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						Set<Entry<String, JsonValue>> set = object.entrySet();
						for (Entry<String, JsonValue> broken : set) {
							if (broken.getValue().toString().equals("\"ThisExpression\"")) {
							}else {
								aryAux = aryAux.add(buildBody(object, treeAux));
								break;
							}
						}
					}
					tree2 = tree2.add(entry.getKey(), aryAux);

				}else{
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						aryAux = aryAux.add(buildBody(object, treeAux));
					}
					tree2 = tree2.add(entry.getKey(), aryAux);
				}
			} else if (entry.getValue() instanceof JsonObject) {
				JsonObject obj1 = (JsonObject) entry.getValue();
				JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
				if (entry.getKey().toString().equals("left")) {
					change.add(entry.getValue());
					JsonObject obj2 = (JsonObject) entry.getValue();
					treeAux1 = buildBody(obj2, treeAux1);
					tree2 = tree2.add(entry.getKey(), treeAux1);
				} else if (entry.getKey().toString().equals("id")) {
				} else if (entry.getKey().toString().equals("body")) {
					JsonObject obj2 = (JsonObject) entry.getValue();
					treeAux1 = changeType(obj2, treeAux1);
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					treeAux1 = treeAux1.add("arguments", aryAux);
					tree2 = tree2.add("expression", treeAux1);
				}else if (entry.getKey().toString().equals("callee")) {
					treeAux1= heritage(obj1, treeAux1);
					tree2 = tree2.add(entry.getKey(), treeAux1);
				}else {
					treeAux1= buildBody(obj1, treeAux1);
					tree2 = tree2.add(entry.getKey(), treeAux1);
				}
			} else if (entry.getValue() instanceof JsonString) {
				if (entry.getValue().toString().equals("\"FunctionDeclaration\"")) {
					tree2 = tree2.add(entry.getKey(), "ExpressionStatement");
					funcDct = !funcDct;
				}else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if ((entry.getKey().equals("generator")) || (entry.getKey().equals("expression"))) {
				change.add(entry.getValue());
			} else if (entry.getKey().toString().equals("computed")) {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
			}else if (entry.getKey().toString().equals("value")) {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
			}
		}
		return tree2;
	}

	private JsonObjectBuilder changeType(JsonObject jsonObject, JsonObjectBuilder tree2) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonString) {
				if (entry.getValue().toString().equals("\"BlockStatement\"")) {
					tree2 = tree2.add(entry.getKey(), "CallExpression");
				} else if (entry.getKey().toString().equals("name")) {
					tree2 = tree2.add(entry.getKey(), "Action");
				} else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if (entry.getValue() instanceof JsonArray) {
				if (entry.getKey().toString().equals("body")) {
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonObject obj = (JsonObject) change.get(i);
					treeAux = changeType(obj, treeAux);
					tree2 = tree2.add("callee", treeAux);
				}
			} else if (entry.getValue() instanceof JsonObject) {
				JsonObject obj1 = (JsonObject) entry.getValue();
				JsonObjectBuilder treeAux = Json.createObjectBuilder();
				tree2 = tree2.add(entry.getKey(), changeType(obj1, treeAux));
			} else {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
			}
		}
		return tree2;
	}

	private JsonObjectBuilder buildAction(JsonObject jsonObject, JsonObjectBuilder tree2) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				if (entry.getKey().toString().equals("params")) {
					change.add(entry.getValue());
					tree2 = tree2.add("computed", false);
				} else if (entry.getKey().toString().equals("range")) {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}else if (entry.getKey().toString().equals("defaults")) {
				} else if (entry.getKey().toString().equals("body")) {
					tree2 = tree2.addNull("id").add("params", change.get(++i)).add("body", change.get(++i))
							.add("generator", false).add("expression", false).add("async", false);
				} else {
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						aryAux = aryAux.add(buildAction(object, treeAux));
					}
					tree2 = tree2.add(entry.getKey(), aryAux);
				}
			} else if (entry.getValue() instanceof JsonObject) {
				if (entry.getKey().toString().equals("id")) {
					if (i == 4) {
						tree2 = tree2.add("key", entry.getValue());
					} else {
						tree2 = tree2.add(entry.getKey(), entry.getValue());
					}
				} else if (entry.getKey().toString().equals("body")) {
					change.add(entry.getValue());
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonObject obj = (JsonObject) entry.getValue();
					tree2 = tree2.add("value", buildAction(obj, treeAux));
				} else if(entry.getKey().toString().equals("expression")) {
					JsonObjectBuilder key= Json.createObjectBuilder();
					key.add("type", "Identifier")
					.add("name", "constructor");
					tree2 = tree2.add("key",key)
							.add("computed", false);
					JsonObject obj1 = (JsonObject) entry.getValue();
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1= heritage(obj1, treeAux1);
					treeAux1= treeAux1.add("generator",false)
							.add("expression", false)
							.add("async", false);
					tree2 = tree2.add("value",treeAux1)
							.add("kind", "constructor")
							.add("static", false);
				}else{
					JsonObject obj1 = (JsonObject) entry.getValue();
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1= buildAction(obj1, treeAux1);
					tree2 = tree2.add(entry.getKey(),treeAux1);
				}
			} else if (entry.getValue() instanceof JsonString) {
				if ((entry.getValue().toString().equals("\"FunctionDeclaration\""))||(entry.getValue().toString().equals("\"ExpressionStatement\""))) {
					tree2 = tree2.add(entry.getKey(), "MethodDefinition");
				} else if (entry.getValue().toString().equals("\"BlockStatement\"")) {
					tree2 = tree2.add(entry.getKey(), "FunctionExpression");
				} else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if(entry.getValue() instanceof JsonNumber) {
				tree2= tree2.add(entry.getKey(), entry.getValue());
			}else if (entry.getKey().toString().equals("rest")) {
				tree2 = tree2.add("kind", "method");
			} else if (entry.getKey().toString().equals("generator")) {
				tree2 = tree2.add("static", entry.getValue());
			}
		}
		return tree2;

	}

	private JsonObjectBuilder heritage(JsonObject jsonObject, JsonObjectBuilder tree2) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				
				if(entry.getKey().toString().equals("range")) {
					tree2 = tree2.add(entry.getKey(),entry.getValue());
				}else if(entry.getKey().toString().equals("arguments")) {
					JsonObject obj1 = (JsonObject) change.get(change.size()-1);
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1= buildBody(obj1, treeAux1);
					tree2 = tree2.add("body",treeAux1);
				}else {
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonObjectBuilder treeAux = Json.createObjectBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						aryAux = aryAux.add(heritage(object, treeAux));
					}
					tree2 = tree2.add(entry.getKey(), aryAux);
				}
			} else if (entry.getValue() instanceof JsonObject) {
				if(entry.getKey().toString().equals("right")) {
					change.add(entry.getValue());
					i++;
				}else if(entry.getKey().toString().equals("callee")) {
					tree2 = tree2.add("params",change.get(change.size() - i));
				}
				else{
					JsonObject obj1 = (JsonObject) entry.getValue();
					JsonObjectBuilder treeAux1 = Json.createObjectBuilder();
					treeAux1= heritage(obj1,treeAux1);
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if (entry.getValue() instanceof JsonString) {
				if (entry.getValue().toString().equals("\"CallExpression\"")) {
					tree2 = tree2.add(entry.getKey(), "FunctionExpression")
							.addNull("id");
				} else if (entry.getValue().toString().equals("\"MemberExpression\"")) {
					tree2 = tree2.add(entry.getKey(), "Super");
					return tree2;
				} else if (entry.getValue().toString().equals("7")) {
					tree2 = tree2.add(entry.getKey(), "9");
				} else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else if (entry.getValue() instanceof JsonNumber) {
				if (entry.getValue().toString().equals("6")) {
					tree2 = tree2.add(entry.getKey(), 8);
				} else if (entry.getValue().toString().equals("7")) {
					tree2 = tree2.add(entry.getKey(), 9);
				} else {
					tree2 = tree2.add(entry.getKey(), entry.getValue());
				}
			} else {
				tree2 = tree2.add(entry.getKey(), entry.getValue());
			}
		}
		return tree2;
	}

	private boolean checkHeritage(JsonObject jsonObject) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				if(entry.getKey().toString().equals("range")) {

				}else {
					JsonObject object = null;
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						return checkHeritage(object);
					}
				}
			} else if (entry.getValue() instanceof JsonObject) {
				JsonObject obj1 = (JsonObject) entry.getValue();
				return checkHeritage(obj1);

			} else if (entry.getValue() instanceof JsonString) {
				if (entry.getValue().toString().equals("\"create\"")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isClass(JsonObject jsonObject) {

		Set<Entry<String, JsonValue>> myset = jsonObject.entrySet();
		for (Entry<String, JsonValue> entry : myset) {
			if (entry.getValue() instanceof JsonArray) {
				if(entry.getKey().toString().equals("range")) {
				}else {
					JsonObject object = null;
					JsonArrayBuilder aryAux = Json.createArrayBuilder();
					JsonArray array = (JsonArray) entry.getValue();
					for (JsonValue obj : array) {
						object = (JsonObject) obj;
						return isClass(object);
					}
				}
			} else if (entry.getValue() instanceof JsonObject) {

				if(entry.getKey().equals("id")) {
					JsonObject obj1 = (JsonObject) entry.getValue();
					return isClass(obj1);	
				}
			} else if (entry.getValue() instanceof JsonString) {
				if (entry.getKey().toString().equals("name")) {
					String name= entry.getValue().toString();
					name = name.replaceAll("\"", "");
					for(Classe obj : classes) {
						if(obj.getName().equals(name)) {
							return true;
						}
					}
				} 
			}
		}
		return false;
	}






	/*
	 * private JsonObjectBuilder buildTree (JsonObject jsonObject,JsonObjectBuilder
	 * tree2) {
	 * 
	 * Set<Entry<String,JsonValue>> myset= jsonObject.entrySet(); for (Entry<String,
	 * JsonValue> entry : myset) { if (entry.getValue() instanceof JsonArray){
	 * JsonObject object= null; JsonArrayBuilder aryAux= Json.createArrayBuilder();
	 * JsonObjectBuilder treeAux= Json.createObjectBuilder(); JsonArray array=
	 * (JsonArray) entry.getValue(); for(JsonValue obj : array ) { object=
	 * (JsonObject) obj; aryAux= aryAux.add(buildTree(object,treeAux)); } tree2 =
	 * tree2.add(entry.getKey(),aryAux); }else if (entry.getValue() instanceof
	 * JsonObject){ JsonObject obj1= (JsonObject) entry.getValue();
	 * JsonObjectBuilder treeAux1= Json.createObjectBuilder(); tree2=
	 * tree2.add(entry.getKey(),buildTree(obj1,treeAux1)); } else if
	 * (entry.getValue() instanceof JsonString){
	 * if(entry.getValue().toString().equals("\"answer\"")) { tree2 =
	 * tree2.add(entry.getKey(),"resposta"); }else
	 * if(entry.getValue().toString().equals("6")) { tree2 =
	 * tree2.add(entry.getKey(),"8"); }else
	 * if(entry.getValue().toString().equals("7")) { tree2 =
	 * tree2.add(entry.getKey(),"9"); }else { tree2 =
	 * tree2.add(entry.getKey(),entry.getValue()); } }else if (entry.getValue()
	 * instanceof JsonNumber){ if(entry.getValue().toString().equals("6")) { tree2 =
	 * tree2.add(entry.getKey(),8); }else
	 * if(entry.getValue().toString().equals("7")) { tree2 =
	 * tree2.add(entry.getKey(),9); }else { tree2 =
	 * tree2.add(entry.getKey(),entry.getValue()); } }else { tree2 =
	 * tree2.add(entry.getKey(),entry.getValue()); } } return tree2; }
	 */
}