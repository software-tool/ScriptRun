package com.rwu.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.rwu.log.Log;
import xml.LogHandler;

public class MyExceptionHandler implements UncaughtExceptionHandler {

	public static Object lockObj = new Object();

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		synchronized (lockObj) {
			StackTraceElement[] stackTrace = throwable.getStackTrace();

			if (stackTrace.length > 0) {
				StackTraceElement element1 = stackTrace[0];
				String name1 = element1.getClassName();
				String methodName = element1.getMethodName();

				if ("com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList".equals(name1) && "subList".equals(methodName)) {
					// Exception of MultipleSelectionModelBase, on selection of cells in tree, if at least one node is expanded
					// Reason not clear, Selection is correct

					return;
				}
			}

			Log.error(throwable);
		}
	}

	public void handle(Throwable throwable) {
		synchronized (lockObj) {
			Log.error(throwable);
		}
	}

	/*
	 *  Not yet implemented(!):
	 *  
	 *  Mon Aug 03 20:23:05 CEST 2020  (no text)  java.lang.IndexOutOfBoundsException: fromIndex = -1
	java.lang.IndexOutOfBoundsException: fromIndex = -1
	at java.base/java.util.AbstractList.subListRangeCheck(AbstractList.java:505)
	at java.base/java.util.AbstractList.subList(AbstractList.java:497)
	at javafx.collections.ListChangeListener$Change.getAddedSubList(ListChangeListener.java:243)
	at xmleditor.panes.tree.PaneFileTree.lambda$0(PaneFileTree.java:267)
	at com.sun.javafx.collections.ListListenerHelper$Generic.fireValueChangedEvent(ListListenerHelper.java:329)
	at com.sun.javafx.collections.ListListenerHelper.fireValueChangedEvent(ListListenerHelper.java:73)
	at javafx.collections.ObservableListBase.fireChange(ObservableListBase.java:233)
	at javafx.collections.ListChangeBuilder.commit(ListChangeBuilder.java:482)
	at javafx.collections.ListChangeBuilder.endChange(ListChangeBuilder.java:541)
	at javafx.collections.ObservableListBase.endChange(ObservableListBase.java:205)
	at com.sun.javafx.scene.control.SelectedItemsReadOnlyObservableList.lambda$new$1(SelectedItemsReadOnlyObservableList.java:103)
	at com.sun.javafx.collections.ListListenerHelper$Generic.fireValueChangedEvent(ListListenerHelper.java:329)
	at com.sun.javafx.collections.ListListenerHelper.fireValueChangedEvent(ListListenerHelper.java:73)
	at javafx.collections.ObservableListBase.fireChange(ObservableListBase.java:233)
	at javafx.collections.ListChangeBuilder.commit(ListChangeBuilder.java:482)
	at javafx.collections.ListChangeBuilder.endChange(ListChangeBuilder.java:541)
	at javafx.collections.ObservableListBase.endChange(ObservableListBase.java:205)
	at com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList._endChange(ReadOnlyUnbackedObservableList.java:64)
	at javafx.scene.control.MultipleSelectionModelBase$SelectedIndicesList._endChange(MultipleSelectionModelBase.java:931)
	at javafx.scene.control.MultipleSelectionModelBase$SelectedIndicesList.set(MultipleSelectionModelBase.java:734)
	at javafx.scene.control.MultipleSelectionModelBase.select(MultipleSelectionModelBase.java:416)
	at javafx.scene.control.TreeView$TreeViewBitSetSelectionModel.select(TreeView.java:1515)
	at javafx.scene.control.TreeView$TreeViewBitSetSelectionModel.select(TreeView.java:1273)
	at xmleditor.panes.tree.PaneFileTree.selectTreeNode(PaneFileTree.java:374)
	at xmleditor.panes.tree.PaneFileTree.selectNode(PaneFileTree.java:366)
	at xmleditor.actions.TreeActions.doDelete(TreeActions.java:57)
	at xmleditor.panes.PaneTree.fireDelete(PaneTree.java:298)
	at xmleditor.main.menu.MenuEdit.lambda$10(MenuEdit.java:269)
	at com.rwu.menu.factory.MenuItemFactory.lambda$0(MenuItemFactory.java:33)
	at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:234)
	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191)
	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
	at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
	at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:49)
	at javafx.event.Event.fireEvent(Event.java:198)
	at javafx.scene.control.MenuItem.fire(MenuItem.java:459)
	at com.sun.javafx.scene.control.ControlAcceleratorSupport.lambda$doAcceleratorInstall$1(ControlAcceleratorSupport.java:165)
	at com.sun.javafx.scene.KeyboardShortcutsHandler.processAccelerators(KeyboardShortcutsHandler.java:382)
	at com.sun.javafx.scene.KeyboardShortcutsHandler.dispatchBubblingEvent(KeyboardShortcutsHandler.java:162)
	at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
	at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
	at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:54)
	at javafx.event.Event.fireEvent(Event.java:198)
	at javafx.scene.Scene$KeyHandler.process(Scene.java:4098)
	at javafx.scene.Scene.processKeyEvent(Scene.java:2157)
	at javafx.scene.Scene$ScenePeerListener.keyEvent(Scene.java:2625)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler$KeyEventNotification.run(GlassViewEventHandler.java:217)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler$KeyEventNotification.run(GlassViewEventHandler.java:149)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:391)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler.lambda$handleKeyEvent$1(GlassViewEventHandler.java:248)
	at com.sun.javafx.tk.quantum.QuantumToolkit.runWithoutRenderLock(QuantumToolkit.java:412)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler.handleKeyEvent(GlassViewEventHandler.java:247)
	at com.sun.glass.ui.View.handleKeyEvent(View.java:547)
	at com.sun.glass.ui.View.notifyKey(View.java:971)
	at com.sun.glass.ui.win.WinApplication._runLoop(Native Method)
	at com.sun.glass.ui.win.WinApplication.lambda$runLoop$3(WinApplication.java:174)
	at java.base/java.lang.Thread.run(Thread.java:830)
	
	 */
}
