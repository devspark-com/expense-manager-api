package org.devspark.expensemanager.api.endpoints;

import org.devspark.aws.lambdasupport.endpoint.BaseRepositoryEndpoint;
import org.devspark.aws.lambdasupport.endpoint.Configuration;
import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.devspark.aws.lorm.EntityManager;
import org.devspark.aws.lorm.Repository;
import org.devspark.aws.lorm.mapping.EntityToItemMapperImpl;
import org.devspark.aws.lorm.mapping.ItemToEntityMapperImpl;
import org.devspark.expensemanager.api.model.Reporter;

@Resource(name="reporter")
public class ReporterEndpoint extends BaseRepositoryEndpoint<Reporter> {

	private final Repository<Reporter> reporterRepository;

	public ReporterEndpoint() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Reporter.class,
				new EntityToItemMapperImpl<Reporter>(Reporter.class),
				new ItemToEntityMapperImpl<Reporter>(Reporter.class,
						entityManager), new EntityToItemMapperImpl<Reporter>(
								Reporter.class));

		reporterRepository = entityManager.getRepository(Reporter.class);
	}
	
	@Override
	protected Repository<Reporter> getRepository() {
		return reporterRepository;
	}

}
