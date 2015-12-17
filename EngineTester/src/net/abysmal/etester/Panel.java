package net.abysmal.etester;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;
import net.abysmal.engine.entities.Player;
import net.abysmal.engine.handlers.misc.Movement;
import net.abysmal.engine.maths.Math;
import net.abysmal.engine.maths.Vector3;

@SuppressWarnings("serial")
public class Panel extends JPanel {

	BufferedImage img = new BufferedImage(Start.w, Start.h, BufferedImage.TYPE_INT_RGB);
	int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	boolean moving = false;
	Vector3[] bp = new Vector3[0x20];
	int bpLength = bp.length;
	int count = 0;

	Vector3 v = new Vector3(300, 300);
	Vector3 lc = new Vector3(200, 25);
	Vector3 rc = new Vector3(800, 25);
	Vector3 start = new Vector3(25, 25);
	Player p = new Player(start);
	int r = 5;
	Vector3[] hpt = {new Vector3(-r, -r), new Vector3(r, r)};
	int clock = 0;

	public Panel() {
		p.setHitboxPoints(hpt);
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, Start.w, Start.h);
		if (!moving && Start.m.getClickInfo()[MouseEvent.BUTTON3][4] == 1) {
			System.out.println("df");
			rc.x = (float) this.getMousePosition().getX();
			rc.y = (float) this.getMousePosition().getY();
			drawCurve(new Vector3(25, 25), lc, rc, v);
		}
		if (!moving && Start.m.getClickInfo()[MouseEvent.BUTTON1][4] == 1) {
			System.out.println("df");
			lc.x = (float) this.getMousePosition().getX();
			lc.y = (float) this.getMousePosition().getY();
			drawCurve(new Vector3(25, 25), lc, rc, v);
		}
		if (Start.k.getCurrentlyPressedKeys()[KeyEvent.VK_W][0] == 1) {
//			p.pos = start;
//			p.pos.x = start.x;
//			p.pos.y = start.y;
			p.teleport(start);
			moving = true;
			bp = calcBezierPoints(start, lc, rc, v, bpLength);
			count = 0;
			System.out.println("Go!");
			for(Vector3 v: bp){
				System.out.println(v.x + ", " + v.y);
			}
		}
//		if(moving){
//			moving = !Movement.walkToBezier(bp, p);
//		}
//			Movement.walkToVector(new Vector3(300, 300), p);
		if (moving) {
			System.out.println(count+1);
			boolean m = Movement.walkToVector(bp[count], p);
			if (m) {
				count++;
				if (count >= bp.length) moving = false;
			}
		}

		g.drawImage(img, 0, 0, null);
		g.setColor(new Color(0x00ff00));
		g.fillRect((int) v.getX() - r / 3, (int) v.getY() - r / 3, (r / 3) * 2 + 1, (r / 3) * 2 + 1);
		g.setColor(new Color(0x0000ff));
		g.fillRect((int) p.getX() - r, (int) p.getY() - r, r * 2 + 1, r * 2 + 1);
// System.out.println(p.getAcceleration());
//		if (p.getX() < 0 || p.getX() >= this.getWidth() || p.getY() < 0 || p.getY() >= this.getHeight()) {
//			p.setX(400);
//			p.setY(200);
//		}
		clock++;
		g.dispose();
	}

	public void drawCurve(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
		float segmentCount = bpLength;
		float t = 0.0f;
		for (int i = 0; i <= segmentCount; i++) {
			t = i / (float) segmentCount;
			Vector3 pixel = Math.calculateBezierPoint(t, p0, p1, p2, p3);
			int x = (int) pixel.x;
			int y = (int) pixel.y;
			if (x >= Start.w || y >= Start.h || x < 0 || y < 0) continue;
			pixels[x + y * Start.w] = 0x555555;
		}
	}

	public Vector3[] calcBezierPoints(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3, int segCount) {
		float segmentCount = segCount - 1;
		float t = 0.0f;
		Vector3[] result = new Vector3[segCount];
		for (int i = 0; i < segmentCount; i++) {
			t = i / (float) segmentCount;
			Vector3 pixel = Math.calculateBezierPoint(t, p0, p1, p2, p3);
			result[i] = new Vector3((int)pixel.x, (int)pixel.y);
		}
		result[(int)segmentCount] = p3;
		return result;
	}

}
