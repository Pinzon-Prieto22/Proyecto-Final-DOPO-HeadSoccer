package dominio;

/**
 * efecto poder camerun
 */
public class EfectoElectrocucion extends EfectoTemporal {
    public EfectoElectrocucion() { 
        super("electrocucion", "Electrocutado", Reglas.DURACION_ELECTROCUCION); 
    }

    @Override public boolean bloqueaSalto() { 
        return true; 
    }
    @Override public double factorVelocidad() { 
        return 0.5;
    }
}
