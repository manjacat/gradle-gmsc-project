package com.intergraph.dude.extensions;

import com.intergraph.tools.utils.disptach.RPAction;
import com.intergraph.tools.utils.disptach.annotations.Action;
import com.intergraph.web.plugin.edit.actions.AddPointToCollectionAction;
import com.intergraph.web.plugin.edit.actions.AdoptAction;
import com.intergraph.web.plugin.edit.actions.MergeAction;
import com.intergraph.web.plugin.edit.actions.MergePolylineAction;
import com.intergraph.web.plugin.edit.actions.ModifyAction;
import com.intergraph.web.plugin.edit.actions.MoveAction;
import com.intergraph.web.plugin.edit.actions.NewArcAction;
import com.intergraph.web.plugin.edit.actions.NewCircleAction;
import com.intergraph.web.plugin.edit.actions.NewPointAction;
import com.intergraph.web.plugin.edit.actions.NewPolygonAction;
import com.intergraph.web.plugin.edit.actions.NewPolylineAction;
import com.intergraph.web.plugin.edit.actions.NewRectangleAction;
import com.intergraph.web.plugin.edit.actions.NewTextAction;
import com.intergraph.web.plugin.edit.actions.RedoAction;
import com.intergraph.web.plugin.edit.actions.RemoveAction;
import com.intergraph.web.plugin.edit.actions.RemoveCollectionPartAction;
import com.intergraph.web.plugin.edit.actions.RotateAction;
import com.intergraph.web.plugin.edit.actions.SplitPolygonAction;
import com.intergraph.web.plugin.edit.actions.SplitPolylineAction;
import com.intergraph.web.plugin.edit.actions.SubstractAction;
import com.intergraph.web.plugin.edit.actions.TextChangeAction;
import com.intergraph.web.viewer.measurement.capture.GeometryCaptureFactory.GeometryCaptureType;

public class IprEditActions {
	private final IprEditPlugin editPlugin;

	public IprEditActions(IprEditPlugin editPlugin) {
		this.editPlugin = editPlugin;
	}

	@Action(icon = "abort.svg", languageKey = "edit.reset_controller")
	public void GE_RESET(RPAction a) {
		editPlugin.reset();
	}

