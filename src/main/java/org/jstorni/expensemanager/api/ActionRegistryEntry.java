package org.jstorni.expensemanager.api;

import java.lang.invoke.MethodHandle;

public class ActionRegistryEntry {
	private final String action;
	private final Class<?> payloadClass;
	private final MethodHandle handle;
	private final Object provider;

	public ActionRegistryEntry(String action, Class<?> payloadClass,
			MethodHandle handle, Object provider) {
		super();
		this.action = action;
		this.payloadClass = payloadClass;
		this.handle = handle;
		this.provider = provider;
	}

	public String getAction() {
		return action;
	}

	public Class<?> getPayloadClass() {
		return payloadClass;
	}

	public MethodHandle getHandle() {
		return handle;
	}

	public Object getProvider() {
		return provider;
	}

}
