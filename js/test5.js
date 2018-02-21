function Foo(attr){
	this.attr = attr; 
}
function Bar(){
}
Bar.prototype = Object.create(Foo.prototype);
var x = 10;
var y = 20;
var z = 30;
Bar.prototype.constructor = Foo;