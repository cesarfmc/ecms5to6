function Foo(attr){	
	this.attr = attr;
	function Action(){
	}
}
var myFoo = new Foo ("foo");

//Classe Foo com o uso da nova palavra-chave
class Foo {
	//Construtor da Classe
	constructor(attr) {
		//Atributos
		this.attr = attr;
		this.Action(); 
	}
	//Metodos
	Action(){} 
}
//var myFoo = new Foo ("foo");