	@Action(icon = "difference.svg", languageKey = "edit.substract", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPolygon;EditGeomCollection", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "typeCount", defaultValue = "2", description = "") }) })
	public void GE_SUBSTRACT(RPAction a) {
		editPlugin.runAction(new SubstractAction(editPlugin), a);
	}

	@Action(icon = "splitarea.svg", languageKey = "edit.splitpolygon", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPolygon;EditGeomCollection", description = "") }) })
	public void GE_SPLITPOLYGON(RPAction a) {
		editPlugin.runAction(new SplitPolygonAction(editPlugin), a);
	}

	@Action(icon = "splitline.svg", languageKey = "edit.splitpolyline", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPolyline", description = "") }) })
	public void GE_SPLITPOLYLINE(RPAction a) {
		editPlugin.runAction(new SplitPolylineAction(editPlugin), a);
	}

	@Action(icon = "merge.svg", languageKey = "edit.merge", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPolygon;EditGeomCollection", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "typeCount", defaultValue = "2", description = "") }) })
	public void GE_MERGE(RPAction a) {
		editPlugin.runAction(new MergeAction(editPlugin), a);
	}

	@Action(icon = "merge.svg", languageKey = "edit.mergepolylines", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPolyline;EditGeomCollection", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "typeCount", defaultValue = "2", description = "") }) })
	public void GE_MERGEPOLYLINES(RPAction a) {
		editPlugin.runAction(new MergePolylineAction(editPlugin), a);
	}

	@Action(icon = "move.svg", languageKey = "edit.move", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "10", description = "") }) })
	public void GE_MOVE(RPAction a) {
		editPlugin.runAction(new MoveAction(editPlugin), a);
	}

	@Action(icon = "modify.svg", languageKey = "edit.modifyaction", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = "") }) })
	public void GE_MODIFY(RPAction a) {
		editPlugin.runAction(new ModifyAction(editPlugin), a);
	}

	@Action(icon = "area.svg", languageKey = "edit.newpolygon")
	public void GE_NEWPOLYGON(RPAction a) {
		editPlugin.runAction(new NewPolygonAction(editPlugin), a);
	}

	@Action(icon = "rectangle.svg", languageKey = "edit.newrectangle")
	public void GE_NEWRECTANGLE(RPAction a) {
		editPlugin.runAction(new NewRectangleAction(editPlugin), a);
	}

	@Action(icon = "circle.svg", languageKey = "edit.newcircle")
	public void GE_NEWCIRCLE(RPAction a) {
		editPlugin.runAction(new NewCircleAction(editPlugin), a);
	}

	@Action(icon = "line.svg", languageKey = "edit.newpolyline")
	public void GE_NEWPOLYLINE(RPAction a) {
		editPlugin.runAction(new NewPolylineAction(editPlugin), a);
	}

	@Action(icon = "text.svg", languageKey = "edit.newtext")
	public void GE_NEWTEXT(RPAction a) {
		editPlugin.runAction(new NewTextAction(editPlugin), a);
	}

	@Action(icon = "point.svg", languageKey = "edit.newpoint")
	public void GE_NEWPOINT(RPAction a) {
		editPlugin.runAction(new NewPointAction(editPlugin), a);
	}

	@Action(icon = "arc.svg", languageKey = "edit.newarc")
	public void GE_NEWARC(RPAction a) {
		editPlugin.runAction(new NewArcAction(editPlugin, GeometryCaptureType.Arc), a);
	}

	@Action(icon = "arc.svg", languageKey = "edit.newarcbycenter")
	public void GE_NEWARCBYCENTER(RPAction a) {
		editPlugin.runAction(new NewArcAction(editPlugin, GeometryCaptureType.ArcByCenter), a);
	}

	@Action(icon = "arc.svg", languageKey = "edit.newarcbythreepoints")
	public void GE_NEWARCBYTHREEPOINTS(RPAction a) {
		editPlugin.runAction(new NewArcAction(editPlugin, GeometryCaptureType.ArcByThreePoints), a);
	}

	@Action(icon = "rotate.svg", languageKey = "edit.rotate", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = "") }) })
	public void GE_ROTATE(RPAction a) {
		editPlugin.runAction(new RotateAction(editPlugin), a);
	}

	@Action(icon = "edittext.svg", languageKey = "edit.textchange", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditText", description = "") }) })
	public void GE_TEXTCHANGE(RPAction a) {
		editPlugin.runAction(new TextChangeAction(editPlugin), a);
	}

	@Action(icon = "copy.svg", languageKey = "edit.adopt")
	public void GE_ADOPT(RPAction a) {
		editPlugin.runAction(new AdoptAction(editPlugin), a);
	}

	@Action(icon = "delete.svg", languageKey = "edit.delete", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = "") }) })
	public void GE_DELETE(RPAction a) {
		editPlugin.runAction(new RemoveAction(editPlugin), a);
	}

	@Action(icon = "deletepart.svg", languageKey = "edit.deletecollectionpart", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditGeomCollection", description = "") }) })
	public void GE_DELETECOLLECTIONPART(RPAction a) {
		editPlugin.runAction(new RemoveCollectionPartAction(editPlugin), a);
	}

	@Action(icon = "pointcollection.svg", languageKey = "edit.addpointtocollection", conditions = {
			@com.intergraph.tools.utils.disptach.annotations.Condition(actionCondition = com.intergraph.web.plugin.edit.actions.EditActionConditionFactory.class, parameters = {
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "minimumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "maximumSelection", defaultValue = "1", description = ""),
					@com.intergraph.tools.utils.disptach.annotations.Parameter(name = "geometryTypes", defaultValue = "EditPoint;EditGeomCollection", description = "") }) })
	public void GE_ADDPOINTTOCOLLECTION(RPAction a) {
		editPlugin.runAction(new AddPointToCollectionAction(editPlugin), a);
	}

	@Action(icon = "undo.svg", languageKey = "edit.undo")
	public void GE_UNDO(RPAction a) {
		editPlugin.undo(a);
	}

	@Action(icon = "redo.svg", languageKey = "edit.redo")
	public void GE_REDO(RPAction a) {
		editPlugin.runAction(new RedoAction(editPlugin), a);
	}

	@Action(icon = "save.svg", languageKey = "edit.save")
	public void GE_SAVE(RPAction a) {
		editPlugin.save();
	}
}