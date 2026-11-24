package dominio;

public abstract class TipoPartidaBase implements TipoPartida {
    private boolean terminado;
    private Lado ganador;
    private boolean muerteSubita;

    @Override public void iniciar(Partida partida) { 
        terminado = false; ganador = null; muerteSubita = false; 
    }
    @Override public void alPasarElTiempo(Partida partida, double dt) { }
    @Override public boolean haTerminado() { 
        return terminado; 
    }
    @Override public Lado getGanador() { 
        return ganador; 
    }
    @Override public double getTiempoRestante() { 
        return -1; 
    }
    @Override public boolean enMuerteSubita() { 
        return muerteSubita; 
    }

    protected void terminar(Lado ganador) { 
        this.terminado = true; this.ganador = ganador; 
    }
    protected void activarMuerteSubita() { 
        this.muerteSubita = true; 
    }
}
