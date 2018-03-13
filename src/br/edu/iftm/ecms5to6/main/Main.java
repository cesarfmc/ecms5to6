package br.edu.iftm.ecms5to6.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import br.edu.iftm.ecms5to6.parser.Classe;
import br.edu.iftm.ecms5to6.parser.File;
import br.edu.iftm.ecms5to6.parser.Parser;
import au.com.bytecode.opencsv.CSVReader;


public class Main {

    public static void main(String[] args) throws Exception {
    	
    	HashMap<String, File> files = new HashMap<String, File>();
    	CSVReader reader = new CSVReader(new FileReader(args[0]),';');
    	String [] nextLine;
    	while ((nextLine = reader.readNext()) != null) {
    	        System.out.println(args[1]+nextLine[0] + "|" + nextLine[1] + "|" + nextLine[2] + "|" + nextLine[3]);
    	        if (files.containsKey(args[1]+nextLine[0])){
    	        	File file = files.get(args[1]+nextLine[0]);
    	        	Classe classe = new Classe(nextLine[1], Integer.parseInt(nextLine[2]), Integer.parseInt(nextLine[3]));
    	        	file.addClasse(classe);
    	        }else{
    	        	File file = new File(args[1]+nextLine[0]);
    	        	Classe classe = new Classe(nextLine[1], Integer.parseInt(nextLine[2]), Integer.parseInt(nextLine[3]));
    	        	file.addClasse(classe);  
    	        	files.put(file.getFileName(), file);
    	        }
    	}
    	for (File file : files.values()) {
    		Parser p = new Parser(file.getFileName(), file.getClasses());
        	p.parse();
		}
		/*
    	Parser p = new Parser(args[0]);
    	p.parse();
    	*/
    }
}
