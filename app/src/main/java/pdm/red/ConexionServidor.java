package pdm.red;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ConexionServidor {

    private String host;
    private int puerto;

    public ConexionServidor(){
        System.out.println("START CLIENT");
        this.host="130.206.82.231";
        this.puerto=1234;
    }

    public void enviaPuntuacion(String alias, int puntos){
        try{
            //Se intenta realizar la conexión con el socket a la dirección dada por el puerto dado.
            Socket socket = new Socket (host, puerto);
            //Creamos dos buffer de datos, uno de entrada desde el socket y otro de salida por el socket.
            //Con el de entrada recibimos los datos
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Con el de salida envaimos los datos.
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()),true);

            //ENVIAMOS nuestros datos al servidor
            salida.println(alias+" "+puntos);

            //Recibimos la respuesta
            String respuesta=entrada.readLine();

                /*Si la respuesta del servidor es OK */
            if(respuesta.equals("OK")){
                System.out.println("Datos enviados.");
            }
            //Cerramos la conexión con el servidor y cerramos el socket.
            socket.close();

        } catch(IOException e){

        }
    }


    public List<String> pedirRanking(){

        List<String>ranking=new ArrayList();

        Vector<String> result = new Vector<String>();
        String respuesta="";

        try {
            //Conexion al servidor
            Socket sk = new Socket(host, puerto);


            BufferedReader entrada =   new BufferedReader(new InputStreamReader(sk.getInputStream()));
            PrintWriter salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),true);

            //Le pedimos al servidor la lista de puntuaciones enviándole el string PUNTUACIONES
            salida.println("PUNTUACIONES");
            //Número de datos rescatados de la entrada.
            int n = 0;
            //Declaramos un string donde almacenar partes de la respuesta que vendrá por "entrada"


            // Extracción de los datos "linea a linea".
            do {
                respuesta = entrada.readLine();
                if (respuesta != null) { ranking.add(respuesta);/*result.add(respuesta);*/ n++; }

                //Mientras no se encuentre con una linea vacía se repite el proceso.
            } while ( respuesta != null);

            //Se cierra el proceso de conexión con el servidor.
            sk.close();

        } catch (Exception e) {
            //Si la conexión falla se muestra este mensaje
            System.out.println("Fallo en la conexión al servidor: Fail"+e);
        }

              /*
              System.out.println(result.size());
              for(int i=0; i<result.size(); i++) System.out.println(result.get(i));
              */
        return ranking;

    }
}
