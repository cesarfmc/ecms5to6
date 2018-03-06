package br.edu.iftm.ecms5to6.main;

import java.io.FileReader;
import java.util.ArrayList;

import br.edu.iftm.ecms5to6.parser.Classe;
import br.edu.iftm.ecms5to6.parser.Parser;
import au.com.bytecode.opencsv.CSVReader;


public class Main {

    public static void main(String[] args) throws Exception {
    	/*ArrayList<Classe> classes = new ArrayList<Classe>();
    	CSVReader reader = new CSVReader(new FileReader(args[0]),';');
    	String [] nextLine;
    	while ((nextLine = reader.readNext()) != null) {
    	        System.out.println(args[1]+nextLine[0] + "|" + nextLine[1] + "|" + nextLine[2] + "|" + nextLine[3]);
    	        classes.add(new Classe (args[1]+nextLine[0], nextLine[1], Integer.parseInt(nextLine[2]), Integer.parseInt(nextLine[3])));
    	}
    	for (Classe classe : classes) {
    		Parser p = new Parser(classe.getFile(), classe.getName());
        	p.parse();
		}
		*/
    	Parser p = new Parser(args[0]);
    	p.parse();
    }
}
