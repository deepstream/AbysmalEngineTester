package net.abysmal.etester;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import net.abysmal.engine.entities.Entity;
import net.abysmal.engine.entities.Player;
import net.abysmal.engine.graphics.Panel;
import net.abysmal.engine.handlers.misc.Movement;
import net.abysmal.engine.maths.Math;
import net.abysmal.engine.maths.Vector;

@SuppressWarnings("serial")
public class ContentPanel extends Panel {

	BufferedImage img = new BufferedImage(Start.width, Start.height, BufferedImage.TYPE_INT_RGB);
	int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	boolean moving = false;
	Vector[] bp = new Vector[0x80];
	int bpLength = bp.length;
	int count = 0;

	Vector v = new Vector(300, 300);
	Vector lc = new Vector(200, 25);
	Vector rc = new Vector(800, 25);
	Vector start = new Vector(25, 25);
	int r = 5;
	Vector[] hpt = {new Vector(-r, -r), new Vector(r, r)};
	Vector ePos = new Vector(20, 200);
	Entity e = new Entity(ePos);
	int clock = 0;

	public ContentPanel() {
		Start.p.setHitboxPoints(hpt);
		e.setHitboxPoints(hpt);
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, Start.width, Start.height);
		if (!moving && Start.w.mouseListener.getClickInfo()[MouseEvent.BUTTON3][4] == 1) {
			System.out.println("df");
			rc.x = (float) this.getMousePosition().getX();
			rc.y = (float) this.getMousePosition().getY();
			drawCurve(new Vector(25, 25), lc, rc, v);
		}
		if (!moving && Start.w.mouseListener.getClickInfo()[MouseEvent.BUTTON1][4] == 1) {
			System.out.println("df");
			lc.x = (float) this.getMousePosition().getX();
			lc.y = (float) this.getMousePosition().getY();
			drawCurve(new Vector(25, 25), lc, rc, v);
		}
//		if (Start.w.keyboardListener.getCurrentlyPressedKeys()[KeyEvent.VK_W][0] == 1) {
////			p.pos = start;
////			p.pos.x = start.x;
////			p.pos.y = start.y;
//			Start.p.teleport(start);
//			moving = true;
//			bp = calcBezierPoints(start, lc, rc, v, bpLength);
//			count = 0;
//			System.out.println("Go!");
//			for(Vector v: bp){
//				System.out.println(v.x + ", " + v.y);
//			}
//		}
//		if(moving){
//			moving = !Movement.walkToBezier(bp, p);
//		}
//			Movement.walkToVector(new Vector(300, 300), p);
		if (moving && !Start.p.hitbox.detectCollision(e.getHitbox())) {
			System.out.println(count+1);
			if (Movement.walkToVector(bp[count], Start.p, 2)) {
				count++;
				if (count >= bp.length) moving = false;
			}
		}
		
		g.drawImage(img, 0, 0, null);
		g.setColor(new Color(0x00ff00));
		g.fillRect((int) v.getX() - r / 3, (int) v.getY() - r / 3, (r / 3) * 2 + 1, (r / 3) * 2 + 1);
		g.setColor(new Color(0x0000ff));
		g.fillRect((int) Start.p.getX() - r, (int) Start.p.getY() - r, r * 2 + 1, r * 2 + 1);
		g.setColor(new Color(0xff0000));
		g.fillRect((int) e.getX() - r, (int) e.getY() - r, r * 2 + 1, r * 2 + 1);
// System.out.println(p.getAcceleration());
//		if (p.getX() < 0 || p.getX() >= this.getWidth() || p.getY() < 0 || p.getY() >= this.getHeight()) {
//			p.setX(400);
//			p.setY(200);
//		}
		clock++;
		g.dispose();
	}

	public void drawCurve(Vector p0, Vector p1, Vector p2, Vector p3) {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
		float segmentCount = bpLength;
		float t = 0.0f;
		for (int i = 0; i <= segmentCount; i++) {
			t = i / (float) segmentCount;
			Vector pixel = Math.calculateBezierPoint(t, p0, p1, p2, p3);
			int x = (int) pixel.x;
			int y = (int) pixel.y;
			if (x >= Start.width || y >= Start.height || x < 0 || y < 0) continue;
			pixels[x + y * Start.width] = 0x555555;
		}
	}

	public Vector[] calcBezierPoints(Vector p0, Vector p1, Vector p2, Vector p3, int segCount) {
		float segmentCount = segCount - 1;
		float t = 0.0f;
		Vector[] result = new Vector[segCount];
		for (int i = 0; i < segmentCount; i++) {
			t = i / (float) segmentCount;
			Vector pixel = Math.calculateBezierPoint(t, p0, p1, p2, p3);
			result[i] = new Vector((int)pixel.x, (int)pixel.y);
		}
		result[(int)segmentCount] = p3;
		return result;
	}

}
