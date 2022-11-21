package script.frame.menu;

import com.rwu.log.Log;
import com.rwu.menu.factory.MenuFactory;
import com.rwu.menu.menudef.MenuDef;
import com.rwu.menu.menudef.MenuItemDef;
import cp.system.AppCommon;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import script.frame.img.GuiImages;

import javax.swing.*;
import java.io.InputStream;

public abstract class MenuBase {

	protected MenuDef menu;

	private Class<?> imagesClass = new GuiImages().getClass();

	public Menu getMenuFx() {
		return MenuFactory.getMenuFx(menu);
	}

	public JMenu getMenuSwing() {
		return MenuFactory.getMenuSwing(menu);
	}

	protected void setImg(MenuItemDef item, String filename) {
		if (AppCommon.isMac) {
			return;
		}

		InputStream resourceAsStream = imagesClass.getResourceAsStream(filename);
		if (resourceAsStream == null) {
			Log.warn("Cannot find image in resources: " + filename);
		}

		item.setGraphic(new ImageView(new Image(resourceAsStream)));
	}

	protected void setImg(MenuDef item, String filename) {
		if (AppCommon.isMac) {
			return;
		}

		Icon icon = new ImageIcon(imagesClass.getResource(filename));

		item.setGraphic(new ImageView(new Image(imagesClass.getResourceAsStream(filename))), icon);
	}

	public abstract void updateOnShowing();

	public abstract void updateOnHiding();

	public void updateOnFileChanged() {
		// Empty in Base
	}

	public MenuBase(String text) {
		menu = new MenuDef(text);

		menu.setOnShowing(() -> {
			updateOnShowing();
		});

		menu.setOnHiding(() -> {
			updateOnHiding();
		});
	}
}
