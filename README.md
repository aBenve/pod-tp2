# TPE 2 - Hazelcast | MapReduce
Este trabajo práctico consiste en la implementación de un sistema de procesamiento distribuido de datos utilizando el framework Hazelcast y el modelo MapReduce. Se implementarán dos consultas sobre un conjunto de datos de prueba.

## Compilación
Una vez clonado el proyecto y ubicado en la raíz del mismo, ejecuta el siguiente comando para compilar el proyecto:

```bash
mvn clean package
```
Después de la ejecución, se generarán archivos `.tar` en la carpeta "target" de cada proyecto. Estos archivos deben ser extraídos para poder ejecutar el programa.

### Extracción de los archivos .tar.gz
```bash
mkdir -p tmp && find . -name '*.tar.gz' -exec tar -C tmp -xzf {} \;
find . -path './tmp/tpe2-*/*' -exec chmod u+x {} \;
```
## Ejecución
Antes de ejecutar, asegúrate de tener las rutas correctas de los archivos de datos necesarios para que el programa funcione:

1. `bikes.csv`
2. `stations.csv`

Para ejecutar el programa, debes iniciar al menos un nodo del servidor. Observa las direcciones IP y los puertos de los nodos, y al ejecutar el cliente, pásalos como parámetros.

### Servidor
```bash
cd ./tmp/tpe2-l61448-server-2023.1Q/ && ./run-server.sh [interfaz-de-red]
```
### Cliente
```bash
cd ./tmp/tpe2-l61448-client-2023.1Q/ && ./queryX.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
```
El resultado se guardará en un archivo `queryX.csv` ubicado en el `outPath`, y los tiempos de ejecución se agregarán en el archivo `times.txt` generado en la ubicación del script de ejecución de la consulta.
Idealmente, `times.txt` debería ubicarse en outPath, pero no logré configurar slf4j de manera programática para que lo haga.