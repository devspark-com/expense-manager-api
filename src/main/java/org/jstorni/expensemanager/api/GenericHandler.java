package org.jstorni.expensemanager.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jstorni.expensemanager.api.endpoints.MerchantEndpoint;
import org.jstorni.expensemanager.api.exceptions.BadRequestException;
import org.jstorni.expensemanager.api.exceptions.InternalException;

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

		String payload = rootNode.get(PAYLOAD_FIELD).asText("");
		if (actionRegistryEntry.getPayloadClass() != null && payload.isEmpty()) {
			throw new BadRequestException("Payload expected for action -> "
					+ action);
		}

		List<Object> invocationArgs = new ArrayList<Object>();
		invocationArgs.add(actionRegistryEntry.getProvider());

		if (actionRegistryEntry.getPayloadClass() != null) {
			Object payloadValue;
			try {
				payloadValue = mapper.readValue(payload,
						actionRegistryEntry.getPayloadClass());

			} catch (Exception ex) {
				throw new BadRequestException(
						"Invalid payload type for action -> " + action);
			}

			invocationArgs.add(payloadValue);
		}

		invocationArgs.add(context);

		Object result;
		try {
			result = actionRegistryEntry.getHandle().invokeWithArguments(
					invocationArgs);
		} catch (BadRequestException ex) {
			throw ex;
		} catch (InternalException ex) {
			throw ex;
		} catch (Throwable ex) {
			throw new InternalException(
					"Undefined exception while execution action -> " + action,
					ex);
		}

		if (result != null) {
			mapper.writeValue(out, result);
		}

	}
}
