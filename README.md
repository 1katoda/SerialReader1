# Serijska komunikacija

Preprost program za serijsko komunikacijo, napisan v jeziku Java.

## Build

uporabi
* [Maven](https://maven.apache.org/) - Dependency Management
-->mvn package

## Run
uporabi
-->mvn exec

### Zahteve

Za delovanje je potrebna rxtx knjiznica.
/target/lib/

Datoteki rxtxSerial-2.1.7.dll in rxtxParallel-2.1.7.dll je potrebno namestiti v Maven kot:
-->mvn install:install-file -Dfile=target/lib/rxtxSerial-2.1.7.dll -DgroupId=org.rxtx -DartifactId=rxtxSerial  -Dversion=2.1.7 -Dpackaging=dll -DgeneratePom=true
in
-->mvn install:install-file -Dfile=target/lib/rxtxParallel-2.1.7.dll -DgroupId=org.rxtx -DartifactId=rxtxParallel  -Dversion=2.1.7 -Dpackaging=dll -DgeneratePom=true


Prilozene datoteke se lahko za samostojno delovanje kopirajo:
	rxtx-2.1.7.jar	v	JAVA_HOME\jre\ext
	rxtxSerial-2.1.7.dll	v	JAVA_HOME\jre


## Verzije

*1.1 - preurejeno kot Maven projekt
	 - preurejena imena za skladnost z Java naming convention
	 - uvoz samih knjiznic, namesto celih classov
	 - metoda updatePorts premaknjena v SR1 class

*1.0 - HelloWorld


## Zgradba

### SR1.java

	SR1 class uporablja rxtx knjiznico za iskanje in vzpostavitev povezave na serijski port.
	
	*public static void launchSR1(String sPort, int[] sr1params)
		-poveze na podani port s podanimi parametri
		-sPort = ime porta
		-sr1params[] = [selectedBaudRate, selectedDataBits, selectedStopBits, selectedParity]
		
	*public void initWriteToPort()
		-inicializira pisanje na serijski port
		
	*public static void writeToPort(String msg)
		-napise vsebino msg na serijski port
		
	*public static void disconnect()
		-zapre serijski port
		
	*public SR1(int[] args)
		-args[] = [selectedBaudRate, selectedDataBits, selectedStopBits, selectedParity]
		-odpre serialPort in usmeri vhodne podatke na inputStream
		-zacne nov Thread tega elementa
		
	*public void run()
		- initWriteToPort
		
	*public void serialEvent(SerialPortEvent event)
		-izpise dogodek na serijskem portu
		-izpise prihajajoce podatke 
		
	*public static void updatePorts(boolean firstRun)
		-firstRun = 1, ce se funkcija klice v inicializaciji, 0 ce se klice kasneje
		-firstRun = 0 	=>	posodobi PortBox komponente
		
### SR1gui.java

	Swing GUI za program
	
	*public static void main(String[] args)
		-ustvari nov objekt SR1
		
	*public SR1gui()
		-klice initialize()
		
	*private void initialize()
		-nastavi GUI elemente
		-nastavi podatke za povezavo na port
	
	*public class TextAreaOutputStream extends OutputStream
		-nastavi outputStream na JTextArea TextArea

## License

	-LGPL-2.1
	




