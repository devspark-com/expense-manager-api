package org.devspark.expensemanager.api.endpoints;

import org.devspark.aws.lambdasupport.endpoint.BaseRepositoryEndpoint;
import org.devspark.aws.lambdasupport.endpoint.Configuration;
import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.devspark.aws.lorm.EntityManager;
import org.devspark.aws.lorm.Repository;
import org.devspark.aws.lorm.mapping.EntityToItemMapperImpl;
import org.devspark.aws.lorm.mapping.ItemToEntityMapperImpl;
import org.devspark.expensemanager.api.model.Category;

@Resource(name="category")
public class CategoryEndpoint extends BaseRepositoryEndpoint<Category> {

	private final Repository<Category> categoryRepository;

	public CategoryEndpoint() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Category.class,
				new EntityToItemMapperImpl<Category>(Category.class),
				new ItemToEntityMapperImpl<Category>(Category.class,
						entityManager), new EntityToItemMapperImpl<Category>(
								Category.class));

		categoryRepository = entityManager.getRepository(Category.class);
	}
	
	@Override
	protected Repository<Category> getRepository() {
		return categoryRepository;
	}

}
