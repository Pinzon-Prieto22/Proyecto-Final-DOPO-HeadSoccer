package dominio;
/**
 * Poder corea sur, tiro recto horizontal, empuja y dannio 40
 */
public class PoderAuraAzul extends Poder {
    public PoderAuraAzul() {
        super("aura_azul", "Aura Azul", "Tiro recto y horizontal a alta velocidad. Empuja al rival hacia su propio arco durante 1,5 s.", 40);
    }

    @Override public Efecto crearEfecto() { 
        return new EfectoEmpuje(); 
    }
    @Override protected double getVelocidadTiro() { 
        return 1500; 
    }
    @Override protected double getInclinacion() { 
        return 0; 
    }
}
