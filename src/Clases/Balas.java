package Clases;

import java.awt.*;

/**
 * Clase Bullet: balas que dispara el jugador.
 */
class Balas {
    int x, y, w = 4, h = 10;
    int velo = 10;

    Balas(int x, int y) { this.x = x; this.y = y; }

    // Sube recto hacia arriba
    void update() { y -= velo; }

    // Verifica si sigue en pantalla
    boolean isOnScreen() { return y + h >= 0; }

    // Dibuja la bala
    void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, w, h);
    }

    Rectangle getBounds() { return new Rectangle(x, y, w, h); }
}
