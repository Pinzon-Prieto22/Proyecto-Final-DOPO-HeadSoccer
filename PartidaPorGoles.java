package dominio;

public class PartidaPorGoles extends TipoPartidaBase {
    private final int meta;

    public PartidaPorGoles(int meta) { 
        this.meta = meta; 
    }

    @Override public String getNombre() { 
        return "Por goles (a " + meta + ")"; 
    }

    @Override
    public void alAnotarse(Partida partida, Lado anotador) {
        if (partida.getMarcador().getGoles(anotador) >= meta) terminar(anotador);
    }
}
