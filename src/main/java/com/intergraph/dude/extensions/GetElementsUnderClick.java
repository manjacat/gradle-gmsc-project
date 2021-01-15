package com.intergraph.dude.extensions;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.ui.models.GenericListModel;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.web.core.browsing.MapSelectionCurator;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.data.feature.FeatureID;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.map.DefaultMapProducer;
import com.intergraph.web.viewer.map.DisplayCacheElement;
import com.intergraph.web.viewer.map.GAbstractDisplayLayer;
import com.intergraph.web.viewer.map.GMap;

@Plugin(alias = "GetElementsUnderClick", vendor = "Intergraph CS")
public class GetElementsUnderClick extends AbstractPlugin {

	@Action
	@Override
	public void loadOnStart() throws Exception {
		ApplicationContext.getBrowser().getMap().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// nothing to do
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// nothing to do
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// nothing to do
			}

			@Override
			public void mouseEntered(MouseEvent me) {
				// nothing to do
			}

			@Override
			public void mouseClicked(MouseEvent me) {

				DefaultMapProducer mapProducer = context.getMap().getMapProducer();
				GMap gmap = context.getMap();

				// coordinates of click in the map (map window coordinates - not the world coords)
				Point2D pt = mapProducer.toMap(new Point2D.Double(mapProducer.toWorldX(me.getPoint().x),
						mapProducer.toWorldY(me.getPoint().y)));
				
				GUIToolkit.showInfo("Mouse (" + me.getPoint().x + "," + me.getPoint().y + ")");

				// very small rectangle around the click
				Rectangle2D rect = new Rectangle2D.Double(pt.getX(), pt.getY(), 0.00001, 0.00001);

				FeatureID activeFeatId = context.getBrowser().getActiveFeatureID();

				GAbstractDisplayLayer maplayer;
				// list of elements for the result of getAffectedElements method (intersection of rectangle and elements)
				List<DisplayCacheElement> feats = new ArrayList<DisplayCacheElement>();				
				// if there is no activated feature, try to search in every visible feature
				// result is written to feats list
				if (activeFeatId == null) {
					for (GAbstractDisplayLayer _maplayer : gmap.getDisplayLayers()) {
						_maplayer.getAffectedElements(rect, feats);
					}
				// some feature is activated so use just the active feature
				} else {
					String activeFeatName = activeFeatId.getLayer();

					maplayer = gmap.getDisplayLayer(activeFeatName);

					maplayer.getAffectedElements(rect, feats); // feats

				}

				// list of ids of intersected elements
				String[] featIds = new String[feats.size()]; // ids of affected			
				
				// list of GPrimitive found 
				GenericListModel<GPrimitive> features = new GenericListModel<GPrimitive>(); 
																							
				// 
				DisplayCacheElement e;
				for (int i = 0; i < feats.size(); i++) {
					e = feats.get(i);

					if (e.contains(pt)) {

						String c = e.getElement().getAction().getClass().getName();

						if (c.equals("com.intergraph.web.viewer.data.GAction")) {
							features.addListItem(e.getElement());
							featIds[i] = e.getElement().getID();
						}
					}
				}

				// if there is less or exactly 1 elements nothing happens and this elements is selected
				if (features.getSize() <= 1) {
					return;
				}
					
				
				MapSelectionCurator msc = context.getBrowser().getMapSelectionCurator();
				// gets the selected elements from the map
				/* TODO: there is an issue in case you have selected 2 elements in the map and you click on the place
				 * where are more than 1 element at this moment, you get 1 selected element instead of 2, because the click
				 * that calls the dialog selects 1 elements immediately before the dialog is loaded. That is why you get
				 * always one element selected in the dialog. In features variable there is proper count of elements so, it
				 * just need probably a small modification.
				 */
				Object[] selEl = msc.getSelectedElements();
				
				Point screenLocation = new Point();
				screenLocation.setLocation(pt.getX(),pt.getY());
				
				// creates the main dialog for the element selection, features is the GPrimitive list
				COPSelectionDialog ssd = new COPSelectionDialog(features, screenLocation , gmap);
				// selects the selected elements from the map as selected in the list in dialog
				ssd.setSelected(selEl);
				// shows the dialog
				ssd.setVisible(true);

				// return selected elements from the dialog
				List<GPrimitive> selFeats = ssd.getSelectedPrimitives();

				// if none of elements were selected, clears the highlighted and selected elements in the map window
				if (selFeats == null) // no selection
				{
					// clear highlighted elements
					msc.clearHighlightedElements();					
					msc.unSelectElements(featIds);
					return;
				// if some elements were selected
				} else {

					// unselects all of them
					msc.unSelectElements(featIds);

					// array for new selection because .select method has to have the Object[] as the input
					String[] selection = new String[selFeats.size()];

					for (int j = 0; j < selFeats.size(); j++) {
						selection[j] = selFeats.get(j).getID();
					}

					// sets the active feature - precondition is that all elements are form the same feature 
					// this is the on way how to do it in GMSC - that is why I can use get(0) - the first element owner
					// because the rest is the same
					Feature featureToAtivate = context.getProject()
							.getFeatureByTitleOrID(selFeats.get(0).getPrimitiveOwner());
					featureToAtivate.setActive(true);
					// select chosen elements as selected
					msc.select(selection);
					// clear highlighted elements
					msc.clearHighlightedElements();
				}

			}

		});
	}
}
