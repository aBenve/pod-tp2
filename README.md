# TPE 2 - Hazelcast | MapReduce

## Compilar

```bash
mvn clean package
```

Extraer los .tar.gz

```bash
mkdir -p tmp && find . -name '*tar.gz' -exec tar -C tmp -xzf {} \;
find . -path './tmp/tpe2-*/*' -exec chmod u+x {} \;
```

## Correr el servidor

```bash
cd ./tmp/tpe2-l61448-server-2023.1Q/ && ./run-server.sh
```

## Correr queries

```bash
cd ./tmp/tpe2-l61448-client-2023.1Q/ && ./queryX.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
```

o

```bash
cd ./script && node run.js [query] [params]
```

Donde 
1. [query] es el nombre de la query
2. [params] son [cantidad-nodos] [cantidad-de-veces] [inPath] [outPath] [N](si es query2)
