package dominio;

/** Estados Unidos: al golpear al rival el balón se divide en 8 balones idénticos (7 falsos) durante 2,5 s. Daño 30. */
public class PoderIlusion extends Poder {
    private static final long serialVersionUID = 1L;

    public PoderIlusion() {
        super("ilusion", "Ilusión", "El balón se divide en 8 idénticos; 7 son falsos y desaparecen a los 2,5 s. El rival no sabe cuál es el real.", 30);
    }

    @Override public Efecto crearEfecto() { return new EfectoIlusion(); }
    @Override protected double getVelocidadTiro() { return 1100; }
}
