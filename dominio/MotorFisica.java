package dominio;

/**
 * Clase MotorFisica
 * Responsable de las interacciones y colisiones del partido 
 * como lo son el balon, los pies, los cuerpos, los arcos, los limites
 * 
 * @author Bryan Pinzon y Valentina Prieto y ayuda de IA
 * @version Entrega Final Proyecto Final
 */

public class MotorFisica {
    private final PartidaListener notificador;

    /**
     * Constructor
     * @param notificador
     */
    public MotorFisica(PartidaListener notificador) { 
        this.notificador = notificador; 
    }

    /**
     * Metodo que resuelve las colisiones despues del movimiento
     * @param p
     */
    public void resolver(Partida p) {
        // obtenemos los entes y elementos 
        Balon b = p.getBalon();
        Cancha c = p.getCancha();
        Jugador izq = p.getJugador(Lado.IZQUIERDO);
        Jugador der = p.getJugador(Lado.DERECHO);
        //evitamos que los jugadores se superpongan
        separarJugadores(izq, der);
        // Verificamos si los jugadores se han golpeado
        golpeDePie(izq, der);
        golpeDePie(der, izq);
        for (Jugador j : new Jugador[] { izq, der }) {
            // verificamos si patean
            pieContraBalon(j, b, p);
            // verificamos si colisionan con su cuerpo con el balon
            cuerpoContraBalon(j, b, p);
        }
        //verificamos si colisiona con el travesanio
        for (Lado l : Lado.values()) travesano(b, c.getArco(l));
        // consideramos los limites para el balon
        b.aplicarLimites(c);
        // consideramos la velocidad para el balon
        b.limitarVelocidad();
    }

    /**
     * Metodo que nos dice el lado del arco en el que entro el balon
     * @param p
     * @return
     */
    public Lado detectarGol(Partida p) {
        for (Lado l : Lado.values()) {
            if (p.getCancha().getArco(l).contieneCompleto(p.getBalon())) return l;
        }
        return null;
    }

    /**
     * Metodo que separa los jugadores y evitar que se superpongan
     * (Metodo mejorado con ayuda de IA)
     * @param a
     * @param b
     */
    private void separarJugadores(Jugador a, Jugador b) {
        // tomamos los circulos de su cabeza y su torso
        Circulo[] ca = { a.getCirculoCabeza(), a.getCirculoTorso() };
        Circulo[] cb = { b.getCirculoCabeza(), b.getCirculoTorso() };
        double requerido = 0;
        for (Circulo p : ca) {
            for (Circulo q : cb) {
                // calculamos la diferencia entre los centros de su cabeza y su torso
                double dy = p.getY() - q.getY();
                // sumamos los radios de los circulos de su cabeza y su torso respectivamente
                double suma = p.getRadio() + q.getRadio();
                // calcula lo que se deben separar para dejar de superponerse
                if (Math.abs(dy) < suma) requerido = Math.max(requerido, Math.sqrt(suma * suma - dy * dy));
            }
        }
        // calcula la distancia horizontal entre los jugadores
        double dx = b.getX() - a.getX();
        // Si estan muy cerca los separa
        if (requerido > 0 && Math.abs(dx) < requerido) {
            double falta = requerido - Math.abs(dx);
            double signo = dx >= 0 ? 1 : -1;
            a.desplazarX(-signo * falta / 2);
            b.desplazarX(signo * falta / 2);
        }
    }

    /**
     * Metodo que se encarga del dannio del golpe del pie si le pega al rival
     * (Metodo mejorado con ayuda de IA)
     * @param atacante
     * @param victima
     */
    private void golpeDePie(Jugador atacante, Jugador victima) {
        // obtenemos el pie del atacante
        Pie pie = atacante.getPie();
        // si el pie esta bloqueado o ya golpeo al rival o ya golpeo el balon sale
        if (!pie.estaActivo() || pie.yaGolpeoRival() || pie.yaGolpeoBalon()) return;
        // obtenemos el circulo para determinar las colisiones
        Circulo cp = atacante.getCirculoPie();
        // con el circulo vemos si interseca con la cabeza o el torso de la victima
        if (cp.intersecta(victima.getCirculoCabeza()) || cp.intersecta(victima.getCirculoTorso())) {
            // marcamos que golpeo al rival
            pie.marcarGolpeoRival();
            // determinamos el dannio que le pega
            double danio = atacante.getEstadisticas().getDanioPatada();
            // le pegamos el dannio
            victima.recibirDanio(danio);
            // notificamos el dannio
            notificador.alRecibirDanio(victima, danio);
        }
    }

