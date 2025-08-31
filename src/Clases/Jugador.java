package Clases;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Jugador {
    int x, y, w = 40, h = 20;   // posición y tamaño
    int speed = 6;              
    boolean left, right;        

    private BufferedImage imagen;

    Jugador(int x, int y) { 
        this.x = x; 
        this.y = y; 

        try {
        	File archivo = new File("C:/Users/TuUsuario/Juego3/imagen/imagen2.jpg");
        	imagen = ImageIO.read(archivo);

            System.out.println("Ruta absoluta: " + archivo.getAbsolutePath()); // debug
            if (!archivo.exists()) {
                System.out.println("No se encontró la imagen.");
            }
            imagen = ImageIO.read(archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void mover(int dx) {
        x += dx;
        if (x < 0) x = 0;
        if (x > 800 - w) x = 800 - w;
    }

    void update() {
        if (left)  x -= speed;
        if (right) x += speed;
        x = Math.max(10, Math.min(Tablero.WIDTH - w - 10, x));
    }

    void reset(int x, int y) { 
        this.x = x; 
        this.y = y; 
        left = right = false; 
    }

    void draw(Graphics2D g2) {
        if (imagen != null) {
            g2.drawImage(imagen, x, y, null);
        } else {
            g2.setColor(Color.GREEN);
            g2.fillRect(x, y, w, h);                  
            g2.fillRect(x + w/2 - 3, y - 10, 6, 10); 
        }
    }

    Rectangle getBounds() { return new Rectangle(x, y, w, h); }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return w; }
    public int getAlto() { return h; }
}
