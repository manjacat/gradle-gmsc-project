package com.hexagon.gmsc.sample;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.event.*;

import com.intergraph.web.viewer.map.DefaultMapProducer;
import com.intergraph.web.viewer.map.GMap;

public class SliderTutorial implements ChangeListener {

	JSlider slider;
	JLabel label;
	JPanel panel;
	JFrame frame;
	GMap map;
	double xcoord;
	double ycoord;
	
	public SliderTutorial(GMap my_map) {
		
		//Temp: WIP.
		//seems like I don't need to create this. will remove later
		
		map = my_map;		
		int scaleConv = (int) map.getScale();
		int sliderConv = 0;
		if (scaleConv > 25000)
		{
			sliderConv = 25000;
		}
		else {
			sliderConv = scaleConv;
		}
		slider = new JSlider(0, 25000, sliderConv);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setPreferredSize(new Dimension(100, 350));
		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(500);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.setPaintLabels(true);
		
		DefaultMapProducer mapProducer = map.getMapProducer();
		// coordinates of click in the map (map window coordinates - not the world coords)
		Point2D pt = mapProducer.toMap(new Point2D.Double(833,458));
		xcoord = pt.getX();
		ycoord = pt.getY();
		
		label = new JLabel("value: " + slider.getValue() + " (" + xcoord + "," + ycoord + ")");
		panel = new JPanel();		
		
		panel.add(slider);
		panel.add(label);
		
		frame = new JFrame("Zoom Slider");
		frame.setLayout(new FlowLayout());
		frame.add(panel);
		frame.setSize(240,500);		
		frame.setVisible(true);		
		
		slider.addChangeListener(this);				
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		//int scaleConv = (int) map.getScale();		
		//map.setCenterAndScale(xcoord, ycoord, slider.getValue());
		//update the new Point2D
		DefaultMapProducer mapProducer = map.getMapProducer();
		Point2D pt = mapProducer.toMap(new Point2D.Double(833,458));
		xcoord = pt.getX();
		ycoord = pt.getY();
		map.setCenterAndScale(xcoord, ycoord, slider.getValue());
		label.setText("value: " + slider.getValue() + " (" + xcoord + "," + ycoord + ")");

	}
}