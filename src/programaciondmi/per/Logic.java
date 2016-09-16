package programaciondmi.per;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logic implements Observer {
	private PApplet app;
	private CommunicationManager com;

	// Positions and direction for drawing the ball;
	private int x;
	private int y;
	private int dir;
	private int vel;
	// Indicates the number of connected members
	int connnected;

	// Indicates if the ball is currently in my screen
	boolean canDraw;

	public Logic(PApplet app) {
		// TODO Auto-generated constructor stub
		this.app = app;
		this.com = new CommunicationManager();
		com.addObserver(this);
		new Thread(com).start();
		vel = 1;
		x = 0;
		y = 0;
		canDraw = false;
		
		if (com.getIdentifier() == 3) {
			// Send data to the first screen to start
			int receiver =1;
			int position = 0;
			ContentMessage message = new ContentMessage(com.getIdentifier(), receiver, position, 100);
			System.out.println("Sending content to " + receiver);
			com.sendObjectMessage(message);
		}
	}

	public void paint() {
		app.background(155);
		// Draw Identifier
		drawIdentifier();

		// Draw the bouncing ball
		drawBall();
		
		// Evaluate position an send messages
		evaluatePosition();
	}

	private void drawIdentifier() {
		app.fill(255, 0, 0);
		app.ellipse(app.width / 2, 10, 20, 20);
		app.fill(255);
		app.rectMode(PApplet.CENTER);
		app.text(com.getIdentifier(), app.width / 2, 10);
	}

	private void drawBall() {
		if (canDraw) {
			app.ellipse(x, y, 50, 50);
			x += dir * vel;
		}else{
			app.rectMode(PApplet.CENTER);
			app.text("waiting the ball", app.width/2, app.height/2);
		}

	}

	private void evaluatePosition() {
		// Control what happen when the ball reaches the limits
		if ((x > app.width || x < 0) && canDraw) {
			ContentMessage message;
			int receiver;
			int position;
			switch (com.getIdentifier()) {
			case 1:
				if (dir > 0) {
					// Send a message to the next screen
					receiver = 2;
					position = 0;
					message = new ContentMessage(com.getIdentifier(), receiver, position, 100);
					System.out.println("Sending content to " + receiver);
					com.sendObjectMessage(message);
					canDraw = false;
				} else if (dir < 0) {
					// bounce
					dir = 1;
				}
				break;
			case 2:
				if (dir > 0) {
					receiver = 3;
					position = 0;

				} else {
					receiver = 1;
					position = app.width;
				}
				message = new ContentMessage(com.getIdentifier(), receiver, position, 100);
				System.out.println("Sending content to " + receiver);
				com.sendObjectMessage(message);
				canDraw = false;
				break;
			case 3:
				if (dir > 0) {
					// bounce
					dir = -1;
				} else if (dir < 0) {
					// Send a message to the next screen
					receiver = 2;
					position = app.width;
					message = new ContentMessage(com.getIdentifier(), receiver, position, 100);
					System.out.println("Sending content to " + receiver);
					com.sendObjectMessage(message);
					canDraw = false;
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (arg instanceof ContentMessage) {
			ContentMessage message = (ContentMessage) arg;
			System.out.println("Content received from " + message.getSender());
			if (com.getIdentifier() == message.getReceiver()) {
				switch (com.getIdentifier()) {
				case 1:
					if (message.getSender() == 2) {
						canDraw = true;
						dir = -1;
						x = message.getX();
						y = message.getY();
					}
					if (message.getSender() == 3) {
						canDraw = true;
						dir = 1;
						x = message.getX();
						y = message.getY();
					}
					break;
				case 2:
					if (message.getSender() == 1) {
						canDraw = true;
						dir = 1;
						x = message.getX();
						y = message.getY();
					}
					if (message.getSender() == 3) {
						canDraw = true;
						dir = -1;
						x = message.getX();
						y = message.getY();
					}
					break;
				case 3:
					if (message.getSender() == 2) {
						canDraw = true;
						dir = 1;
						x = message.getX();
						y = message.getY();
					}
					break;
				default:
					break;
				}
			}
		}
	}

}