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
    	
    	ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        ScriptContext context = engine.getContext();
        Invocable inv = (Invocable) engine;
        
        engine.eval(readFile("src/js/rinoceronte/escodegen.browser.js"), context);
        Object escodegen = engine.get("escodegen");
        Object JSON = engine.get("JSON");
        Object ast = inv.invokeMethod(JSON, "parse", readFile(fileJSON.getPath()));
        engine.put("ast", ast);
        ast = inv.invokeMethod(escodegen, "attachComments", engine.eval("ast"), engine.eval("ast.comments"),engine.eval("ast.tokens"));
        engine.eval("args2 = {comment: true}");
        Object resultEscodegen = inv.invokeMethod(escodegen, "generate", ast, engine.eval("args2"));
        
        FileOutputStream fout = new FileOutputStream(fileJS);
     	byte[] contentInBytes = resultEscodegen.toString().getBytes();
     	fout.write(contentInBytes);
     	fout.flush();
     	fout.close();
     }
    

}
