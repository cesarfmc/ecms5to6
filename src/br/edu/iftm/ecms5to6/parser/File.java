package br.edu.iftm.ecms5to6.parser;

import java.util.ArrayList;

public class File {
	
	private String fileName;
	private ArrayList<Classe> classes;
	
	
	

	public File(String fileName) {
		super();
		this.fileName = fileName;
		this.classes = new ArrayList<Classe>();
	}
	
	
	public ArrayList<Classe> getClasses() {
		return classes;
	}

	public void addClasse(Classe classe){
		classes.add(classe);
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
