function Foo(attr){	
	this.attr = attr;
	function Action(){
		var x = 10;
		var y = 20;
		var z = x + y;
	}
}
var myFoo = new Foo ("foo");