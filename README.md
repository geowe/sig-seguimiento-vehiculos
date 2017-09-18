# sig-seguimiento-vehículos
Adaptación del sig GeoWE para trabajar con información geográfica de vehículos.

![Screenshot](https://github.com/geowe/sig-seguimiento-vehiculos/blob/master/screenshot.jpg)

## License

The **geowe-core** is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), meaning you can use it free of charge, according with license terms and conditions.

## Configuration
In order to use all **GeoWE** features, you must configure following basic params:

#### Google Maps config:
To use Google maps, you need to specify yout own Google Maps Api key. Please, [read this carefully] (https://developers.google.com/maps/documentation/javascript/get-api-key).

Append your key to the URL of the script, located on App.html.

	For example:
	<script type="text/javascript"
	src="//maps.googleapis.com/maps/api/js?libraries=places&key=YOUR_GOOGLE_MAPS_API_KEY"></script>


#### Bing Maps config:
To use Bing maps, you need to specify your own Bing maps key. Please, [read this carefully] (https://www.microsoft.com/maps/create-a-bing-maps-key.aspx).

Put your key in BingConstants.properties file, located at: src\main\java\org\geowe\client\local\main\tool\map\catalog\model

	For example:
	bingKey = YOUR_BING_MAPS_KEY

#### what3words config:
To use what3words feature, you need to specify your own what3words api key. You must contact [what3words](http://what3words.com) to obtain it.

Put your key in ErraiApp.properties file, located at: src\main\resources

	For example:
	w3w.key = YOUR_W3W_API_KEY
	
## Build the software
In order to compile and build **GeoWE**, the JDK 7 platform is necessary. The project uses maven for building and packaging.
	
	mvn clean package

## Deploy
Once you compiled the software, the geowe.war file can be deployed on any server/application container, like Apache Tomcat.

