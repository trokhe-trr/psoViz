package driver2d;
import java.awt.Dimension;

import javax.swing.JFrame;

import pso.PsoConfigOptions;



public class Init2D {

	public static void main (String[] args) {

		int w = 900;
		int h = 700;
		
		PsoConfigOptions options = new PsoConfigOptions();
		options.c1 = 0.1f;
		options.c2 = 0.0001f;
		options.speedLimit = 10.0f;
		
		UiControls2D c = new UiControls2D(options);
		Driver2d swarmDriver = new Driver2d(w, h, 500, options);
		
		JFrame imgFrame = new JFrame();
		imgFrame.add(swarmDriver);
		imgFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imgFrame.setSize(w, h);
		swarmDriver.setMinimumSize(new Dimension(w, h));
		swarmDriver.setPreferredSize(new Dimension(w, h));
		imgFrame.pack();
		imgFrame.setLocation(440, 10);
		imgFrame.setVisible(true);
		
		swarmDriver.start();
	}
	
}
