var esprima = require('esprima');
var escodegen = require('escodegen');
var fs = require('fs');
var file = '/Volumes/HD2/ic/Projetos/algorithms.js/data_structures/bst_6.json';


fs.readFile(file, 'utf8', function (err, data) {
  if (err) {
    console.log('Error: ' + err);
    return;
  }
  ast = JSON.parse(data);
});

ast = escodegen.attachComments(ast, ast.comments, ast.tokens);
var result = escodegen.generate(ast,  {comment: true});

fs.writeFile("/Volumes/HD2/ic/Projetos/algorithms.js/data_structures/bst_6.js", result, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 



//teste para preservar commments
var content;
fs.readFile('/Volumes/HD2/ic/Projetos/algorithms.js/data_structures/bst.js', 'utf8', function read(err, data) {
    if (err) {
        throw err;
    }
    content = data;
});
console.log(content);

var ast = esprima.parse(content, {range: true, tokens: true, comment: true}); 

var result = JSON.stringify(ast, null, 2);

fs.writeFile("/Volumes/HD2/ic/Projetos/algorithms.js/data_structures/bst.json", result, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 



ast  = es.attachComments(ast, ast.comments, ast.tokens);

var result = escodegen.generate(ast, {comment: true});

fs.writeFile("/Volumes/HD2/ic/Projetos/algorithms.js/data_structures/bst_6.js", result, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 