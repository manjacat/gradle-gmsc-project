package com.intergraph.dude.extensions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.ImageIcon;

import com.intergraph.service.ServiceFactory;
import com.intergraph.services.emea._2011._03.geometry.GeometryServiceGetGeometryInvalidRequestFaultFaultMessage;
import com.intergraph.services.emea._2011._03.geometry.GeometryServiceGetGeometryUnauthorizedFaultFaultMessage;
import com.intergraph.services.emea._2011._03.types.ArrayOfGeometry;
import com.intergraph.services.emea._2011._03.types.FeatureRight;
import com.intergraph.services.emea._2011._03.types.Geometry;
import com.intergraph.services.emea._2011._03.types.GeometryRequest;
import com.intergraph.tools.ui.GUIToolkit;
import com.intergraph.tools.utils.Assertion;
import com.intergraph.tools.utils.IOUtils;
import com.intergraph.tools.utils.Languages;
import com.intergraph.tools.utils.Utils;
import com.intergraph.tools.utils.disptach.ActionDispatcher;
import com.intergraph.tools.utils.disptach.MiniDispatcher;
import com.intergraph.tools.utils.disptach.RPAction;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.tools.utils.disptach.annotations.Plugin;
import com.intergraph.tools.utils.log.Log;
import com.intergraph.web.core.browsing.Browser;
import com.intergraph.web.core.browsing.DefaultMeasureVertexFilter;
//import com.intergraph.web.core.browsing.MapSelectionCurator;
import com.intergraph.web.core.data.ConvertUtils;
//import com.intergraph.web.core.data.FeatureFilterFactory;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.data.loader.ValidationException;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.core.kernel.job.Job;
import com.intergraph.web.core.kernel.plugin.AbstractPlugin;
//import com.intergraph.web.core.kernel.plugin.PluginContext;
import com.intergraph.web.core.warehouse.featurestorage.Storage;
import com.intergraph.web.plugin.edit.ActionConfiguration;
import com.intergraph.web.plugin.edit.EditConfigurationException;
import com.intergraph.web.plugin.edit.EditSettings;
import com.intergraph.web.plugin.edit.ValidationResultDialog;
import com.intergraph.web.plugin.edit.actions.EditAction;
import com.intergraph.web.plugin.edit.actions.EditActionConditionFactory;
import com.intergraph.web.plugin.edit.actions.EditControllerInterface;
import com.intergraph.web.plugin.edit.actions.UndoAction;
import com.intergraph.web.plugin.edit.actions.interfaces.IRepeatableAction;
import com.intergraph.web.plugin.edit.datamodel.EditDataModelHandler;
import com.intergraph.web.plugin.edit.datamodel.EditSelectionManager;
import com.intergraph.web.plugin.edit.datamodel.primitives.EditPrimitive;
import com.intergraph.web.plugin.edit.datamodel.primitives.EditPrimitiveFactory;
import com.intergraph.web.plugin.edit.persistence.DefaultPersistenceProvider;
import com.intergraph.web.plugin.edit.persistence.IPersistenceProvider;
import com.intergraph.web.plugin.edit.persistence.PersistenceElement;
import com.intergraph.web.plugin.edit.ui.IEditUserInterface;
import com.intergraph.web.plugin.edit.validation.ValidatorFactory;
//import com.intergraph.web.plugin.webbrowser.handler.Deferred;
import com.intergraph.web.viewer.data.GAction;
import com.intergraph.web.viewer.data.GBounds;
import com.intergraph.web.viewer.data.GDefaultDataLayer;
//import com.intergraph.web.viewer.data.GDefaultDataLayer.InIDFilter;
import com.intergraph.web.viewer.data.GLayerFilter;
import com.intergraph.web.viewer.data.GPrimitive;
import com.intergraph.web.viewer.map.GMap;
import com.intergraph.web.viewer.map.MapState;
import com.intergraph.web.viewer.map.style.symbolizers.FeatureTypeStyleFactory;
//import com.intergraph.web.viewer.measurement.MeasurementController;
import com.intergraph.web.viewer.measurement.SmartSnapHandler;
import com.intergraph.web.viewer.reader.gdoplus.GDOInputStreamHandler;
import com.intergraph.web.viewer.reader.gdoplus.types.GDOCompatibleException;
import com.intergraph.web.viewer.reader.gdoplus.types.GDOFactory;
import com.intergraph.web.viewer.reader.gdoplus.types.GDONotSupportedException;

