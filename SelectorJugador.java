package presentacion;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import dominio.CatalogoPersonajes;
import dominio.Lado;
import dominio.Personaje;

/**
 * Panel de la tarjeta del jugador
 */
public class SelectorJugador extends PanelTranslucido {
    private final SelectorPersonaje personaje;
    private final JLabel lblTitulo = new JLabel(" ", SwingConstants.CENTER);

    public SelectorJugador(Lado lado) {
        super(new BorderLayout(6, 6));
        setBorder(new EmptyBorder(10, 14, 10, 14));
        personaje = new SelectorPersonaje(lado, CatalogoPersonajes.getInstancia().getTodos());
        lblTitulo.setFont(Estilos.fuente(15));
        lblTitulo.setForeground(Estilos.DORADO);
        add(lblTitulo, BorderLayout.NORTH);
        add(personaje, BorderLayout.CENTER);
    }

    public void setTitulo(String titulo) { lblTitulo.setText(titulo); }
    public Personaje getPersonaje() { return personaje.getSeleccionado(); }
    public void setPersonaje(Personaje p) { personaje.setSeleccionado(p); }
}
