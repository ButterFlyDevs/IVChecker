package pdm.red;

import android.content.Context;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ConexionServidor {

    private List<String>ranking;
    private int puntuacionJugadorTMP;
    private String nombreJugadorTMP;

    public ConexionServidor(){
        System.out.println("START CLIENT");
        this.puntuacionJugadorTMP=0;
        this.nombreJugadorTMP="";
        this.ranking=new ArrayList();
    }

    private void setPuntuacionJugador(int puntuacion){
        puntuacionJugadorTMP=puntuacion;
    }
    private int getPuntuacionJugador(){
        return puntuacionJugadorTMP;
    }
    private void setNombreJugadorTMP(String nombre){
        nombreJugadorTMP=nombre;
    }
    private String getNombreJugadorTMP(){
        return nombreJugadorTMP;
    }


    public String recorteNombre(String nombre){
        //Esto siempre dependerá de nuestra base de datos.
        if(nombre.length()>10) {
            return nombre.substring(0, 9);

        }else{
            return nombre;
        }

    }

    public void enviaPuntuacion(String alias, int puntos){

        /*
        El campo nombre de nuestra base de datos tiene una limitación (10char) por lo que podremos permitir que los nombres tenan mayor longitud,
        para asegurarnos de esto llamaremos siempre a la función recorteNombre que en el caso de que excedan eliminará los carácteres necesarios.
         */
        alias=recorteNombre(alias);


        setNombreJugadorTMP(alias);
        setPuntuacionJugador(puntos);

        sqlThreadintroducirPuntuacion.start();
        //Forzamos a que la ejecución de la hebra termine antes de continuar:
        try {
            sqlThreadintroducirPuntuacion.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public List<String> pedirRanking(){

        //Lanzamos la conexión a la base de datos y la petición de los datos en una hebra (debe de ser así para que funcione):
        sqlThreadpedirRanking.start();

        //Forzamos a que la ejecución de la hebra termine antes de continuar:
        try {
            sqlThreadpedirRanking.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Devolvemos el ranking que es una variable pribada de clase
        return ranking;

    }



    Thread sqlThreadpedirRanking = new Thread() {
        public void run() {


            try{
                Context mContext;
                //  mContext.getResources().getClass()
                //Registramos el driver:

                System.out.println(Class.forName("org.postgresql.Driver"));

                System.out.println("Cargado driver de la base de datos!!");
            }
            catch(java.lang.ClassNotFoundException e){
                System.err.println("Imposible encontrar driver del motor: ");
                e.printStackTrace();
            }

            String url="jdbc:postgresql://horton.elephantsql.com:5432/idviomlw";
            String username = "idviomlw";
            String password = "CiOKqiaqBk6FXQzDAVbEUbP-Kj5Oeopb";
            // jdbc:postgresql://host:port/database";
            try{

                System.out.println("Ejecutando la conexión");
                //Establecemos la conexión:
                Connection db = DriverManager.getConnection(url, username, password);
                Statement st = db.createStatement();

                //Creamos la tabla
                //String orden="CREATE TABLE puntuaciones (nombre varchar(10), puntuacion integer)";
                //Introducimos datos
                //ResultSet rs = st.executeQuery("INSERT INTO puntuaciones VALUES ( 'pepe', 47857)");


                ResultSet rs = st.executeQuery("SELECT * FROM puntuaciones");

                while (rs.next()) {
                    System.out.print("Nombre: ");
                    System.out.println(rs.getString(1));
                    System.out.print("Puntuación: ");
                    System.out.println(rs.getString(2));
                    //Cargamos los datos que obtenemos en la variable ranking propia del objeto que luego devolveremos:
                    ranking.add(rs.getString(1)+" "+rs.getString(2));

                }
                System.out.println("añadidos "+ranking.size()+" elementos a ranking");


                rs.close();
                st.close();
                db.close();

                System.out.println("Conexión terminada");


            }catch (java.sql.SQLException e) {
                System.out.println(e.getMessage()+e.getErrorCode()+e);
            }
        }
    };

    Thread sqlThreadintroducirPuntuacion = new Thread() {
        public void run() {


            try{

                //Registramos el driver:
                System.out.println(Class.forName("org.postgresql.Driver"));

                System.out.println("Cargado driver de la base de datos!!");
            }
            catch(java.lang.ClassNotFoundException e){
                System.err.println("Imposible encontrar driver del motor: ");
                e.printStackTrace();
            }

            String url="jdbc:postgresql://horton.elephantsql.com:5432/idviomlw";
            String username = "idviomlw";
            String password = "CiOKqiaqBk6FXQzDAVbEUbP-Kj5Oeopb";
            // jdbc:postgresql://host:port/database";
            try{

                System.out.println("Ejecutando la conexión");
                //Establecemos la conexión:
                Connection db = DriverManager.getConnection(url, username, password);
                Statement st = db.createStatement();

                System.out.println(ranking.size()+" elementos en ranking. ANTES");

                String query="INSERT INTO puntuaciones VALUES ( '"+getNombreJugadorTMP()+"', "+getPuntuacionJugador()+")";

                //Introducimos datos
                ResultSet rs = st.executeQuery(query);

                System.out.println(ranking.size()+" elementos en ranking. DESPUÉS");


                rs.close();
                st.close();
                db.close();

                System.out.println("Conexión terminada");


            }catch (java.sql.SQLException e) {
                System.out.println(e.getMessage()+e.getErrorCode()+e);
            }
        }
    };

}
