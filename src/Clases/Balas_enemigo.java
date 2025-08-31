package Clases;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

class Balas_enemigo {
	int x, y, w = 4, h=10;
	int velo =5;
	
	Balas_enemigo(int x, int y) {
		this.x = x;
		this.y= y;
	}
	void update() {
		y += velo;
	}
	boolean Pantalla(int height) { // si la bla esta ne pantalla lo verifica
		return y <= height;
	}
	
	void draw(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.fillRect(x, y, w, h);
		
	}
	Rectangle limites() {
		return new Rectangle(x,y,w,h);
	}
		

}
