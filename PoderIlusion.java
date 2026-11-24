package dominio;
/**
 * Poder de usa, crea balones falsos y dannio de 30
 */
public class PoderIlusion extends Poder {
    public PoderIlusion(){
        super("ilusion", "Ilusión", "El balón se divide en 8 idénticos; 7 son falsos y desaparecen a los 2,5 s. El rival no sabe cuál es el real.", 30);
    }

    @Override public Efecto crearEfecto(){ 
        return new EfectoIlusion(); 
    }
    @Override protected double getVelocidadTiro() { 
        return 1100; 
    }
}
