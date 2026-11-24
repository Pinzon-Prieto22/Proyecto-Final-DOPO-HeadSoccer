package presentacion;

import dominio.ConfiguracionJuego;
import dominio.Partida;
import dominio.ResultadoPartida;

public class ContextoJuego {
    private ConfiguracionJuego configuracion = new ConfiguracionJuego();
    private Resolucion resolucion = Resolucion.MEDIANA;
    private Partida partida;
    private ResultadoPartida ultimoResultado;

    public ConfiguracionJuego getConfiguracion() { 
        return configuracion; 
    }
    public void nuevaConfiguracion() { 
        configuracion = new ConfiguracionJuego(); 
    }
    public Resolucion getResolucion() { 
        return resolucion; 
    }
    public void setResolucion(Resolucion r) { 
        this.resolucion = r; 
    }
    public Partida getPartida() { 
        return partida; 
    }
    public void setPartida(Partida p) { 
        this.partida = p; 
    }
    public ResultadoPartida getUltimoResultado() { 
        return ultimoResultado; 
    }
    public void setUltimoResultado(ResultadoPartida r) { 
        this.ultimoResultado = r; 
    }
}
