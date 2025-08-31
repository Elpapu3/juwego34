package Clases;

import java.awt.*;

/**
 * Clase Alien: representa un enemigo.
 */
class Ene {
    int x, y, w = 30, h = 20;

    Ene(int x, int y) { this.x = x; this.y = y; }

    // Dibuja el alien (rectángulos simples para no usar imágenes)
    void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillRect(x, y, w, h);                     // cuerpo
        g2.fillRect(x - 4, y + h - 4, 6, 4);         // pata izquierda
        g2.fillRect(x + w - 2, y + h - 4, 6, 4);     // pata derecha
    }

    Rectangle getBounds() { return new Rectangle(x, y, w, h); }
}

