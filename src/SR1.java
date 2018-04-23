import java.io.*;

/*
 * * Uses RxTx library
 */
import java.util.*;
import gnu.io.*; // for rxtxSerial library
 
public class SR1 implements Runnable, SerialPortEventListener 
{
   static CommPortIdentifier portId;
   static CommPortIdentifier saveportId;
   static Enumeration<?>        portList;
   static InputStream           inputStream;
   static SerialPort           serialPort;
   Thread           readThread;
 
   static OutputStream      outputStream;
   static boolean        outputBufferEmptyFlag = false;
 
   public static void launchSR1(String sPort, int[] SR1params) {		

	  boolean           portFound = false;
      System.out.println("Connecting to "+sPort);
       
        // parse ports and if the default port is found, initialized the reader
      portList = CommPortIdentifier.getPortIdentifiers();
      while (portList.hasMoreElements()) {
         portId = (CommPortIdentifier) portList.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(sPort)) {
               System.out.println("Found port: "+sPort);
               portFound = true;
               // init reader thread
               @SuppressWarnings("unused")
               SR1 reader = new SR1(SR1params);
               setConnected = true;
            } 
         } 
          
      } 
      if (!portFound) {
         System.out.println("port " + sPort + " not found.");
      } 
       
   } 
 
   public void initwritetoport() {
      // initwritetoport() assumes that the port has already been opened and
      //    initialized by "public SR1()"
 
      try {
         // get the outputstream
         outputStream = serialPort.getOutputStream();
      } catch (IOException e) {}
 
      try {
         // activate the OUTPUT_BUFFER_EMPTY notifier
         serialPort.notifyOnOutputEmpty(true);
      } catch (Exception e) {
         System.out.println("Error setting event notification");
         System.out.println(e.toString());
         System.exit(-1);
      }
       
   }
 
   public static void writetoport(String msg) {
 //     System.out.println("Writing \""+msg+"\" to "+serialPort.getName());
      try {
         // write string to serial port
         outputStream.write(msg.getBytes());
      } catch (IOException e) {}
   }
 static public Boolean setConnected = false;
 
   public static void disconnect()
   {
       //close the serial port
       try
       {
    	   if(setConnected) {
           serialPort.removeEventListener();
           serialPort.close();
           inputStream.close();
           outputStream.close();
           setConnected = false;

           System.out.println("Disconnected");
    	   }
       }
       catch (Exception e)
       {
    	   System.out.println("Failed to close " + serialPort.getName());
       }
   }
   
   
   public SR1(int[] args) {

      // initialize serial port
      try {
         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
      } catch (PortInUseException e) {}
    
      try {
         inputStream = serialPort.getInputStream();
      } catch (IOException e) {}
    
      try {
         serialPort.addEventListener(this);
      } catch (TooManyListenersException e) {}
       
      // activate the DATA_AVAILABLE notifier
      serialPort.notifyOnDataAvailable(true);
    
      try {
         // set port parameters
         serialPort.setSerialPortParams(args[0], args[1], args[2], args[3]);
      } catch (UnsupportedCommOperationException e) {System.out.println("UnsupportedCommOperationException");}
       
      // start the read thread
      readThread = new Thread(this);
      readThread.start();
      setConnected = true;
       
   }
 
   public void run() {
      // first thing in the thread, we initialize the write operation
      initwritetoport();
      try {
         while (true) {
            // write string to port, the serialEvent will read it
//            writetoport("Ping");
            Thread.sleep(1000);
         }
      } catch (InterruptedException e) {}
   } 
 
   public void serialEvent(SerialPortEvent event) {
//       System.out.println(".............."+event.getEventType());
      switch (event.getEventType()) {
      case SerialPortEvent.BI:
          System.out.println("BI");
      case SerialPortEvent.OE:
          System.out.println("OE");
      case SerialPortEvent.FE:
          System.out.println("FE");
      case SerialPortEvent.PE:
          System.out.println("PE");
      case SerialPortEvent.CD:
          System.out.println("CD");
      case SerialPortEvent.CTS:
          System.out.println("CTS");
      case SerialPortEvent.DSR:
          System.out.println("DSR");
      case SerialPortEvent.RI:
          System.out.println("RI");
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
  //        System.out.println("OUTPUT_BUFFER_EMPTY");
          break;
      case SerialPortEvent.DATA_AVAILABLE:
         // Data waiting
         byte[] readBuffer = new byte[1000];
         try {
            // read data
            if (inputStream.available() > 0) {
               @SuppressWarnings("unused")
			int numBytes = inputStream.read(readBuffer);
            } 
            // print data
            String result  = new String(readBuffer);
            System.out.println(result.trim());
         } catch (IOException e) {System.out.println("IO exception");}
    
         break;
      }
   } 
 
}