
function Foo(attr){
	this.attr = attr; 
}

Bar.prototype = Object.create(Foo.prototype);
Bar.prototype.constructor = Foo;

function Bar(){
}
