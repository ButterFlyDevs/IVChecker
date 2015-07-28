package pdm.ivchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by juan on 28/07/15.
 */
public class ProcesadorCSVs {

    /**
     * Función de clase para a partir de un flujo de entrada (Desde un fichero CSV crudo) procesar los datos
     * para convertirlos bajo una estructura conocida y específica en una lista de objetos de tipo Verbo
     * @param flujoCSV El flujo de lectura del fichero
     * @return Un array de objetos de tipo Verbo cargados con los datos del fichero.
     */
    static public ArrayList<Verbo> obtenerVerbos(InputStream flujoCSV){
        //Contenedor para los objetos de tipo Verbo
        ArrayList<Verbo> verbos = new ArrayList();
        //Buffer de lectura para el fichero
        BufferedReader reader = new BufferedReader(new InputStreamReader(flujoCSV));

        int numero=1;
        try {
            //Linea
            String line;
            //Sacamos el número del fichero CSV
            line=reader.readLine();


            // ### PROCESAMIENTO POR CADA LINEA ### //
            while(true){
                //Leemos una linea
                line=reader.readLine();

                //PRUEBA!!!
                //if(line!=null)
                //    System.out.println(line);

                //Cuando una linea no tenga contenido habremos llegado al fin del fichero
                if (line == null) break;

                //Dividimos la linea leída es secciones, divisiones realizadas por las comas en las lineas.
                String[] RowData = line.split(",");

                    // PROCESAMIENTO

                        //Si se trata de un verbo
                        if(RowData[0].equals("v")) {
                            //Añadimos un nuevo elemento a la lista solo cuando encontramos v !!!
                            verbos.add(new Verbo());
                            //Añadimos las tres formas del verbo
                            verbos.get(verbos.size()-1).getVerbo().infinitivo=RowData[1];
                            verbos.get(verbos.size()-1).getVerbo().pasado=RowData[2];
                            verbos.get(verbos.size()-1).getVerbo().participio=RowData[3];
                        }
                        //Si nos encontramos ante la linea que define la transcripción fonética del verbo:
                        else if(RowData[0].equals("tf")){
                            //Cogemos el último verbo de la lista y le añadimos la transcripció fonética
                            verbos.get(verbos.size()-1).getTranscripcionFonetica().tfInfinitivo=RowData[1];
                            verbos.get(verbos.size()-1).getTranscripcionFonetica().tfPasado=RowData[2];
                            verbos.get(verbos.size()-1).getTranscripcionFonetica().tfParticipio=RowData[3];
                        }
                        //Si vemos que tiene significado en español
                        else if(RowData[0].equals("se")){
                            verbos.get(verbos.size()-1).getSignificado().significadoES=RowData[1];
                        }
                        //Si vemos que tiene significado en francés
                        else if(RowData[0].equals("sf")){
                            verbos.get(verbos.size()-1).getSignificado().significadoFR=RowData[1];
                        }
                        //Si vemos que tiene un ejemplos
                        else if(RowData[0].equals("e")){
                            verbos.get(verbos.size()-1).getEjemplos().add(new Verbo.FraseEjemplo());
                            verbos.get(verbos.size()-1).getEjemplos().get(verbos.get(verbos.size()-1).getEjemplos().size()-1).ejemplo=RowData[1];
                        }
                        else if(RowData[0].equals("tee")){
                            //Cogemos el último verbo, el último ejemplo y añadimos su traducción en español
                            verbos.get(verbos.size()-1).getEjemplos().get(verbos.get(verbos.size()-1).getEjemplos().size()-1).traduccionES=RowData[1];
                        }
                        else if(RowData[0].equals("tef")){
                            //Cogemos el último verbo, el último ejemplo y añadimos su traducción en español
                            verbos.get(verbos.size()-1).getEjemplos().get(verbos.get(verbos.size()-1).getEjemplos().size()-1).traduccionFR=RowData[1];
                        }
                //El otro posible caso es que la fila tenga un número, esto sólo indica el número del verbo
                //y no es un dato útil en el procesamiento.


            }
            flujoCSV.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return verbos;
    }


}
