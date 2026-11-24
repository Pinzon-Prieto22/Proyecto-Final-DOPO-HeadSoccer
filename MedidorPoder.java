package dominio;

/**
 * Clase responsable de la barra del poder
 */
public class MedidorPoder {
    private double carga;

    public void recargar(double dt) {
        carga = Math.min(Reglas.PODER_MAXIMO, carga + Reglas.PODER_MAXIMO / Reglas.SEGUNDOS_RECARGA_PODER * dt);
    }

    public void sumarGolRecibido() {
        carga = Math.min(Reglas.PODER_MAXIMO, carga + Reglas.PODER_MAXIMO * Reglas.FRACCION_CARGA_POR_GOL_RECIBIDO);
    }

    public void vaciar(){ 
        carga = 0; 
    }
    public boolean estaLleno(){ 
        return carga >= Reglas.PODER_MAXIMO; 
    }
    public double getPorcentaje(){ 
        return carga; 
    }
}
