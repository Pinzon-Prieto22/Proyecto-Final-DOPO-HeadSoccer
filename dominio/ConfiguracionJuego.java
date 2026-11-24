package dominio;

import java.util.List;

/**
 * Clase configuracionJuego
 * Responsable de las cosas "configurables" que el jugador puede modificar antes de jugar
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class ConfiguracionJuego {
    private Modalidad modalidad = Modalidad.PVP;
    private FabricaTipoPartida tipo;
    private int valorTipo;
    private Personaje personajeIzquierdo;
    private Personaje personajeDerecho;
    private Perfil perfilIzquierdo;
    private Perfil perfilDerecho;
    private FabricaMaquina maquina;

    /**
     * Constructor
     */
    public ConfiguracionJuego(){
        tipo = CatalogoTiposPartida.getInstancia().getTodos().get(0);
        valorTipo = tipo.getValorPorDefecto();
        List<Personaje> todos = CatalogoPersonajes.getInstancia().getTodos();
        personajeIzquierdo = todos.get(0);
        personajeDerecho = todos.get(1);
        maquina = CatalogoMaquinas.getInstancia().getTodas().get(0);
    }

    // metodos getter y setter
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
        this.tipo = tipo; this.valorTipo = valor; 
    }

    public Personaje getPersonaje(Lado lado){ 
        return lado == Lado.IZQUIERDO ? personajeIzquierdo : personajeDerecho; 
    }
    public void setPersonaje(Lado lado, Personaje p){ 
        if (lado == Lado.IZQUIERDO) personajeIzquierdo = p; 
        else personajeDerecho = p; 
    }

    public Perfil getPerfil(Lado lado){ 
        return lado == Lado.IZQUIERDO ? perfilIzquierdo : perfilDerecho; 
    }
    public void setPerfil(Lado lado, Perfil p){ 
        if (lado == Lado.IZQUIERDO) perfilIzquierdo = p; 
        else perfilDerecho = p; 
    }

    public FabricaMaquina getMaquina(){ 
        return maquina; 
    }
    public void setMaquina(FabricaMaquina m){ 
        this.maquina = m; 
    }

    public TipoPartida crearTipoPartida() throws DOPOSoccerException { 
        return tipo.crear(valorTipo); 
    }

    /**
     * MEtodo que verifica que la configuracion este completa y no tenga cosas invalidas
     * @throws DOPOSoccerException
     */
    public void validar() throws DOPOSoccerException {
        for (Lado l : Lado.values()) {
            if (getPersonaje(l) == null) {
                throw new DOPOSoccerException("Falta elegir el personaje del jugador " + (l == Lado.IZQUIERDO ? "1" : "2") + ".", "Elige un personaje con las flechas.");
            }
            if (modalidad.esHumano(l) && getPerfil(l) == null) {
                throw new DOPOSoccerException("Falta elegir el perfil del jugador " + (l == Lado.IZQUIERDO ? "1" : "2") + ".", "Crea o selecciona un perfil.");
            }
        }
        if (modalidad == Modalidad.PVP && perfilIzquierdo == perfilDerecho) {
            throw new DOPOSoccerException("Los dos jugadores no pueden usar el mismo perfil (\"" + perfilIzquierdo.getNombre() + "\").",
                    "Elige un perfil distinto para cada jugador, o crea uno nuevo.");
        }
        if (!modalidad.esHumano(Lado.DERECHO) && maquina == null) {
            throw new DOPOSoccerException("Falta elegir el perfil de la maquina.", "Eligelo en la pantalla de configuracion.");
        }
    }
}
