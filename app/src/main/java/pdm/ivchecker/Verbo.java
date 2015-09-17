/*
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.
        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
        GNU General Public License for more details.
        You should have received a copy of the GNU General Public License
        along with this program. If not, see <http://www.gnu.org/licenses/>.
        Copyright 2015 Jose A. Gonzalez Cervera
        Copyright 2015 Juan A. Fernández Sánchez
*/
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

        public String getForma(int forma){
            if(forma==0)
                return infinitivo;
            else if(forma==1)
                return pasado;
            else if(forma==2)
                return participio;
            else
                return null;

        }

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


