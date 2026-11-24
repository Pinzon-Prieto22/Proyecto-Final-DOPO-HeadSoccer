package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import dominio.Lado;
import dominio.Personaje;

/** Selector de personaje: flechas, vista previa, bandera, nombre y tiro de poder con su daño. */
public class SelectorPersonaje extends JPanel {
    private final List<Personaje> lista;
    private final VistaPersonaje vista;
    private final JLabel lblNombre = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblPoder = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblDescripcion = new JLabel(" ", SwingConstants.CENTER);
    private int indice;

    public SelectorPersonaje(Lado lado, List<Personaje> lista) {
        super(new BorderLayout(6, 6));
        setOpaque(false);
        this.lista = lista;
        this.vista = new VistaPersonaje(lado == Lado.IZQUIERDO ? 1 : -1);
        vista.setPreferredSize(new Dimension(220, 200));

        lblNombre.setFont(Estilos.fuente(18));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setIconTextGap(10);
        lblPoder.setFont(Estilos.fuente(14));
        lblPoder.setForeground(new Color(150, 220, 255));
        lblDescripcion.setFont(Estilos.fuente(12));
        lblDescripcion.setForeground(new Color(200, 210, 235));

        BotonDorado anterior = new BotonDorado("\u25C0");
        BotonDorado siguiente = new BotonDorado("\u25B6");
        for (BotonDorado b : new BotonDorado[] { anterior, siguiente }) b.setPreferredSize(new Dimension(50, 50));
        anterior.addActionListener(e -> anterior());
        siguiente.addActionListener(e -> siguiente());

        JPanel medio = new JPanel(new BorderLayout(8, 0));
        medio.setOpaque(false);
        JPanel izq = new JPanel(new GridBagLayout()); izq.setOpaque(false); izq.add(anterior);
        JPanel der = new JPanel(new GridBagLayout()); der.setOpaque(false); der.add(siguiente);
        medio.add(izq, BorderLayout.WEST);
        medio.add(vista, BorderLayout.CENTER);
        medio.add(der, BorderLayout.EAST);

        JPanel info = new JPanel(new GridLayout(0, 1, 0, 2));
        info.setOpaque(false);
        info.add(lblNombre);
        info.add(lblPoder);
        info.add(lblDescripcion);

        add(medio, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);
        mostrar();
    }

    public void siguiente() { indice = (indice + 1) % lista.size(); mostrar(); }
    public void anterior() { indice = (indice - 1 + lista.size()) % lista.size(); mostrar(); }
    public Personaje getSeleccionado() { return lista.get(indice); }

    public void setSeleccionado(Personaje p) {
        int i = lista.indexOf(p);
        if (i >= 0) { indice = i; mostrar(); }
    }

    private void mostrar() {
        Personaje p = getSeleccionado();
        vista.setPersonaje(p);
        lblNombre.setIcon(new IconoBandera(p.getEquipo().getCodigo(), 44, 30));
        lblNombre.setText(p.getNombre() + " \u00b7 " + p.getEquipo().getPais());
        lblPoder.setText("Tiro: " + p.getPoder().getNombre() + "  (da\u00f1o " + (int) p.getPoder().getDanioBase() + " \u00d7 Kick)");
        lblDescripcion.setText("<html><div style='text-align:center;width:330px'>" + p.getPoder().getDescripcion() + "</div></html>");
    }
}
