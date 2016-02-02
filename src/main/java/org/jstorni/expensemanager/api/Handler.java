package org.jstorni.expensemanager.api;

import java.util.Arrays;
import java.util.List;

import org.devspark.aws.lambdasupport.endpoint.Endpoint;
import org.devspark.aws.lambdasupport.endpoint.GenericHandler;
import org.devspark.aws.lambdasupport.endpoint.annotations.lambda.Lambda;
import org.jstorni.expensemanager.api.endpoints.MerchantEndpoint;

@Lambda(name="expense-api")
public class Handler extends GenericHandler {

	@Override
	protected List<Endpoint> getEndpoints() {
		return Arrays.asList(new Endpoint[] { new MerchantEndpoint() });
	}

}
