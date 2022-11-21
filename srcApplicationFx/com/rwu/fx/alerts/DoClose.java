package com.rwu.fx.alerts;

public enum DoClose {

	Yes, No, Cancel;

	public boolean isNo() {
		return this == DoClose.No;
	}

	public boolean isYes() {
		return this == DoClose.Yes;
	}

	public boolean isCancel() {
		return this == DoClose.Cancel;
	}
}
