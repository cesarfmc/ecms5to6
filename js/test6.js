Bar.prototype = Object.create(Foo.prototype);
Bar.prototype.constructor = Foo;
function Foo(attr){
	this.attr = attr; 
}
function Bar(attr){
	Foo.call (this, attr);
}