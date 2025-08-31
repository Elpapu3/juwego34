package Clases;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Tablero extends JPanel implements ActionListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Timer tem = new Timer(16, this); // ~60 FPS
    private final Jugador jugador = new Jugador(WIDTH / 2 - 20, HEIGHT - 60);

    private final ArrayList<Balas> balas = new ArrayList<>();
    private final ArrayList<Ene> ene = new ArrayList<>();
    private final ArrayList<Balas_enemigo> balas_enemigos = new ArrayList<>();

    private int eneDir = 1;
    private int eneVelo = 2;
    private int stepDown = 20;

    private enum Estado { RUNNING, WIN, GAME_OVER }
    private Estado estado = Estado.RUNNING;

    private final Font hudFont = new Font("Consolas", Font.PLAIN, 18);
    private final Font bigFont = new Font("Consolas", Font.BOLD, 36);

    private int puntos = 0;

    // Movimiento jugador
    private boolean izquierda, derecha;

    // Cooldown para disparo
    private long ultimoDisparo = 0;
    private final int tiempoRecarga = 300; // ms

    public Tablero() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        initEne();
        initKeys();
        tem.start();
    }

    // Asegura que el panel reciba foco
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    private void initEne() {
        ene.clear();
        int filas = 4, columnas = 10;
        int startX = 80, startY = 80;
        int sepX = 60, sepY = 50;

        for (int i = 0; i < filas; i++)
            for (int j = 0; j < columnas; j++)
                ene.add(new Ene(startX + j * sepX, startY + i * sepY));

        eneDir = 1;
        eneVelo = 2;
    }

    private void initKeys() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> izquierda = true;
                    case KeyEvent.VK_RIGHT -> derecha = true;
                    case KeyEvent.VK_Z -> disparar();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> izquierda = false;
                    case KeyEvent.VK_RIGHT -> derecha = false;
                }
            }
        });
    }

    private void disparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo >= tiempoRecarga) {
            balas.add(new Balas(jugador.getX() + jugador.getAncho() / 2 - 2, jugador.getY()));
            ultimoDisparo = ahora;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        jugador.draw(g2);
        for (Balas b : balas) b.draw(g2);
        for (Ene e : ene) e.draw(g2);
        for (Balas_enemigo b : balas_enemigos) b.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(hudFont);
        g2.drawString("Puntaje: " + puntos, 10, 20);

        if (estado == Estado.WIN) drawCentered(g2, "¡GANASTE!");
        if (estado == Estado.GAME_OVER) drawCentered(g2, "GAME OVER");
    }

    private void drawCentered(Graphics2D g2, String text) {
        g2.setFont(bigFont);
        int w = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (WIDTH - w) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (estado != Estado.RUNNING) return;

        // Movimiento jugador
        if (izquierda) jugador.mover(-5);
        if (derecha) jugador.mover(5);

        // Actualizar balas jugador
        ArrayList<Balas> balasParaEliminar = new ArrayList<>();
        for (Balas b : balas) {
            b.update();
            if (!b.isOnScreen()) balasParaEliminar.add(b);
        }
        balas.removeAll(balasParaEliminar);

        // Movimiento enemigos
        if (!ene.isEmpty()) {
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            for (Ene en : ene) {
                minX = Math.min(minX, en.x);
                maxX = Math.max(maxX, en.x + en.w);
            }

            if (minX <= 10 || maxX >= WIDTH - 10) {
                eneDir *= -1;
                for (Ene en : ene) en.y += stepDown;
                if (eneVelo < 6) eneVelo++;
            }
            for (Ene en : ene) en.x += eneDir * eneVelo;
        }

        // Disparo enemigos
        for (Ene en : ene) {
            if (Math.random() < 0.01 && balas_enemigos.size() < 10)
                balas_enemigos.add(new Balas_enemigo(en.x + en.w / 2, en.y + en.h));
        }

        // Actualizar balas enemigos
        ArrayList<Balas_enemigo> balasEnemParaEliminar = new ArrayList<>();
        for (Balas_enemigo b : balas_enemigos) {
            b.update();
            if (!b.Pantalla(HEIGHT)) balasEnemParaEliminar.add(b);
            else if (b.limites().intersects(jugador.getBounds())) estado = Estado.GAME_OVER;
        }
        balas_enemigos.removeAll(balasEnemParaEliminar);

        // Colisiones
        ArrayList<Balas> balasGolpeadas = new ArrayList<>();
        ArrayList<Ene> enemigosGolpeados = new ArrayList<>();
        for (Balas b : balas) {
            for (Ene en : ene) {
                if (b.getBounds().intersects(en.getBounds())) {
                    balasGolpeadas.add(b);
                    enemigosGolpeados.add(en);
                    puntos += 10;
                    break;
                }
            }
        }
        balas.removeAll(balasGolpeadas);
        ene.removeAll(enemigosGolpeados);

        // Victoria
        if (ene.isEmpty()) estado = Estado.WIN;

        repaint();
    }
}