package org.jstorni.expensemanager.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jstorni.expensemanager.api.endpoints.MerchantEndpoint;
import org.jstorni.expensemanager.api.exceptions.BadRequestException;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericHandler {

	private final Map<String, ActionRegistryEntry> actionRegistryEntries;
	private final static String ACTION_FIELD = "action";
	private final static String PAYLOAD_FIELD = "payload";

	public GenericHandler() {
		actionRegistryEntries = new HashMap<String, ActionRegistryEntry>();
		addRegistryEntries(new MerchantEndpoint());
	}

	private void addRegistryEntries(Endpoint endpoint) {
		Set<ActionRegistryEntry> entries = endpoint.getActionRegistryEntries();
		for (ActionRegistryEntry entry : entries) {
			actionRegistryEntries.put(entry.getAction(), entry);
		}
	}

	public void handle(InputStream in, OutputStream out, Context context)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		// no JSON exceptions are managed here, at least a valid JSON is
		// expected!
		JsonNode rootNode = mapper.readValue(in, JsonNode.class);

		String action = rootNode.get(ACTION_FIELD).asText("");
		if (action.isEmpty()) {
			throw new BadRequestException(
					"No action identifier was provided in payload");
		}

		ActionRegistryEntry actionRegistryEntry = actionRegistryEntries
				.get(action);
		if (actionRegistryEntry == null) {
			throw new BadRequestException(
					"No action registry entry found for action -> " + action);
		}

		JsonNode payload = rootNode.get(PAYLOAD_FIELD);
		if (actionRegistryEntry.getPayloadClass() != null && payload == null) {
			throw new BadRequestException("Payload expected for action -> "
					+ action);
		}

		Object payloadValue;
		if (actionRegistryEntry.getPayloadClass() != null) {
			try {
				payloadValue = mapper.readValue(payload.traverse(),
						actionRegistryEntry.getPayloadClass());

			} catch (Exception ex) {
				throw new BadRequestException(
						"Invalid payload type for action -> " + action);
			}
		} else {
			payloadValue = null;
		}

		Object result = actionRegistryEntry.getHandle().apply(payloadValue);

		if (result != null) {
			mapper.writeValue(out, result);
		}

	}
}
