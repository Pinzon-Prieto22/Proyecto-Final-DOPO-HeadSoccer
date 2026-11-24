package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dominio.CatalogoPersonajes;
import dominio.CatalogoTiposPartida;
import dominio.FabricaTipoPartida;
import dominio.Lado;
import dominio.Personaje;
import dominio.Reglas;

/**
 * Tabla de controles que usa la pantalla de ayuda y pausa
 */
public class PanelAyuda extends JPanel {
    public PanelAyuda() {
        super(new BorderLayout(0, 10));
        setBackground(Estilos.AZUL_OSCURO);
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(740, 540));

        String[] acciones = { "Mover a la izquierda", "Mover a la derecha", "Saltar", "Patear", "Usar poder" };
        String[] j1 = EsquemaTeclas.para(Lado.IZQUIERDO).getTextos();
        String[] j2 = EsquemaTeclas.para(Lado.DERECHO).getTextos();
        DefaultTableModel modelo = new DefaultTableModel(new String[] { "Acci\u00f3n", "Jugador 1", "Jugador 2" }, 0) {
            @Override public boolean isCellEditable(int f, int c) { return false; }
        };
        for (int i = 0; i < acciones.length; i++) modelo.addRow(new Object[] { acciones[i], j1[i], j2[i] });
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(26);
        tabla.setFont(Estilos.fuente(14));
        tabla.getTableHeader().setFont(Estilos.fuente(14));
        tabla.setEnabled(false);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(100, 26 * 6 + 4));

        JTextArea texto = new JTextArea(construirTexto());
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(Estilos.fuente(14).deriveFont(java.awt.Font.PLAIN));
        texto.setBackground(new Color(245, 245, 250));
        texto.setBorder(new EmptyBorder(8, 10, 8, 10));
        texto.setCaretPosition(0);

        add(scrollTabla, BorderLayout.NORTH);
        add(new JScrollPane(texto), BorderLayout.CENTER);
    }

    private static String construirTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("OBJETIVO\n");
        sb.append("Mete el bal\u00f3n en el arco contrario. Despu\u00e9s de cada gol, los personajes vuelven a su posici\u00f3n inicial y el bal\u00f3n cae desde el centro.\n\n");
        sb.append("TIPOS DE PARTIDO\n");
        for (FabricaTipoPartida f : CatalogoTiposPartida.getInstancia().getTodos()) {
            sb.append("  \u2022 ").append(f.getNombre()).append(": ").append(f.getDescripcion()).append('\n');
        }
        sb.append("\nVIDA Y GOLPES\n");
        sb.append("  \u2022 Una patada hace da\u00f1o solo si el rival est\u00e1 al alcance de tu pie. Si la patada toca el bal\u00f3n, solo lo impulsa.\n");
        sb.append("  \u2022 Cuando la vida de un jugador llega a cero queda aturdido ").append((int) Reglas.DURACION_ATURDIMIENTO)
          .append(" s (no puede moverse, saltar ni patear); luego recupera toda su vida.\n\n");
        sb.append("PODERES\n");
        sb.append("  \u2022 La barra se llena en ").append((int) Reglas.SEGUNDOS_RECARGA_PODER).append(" s y sube un cuarto cada vez que recibes un gol.\n");
        sb.append("  \u2022 Con la barra llena pulsa la tecla de poder: la barra se vac\u00eda (y empieza a recargarse) y tu personaje queda con un AURA que no se acaba.\n");
        sb.append("  \u2022 Al tocar el bal\u00f3n con el aura, sale tu tiro de poder y el aura desaparece. No puedes activar otro poder mientras tengas el aura.\n");
        sb.append("  \u2022 Si el tiro golpea al rival, recibe da\u00f1o y el efecto del poder (el nuevo efecto reemplaza al anterior). Si entra al arco sin tocarlo, es gol sin da\u00f1o ni efecto.\n");
        for (Personaje p : CatalogoPersonajes.getInstancia().getTodos()) {
            sb.append("      - ").append(p.getPoder().getNombre()).append(" (").append(p.getEquipo().getPais()).append("): ")
              .append(p.getPoder().getDescripcion()).append(" Da\u00f1o ").append((int) p.getPoder().getDanioBase()).append(" \u00d7 Kick.\n");
        }
        sb.append("PAUSA\n  ESC o P pausa el partido. Desde la pausa puedes reanudarlo, terminarlo o salir al men\u00fa.\n");
        return sb.toString();
    }
}
