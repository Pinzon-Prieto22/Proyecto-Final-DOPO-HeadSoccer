package dominio;

/**
 * Clase que se encargad de las colisiones del partido
 */
public class MotorFisica {
    private final PartidaListener notificador;

    public MotorFisica(PartidaListener notificador) { 
        this.notificador = notificador; 
    }

    public void resolver(Partida p) {
        Balon b = p.getBalon();
        Cancha c = p.getCancha();
        Jugador izq = p.getJugador(Lado.IZQUIERDO);
        Jugador der = p.getJugador(Lado.DERECHO);

        separarJugadores(izq, der);
        golpeDePie(izq, der);
        golpeDePie(der, izq);
        for (Jugador j : new Jugador[] { izq, der }) {
            pieContraBalon(j, b, p);
            cuerpoContraBalon(j, b, p);
        }
        for (Lado l : Lado.values()) travesano(b, c.getArco(l));
        b.aplicarLimites(c);
        b.limitarVelocidad();
    }

    /** Devuelve el lado del arco en el que entró el balón real por completo, o null si no hubo gol. */
    public Lado detectarGol(Partida p) {
        for (Lado l : Lado.values()) {
            if (p.getCancha().getArco(l).contieneCompleto(p.getBalon())) return l;
        }
        return null;
    }

    private void separarJugadores(Jugador a, Jugador b) {
        Circulo[] ca = { a.getCirculoCabeza(), a.getCirculoTorso() };
        Circulo[] cb = { b.getCirculoCabeza(), b.getCirculoTorso() };
        double requerido = 0;
        for (Circulo p : ca) {
            for (Circulo q : cb) {
                double dy = p.getY() - q.getY();
                double suma = p.getRadio() + q.getRadio();
                if (Math.abs(dy) < suma) requerido = Math.max(requerido, Math.sqrt(suma * suma - dy * dy));
            }
        }
        double dx = b.getX() - a.getX();
        if (requerido > 0 && Math.abs(dx) < requerido) {
            double falta = requerido - Math.abs(dx);
            double signo = dx >= 0 ? 1 : -1;
            a.desplazarX(-signo * falta / 2);
            b.desplazarX(signo * falta / 2);
        }
    }

    private void golpeDePie(Jugador atacante, Jugador victima) {
        Pie pie = atacante.getPie();
        if (!pie.estaActivo() || pie.yaGolpeoRival() || pie.yaGolpeoBalon()) return;
        Circulo cp = atacante.getCirculoPie();
        if (cp.intersecta(victima.getCirculoCabeza()) || cp.intersecta(victima.getCirculoTorso())) {
            pie.marcarGolpeoRival();
            double danio = atacante.getEstadisticas().getDanioPatada();
            victima.recibirDanio(danio);
            notificador.alRecibirDanio(victima, danio);
        }
    }

    private void pieContraBalon(Jugador j, Balon b, Partida p) {
        Pie pie = j.getPie();
        if (!pie.estaActivo()) return;
        Circulo cp = j.getCirculoPie();
        double dx = b.getX() - cp.getX(), dy = b.getY() - cp.getY();
        double dist = Math.hypot(dx, dy);
        if (dist >= b.getRadio() + cp.getRadio()) return;

        if (pie.yaGolpeoBalon()) {            // ya lo pateó en esta patada: solo lo empuja fuera
            chocarCirculo(b, cp, j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
            return;
        }
        pie.marcarGolpeoBalon();
        double nx = dist < 1e-6 ? j.getDireccion() : dx / dist;
        double ny = dist < 1e-6 ? 0 : dy / dist;
        double ang = pie.getAngulo();
        double tx = -j.getDireccion() * Math.sin(ang), ty = -Math.cos(ang);   // dirección del recorrido del pie
        double ux = 0.6 * tx + 0.4 * nx, uy = 0.6 * ty + 0.4 * ny;
        double n = Math.hypot(ux, uy);
        ux /= n; uy /= n;
        double v = Reglas.FUERZA_PATADA;   // la fuerza del balón no depende de Kick
        b.setVelocidad(ux * v + 0.3 * j.getVx(), uy * v + 0.3 * j.getVy());
        double sep = b.getRadio() + cp.getRadio();
        b.setPosicion(cp.getX() + nx * sep, cp.getY() + ny * sep);
        contactoConJugador(b, j, p);
    }

    private void cuerpoContraBalon(Jugador j, Balon b, Partida p) {
        boolean toco = chocarCirculo(b, j.getCirculoCabeza(), j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
        toco |= chocarCirculo(b, j.getCirculoTorso(), j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
        if (toco) contactoConJugador(b, j, p);
    }

    private void contactoConJugador(Balon b, Jugador j, Partida p) {
        if (b.tienePoder() && b.getLanzador() != j) {
            Poder poder = b.getPoder();
            Jugador autor = b.getLanzador();
            double danio = poder.calcularDanio(autor);
            Efecto e = poder.aplicarA(j, autor, p);
            b.descartarPoder();
            b.frenar(0.5);
            notificador.alRecibirDanio(j, danio);
            notificador.alAplicarEfecto(j, e);
            return;
        }
        if (b.tienePoder()) return;           // el balón de su propio tiro: no hace nada
        b.setUltimoToque(j);
        if (j.tieneAura()) {
            j.dispararPoder(b);
            notificador.alLanzarPoder(j);
        }
    }

    private void travesano(Balon b, Arco arco) {
        boolean choco = chocarRectangulo(b, arco.getX(), arco.getYTravesano(), arco.getAncho(),
                Reglas.GROSOR_TRAVESANO, Reglas.REBOTE_TRAVESANO);
        if (choco && b.tienePoder()) b.descartarPoder();
    }

    static boolean chocarCirculo(Balon b, Circulo c, double cvx, double cvy, double rebote) {
        double dx = b.getX() - c.getX(), dy = b.getY() - c.getY();
        double min = b.getRadio() + c.getRadio();
        double d2 = dx * dx + dy * dy;
        if (d2 >= min * min) return false;
        double d = Math.sqrt(d2);
        double nx = d < 1e-6 ? 0 : dx / d;
        double ny = d < 1e-6 ? -1 : dy / d;
        b.setPosicion(c.getX() + nx * min, c.getY() + ny * min);
        double rvx = b.getVx() - cvx, rvy = b.getVy() - cvy;
        double vn = rvx * nx + rvy * ny;
        if (vn < 0) {
            rvx -= (1 + rebote) * vn * nx;
            rvy -= (1 + rebote) * vn * ny;
            b.setVelocidad(rvx + cvx, rvy + cvy);
        }
        return true;
    }

    static boolean chocarRectangulo(Balon b, double rx, double ry, double rw, double rh, double rebote) {
        double cx = Math.max(rx, Math.min(b.getX(), rx + rw));
        double cy = Math.max(ry, Math.min(b.getY(), ry + rh));
        double dx = b.getX() - cx, dy = b.getY() - cy;
        double d2 = dx * dx + dy * dy;
        double r = b.getRadio();
        if (d2 >= r * r) return false;
        double d = Math.sqrt(d2);
        double nx, ny;
        if (d < 1e-6) { nx = 0; ny = -1; } else { nx = dx / d; ny = dy / d; }
        b.setPosicion(cx + nx * r, cy + ny * r);
        double vn = b.getVx() * nx + b.getVy() * ny;
        if (vn < 0) b.setVelocidad(b.getVx() - (1 + rebote) * vn * nx, b.getVy() - (1 + rebote) * vn * ny);
        return true;
    }
}
