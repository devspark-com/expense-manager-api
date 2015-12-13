package org.jstorni.expensemanager.api.endpoints;

import java.util.List;

import org.jstorni.expensemanager.api.Configuration;
import org.jstorni.expensemanager.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;

import com.amazonaws.services.lambda.runtime.Context;

public class MerchantEndpoint {

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

	public Merchant findById(Key id, Context context) {
		return merchantRepository.findOne(id.getId());
	}

	public List<Merchant> findAll(Context context) {
		return merchantRepository.findAll();
	}

	public void delete(Key id, Context context) {
		merchantRepository.deleteById(id.getId());
	}

	public Merchant save(Merchant merchant, Context context) {
		return merchantRepository.save(merchant);
	}

}
