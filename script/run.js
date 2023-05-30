const {exec, spawn} = require('child_process');
const { exit } = require('process');
const {writeFileSync, writeFile, appendFileSync} = require('fs');
const { getSystemErrorMap } = require('util');

/*

    Primer argumento: Query a ejecutar
    Seundo argumento cantidad de nodos a usar
    Tercer agumento: Cantidad de veces a ejecutar la query

*/

const COLOR = {
    RESET: "\x1b[0m",
    RED: "\x1b[31m",
    YELLOW: "\x1b[33m",
    MAGENTA: "\x1b[35m",
    CYAN: "\x1b[36m",
    GRAY: "\x1b[90,"
}



const query = process.argv[2];
const nodes = process.argv[3];
const times = process.argv[4];

const ADDRESS = "192.168.1.98"
const IN_PATH = process.argv[5];
const OUT_PATH = process.argv[6];

const N = process.argv[7];


// El primer argumento es "node" y el segundo es el nombre del archivo
if(query === "query2" && process.argv.length !== 8){
    console.error("Error: Cantidad de argumentos de query2 incorrecta");
    exit(1);
}
if(query === "query1" && process.argv.length !== 7){
    console.error("Error: Cantidad de argumentos de query1 incorrecta");
    exit(1);
}

const processes = []


const setupNodes = async (nodes) => {
    const cmd_cd = "../tmp/tpe2-l61448-server-2023.1Q/"
    const cmd = `cd ${cmd_cd} && ./run-server.sh`;

    const promises = []

    for(let i = 0; i < nodes; i++){
        console.log(COLOR.YELLOW ,`Starting node ${i}`);
        promises.push(run(cmd, "node"));
        await new Promise(r => setTimeout(r, 1000));
    }

    return Promise.all(promises);
}

const kill = async (type) => {
    console.log(COLOR.RED, `Killing ${type}s`);
    processes.filter((p) => p.type === type).forEach((p) => {
        p.exec.kill();
    })
}

const killAll = async () => {
    const cmd = `killall java`;
    console.log(COLOR.RED, `Killing all`);
    run(cmd);
}

const setupClient = async (query, nodes, times) => {
    const cmd_cd = "../tmp/tpe2-l61448-client-2023.1Q/"
    const port = 5701;


    let completeAddress = ""
    new Array(nodes).fill(0).forEach((_, i) => {
        if(i !== nodes - 1)
            completeAddress += `${ADDRESS}:${port + i};`
        else
            completeAddress += `${ADDRESS}:${port + i}`
    })

    const cmd = `cd ${cmd_cd} && ./${query}.sh -Daddresses='${completeAddress}' -DinPath=${IN_PATH} -DoutPath=${OUT_PATH} ${query === "query2" ? `-Dn=${N}`:""}`;

    const results = []

    for(let i = 0; i < times; i++){
        console.log(COLOR.RESET, `Running query`, COLOR.CYAN, `${query}`, COLOR.RESET, "for the", COLOR.CYAN, `${i + 1} time`);
        
        // Espero a que termine la query
        results.push(await run(cmd, "client"))

        // Mato el cliente
        await kill("client");

        await new Promise(r => setTimeout(r, 2000));
    }

    return results;
}



const run = (cmd, type) => {
    return new Promise((resolve, reject) => {
        processes.push({
            exec: exec(cmd, (err, stdout, stderr) => {
                    if (err) {
                        reject(err);
                        return;
                    }
                    // process.stdout.write(stdout);
                    resolve(stdout);    
                }),
            type: type
        })
    });
}

async function main(){
    try{
        setupNodes(nodes);
        // los nodos tardan en levantarse y cuando lo hacen, no terminan.
        await new Promise(r => setTimeout(r, nodes * 5000));
        const results = await setupClient(query, nodes, times);

        // console.log(COLOR.RESET, results);

        killAll();

        console.log(COLOR.RESET, "Creating output file");

        writeFileSync(OUT_PATH + "run-" + query + ".txt", "", (err) => {
            if (err) {
              console.error(COLOR.RED,'Error writing to file:', err);
            } else {
              console.log(COLOR.GRAY ,'Data written to file successfully.');
            }
          });
        console.log(COLOR.RESET, "Writing results to file")

        results.forEach((r, i) => {
            appendFileSync(OUT_PATH + "run-" + query + ".txt", r, (err) => {
                if (err) {
                  console.error(COLOR.RED, 'Error writing to file:', err);
                } else {
                  console.log(COLOR.GRAY,'Data written to file successfully.');
                }
              }
            );
        })
        exit(0)

    } catch(err){
        console.error(COLOR.RED, err);
        killAll();
        exit(1);
    }
}

main()
