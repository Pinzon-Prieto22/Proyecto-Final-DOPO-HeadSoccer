package dominio;

public class Equipo {
    private final String codigo;
    private final String pais;

    public Equipo(String codigo, String pais){ 
        this.codigo = codigo; 
        this.pais = pais; 
    }

    public String getCodigo(){ 
        return codigo; 
    }
    public String getPais(){ 
        return pais; 
    }

    @Override
    public String toString() { 
        return pais; 
    }
}
