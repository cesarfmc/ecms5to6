var esprima = require('esprima');
var escodegen = require('escodegen');
var fs = require('fs');
var file = '/home/matheus/Projetos/algorithms.js/data_structures/linked_list_6.json';


fs.readFile(file, 'utf8', function (err, data) {
  if (err) {
    console.log('Error: ' + err);
    return;
  }
  obj_pulseconfig = JSON.parse(data);
});

var result = escodegen.generate(obj_pulseconfig);

fs.writeFile("/home/matheus/Projetos/algorithms.js/data_structures/linked_list_after.js", result, function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 