@Plugin(alias = "IprEditPlugin", vendor = "Intergraph Ges.m.b.H.", parameters = {
		@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geoservice", type = com.intergraph.tools.utils.disptach.annotations.Parameter.ParameterType.URL, description = "webservice for saving geometries") })
public class IprEditPlugin extends AbstractPlugin implements EditControllerInterface {
	private static enum State {
		NOTINITIALIZED, PREPARING, IDLE, RUNNING;

		private State() {
		}
	}

	protected static final String[] PRIVILEGED_ACTION_COMMANDS = { "exit", "showHelp", "showLogFile",
			"showLogDirectory", "EXPAND_NODE", "TREE_EXPAND", "TREE_COLLAPS", "HIDE_ALL", "refreshView", "zoomIn",
			"zoomOut", "zoomBox", "navigateLeft", "navigateRight", "navigateUp", "navigateDown", "pan",
			"setLastZoomBox", "fitProject", "fitSelection", "showElementInfo", "ACM_POINT", "ACM_LOADFILE",
			"ACM_DISTANCE", "ACM_LENGTHENING", "ACM_POLYLINE", "ACM_POLYLINEWITHRELATION", "ACM_ARC",
			"ACM_ARCBY3POINTS", "ACM_DELETE_SINGLE", "ACM_DELETE_ALL", "ACM_SEGMENT_BUFFER", "ACM_AREA_BUFFER",
			"ACM_SEGMENT_RELATION", "ACM_CHAIN", "ACM_CHAIN_DELTA", "startGPS", "deleteAllGPSPrimitives", "ACM_GPS" };

	public static final String PREPROCESSORS = "PreProcessors";

	public static final String POSTPROCESSORS = "PostProcessors";

	private State state;

	private EditAction currentaction;

	private EditDataModelHandler datamodelhandler;

	private IEditUserInterface editUserInterface;

	private Job loadingjob;

	private EditSettings editSettings;

	private String[] featuresToReloadIds;

	public IprEditPlugin() {
		loadingjob = null;
		state = State.NOTINITIALIZED;
	}

	@Action(actionLocation = "HIDDEN", shortCut = "ctrl shift E")
	public void COPYELEMENTS_EDIT() throws EditConfigurationException {
		Browser browser = context.getBrowser();

		EditSettings defaultsettings = new EditSettings();

		if (browser.getMapSelectionCurator().getSelectedElementsCount() > 0)
			defaultsettings
					.setElementIds(Utils.asModifyableList(browser.getMapSelectionCurator().getSelectedElements()));
		defaultsettings.setFeature(browser.getMapSelectionCurator().getActiveFeature());
		defaultsettings.setActions(ActionConfiguration.parseMapActions(new String[] { "GE_NEWPOINT", "GE_NEWTEXT",
				"GE_NEWPOLYLINE", "GE_NEWPOLYGON", "GE_NEWRECTANGLE", "GE_NEWCIRCLE", "GE_NEWARC", "GE_NEWARCBYCENTER",
				"GE_NEWARCBYTHREEPOINTS", "GE_MODIFY", "GE_ROTATE", "GE_TEXTCHANGE", "GE_SUBSTRACT", "GE_MERGE",
				"GE_DELETECOLLECTIONPART", "GE_SPLITPOLYGON", "GE_SPLITPOLYLINE", "GE_MERGEPOLYLINES",
				"GE_ADDPOINTTOCOLLECTION", "GE_MOVE", "GE_ADOPT", "GE_DELETE", "GE_RESET" }));

		defaultsettings.setPersistenceProvider(new DefaultPersistenceProvider());
		defaultsettings.setMeasureVertexFilter(new DefaultMeasureVertexFilter());

		startEdit(defaultsettings);
	}

	void runAction(EditAction action, RPAction rpAction) {
		if (!abortRunningAction()) {
			return;
		}
		if (!isIdle()) {
			GUIToolkit.showError(Languages.getString("geometryedit.error.notidle"));
			return;
		}

		assertIdle();

		getMap().getMeasurementController().abortAll();
		resetMap2Idle();

		try {
			setState(State.RUNNING);
			updateState(rpAction);

			currentaction = action;
			currentaction.run();
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, null, e);
			currentaction = null;
			setState(State.IDLE);
		}
	}

	protected void resetMap2Idle() {
		try {
			GMap gmap = context.getMap();
			if (gmap.getState() != MapState.IDLE) {
				gmap.setState(MapState.IDLE);
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			Log.getLogger().finest(e.getMessage());
		}
	}

	private boolean isIdle() {
		return state == State.IDLE;
	}

	private void assertIdle() {
		if (state != State.IDLE) {
			throw new IllegalStateException("State IDLE was expected, but is " + state);
		}
	}

	private void assertRunning() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("State RUNNING was expected, but is " + state);
		}
	}

	private void assertNotInitialized() {
		if (state != State.NOTINITIALIZED) {
			throw new IllegalStateException("State NOT_INITIALIZED was expected, but is " + state);
		}
	}

	public void setFeaturesToReloadIds(String[] featuresToReload) {
		if (featuresToReload != null) {
			featuresToReloadIds = featuresToReload;
		}
	}

	private void setState(State newstate) {
		state = newstate;
		updateState();
	}

	private GLayerFilter createEditFilter(List<? extends Object> ids) {
		if (!Assertion.isValid(ids)) {
			return null;
		}
		String[] toFilter = new String[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			toFilter[i] = ids.get(i).toString();
		}

		return new GDefaultDataLayer.InIDFilter(toFilter);
	}

	public void actionFinished() {
		assertRunning();
		clearInfoText();

		EditAction restartAction = currentaction;

		currentaction = null;
		setState(State.IDLE);

		if ((restartAction instanceof IRepeatableAction)) {
			runAction(restartAction, null);
		}
	}

	public void actionAborted() {
		assertRunning();
		clearInfoText();
		currentaction = null;
		setState(State.IDLE);
	}

	public EditDataModelHandler getDataModelHandler() {
		if ((state == State.PREPARING) || (state == State.NOTINITIALIZED)) {
			throw new IllegalStateException(
					"state is " + state + " but a state > " + State.PREPARING + " was expected!");
		}
		return datamodelhandler;
	}

	public GMap getMap() {
		return context.getMap();
	}

	private boolean abortRunningAction() {
		if (state == State.RUNNING) {
			if (!currentaction.isAbortable()) {
				GUIToolkit.showInfo(Languages.getString("geometryedit.info.actionrunning"));
				return false;
			}

			currentaction.abort();
		}
		return true;
	}

	void reset() {
		IPersistenceProvider persistenceProvider = editSettings.getPersitenceProvider();
		if (!persistenceProvider.shouldDiscardChanges()) {
			return;
		}
		persistenceProvider.discardChanges(getProperties());
		endEdit();
	}

	void undo(RPAction action) {
		if ((state == State.RUNNING) && (currentaction.isUndoable())) {
			currentaction.undo();
			return;
		}

		runAction(new UndoAction(this), action);
	}

	void save() {
		Job loadingjob = new Job(Languages.getString("geometryedit.state.save"));
		try {
			EditPrimitive[] prims = datamodelhandler.getAll();
			List<Object> todelete = datamodelhandler.getIDsOfElementsToDelete();

			if ((!Assertion.isValid(prims)) && (!Assertion.isValid(todelete))) {
				return;
			}
			Log.getLogger().finest("Persist changes ...");

			List<PersistenceElement> persistenceElements = PersistenceElement.create(prims, todelete);
			ValidatorFactory.validate(persistenceElements);
			IPersistenceProvider persistenceProvider = getEditSettings().getPersitenceProvider();
			persistenceProvider.persistChanges(getProperties(), getEditSettings().getFeature(), persistenceElements);

			endEdit();

			if (persistenceProvider.isPostProcessingRequired()) {
				persistenceProvider.runPostProcessing();
			}
		} catch (ValidationException ex) {
			ValidationResultDialog validationDialog = new ValidationResultDialog(ex.getValidationErrors());
			validationDialog.setVisible(true);

			if (validationDialog.isDiscardChanges()) {
				editSettings.getPersitenceProvider().discardChanges(getProperties());
				endEdit();
				return;
			}
		} catch (Throwable throwable) {
			Log.getLogger().log(Level.SEVERE, "Error during save!", throwable);
			GUIToolkit.showError(Languages.getString("geometryedit.error.not_saved"));
			
		} finally {
			if (loadingjob != null)
				loadingjob.close();
			loadingjob = null;
		}
	}

	private void endEdit() {
		if (state == State.NOTINITIALIZED) {
			return;
		}
		if (editSettings.getMeasureVertexFilter() != null) {
			SmartSnapHandler smartSnapHandler = getMap().getMeasurementController().getSmartSnapHandler();
			smartSnapHandler.removeMeasureVertexFilter(editSettings.getMeasureVertexFilter());
		}

		abortRunningAction();

		EditActionConditionFactory.getInstance().tearDownAfterEdit(getDataModelHandler());
		setState(State.NOTINITIALIZED);

		context.getMapSelectionCurator().setActiveFeatureLocked(false);

		if (datamodelhandler != null) {
			datamodelhandler.dispose();
		}
		datamodelhandler = null;
		currentaction = null;

		Feature sourceFeature = editSettings.getFeature();
		sourceFeature.clearFilter();
		sourceFeature.setActive(true);
		sourceFeature.reloadWithDependencies();

		//Reload of defined features, original reloads wasn't changed
		List<Feature> featuresToReload = new ArrayList<>();
		if (featuresToReloadIds != null) {
			try {
				for (String featureId : featuresToReloadIds) {
					Feature editFeature = ApplicationContext.getProject().getFeatureByTitleOrID(featureId);
					if (editFeature == null)
					{
						Log.getLogger().log(Level.WARNING, String.format("Feature, which should be reloaded '%s' was not found in the project!",featureId));
					} 
					else {
						featuresToReload.add(ApplicationContext.getProject().getFeatureByTitleOrID(featureId));
					}
					
				}
				ApplicationContext.getBrowser().reloadFeatures(featuresToReload);			
			} 
			catch (Exception e){
				Log.getLogger().log(Level.WARNING, null,e);				
			}
		}

		editSettings = null;

		ActionDispatcher.getInstance().setEnableOthers(PRIVILEGED_ACTION_COMMANDS, true);
		ActionDispatcher.getInstance().actionConditionChanged();

		removeEditSelector();
	}

	public void startEdit(EditSettings editsettings) throws EditConfigurationException {

		editSettings = editsettings;
		assertNotInitialized();
		if (editUserInterface != null) {
			Log.getLogger().warning("Edit Panel is already in use. Can't create a new one!");
			return;
		}

		if (!Assertion.isValid(editsettings.getActions())) {
			throw new EditConfigurationException("Missing action configuration!");
		}
		if (editsettings.getFeature() == null) {
			throw new EditConfigurationException("Missing feature configuration!");
		}
		if ((editsettings.getFeature().getFeatureRight() != FeatureRight.MODIFY)
				&& (editsettings.getFeature().getFeatureRight() != FeatureRight.ANALYZE)) {
			throw new EditConfigurationException(String.format("Insufficient privileges to edit feature: %s",
					new Object[] { editsettings.getFeature() }));
		}
		editUserInterface = editsettings.getUserInterface();

		getMap().getMeasurementController().abortAll();
		resetMap2Idle();

		prepareForEdit(editsettings);
	}

	public void removeEditSelector() {
		editUserInterface.tearDown();
		editUserInterface = null;
	}

	protected List<GPrimitive> getGeometriesToEdit(EditSettings editSettings)
			throws GeometryServiceGetGeometryUnauthorizedFaultFaultMessage, GDOCompatibleException, IOException,
			GDONotSupportedException, GeometryServiceGetGeometryInvalidRequestFaultFaultMessage {
		List<GPrimitive> geometriesToEdit = new ArrayList<GPrimitive>();
		if (!Assertion.isValid(editSettings.getElementIds())) {
			return geometriesToEdit;
		}
		GeometryRequest geometryRequest = new GeometryRequest();
		geometryRequest.setCredentials(ApplicationContext.getClientCredentials());
		geometryRequest.setFeatureId(editSettings.getFeature().getID().getIntValue());
		geometryRequest.setFilter(ConvertUtils.convertToValueListFilter(editSettings.getElementIds()));

		ArrayOfGeometry arrayOfGeometry = ServiceFactory.getGeometryService().getGeometry(geometryRequest);
		List<Geometry> geometries = arrayOfGeometry.getGeometry();
		for (Geometry geometry : geometries) {
			if (geometry.getGdoGeometry() != null) {

				DataInputStream dataInputStream = null;
				try {
					GDOInputStreamHandler inputStreamHandler = new GDOInputStreamHandler(
							new ByteArrayInputStream(geometry.getGdoGeometry()));
					Throwable localThrowable3 = null;
					try {
						geometriesToEdit
								.add(GDOFactory.getInstance().readPrimitive(new GAction(geometry.getIdentifier()),
										FeatureTypeStyleFactory.createAreaStyle(), inputStreamHandler));
					} catch (Throwable localThrowable1) {
						localThrowable3 = localThrowable1;
						throw localThrowable1;
					} finally {
						if (inputStreamHandler != null)
							if (localThrowable3 != null)
								try {
									inputStreamHandler.close();
								} catch (Throwable localThrowable2) {
									localThrowable3.addSuppressed(localThrowable2);
								}
							else
								inputStreamHandler.close();
					}
				} finally {
					IOUtils.closeQuietly(dataInputStream);
				}
			}
		}
		return geometriesToEdit;
	}

	private void prepareForEdit(EditSettings editSettings) {
		assertNotInitialized();
		setState(State.PREPARING);

		loadingjob = new Job(Languages.getString("geometryedit.state.info"));
		try {

			Feature sourceFeature = editSettings.getFeature();

			List<GPrimitive> geometriesToEdit = null;
			if (Assertion.isValid(editSettings.getElementIds())) {
				geometriesToEdit = getGeometriesToEdit(editSettings);
				GBounds boundsToFit = new GBounds();
				for (GPrimitive primitive : geometriesToEdit) {
					boundsToFit.union(primitive.getBounds());
				}

				sourceFeature.clearFilter();
				sourceFeature.addFilter(createEditFilter(editSettings.getElementIds()));

				if ((!geometriesToEdit.isEmpty()) && (editSettings.isFitSelection())) {
					if (sourceFeature.getFitFactor() < 0.0F) {
						context.getBrowser().setMapCenter(boundsToFit.getCenter(),
								Math.round(-sourceFeature.getFitFactor()));
					} else {
						context.getBrowser().setMapBounds(boundsToFit.grow(sourceFeature.getFitFactor()));
					}
				}
			}
			loadingFinished(geometriesToEdit, editSettings);

			if (sourceFeature.isVisible()) {
				sourceFeature.reload();
			} else {
				sourceFeature.setVisible(editSettings.shouldFeatureBeVisible());
			}
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, null, e);
			setState(State.NOTINITIALIZED);

			context.getMapSelectionCurator().setActiveFeatureLocked(false);

			if (datamodelhandler != null) {
				datamodelhandler.dispose();
				datamodelhandler = null;
			}

			removeEditSelector();
		} finally {
			loadingjob.close();
		}
	}

	public void loadingFinished(List<GPrimitive> geometriesToEdit, EditSettings editSettings) {
		Storage dataStorage = context.createTemporaryDataStorage("Edit", null);
		dataStorage.getFeature().setVisible(true);
		try {
			datamodelhandler = new EditDataModelHandler(dataStorage, getMap(), editSettings);

			if (Assertion.isValid(geometriesToEdit)) {

				datamodelhandler.add(EditPrimitiveFactory.createEditPrimitives(geometriesToEdit, editSettings));
			}

			dataStorage.getFeature().setActive(true);

			context.getMapSelectionCurator().setActiveFeatureLocked(true);

			ActionDispatcher.getInstance().setEnableOthers(PRIVILEGED_ACTION_COMMANDS, false);
			ActionDispatcher.getInstance().setEnableAllActions(this, true);

			RPAction action = datamodelhandler.getMiniDispatcher().getAction("GE_SAVE");
			action.setEnabled(false);

			if (Assertion.isValid(datamodelhandler.getAll())) {
				EditSelectionManager selectionManager = datamodelhandler.getSelectionManager();

				EditPrimitive[] primitives = datamodelhandler.getAll();
				for (EditPrimitive editPrimitive : primitives) {
					selectionManager.selectElement(editPrimitive.getID());
				}
			}

			if (editSettings.getMeasureVertexFilter() != null) {
				SmartSnapHandler smartSnapHandler = getMap().getMeasurementController().getSmartSnapHandler();
				smartSnapHandler.addMeasureVertexFilter(editSettings.getMeasureVertexFilter());
			}
		} catch (Exception e) {
			Log.getLogger().log(Level.SEVERE, null, e);
			endEdit();
			return;
		} finally {
			loadingjob.close();
		}

		setState(State.IDLE);

		MiniDispatcher miniDispatcher = getDataModelHandler().getMiniDispatcher();
		miniDispatcher.init();
		miniDispatcher.addActionConditionFactory(new EditActionConditionFactory(miniDispatcher));
		miniDispatcher.registerAllActions(new IprEditActions(this));

		EditActionConditionFactory.getInstance().tearUpBeforeEdit(getDataModelHandler());

		editUserInterface.setUp(editSettings, datamodelhandler, miniDispatcher);

		runStartUpAction(miniDispatcher, editSettings);
	}

	private void runStartUpAction(MiniDispatcher miniDispatcher, EditSettings editSettings) {
		List<ActionConfiguration> mapActions = editSettings.getActions();

		if (!Assertion.isValid(mapActions)) {
			return;
		}
		for (ActionConfiguration mapAction : mapActions) {
			if (mapAction.isStartonload()) {
				miniDispatcher.fireAction(mapAction.getCommand().toUpperCase());
				return;
			}
		}
	}

	public void setUserHint(String text) {
		if (editUserInterface == null) {
			return;
		}
		editUserInterface.updateInfoMessage(text);
	}

	public void clearInfoText() {
		if (editUserInterface == null) {
			return;
		}
		editUserInterface.clearInfoMessage();
	}

	private void updateState(RPAction action) {
		if (editUserInterface == null) {
			return;
		}
		editUserInterface.updateState(action);
	}

	private void updateState() {
		if (editUserInterface == null) {
			return;
		}
		String statemessage = null;
		switch (state) {
		case IDLE:
			statemessage = Languages.getString("geometryedit.state.idle");
			break;
		case NOTINITIALIZED:
			statemessage = Languages.getString("geometryedit.state.not_initialized");
			break;
		case PREPARING:
			statemessage = Languages.getString("geometryedit.state.preparing");
			break;

		default:
			return;
		}

		editUserInterface.updateState(null, statemessage);
	}

	public EditSettings getEditSettings() {
		return editSettings;
	}
}