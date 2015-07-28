package pdm.ivchecker;

import java.util.ArrayList;

/**
 * Clase auxiliar para la creación de objetos más manejables de tipo Verbo a partir de la información
 * plana extraida del CSV.
 */
public class Verbo {

    private verbo verbo;
    private tf transcripcionFonetica;
    private significado significado; //En varios idiomas

    //Ejemplos
    private ArrayList<FraseEjemplo> ejemplosUso; //En varios idiomas


    //Clases que definen las estructuras de los objetos:
    public static class FraseEjemplo{
        public String ejemplo;
        public String traduccionES;
        public String traduccionFR;
    }
    public class verbo{
        public String infinitivo;
        public String pasado;
        public String participio;


    }
    public class significado{
        public String significadoES;
        public String significadoFR;
    }
    public class tf{
        public String tfInfinitivo;
        public String tfPasado;
        public String tfParticipio;
    }


    //## Constructores ##//
    public Verbo(){
        verbo=new verbo();
        transcripcionFonetica=new tf();
        significado=new significado();
            significado.significadoES=null;
            significado.significadoFR=null;
        ejemplosUso=new ArrayList();
    }


    //Set/Get del verbo
    public verbo getVerbo(){return verbo;}
    public void setVerbo(verbo newVerbo){verbo=newVerbo;}

    //Set/Get de la transcripción fonética
    public tf getTranscripcionFonetica(){return transcripcionFonetica;}
    public void setTranscripcionFonetica(tf newTF){transcripcionFonetica=newTF;}

    //Set/Get de los significados del verbo
    public significado getSignificado(){return significado;}
    public void setSignificado(significado newSignificado){significado=newSignificado;}

    //Set/Get de la lista de ejemplos:
    public void setEjemplos(ArrayList<FraseEjemplo> listaEjemplos){ejemplosUso=listaEjemplos;}
    public ArrayList<FraseEjemplo> getEjemplos(){return ejemplosUso;}

    public String toString(){

        String info="Verbo: " + verbo.infinitivo+" "+verbo.pasado+" "+verbo.participio+"\n"+
                    "Trans. Fonética: " + transcripcionFonetica.tfInfinitivo+" "+transcripcionFonetica.tfPasado+" "+transcripcionFonetica.tfParticipio+"\n";
        if(significado.significadoES!=null)
            info+="Significado ES: "+significado.significadoES+"\n";
        if(significado.significadoFR!=null)
            info+="Significado FR: "+significado.significadoFR+"\n";
        if(!ejemplosUso.isEmpty())
            for(FraseEjemplo frase: ejemplosUso) {
                if(frase.ejemplo != null)
                    info += "Ejemplo " + Integer.toString(ejemplosUso.indexOf(frase) + 1) +" "+ frase.ejemplo + "\n";
                if(frase.traduccionES!=null)
                    info+="Ejemplo ES: "+frase.traduccionES+"\n";
                if(frase.traduccionFR!=null)
                    info+="Ejemplo FR: "+frase.traduccionFR+"\n";
            }


        return info;
    }

}


