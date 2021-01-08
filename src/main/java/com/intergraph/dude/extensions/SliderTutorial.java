package com.intergraph.dude.extensions;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

public class SliderTutorial extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JSlider slider;
	JLabel label;
	
	public SliderTutorial() {
		setLayout(new FlowLayout());
		
		slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
		slider.setMajorTickSpacing(5);
		slider.setPaintTicks(true);
		add(slider);
		
		label = new JLabel("Current value: 0");
		
		event e = new event();
		slider.addChangeListener(e);
				
	}
	
	public class event implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			int value = slider.getValue();
			label.setText("Current value: " + value);
		}
	}
}


