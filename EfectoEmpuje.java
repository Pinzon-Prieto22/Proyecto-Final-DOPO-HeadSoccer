package dominio;
/**
 * efecto poder corea
 */
public class EfectoEmpuje extends EfectoTemporal {
    public EfectoEmpuje() { 
        super("empuje", "Empujado", Reglas.DURACION_EMPUJE); 
    }

    @Override
    public void enCadaPaso(Jugador victima, double dt) {
        double fuerza = Reglas.VELOCIDAD_EMPUJE * (getTiempoRestante() / getDuracion());
        victima.fijarImpulso(-victima.getDireccion() * fuerza); 
    }
}
