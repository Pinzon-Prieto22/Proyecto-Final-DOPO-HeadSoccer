package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import dominio.CatalogoTiposPartida;
import dominio.ConfiguracionJuego;
import dominio.FabricaTipoPartida;
import dominio.Modalidad;

/**
 * Pantalla donde nos permite gestionar toda la configuraicon
 */
public class PantallaConfiguracion extends PantallaBase {
    private final Map<Modalidad, BotonOpcion> botonesModalidad = new EnumMap<Modalidad, BotonOpcion>(Modalidad.class);
    private final Map<Resolucion, BotonOpcion> botonesResolucion = new EnumMap<Resolucion, BotonOpcion>(Resolucion.class);
    private final JPanel panelTipos = new JPanel(new GridLayout(1, 0, 10, 0));
    private final JLabel lblTipo = descripcion();
    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(2, 1, 30, 1));
    private final JLabel lblUnidad = new JLabel("minutos");
    private Modalidad modalidad = Modalidad.PVP;
    private Resolucion resolucion = Resolucion.MEDIANA;
    private FabricaTipoPartida tipo;

    public PantallaConfiguracion(final Navegador navegador) {
        super(navegador);
        setBorder(new EmptyBorder(16, 40, 24, 40));
        add(new Titulo("CONFIGURAR PARTIDA", 44), BorderLayout.NORTH);

        PanelTranslucido caja = new PanelTranslucido(new GridBagLayout());
        caja.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel filaModalidad = new JPanel(new GridLayout(1, 0, 10, 0));
        filaModalidad.setOpaque(false);
        ButtonGroup grupoMod = new ButtonGroup();
        for (final Modalidad m : Modalidad.values()) {
            BotonOpcion b = new BotonOpcion(m.getNombre());
            b.addActionListener(e -> seleccionarModalidad(m));
            grupoMod.add(b);
            filaModalidad.add(b);
            botonesModalidad.put(m, b);
        }

        panelTipos.setOpaque(false);
        ButtonGroup grupoTipos = new ButtonGroup();
        for (final FabricaTipoPartida f : CatalogoTiposPartida.getInstancia().getTodos()) {
            BotonOpcion b = new BotonOpcion(f.getNombre());
            b.addActionListener(e -> seleccionarTipo(f));
            grupoTipos.add(b);
            panelTipos.add(b);
            b.putClientProperty("fabrica", f);
        }
        lblUnidad.setForeground(Color.WHITE);
        lblUnidad.setFont(Estilos.fuente(15));
        JPanel filaValor = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaValor.setOpaque(false);
        filaValor.add(spinner);
        filaValor.add(lblUnidad);

        JPanel filaRes = new JPanel(new GridLayout(1, 0, 10, 0));
        filaRes.setOpaque(false);
        ButtonGroup grupoRes = new ButtonGroup();
        for (final Resolucion r : Resolucion.values()) {
            BotonOpcion b = new BotonOpcion(r.toString());
            b.setFont(Estilos.fuente(14));
            b.addActionListener(e -> resolucion = r);
            grupoRes.add(b);
            filaRes.add(b);
            botonesResolucion.put(r, b);
        }

        int y = 0;
        fila(caja, y++, "Modalidad", filaModalidad);
        fila(caja, y++, "Tipo de partido", panelTipos);
        fila(caja, y++, "", lblTipo);
        fila(caja, y++, "Duraci\u00f3n / meta", filaValor);
        fila(caja, y++, "Resoluci\u00f3n de la cancha", filaRes);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(caja);
        add(centro, BorderLayout.CENTER);

        BotonDorado volver = new BotonDorado("VOLVER");
        BotonDorado siguiente = new BotonDorado("SIGUIENTE");
        volver.addActionListener(e -> navegador.ir(IdPantalla.INICIO));
        siguiente.addActionListener(e -> {
            ConfiguracionJuego c = navegador.getContexto().getConfiguracion();
            c.setModalidad(modalidad);
            c.setTipo(tipo, ((Number) spinner.getValue()).intValue());
            navegador.getContexto().setResolucion(resolucion);
            navegador.ir(IdPantalla.SELECCION);
        });
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        sur.setOpaque(false);
        sur.add(volver);
        sur.add(siguiente);
        add(sur, BorderLayout.SOUTH);
    }

    private static JLabel descripcion() {
        JLabel l = new JLabel(" ");
        l.setForeground(new Color(200, 215, 255));
        l.setFont(Estilos.fuente(13));
        return l;
    }

    private void fila(JPanel p, int y, String texto, Component c) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = y; g.insets = new Insets(6, 6, 6, 6); g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        l.setFont(Estilos.fuente(16));
        p.add(l, g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        p.add(c, g);
    }

    private void seleccionarModalidad(Modalidad m) {
        modalidad = m;
        botonesModalidad.get(m).setSelected(true);
    }

    private void seleccionarTipo(FabricaTipoPartida f) {
        tipo = f;
        for (Component c : panelTipos.getComponents()) {
            if (c instanceof BotonOpcion && ((BotonOpcion) c).getClientProperty("fabrica") == f) ((BotonOpcion) c).setSelected(true);
        }
        lblTipo.setText("<html><body style='width:480px'>" + f.getDescripcion() + "</body></html>");
        lblUnidad.setText(f.getUnidad() + "  (de " + f.getMinimo() + " a " + f.getMaximo() + ")");
        int actual = ((Number) spinner.getValue()).intValue();
        spinner.setModel(new SpinnerNumberModel(Math.max(f.getMinimo(), Math.min(f.getMaximo(), actual)), f.getMinimo(), f.getMaximo(), 1));
    }

    @Override
    public void alMostrar() {
        ConfiguracionJuego c = navegador.getContexto().getConfiguracion();
        seleccionarModalidad(c.getModalidad());
        resolucion = navegador.getContexto().getResolucion();
        botonesResolucion.get(resolucion).setSelected(true);
        List<FabricaTipoPartida> tipos = CatalogoTiposPartida.getInstancia().getTodos();
        FabricaTipoPartida f = tipos.contains(c.getTipo()) ? c.getTipo() : tipos.get(0);
        spinner.setModel(new SpinnerNumberModel(c.getValorTipo(), f.getMinimo(), f.getMaximo(), 1));
        seleccionarTipo(f);
    }

    @Override public IdPantalla getId() { return IdPantalla.CONFIGURACION; }
}
