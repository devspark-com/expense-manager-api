package org.jstorni.expensemanager.api.endpoints;

import org.jstorni.expensemanager.api.BaseRepositoryEndpoint;
import org.jstorni.expensemanager.api.Configuration;
import org.jstorni.expensemanager.api.http.Resource;
import org.jstorni.expensemanager.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;

@Resource(name="merchant")
public class MerchantEndpoint extends BaseRepositoryEndpoint<Merchant> {

	private final Repository<Merchant> merchantRepository;

	public MerchantEndpoint() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Merchant.class,
				new EntityToItemMapperImpl<Merchant>(Merchant.class),
				new ItemToEntityMapperImpl<Merchant>(Merchant.class,
						entityManager), new EntityToItemMapperImpl<Merchant>(
						Merchant.class));

		merchantRepository = entityManager.getRepository(Merchant.class);
	}
	
	@Override
	protected Repository<Merchant> getRepository() {
		return merchantRepository;
	}

}
