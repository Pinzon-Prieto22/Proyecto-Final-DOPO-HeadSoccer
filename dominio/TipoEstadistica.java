package dominio;

/** Las cuatro estadísticas de un jugador. */
public enum TipoEstadistica {
    SPEED("Speed", "Velocidad de desplazamiento"),
    KICK("Kick", "Daño que hace al rival (patadas y tiros de poder)"),
    JUMP("Jump", "Altura del salto"),
    SURVIVAL("Survival", "Vida máxima");

    private final String nombre;
    private final String descripcion;

    TipoEstadistica(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return nombre; }
}
