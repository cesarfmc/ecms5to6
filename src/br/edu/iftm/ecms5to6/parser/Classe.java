package br.edu.iftm.ecms5to6.parser;

public class Classe {
	
	private String file;
	

	private String name;
	private int numberOfAttributes;
	private int numberOfMethods;
	
	public Classe(String file, String name, int numberOfAttributes,
			int numberOfMethods) {
		super();
		this.file = file;
		this.name = name;
		this.numberOfAttributes = numberOfAttributes;
		this.numberOfMethods = numberOfMethods;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
