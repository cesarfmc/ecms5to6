package rinoceronte;

import static javax.script.ScriptContext.ENGINE_SCOPE;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.json.JsonObject;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Escodegen {
   
    static String readFile(String fileName) throws IOException,FileNotFoundException {
        return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
    }    

    public static void generate(File fileJSON, File fileJS) throws ScriptException, IOException, NoSuchMethodException {
    	
    	/*ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        ScriptContext context = engine.getContext();

        
      
        engine.eval(readFile("src/js/rinoceronte/escodegen.js"), context);
       
        
        Invocable inv = (Invocable) engine;
        Object escodegen = engine.get("Function");
      
        String code = "{type: 'BinaryExpression',operator: '+',left: { type: 'Literal', value: 40 },right: { type: 'Literal', value: 2 }}";
        Object tree = inv.invokeMethod(escodegen, "generate", code);
        
        System.out.println("Code:"+(String) tree);
        */
    	ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        ScriptContext context = engine.getContext();
        engine.eval(readFile("/Users/cesarcouto/workspace/ecms5to6/src/js/rinoceronte/esprima.js"), context);
        Invocable inv = (Invocable) engine;
        Object esprima = engine.get("esprima");
        engine.eval("args = {range: true, tokens: true, comment: true}");
        Object resultEsprima = inv.invokeMethod(esprima, "parse", "//comment here\nvar answer = 42;", engine.eval("args"));
        Object JSON = engine.get("JSON");
        String json = (String) inv.invokeMethod(JSON, "stringify", resultEsprima, null, 2);
        System.out.println(json);
        
        engine.eval(readFile("/Users/cesarcouto/workspace/ecms5to6/src/js/rinoceronte/escodegen.browser.js"), context);
        Object escodegen = engine.get("escodegen");
        engine.put("ast", resultEsprima);
        resultEsprima = inv.invokeMethod(escodegen, "attachComments", engine.eval("ast"), engine.eval("ast.comments"),engine.eval("ast.tokens"));
        engine.eval("args2 = {comment: true}");
        Object resultEscodegen = inv.invokeMethod(escodegen, "generate", resultEsprima, engine.eval("args2"));
        System.out.println(resultEscodegen);
    	
    }
    

}
