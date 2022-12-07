const http    = require('http');
const fs      = require('fs');
const jwt     = require('jsonwebtoken');
const sha256  = require('sha256');
const axios   = require('axios');

const hostname = '127.0.0.1';
const port = 3000;
const utf8 = 'utf8' 

const server = http.createServer((req, res) => {
    
    /*PAYLOAD */
    var payload = makePayload();
    
    //GERA O JSON DO PAYLOAD
    var jsonStringify = JSON.stringify(payload)
    
    //TAG BODY QUE VAI NO JWT
    var valueBody = sha256(jsonStringify)
    var bodyJwt = { body: valueBody};
    
    //JWT GERADO COM CHAVE PRIVADA
    var jwtToken = generateJwt(bodyJwt);

    const url = "http://localhost:8080/person";
    axios.post(url, jsonStringify, {
        headers: {
            'authentication': jwtToken,  
            'content-type': 'text/json'
          }
    }).then((respServer) => {
        var isEquals = respServer.data == valueBody
        var response = {server: respServer.data,  client: valueBody,  equals: isEquals}
        res.statusCode = 200;
        res.setHeader('Content-Type', 'text/json');
        res.end(JSON.stringify(response));
    }).catch((error) => {
        console.error(error)
    })

});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});


function generateJwt(json) {
    return jwt.sign(json, findPrivateKey(), prepareSignOptions());
}

function makePayload() {
    return person = { id:1, name: "Cicero" }   
}

function findPrivateKey() {
    return fs.readFileSync('./private.key', utf8)
}


function prepareSignOptions() {
    var signOptions = {
        issuer:  'Cícero Ednilson',
        subject:  'ciceroednilson@gmail.com',
        audience:  'https://www.github.com/ciceroednilson',
        expiresIn:  "12h",
        algorithm:  "RS256"
    };
    return signOptions;
}
