package org.jstorni.expensemanager.api;

import java.util.Set;

public interface Endpoint {

	Set<ActionRegistryEntry> getActionRegistryEntries();
	
}
