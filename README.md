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
cd ./tmp/tpe2-l61448-server-2023.1Q/ && ./run-server.sh [interfaz]
```

## Correr queries

```bash
cd ./tmp/tpe2-l61448-client-2023.1Q/ && ./queryX.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
```

El resultado de se guardara en un archivo queryX.csv y los tiempos de dicha ejecucion se apendearan en el archivo times.txt
