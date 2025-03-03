package com.mmstechnology.dmw.wallet_service.repository;

import com.mmstechnology.dmw.wallet_service.model.Alias;
import org.springframework.data.repository.CrudRepository;

public interface AliasGeneratorRepository extends CrudRepository<Alias, Long> {

    boolean existsByAlias(String alias);
}
