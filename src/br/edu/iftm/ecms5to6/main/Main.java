package br.edu.iftm.ecms5to6.main;

import br.edu.iftm.ecms5to6.parser.Parser;

public class Main {

    public static void main(String[] args) throws Exception {
    	Parser p = new Parser(args[0]);
    	p.parse();
    }
}
