package script.frame.menu;

import com.rwu.application.lang.Ln;
import com.rwu.fx.window.AppWindowFx;
import com.rwu.menu.menudef.MenuItemDef;
import com.rwu.menu.menudef.base.MenuEntryDef;
import com.rwu.misc.SystemUtils;
import cp.system.AppCommon;
import script.frame.dialogs.DialogAbout;
import script.lang.K;

import java.util.List;

public class MenuHelp extends MenuBase {

	public MenuHelp() {
		super(Ln.get(K.MENU_HELP));

		initItems();
	}

	private void initItems() {
		String textAbout = null;
		if (AppCommon.isMac) {
			textAbout = Ln.get(K.MENU_ABOUT_MAC);
		} else {
			textAbout = Ln.get(K.MENU_ABOUT);
		}

		MenuItemDef itemHelp = new MenuItemDef(Ln.get(K.MENU_OPEN_HELP));
		MenuItemDef itemAbout = new MenuItemDef(textAbout);

		itemHelp.setOnAction(() -> {
			SystemUtils.openUrl(Ln.get(K.UrlDocumentation));
		});

		itemAbout.setOnAction(() -> {
			String textAboutTitle = Ln.get(K.IntroductionTitle) + " " + Ln.get(K.AppVersion);
			String textThirdParty = Ln.get(K.AboutThirdParty);
			String textThirdPartyIcons = Ln.get(K.AboutThirdPartyIcons);

			String textUrl = Ln.get(K.UrlMain);

			DialogAbout dialog = new DialogAbout(AppWindowFx.getStage(), textAboutTitle, textUrl, textThirdParty, textThirdPartyIcons);
			dialog.open();
		});

		List<MenuEntryDef> items = menu.getItems();

		items.add(itemHelp);

		menu.addSeparator();
		items.add(itemAbout);
	}

	@Override
	public void updateOnShowing() {
	}

	private void openSystemInformation() {
		//		VBox box = new VBox();
		//
		//		File logFile = App.getLogFile();
		//
		//		String logFileText = L.get("OPEN_LOG_FILE");
		//		if (!logFile.exists()) {
		//			logFileText += (" " + L.get("LOG_FILE_NOT_EXISTING"));
		//		}
		//
		//		Hyperlink userDir = FxBase.createHyperlink(L.get("OPEN_USER_CONFIG_DIR"), L.get("CLICK_TO_OPEN"));
		//		Hyperlink linkLogfile = FxBase.createHyperlink(logFileText, L.get("CLICK_TO_OPEN"));
		//
		//		Hyperlink linkLabel = FxBase.createHyperlink(L.get("OPEN_APPLICATION_DIR"), L.get("CLICK_TO_OPEN"));
		//
		//		userDir.setOnMousePressed((Event e) -> {
		//			SystemUtils.openDirectory(AppSpecific.getConfigDir());
		//		});
		//		linkLogfile.setOnMousePressed((Event e) -> {
		//			SystemUtils.openFile(logFile);
		//		});
		//
		//		linkLabel.setOnMousePressed((Event e) -> {
		//			SystemUtils.openDirectory(App.getApplicationDir());
		//		});
		//
		//		if (!logFile.exists()) {
		//			linkLogfile.setDisable(true);
		//		}
		//
		//		box.getChildren().add(userDir);
		//		box.getChildren().add(linkLogfile);
		//
		//		box.getChildren().add(linkLabel);
		//
		//		// Dialog
		//
		//		SystemInfoDialog dialog = new SystemInfoDialog(AppWindowCommon.getMessagesStage(), App.getApplicationDir(),
		//				L.get("MENU_SYSTEM_INFO"), box);
		//		dialog.open();
	}

	@Override
	public void updateOnHiding() {
	}

}
