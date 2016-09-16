package programaciondmi.per;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logic implements Observer{
	private PApplet app;
	private CommunicationManager com;
	public Logic(PApplet app) {
		// TODO Auto-generated constructor stub
		this.app = app;
		this.com = new CommunicationManager();
		new Thread(com).start();
	}

	public void paint() {
		app.fill(255,0,0);
		app.ellipse(app.width/2, app.height/2, 100, 100);
		app.fill(255);
		app.text(com.getIdentifier(), app.width/2, app.height/2);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}