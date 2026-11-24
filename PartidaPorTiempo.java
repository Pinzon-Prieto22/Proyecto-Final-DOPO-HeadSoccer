package dominio;

public class PartidaPorTiempo extends TipoPartidaBase {
    private final int minutos;
    private double restante;

    public PartidaPorTiempo(int minutos) {
        this.minutos = minutos;
        this.restante = minutos * 60.0;
    }

    @Override public String getNombre() { 
        return "Por tiempo (" + minutos + " min)"; 
    }

    @Override
    public void iniciar(Partida partida) { 
        super.iniciar(partida); restante = minutos * 60.0; 
    }

    @Override
    public void alPasarElTiempo(Partida partida, double dt) {
        if (haTerminado() || enMuerteSubita()) return;
        restante -= dt;
        if (restante > 0) return;
        restante = 0;
        int i = partida.getMarcador().getGoles(Lado.IZQUIERDO);
        int d = partida.getMarcador().getGoles(Lado.DERECHO);
        if (i != d) terminar(i > d ? Lado.IZQUIERDO : Lado.DERECHO);
        else activarMuerteSubita();
    }

    @Override
    public void alAnotarse(Partida partida, Lado anotador) {
        if (enMuerteSubita()) terminar(anotador);
    }

    @Override public double getTiempoRestante() { 
        return restante; 
    }
}
