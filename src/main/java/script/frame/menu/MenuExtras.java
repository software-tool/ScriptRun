package script.frame.menu;

import java.util.List;

import com.rwu.application.lang.Ln;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;

import script.controller.ControllerPages;
import script.lang.K;

public class MenuExtras extends MenuBase {

	private MenuItemDef itemPreferences;

	public MenuExtras() {
		super(Ln.get(K.MenuTools));

		init();
		initItems();
	}

	private void init() {
		itemPreferences = new MenuItemDef(Ln.dots(K.MenuPreferences));
	}

	private void initItems() {
		itemPreferences.setOnAction(() -> {
			ControllerPages.openOptions();
		});

		List<MenuEntryDef> items = menu.getItems();

		items.add(itemPreferences);
	}

	@Override
	public void updateOnShowing() {
	}

	@Override
	public void updateOnHiding() {
	}
}
