package br.edu.iftm.ecms5to6.parser;

public class Classe {
	
	private String name;
	private int numberOfAttributes;
	private int numberOfMethods;
	
	public Classe(String name, int numberOfAttributes, int numberOfMethods) {
		super();
		this.name = name;
		this.numberOfAttributes = numberOfAttributes;
		this.numberOfMethods = numberOfMethods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfAttributes() {
		return numberOfAttributes;
	}

	public void setNumberOfAttributes(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
	}

	public int getNumberOfMethods() {
		return numberOfMethods;
	}

	public void setNumberOfMethods(int numberOfMethods) {
		this.numberOfMethods = numberOfMethods;
	}
}
