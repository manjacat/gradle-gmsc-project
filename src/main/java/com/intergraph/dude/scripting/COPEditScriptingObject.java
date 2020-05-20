package com.intergraph.dude.scripting;


import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.intergraph.dude.extensions.IprEditPlugin;
import com.intergraph.tools.utils.Assertion;
import com.intergraph.tools.utils.Str;
import com.intergraph.web.core.data.feature.Feature;
import com.intergraph.web.core.kernel.ApplicationContext;
import com.intergraph.web.plugin.edit.ActionConfiguration;
//import com.intergraph.web.plugin.edit.EditPlugin;
import com.intergraph.web.plugin.edit.EditSettings;
import com.intergraph.web.plugin.edit.persistence.DefaultPersistenceProvider;
import com.intergraph.web.plugin.edit.persistence.WorkflowPersitenceProvider;
import com.intergraph.web.plugin.webbrowser.handler.AbstractScriptingObject;
import com.intergraph.web.plugin.webbrowser.handler.Deferred;
import com.intergraph.web.plugin.webbrowser.handler.JSUtils;
import com.intergraph.web.plugin.webbrowser.handler.ScriptingObject;

@ScriptingObject(name = "COPEdit")
public class COPEditScriptingObject extends AbstractScriptingObject
{
	/**
	 * usage: 
	 * define a custom script like this (default edit behavior copied and modified):
		// Override of IG.CaptureGeometry with reload defined features after editing/creating		
		captureGeomWithReload = function (featureToReloadIds) {
			return IG.url('SmartClient/SaveAttributes')
				.includeFormContext({ postForm: true, validateForm: true })
				.ajaxPost()
				.then(function (result) {
					var actions = [];
					var i = 0;
					for (; i < result.actions.length; i++) {
						var currentAction = result.actions[i];
						var currentActionString = currentAction.name + '[';
						if (currentAction.name === result.startonloadaction) {
							currentActionString += true;
						}
						else {
							currentActionString += false;
						}
						if (currentAction.label) {
							currentActionString += ',' + currentAction.label;
						}
						currentActionString += ']';
						actions[i] = currentActionString;
					}
					COPEdit.startEdit(result.layer, actions, IG.convertToArray(result.geometryid), null, IG.url('SmartClient/SaveGeometries').includeWorkflowContext({ includeId: true, postForm: false }).toString({ absolute: true }), true, featureToReloadIds)
						.done(function () {
							return SC.Map.closeWebBrowser();
						})
						.fail(function (message, exception) {
							SC.handleFail(new SC.Throwable(message));
						});
				})
				.fail(function (jqXHR, textStatus, errorThrown) {
					var jsonResponse = {};
					try {
						jsonResponse = jQuery.parseJSON(jqXHR.responseText);
					} catch (e) {
						jsonResponse.message = textStatus + " - " + errorThrown;
		
					}
					SC.handleFail(new SC.Throwable(jsonResponse.message));
					IG.vent.trigger('form:submit:error', result, textStatus, errorThrown);
				});
		
		};	
	 * 
	 * Define an action to call the function and pass the simpleId of the feature class that you would like to reload):
	 * <FormAction name="fmaMapEdit" action="SCRIPT[captureGeomWithReload(['461','462'])" visible="form"/>
	 * 
	 * 
	 * Starts the edit plugin with the given parameters.
	 * 
	 * <p>
	 * <strong>Reject:</strong> An error message and a exception if any was thrown.
	 * </p>
	 * <p>
	 * <strong>Resolve:</strong> always <code>true</code>
	 * </p>
	 * 
	 * @param featureIdOrName
	 *            the id or name of the feature to edit
	 * @param actions
	 *            a single edit action or a JavaScript array of edit actions
	 * @param elementIds
	 *            a single element id or a JavaScript array of element id's to select.
	 * @param styleId
	 *            the id of a style to use - the default will be used if the id is <code>null</code>
	 * @param persistenceLocation
	 *            the name of the persistence location
	 * @param fitSelection
	 *            defines if the map should fit to the selection
	 *@param oFeaturesToReloadIds
	 *            comma separated values the features reloaded after editing            
	 * @return a Deferred Promise object.
	 */
	public Object startEdit(final String featureIdOrName, final Object actions, final Object elementIds, final String styleId,
			final String persistenceLocation, final boolean fitSelection, final Object featuresToReloadIds)
	{
		final String[] actionDelegate = JSUtils.convertToStringArray(actions);
		final List<String> idDelegate = JSUtils.convertToStringList(elementIds);
		final String[] feauturesToReloadIds = JSUtils.convertToStringArray(featuresToReloadIds);
		
		return swingInvoke(() ->
		{
			try
			{				
				Feature editFeature = ApplicationContext.getProject().getFeatureByTitleOrID(featureIdOrName);
				if (editFeature == null)
				{
					return Deferred.reject(String.format("Feature '%s' was not found in the project!", featureIdOrName));
				}
				
				EditSettings settings = new EditSettings();
				settings.setFeature(editFeature);
				settings.setActions(ActionConfiguration.parseMapActions(actionDelegate));
				settings.setFitSelection(fitSelection);
				
				if (Assertion.isValid(idDelegate))
					settings.setElementIds(idDelegate);
				
				if (Str.valid(styleId))
					settings.setStyleId(UUID.fromString(styleId));
				
				if (Str.valid(persistenceLocation))
					settings.setPersistenceProvider(new WorkflowPersitenceProvider(new URL(persistenceLocation), getWebBrowser().getSourceAction()));
				else
					settings.setPersistenceProvider(new DefaultPersistenceProvider());
				
				ApplicationContext.getPluginCurator().getByType(IprEditPlugin.class).get().setFeaturesToReloadIds(feauturesToReloadIds);
				ApplicationContext.getPluginCurator().getByType(IprEditPlugin.class).get().startEdit(settings);
				ApplicationContext.getMainFrame().toFront();
				return Deferred.resolve(true);
			}
			catch (Throwable t)
			{
				return Deferred.reject(String.format("Error in action startEdit! Check the log file!", featureIdOrName), t);
			}
		});
		
	}
}

