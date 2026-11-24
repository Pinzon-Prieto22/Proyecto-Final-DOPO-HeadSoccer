package dominio;

public class Pie {
    private boolean activo;
    private double tiempo;
    private double enfriamiento;
    private boolean golpeoBalon;
    private boolean golpeoRival;

    public boolean iniciar() {
        if (activo || enfriamiento > 0) return false;
        activo = true; tiempo = 0; golpeoBalon = false; golpeoRival = false;
        return true;
    }

    public void actualizar(double dt){
        if (activo) {
            tiempo += dt;
            if (tiempo >= Reglas.DURACION_PATADA) { activo = false; enfriamiento = Reglas.ENFRIAMIENTO_PATADA; }
        } else if (enfriamiento > 0) {
            enfriamiento -= dt;
        }
    }

    public void cancelar(){ 
        activo = false; tiempo = 0; enfriamiento = 0;
    }

    public boolean estaActivo(){ 
        return activo; 
    }
    public double getProgreso(){ 
        return Math.min(1, tiempo / Reglas.DURACION_PATADA); 
    }

    public double getAngulo() {
        double g = Reglas.GRADOS_INICIO_PATADA + (Reglas.GRADOS_FIN_PATADA - Reglas.GRADOS_INICIO_PATADA) * getProgreso();
        return Math.toRadians(g);
    }

    public boolean yaGolpeoBalon() { 
        return golpeoBalon; 
    }
    public void marcarGolpeoBalon() { 
        golpeoBalon = true; 
    }
    public boolean yaGolpeoRival() { 
        return golpeoRival; 
    }
    public void marcarGolpeoRival() { 
        golpeoRival = true; 
    }
}
