package dominio;

import java.util.List;

/**
 * Clase responsable de todo lo que el ususario debe elegir antes de jugar
 */
public class ConfiguracionJuego {
    private Modalidad modalidad = Modalidad.PVP;
    private FabricaTipoPartida tipo;
    private int valorTipo;
    private Personaje personajeIzquierdo;
    private Personaje personajeDerecho;

    public ConfiguracionJuego() {
        tipo = CatalogoTiposPartida.getInstancia().getTodos().get(0);
        valorTipo = tipo.getValorPorDefecto();
        List<Personaje> todos = CatalogoPersonajes.getInstancia().getTodos();
        personajeIzquierdo = todos.get(0);
        personajeDerecho = todos.get(1);
    }

    public Modalidad getModalidad(){ 
        return modalidad; 
    }
    public void setModalidad(Modalidad m){
        this.modalidad = m; 
    }
    public FabricaTipoPartida getTipo(){ 
        return tipo; 
    }
    public int getValorTipo(){ 
        return valorTipo; 
    }
    public void setTipo(FabricaTipoPartida tipo, int valor){ 
        this.tipo = tipo; 
        this.valorTipo = valor; 
    }
    public Personaje getPersonaje(Lado lado){ 
        return lado == Lado.IZQUIERDO ? personajeIzquierdo : personajeDerecho; 
    }
    public void setPersonaje(Lado lado, Personaje p){ 
        if (lado == Lado.IZQUIERDO) personajeIzquierdo = p; 
        else personajeDerecho = p; 
    }

    public TipoPartida crearTipoPartida() throws DOPOSoccerException { 
        return tipo.crear(valorTipo); 
    }

    /**
     * Metodo que comprueba que todo este bien
     */
    public void validar() throws DOPOSoccerException {
        if (modalidad == null) {
            throw new DOPOSoccerException("Falta elegir la modalidad de juego", "Elígela en la pantalla de configuración");
        }
        if (tipo == null) {
            throw new DOPOSoccerException("Falta elegir el tipo de partido", "Elige entre partido por tiempo o por goles");
        }
        for (Lado l : Lado.values()) {
            if (getPersonaje(l) == null) {
                throw new DOPOSoccerException("Falta elegir el personaje del jugador " + (l == Lado.IZQUIERDO ? "1" : "2") + ".", "Elige un personaje con las flechas");
            }
        }
    }
}
