package org.jstorni.expensemanager.api.endpoints;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jstorni.expensemanager.api.ActionRegistryEntry;
import org.jstorni.expensemanager.api.Configuration;
import org.jstorni.expensemanager.api.Endpoint;
import org.jstorni.expensemanager.api.exceptions.InternalException;
import org.jstorni.expensemanager.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;

import com.amazonaws.services.lambda.runtime.Context;

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

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		entries.add(buildRegistryEntry(lookup, "Merchant.findById", "findById",
				Key.class, Merchant.class));
		entries.add(buildRegistryEntry(lookup, "Merchant.findAll", "findAll",
				null, List.class));
		entries.add(buildRegistryEntry(lookup, "Merchant.delete", "delete",
				Key.class, Void.class));
		entries.add(buildRegistryEntry(lookup, "Merchant.save", "save",
				Merchant.class, Merchant.class));

		return entries;
	}

	private ActionRegistryEntry buildRegistryEntry(MethodHandles.Lookup lookup,
			String actionName, String methodName, Class<?> paramClass,
			Class<?> returnClass) {

		MethodHandle handle;

		try {
			handle = lookup.bind(this, methodName,
					getMethodTypeForType(returnClass, paramClass));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new InternalException(
					"Error while creating handle for method " + methodName
							+ " on class " + getClass().getName(), e);
		}

		return new ActionRegistryEntry(actionName, paramClass, handle, this);

	}

	private MethodType getMethodTypeForType(Class<?> returnClass, Class<?> type) {
		if (type == null) {
			return MethodType.methodType(returnClass,
					new Class<?>[] { Context.class });
		} else {
			return MethodType.methodType(returnClass, new Class<?>[] { type,
					Context.class });
		}
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
