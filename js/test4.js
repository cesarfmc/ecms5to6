function Foo(attr){
	this.attr = attr; 
}
function Bar(){
}
Bar.prototype = Object.create(Foo.prototype);
Bar.prototype.constructor = Foo;