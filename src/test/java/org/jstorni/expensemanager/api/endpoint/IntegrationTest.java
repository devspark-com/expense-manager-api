package org.jstorni.expensemanager.api.endpoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jstorni.expensemanager.api.GenericHandler;
import org.jstorni.expensemanager.api.endpoints.MerchantEndpoint;
import org.jstorni.expensemanager.api.model.Merchant;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationTest {

	@Test
	public void findAllTest() {
		MerchantEndpoint endpoint = new MerchantEndpoint();
		endpoint.findAll(null);
	}

	@Test
	public void findByIdTest() throws JsonParseException, JsonMappingException, IOException {
		MerchantEndpoint endpoint = new MerchantEndpoint();
		List<Merchant> merchants = endpoint.findAll(null);
		String id;
		if (merchants.size() > 0) {
			id = merchants.get(0).getId();	
		} else {
			Merchant merchant = new Merchant();
			merchant.setName("sample for test");
			Merchant updatedMerchant = endpoint.save(merchant, null);
			id = updatedMerchant.getId();
		}
		
		String request = "{\"action\": \"Merchant.findById\", \"payload\": { \"id\": \"" + id + "\" }}";

		InputStream in = new ByteArrayInputStream(request.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		GenericHandler handler = new GenericHandler();
		handler.handle(in, out, null);
		
		byte[] response = out.toByteArray();
		
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length > 0);
		
		ObjectMapper mapper = new ObjectMapper();
		Merchant merchant = mapper.readValue(response, Merchant.class);
		
		Assert.assertNotNull(merchant);
		Assert.assertEquals(id, merchant.getId());
		
	}
}
