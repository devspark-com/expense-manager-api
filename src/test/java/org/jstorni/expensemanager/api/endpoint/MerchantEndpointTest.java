package org.jstorni.expensemanager.api.endpoint;

import org.jstorni.expensemanager.api.endpoints.MerchantEndpoint;
import org.junit.Test;

public class MerchantEndpointTest {

	
	@Test
	public void findAllTest() {
		MerchantEndpoint endpoint = new MerchantEndpoint();
		endpoint.findAll(null);
	}
}