    /**
     * Metodo que se encarga de lo que le pasa el balon al ser pateado
     * (Metodo mejorado con ayuda de IA)
     * @param j
     * @param b
     * @param p
     */
    private void pieContraBalon(Jugador j, Balon b, Partida p) {
        // obtenemos el pie del jugador
        Pie pie = j.getPie();
        // si el pie no esta activo sale
        if (!pie.estaActivo()) return;
        // Obtenemos el circulo del pie para determinar las colisiones
        Circulo cp = j.getCirculoPie();
        // calculamos las distancias de los centros del balon y el pie
        double dx = b.getX() - cp.getX(), 
        dy = b.getY() - cp.getY();
        // calculamos la distancia
        double dist = Math.hypot(dx, dy);
        // si la distancia es mayor ala suma de sus radios sale, porquie no colisiona
        if (dist >= b.getRadio() + cp.getRadio()) return;

        // Si ya pateo el balon (aqui la IA nos ayudo con toda esta parte)
        if (pie.yaGolpeoBalon()) {
            chocarCirculo(b, cp, j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
            return;
        }
        pie.marcarGolpeoBalon();
        double nx = dist < 1e-6 ? j.getDireccion() : dx / dist;
        double ny = dist < 1e-6 ? 0 : dy / dist;
        double ang = pie.getAngulo();
        double tx = -j.getDireccion() * Math.sin(ang), ty = -Math.cos(ang); //direccion del recorrido del pie
        double ux = 0.6 * tx + 0.4 * nx, uy = 0.6 * ty + 0.4 * ny;
        double n = Math.hypot(ux, uy);
        ux /= n; uy /= n;
        double v = Reglas.FUERZA_PATADA;
        b.setVelocidad(ux * v + 0.3 * j.getVx(), uy * v + 0.3 * j.getVy());
        double sep = b.getRadio() + cp.getRadio();
        b.setPosicion(cp.getX() + nx * sep, cp.getY() + ny * sep);
        contactoConJugador(b, j, p);
    }

    /**
     * Metodo que verifica si el cuerpo (cabeza o torso) choca con el balon
     * (Metodo mejorado con ayuda de IA)
     * @param j
     * @param b
     * @param p
     */
    private void cuerpoContraBalon(Jugador j, Balon b, Partida p) {
        boolean toco = chocarCirculo(b, j.getCirculoCabeza(), j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
        toco |= chocarCirculo(b, j.getCirculoTorso(), j.getVx(), j.getVy(), Reglas.REBOTE_JUGADOR);
        if (toco) contactoConJugador(b, j, p);
    }

    /**
     * Metodo que se encarga de lo que pasa cuando un jugador toca el balon
     * @param b
     * @param j
     * @param p
     */
    private void contactoConJugador(Balon b, Jugador j, Partida p) {
        // si el balon ttiene poder y el lanzador fue el rival hace dannio
        if (b.tienePoder() && b.getLanzador() != j) {
            Poder poder = b.getPoder();
            // obtenemos quien lo lanzo
            Jugador autor = b.getLanzador();
            //calculamos el danio
            double danio = poder.calcularDanio(autor);
            // aplicamos el efecto al jugador
            Efecto e = poder.aplicarA(j, autor, p);
            // le quitamos el poder al balon y lo frenamos
            b.descartarPoder();
            b.frenar(0.5);
            // notificamos lo que paso
            notificador.alRecibirDanio(j, danio);
            notificador.alAplicarEfecto(j, e);
            return;
        }
        // si el balon tiene poder y choco con el mismo que lo lanzo no pasas nada
        if (b.tienePoder()) return; 
        // registramos el ultimo toque del balon
        b.setUltimoToque(j);
        // si el jugador tiene aura (poder activo) dispara con poder
        if (j.tieneAura()) {
            j.dispararPoder(b);
            notificador.alLanzarPoder(j);
        }
    }

    /**
     * Metodo que nos dice si el balon choco con el travesannio del arco
     * (Metodo mejorado con ayuda de IA)
     * @param b
     * @param arco
     */
    private void travesano(Balon b, Arco arco) {
        boolean choco = chocarRectangulo(b, arco.getX(), arco.getYTravesano(), arco.getAncho(),
                Reglas.GROSOR_TRAVESANO, Reglas.REBOTE_TRAVESANO);
        // si choco y tenia poder le quitamos el poder al balon
        if (choco && b.tienePoder()) b.descartarPoder();
    }

    /**
     * Metodo que nos dice si el balon choco contra un circulo
     * (Metodo mejorado con ayuda de IA)
     * @param b
     * @param c
     * @param cvx
     * @param cvy
     * @param rebote
     * @return true si hubo contacto
     */
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

    /**
     * Metodo que nos dice si el balon choco con el travesannio
     * (Metodo mejorado con ayuda de IA)
     * @param b
     * @param rx
     * @param ry
     * @param rw
     * @param rh
     * @param rebote
     * @return true si se choco
     */
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
