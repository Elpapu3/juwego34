package Clases;

import javax.swing.*;
public class Main extends JFrame{
	
	public Main() {
		setTitle("Invesil");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		Tablero panel = new Tablero(); //panel donde se dibuja el juego
		setContentPane(panel);// se lo añadimos al frame
		pack(); // se ajusta el tamaño segun el panel
		setLocationRelativeTo(null); // centrear la venta
		setVisible(true);
	}
	public static void main(String[] args) {
		//asi re ejecuta en un hilo separado esta clase
		 SwingUtilities.invokeLater(Main::new);		
	}
}