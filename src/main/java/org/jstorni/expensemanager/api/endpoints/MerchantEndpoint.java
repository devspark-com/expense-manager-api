package org.jstorni.expensemanager.api.endpoints;

import java.util.HashSet;
import java.util.Set;

import org.jstorni.expensemanager.api.ActionRegistryEntry;
import org.jstorni.expensemanager.api.Configuration;
import org.jstorni.expensemanager.api.Endpoint;
import org.jstorni.expensemanager.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;

public class MerchantEndpoint implements Endpoint {

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
	public Set<ActionRegistryEntry> getActionRegistryEntries() {
		Set<ActionRegistryEntry> entries = new HashSet<ActionRegistryEntry>();

		entries.add(new ActionRegistryEntry("Merchant.findById", Key.class, (
				payload) -> {
			return merchantRepository.findOne(((Key) payload).getId());
		}));

		entries.add(new ActionRegistryEntry("Merchant.findAll", null, (
				payload) -> {
			return merchantRepository.findAll();
		}));

		entries.add(new ActionRegistryEntry("Merchant.delete", Key.class, (
				payload) -> {
			merchantRepository.deleteById(((Key) payload).getId());
			return null;
		}));

		entries.add(new ActionRegistryEntry("Merchant.save", Merchant.class, (
				payload) -> {
			return merchantRepository.save((Merchant) payload);
		}));

		return entries;
	}

}
