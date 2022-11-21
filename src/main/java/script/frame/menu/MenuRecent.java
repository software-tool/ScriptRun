package script.frame.menu;

import java.io.File;
import java.util.List;

import com.rwu.application.lang.Ln;
import com.rwu.menu.menudef.MenuItemDef;

import script.controller.ControllerPages;
import script.controller.ControllerScripts;
import script.data.Script;
import script.lang.K;
import script.recent.RecentFileManager;

public class MenuRecent extends MenuBase {

	private MenuItemDef itemNoEntries;

	private MenuItemDef itemManage;

	public MenuRecent() {
		super(Ln.get(K.MenuRecent));

		init();
		initItems();
	}

	private void init() {
	}

	private void initItems() {
		itemManage = new MenuItemDef(Ln.dots(K.RecentManage));
		itemManage.setOnAction(() -> {
			ControllerPages.openRecents();
		});
	}

	@Override
	public void updateOnShowing() {
		menu.getItems().clear();

		menu.getItems().add(itemManage);
		menu.addSeparator();

		List<File> entries = RecentFileManager.getRecentEntries();

		if (entries.isEmpty()) {
			itemNoEntries = new MenuItemDef(Ln.get(K.RecentNoEntries));
			itemNoEntries.setDisable(true);

			menu.getItems().add(itemNoEntries);
		}

		for (File entry : entries) {
			MenuItemDef item = new MenuItemDef(entry.getAbsolutePath(), false);
			item.setOnAction(() -> {
				Script script = new Script(entry.getParentFile(), entry.getName());

				ControllerScripts.openDetails(script, false, false);
			});

			menu.getItems().add(item);
		}

		menu.refill();
	}

	@Override
	public void updateOnHiding() {
	}
}
