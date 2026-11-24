package presentacion;

import dominio.ConfiguracionJuego;
import dominio.Partida;
import dominio.RegistroPerfiles;
import dominio.RepositorioPartidas;
import dominio.RepositorioPartidasArchivo;
import dominio.RepositorioPerfilesArchivo;
import dominio.ResultadoPartida;

/** Datos compartidos entre pantallas: perfiles, lo que se va eligiendo, el partido en curso y el último resultado. */
public class ContextoJuego {
    private final RegistroPerfiles registro = new RegistroPerfiles(new RepositorioPerfilesArchivo());
    private final RepositorioPartidas repositorioPartidas = new RepositorioPartidasArchivo();
    private ConfiguracionJuego configuracion = new ConfiguracionJuego();
    private Resolucion resolucion = Resolucion.MEDIANA;
    private Partida partida;
    private boolean partidaAbierta;
    private ResultadoPartida ultimoResultado;

    public RegistroPerfiles getRegistro() { return registro; }
    public RepositorioPartidas getRepositorioPartidas() { return repositorioPartidas; }
    public ConfiguracionJuego getConfiguracion() { return configuracion; }
    public void nuevaConfiguracion() { configuracion = new ConfiguracionJuego(); }
    public Resolucion getResolucion() { return resolucion; }
    public void setResolucion(Resolucion r) { this.resolucion = r; }
    public Partida getPartida() { return partida; }
    /** @param abierta true si el partido viene de un archivo (no tiene configuración asociada) */
    public void setPartida(Partida p, boolean abierta) { this.partida = p; this.partidaAbierta = abierta; }
    public boolean esPartidaAbierta() { return partidaAbierta; }
    public ResultadoPartida getUltimoResultado() { return ultimoResultado; }
    public void setUltimoResultado(ResultadoPartida r) { this.ultimoResultado = r; }
}
