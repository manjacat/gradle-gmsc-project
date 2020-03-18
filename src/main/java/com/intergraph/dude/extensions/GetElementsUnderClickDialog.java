package com.intergraph.dude.extensions;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import com.intergraph.tools.ui.DefaultDialog;
import com.intergraph.tools.ui.controls.GenericListRenderer;
import com.intergraph.tools.ui.controls.IListValueProvider;
import com.intergraph.tools.ui.models.GenericListModel;
import com.intergraph.tools.utils.Languages;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.map.GMap;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class COPSelectionDialog extends DefaultDialog {

	private final GenericListModel<GPrimitive> listModel;
	private JList<GPrimitive> list;
	private final Point screenLocation;
	private final GMap map;
	private Object[] selEl;

	COPSelectionDialog(GenericListModel<GPrimitive> listModel, Point screenLocation, GMap map) {
		super(ApplicationContext.getMainFrame(), "More elements have been found.");
		this.listModel = listModel;
		this.screenLocation = screenLocation;
		this.map = map;
		this.selEl = null;		
	}

	@Override
	protected JComponent getDisplayView() {
		JPanel panel = new JPanel(new MigLayout("ins 0", "[grow]", "[][grow]"));
		panel.setOpaque(false);
		panel.add(new JLabel("List of elements"), "wrap");

		listModel.sortContent(new Comparator<GPrimitive>() {
			Collator collator = Collator.getInstance(Languages.getLocale());

			@Override
			public int compare(GPrimitive o1, GPrimitive o2) {
				return collator.compare(o1.getID(), o2.getID());
			}

		});

		list = new JList<>(listModel);

		// select selected elements from map in dialog
		if (selEl != null) {
			this.setSelectedItems();
		}

		list.setVisibleRowCount(5);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setSelectionBackground(new Color(0, 155, 194));
		list.setSelectionForeground(new Color(255, 255, 255));
		list.setCellRenderer(getListRenderer());
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Doubleclick in the list
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
					ok();

				// Singleclick in the list - check if the clicked item has the same feature (is from the same feautere)
				// as other selected elements - in this case you can select more elements, otherwise the previous selection
				// is unselected and the clicked element is selected and you can select more elements, but again from the
				// same feature
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
					ApplicationContext.getBrowser().getMapSelectionCurator().clearHighlightedElements();

					@SuppressWarnings("rawtypes")
					JList list = (JList) e.getSource();
					// row (index) that you clicked in
					int row = list.locationToIndex(e.getPoint());

					// GPrimitive on the clicked row
					GPrimitive f = (GPrimitive) list.getModel().getElementAt(row);
					// Feature - the owner of GPrimitive f
					String featureOfPrimitive = f.getPrimitiveOwner();

					// iterates over previously selected elements and if the element has not the same feature it is not 
					// added as newly selected
					ArrayList<Integer> rowIds = new ArrayList<>();
					ArrayList<Integer> elementIds = new ArrayList<>();
					// for each selected item from the list
					for (int i : list.getSelectedIndices()) {
						// get GPrimitive
						GPrimitive fComp = (GPrimitive) list.getModel().getElementAt(i);
						// compare FeatureID of the newly selected elements with previously selected elements
						// if equals add rowid into the list
						// real Id of features are added as well into the elementIds list for highlighting
						if (featureOfPrimitive == fComp.getPrimitiveOwner()) {
							rowIds.add(i);
							elementIds.add(Integer.parseInt(fComp.getID()));
						}
					}

					// cast Object[] to int[], because of setSelectedIndices can't use Object[]
					int[] intA = new int[rowIds.size()];
					for (int i = 0; i < intA.length; i++) {
						intA[i] = rowIds.get(i);
					}

					// set selected elements in the dialog list
					list.setSelectedIndices(intA);
					// set active feature
					Feature tempHighlightFeat = ApplicationContext.getProject()
							.getFeatureByTitleOrID(featureOfPrimitive);
					tempHighlightFeat.setActive(true);
					// highlight the selected elements from dialog in the map
					ApplicationContext.getBrowser().getMapSelectionCurator().highlight(elementIds.toArray());

				}

			}
		});

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane, "grow");

		return panel;

	}

	@Override
	protected boolean isUserInputValid() {
		return true;
	}

	private GenericListRenderer<GPrimitive> getListRenderer() {
		return new GenericListRenderer<GPrimitive>(new IListValueProvider<GPrimitive>() {
			// TODO: put your values here what you need to see in the dialog
			/* In case you need to get more attributes than ID, etc. to the client, add them into the table 
			 * RPI_FEATURE in GMSC schema like ATTRIBUTE1, ATTTRIBUTE2, ... comma separated and you can reach them
			 * on the client side like GPrimitive.getCustomValues(). ... you have the example few lines belo
			 */
			@Override
			public String getDisplayValue(GPrimitive value) {
				String elDesc = value.getID();
				String elFeature = value.getPrimitiveOwner();

				elDesc = "Id: " + elDesc + ", FeatureId: " + elFeature;
				
				//access the customvalue 
				//value.getCustomValues().get("ATTRIBUTE1");

				return elDesc;
			}

			@Override
			public String getTooltip(GPrimitive arg0) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}

	/**
	 * @see com.intergraph.tools.ui.DefaultDialog#alignDialog()
	 */
	@Override
	protected void alignDialog() {
		if (screenLocation != null) {
			Point temp = map.getLocationOnScreen();
			Rectangle mapBounds = new Rectangle(temp.x, temp.y, map.getWidth(), map.getHeight());
			Rectangle dialogBounds = new Rectangle(screenLocation.x, screenLocation.y, getWidth(), getHeight());

			if (dialogBounds.getMaxY() > mapBounds.getMaxY())
				screenLocation.y = screenLocation.y - getHeight();

			if (dialogBounds.getMaxX() > mapBounds.getMaxX())
				screenLocation.x = screenLocation.x - getWidth();

			setLocation(screenLocation.x, screenLocation.y);
		} else
			setLocationRelativeTo(ApplicationContext.getMainFrame());
	}

	public GPrimitive getSelectedPrimitive() {
		if (this.wasCanceled() || this.list.getSelectedIndex() < 0) {
			ApplicationContext.getBrowser().getMapSelectionCurator().clearHighlightedElements();			
			return null;
		}

		return list.getSelectedValue();
	}

	public List<GPrimitive> getSelectedPrimitives() {
		if (this.wasCanceled() || this.list.getSelectedIndex() < 0) {
			ApplicationContext.getBrowser().getMapSelectionCurator().clearHighlightedElements();
			return null;
		}

		return list.getSelectedValuesList();
	}

	public void setSelected(Object[] sel) {
		selEl = sel;
	}

	protected void setSelectedItems() {

		if (this.listModel.isEmpty()) {
			return;
		}

		// select already selected elements in dialog
		if (selEl.length > 0) {
			for (int j = 0; j < selEl.length; j++) {
				String selId = selEl[j].toString();
				for (int i = 0; i < listModel.getSize(); i++) {
					String id = listModel.get(i).getID();
					if (id.equals(selId)) {
						this.list.addSelectionInterval(i, i);
						break;
					}
				}
			}

		} else {
			list.setSelectedIndex(0);
		}

	}
}	


