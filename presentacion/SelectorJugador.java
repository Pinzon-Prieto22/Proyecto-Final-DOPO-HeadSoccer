package presentacion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import dominio.CatalogoPersonajes;
import dominio.FabricaMaquina;
import dominio.Lado;
import dominio.Perfil;
import dominio.Personaje;
import dominio.RegistroPerfiles;

/** Tarjeta de un jugador: personaje + (perfil humano con estadísticas, o información de la máquina). */
public class SelectorJugador extends PanelTranslucido {
    private final SelectorPersonaje personaje;
    private final PanelPerfil perfil;
    private final JLabel lblTitulo = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblMaquina = new JLabel(" ", SwingConstants.CENTER);
    private final CardLayout cartas = new CardLayout();
    private final JPanel inferior = new JPanel(cartas);

    public SelectorJugador(Lado lado, RegistroPerfiles registro) {
        super(new BorderLayout(6, 6));
        setBorder(new EmptyBorder(10, 14, 10, 14));
        personaje = new SelectorPersonaje(lado, CatalogoPersonajes.getInstancia().getTodos());
        perfil = new PanelPerfil(registro);

        lblTitulo.setFont(Estilos.fuente(15));
        lblTitulo.setForeground(Estilos.DORADO);
        lblMaquina.setFont(Estilos.fuente(15));
        lblMaquina.setForeground(Color.WHITE);
        JPanel panelMaquina = new JPanel(new GridBagLayout());
        panelMaquina.setOpaque(false);
        panelMaquina.add(lblMaquina);

        inferior.setOpaque(false);
        inferior.add(perfil, "perfil");
        inferior.add(panelMaquina, "maquina");

        add(lblTitulo, BorderLayout.NORTH);
        add(personaje, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
    }

    public void configurarHumano(String titulo) {
        lblTitulo.setText(titulo);
        cartas.show(inferior, "perfil");
    }

    public void configurarMaquina(String titulo, FabricaMaquina m) {
        lblTitulo.setText(titulo);
        lblMaquina.setText("<html><div style='text-align:center;width:330px'><b>M\u00e1quina " + m.getNombre() + "</b><br>" + m.getDescripcion()
                + "<br><br>Sus estad\u00edsticas est\u00e1n en nivel 1 y no acumula puntos.</div></html>");
        cartas.show(inferior, "maquina");
    }

    public Personaje getPersonaje() { return personaje.getSeleccionado(); }
    public void setPersonaje(Personaje p) { personaje.setSeleccionado(p); }
    public Perfil getPerfil() { return perfil.getPerfil(); }
    public void setPerfil(Perfil p) { perfil.setPerfil(p); }
    public void recargarPerfiles(Perfil elegir) { perfil.recargarLista(elegir); }
}
