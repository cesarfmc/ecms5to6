class Foo {
    constructor(attr) {
        this.attr = attr;
    }
}
class Bar extends Foo {
    constructor(attr) {
        super(attr);
    }
}