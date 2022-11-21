package script.frame.menu;

import java.util.List;

import com.rwu.application.lang.Ln;
import com.rwu.fx.util.FxKeyUtils;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;

import script.controller.ControllerFileSave;
import script.controller.ControllerScripts;
import script.frame.PaneMain;
import script.frame.pages.PageTypeEnum;
import script.lang.K;

public class MenuScript extends MenuBase {

	private MenuItemDef itemRun;
	private MenuItemDef itemEdit;

	private MenuItemDef itemSave;

	public MenuScript() {
		super(Ln.get(K.MenuScript));

		init();
		initItems();
	}

	private void init() {
		itemRun = new MenuItemDef(Ln.get(K.RunScript));
		itemEdit = new MenuItemDef(Ln.get(K.EditScript));

		itemSave = new MenuItemDef(Ln.get(K.MENU_SAVE));

		// Icons
		setImg(itemSave, "save.png");

		itemRun.setAccelerator(FxKeyUtils.meta("r"));

		itemSave.setAccelerator(FxKeyUtils.meta("s"));
	}

	private void initItems() {
		itemRun.setOnAction(() -> {
			ControllerScripts.doRunSelected(false);
		});
		itemEdit.setOnAction(() -> {
			ControllerScripts.doEditSelected();
		});

		itemSave.setOnAction(() -> {
			ControllerFileSave.doSaveSelected();
		});

		List<MenuEntryDef> items = menu.getItems();

		items.add(itemRun);
		items.add(itemEdit);
		menu.addSeparator();
		items.add(itemSave);
	}

	/**
	 * Update for shortcuts to work
	 */
	public void update() {
		if (PaneMain.inst == null) {
			return;
		}

		PageTypeEnum type = PaneMain.inst.getDockNodeTypeSelected();

		boolean canSave = false;
		boolean canRun = false;
		boolean canEdit = false;

		if (type != null) {
			canSave = type.isScriptPage();
			canRun = type.isScriptPage();
			canEdit = type.isScriptPage();
		}

		//System.err.println(" isScript: " + isScript + ", " + type + ", " + PaneMain.inst.getDockNodeSelected());

		itemRun.setDisable(!canRun);
		itemEdit.setDisable(!canEdit);

		itemSave.setDisable(!canSave);
	}

	@Override
	public void updateOnShowing() {
	}

	@Override
	public void updateOnHiding() {
	}
}
