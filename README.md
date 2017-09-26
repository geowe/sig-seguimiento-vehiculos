# sig-seguimiento-vehículos
Adaptación del sig GeoWE para trabajar con información geográfica de vehículos.

![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-car-icon-route.png)
![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-line-report.png)
![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-preview-report.png)

Visualización de rutas de un vehículo a partir de la toma de datos GPS 

![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-report.png)
![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-vehicle-layer.png)
![Screenshot](https://github.com/jmmluna/sig-seguimiento-vehiculos/blob/master/sgf-vehicle.png)

## License

The **geowe-core** is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), meaning you can use it free of charge, according with license terms and conditions.
	
## Build the software
In order to compile and build **GeoWE**, the JDK 7 platform is necessary. The project uses maven for building and packaging.
	
	mvn clean package

## Deploy
Once you compiled the software, the geowe.war file can be deployed on any server/application container, like Apache Tomcat.

