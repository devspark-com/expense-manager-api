package org.devspark.expensemanager.api;

import java.util.Arrays;
import java.util.List;

import org.devspark.aws.lambdasupport.endpoint.Endpoint;
import org.devspark.aws.lambdasupport.endpoint.GenericHandler;
import org.devspark.aws.lambdasupport.endpoint.annotations.lambda.Lambda;
import org.devspark.expensemanager.api.endpoints.CategoryEndpoint;
import org.devspark.expensemanager.api.endpoints.ExpenseEndpoint;
import org.devspark.expensemanager.api.endpoints.MerchantEndpoint;
import org.devspark.expensemanager.api.endpoints.ReporterEndpoint;

@Lambda(name = "expense-api")
public class Handler extends GenericHandler {

	@Override
	protected List<Endpoint> getEndpoints() {
		return Arrays.asList(new Endpoint[] { new MerchantEndpoint(), new CategoryEndpoint(), new ExpenseEndpoint(),
				new ReporterEndpoint() });
	}

}
