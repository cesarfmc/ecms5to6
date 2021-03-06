var esprima = require('esprima');
var escodegen = require('escodegen');
var fs = require('fs');
var file = '/home/matheus/Projetos/algorithms.js/data_structures/linked_list_Alterado_6.json';


fs.readFile(file, 'utf8', function (err, data) {
  if (err) {
    console.log('Error: ' + err);
    return;
  }
  ast = JSON.parse(data);
});

ast = escodegen.attachComments(ast, ast.comments, ast.tokens);
var result = escodegen.generate(ast,  {comment: true});

fs.writeFile("/home/matheus/Projetos/algorithms.js/data_structures/linked_list_Alterado_6.js", result, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 
