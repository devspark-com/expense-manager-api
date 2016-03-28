package org.devspark.expensemanager.api.endpoints;

import org.devspark.aws.lambdasupport.endpoint.BaseRepositoryEndpoint;
import org.devspark.aws.lambdasupport.endpoint.Configuration;
import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.devspark.aws.lorm.EntityManager;
import org.devspark.aws.lorm.Repository;
import org.devspark.aws.lorm.mapping.EntityToItemMapperImpl;
import org.devspark.aws.lorm.mapping.ItemToEntityMapperImpl;
import org.devspark.expensemanager.api.model.Expense;

@Resource(name="expense")
public class ExpenseEndpoint extends BaseRepositoryEndpoint<Expense> {

	private final Repository<Expense> expenseRepository;

	public ExpenseEndpoint() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Expense.class,
				new EntityToItemMapperImpl<Expense>(Expense.class),
				new ItemToEntityMapperImpl<Expense>(Expense.class,
						entityManager), new EntityToItemMapperImpl<Expense>(
								Expense.class));

		expenseRepository = entityManager.getRepository(Expense.class);
	}
	
	@Override
	protected Repository<Expense> getRepository() {
		return expenseRepository;
	}

}
