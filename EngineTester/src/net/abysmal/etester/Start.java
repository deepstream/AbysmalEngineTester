package net.abysmal.etester;

import java.awt.Dimension;
import javax.swing.JFrame;
import net.abysmal.engine.graphics.Window;
import net.abysmal.engine.handlers.HID.Keyboard;
import net.abysmal.engine.handlers.HID.Mouse;

public class Start {

	public static Mouse m;
	public static Keyboard k;
	public static int w = 650;
	public static int h = w / 16 * 9;
	public static void main(String[] args) {
		JFrame f = Window.createWindow("Hi", new Dimension(w, h));
		f.setContentPane(new Panel());
		m = new Mouse();
		k = new Keyboard();
		f.getContentPane().addMouseListener(m);
		f.addKeyListener(k);
		
		new Thread(new Runnable(){
			public void run() {
				while (true){
					f.repaint();
					try{
					Thread.sleep(16);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}

}
