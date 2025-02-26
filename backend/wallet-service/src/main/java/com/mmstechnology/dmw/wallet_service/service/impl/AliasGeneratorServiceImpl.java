package com.mmstechnology.dmw.wallet_service.service.impl;

import com.mmstechnology.dmw.wallet_service.model.Alias;
import com.mmstechnology.dmw.wallet_service.repository.AliasGeneratorRepository;
import com.mmstechnology.dmw.wallet_service.service.IAliasGeneratorService;
import org.springframework.stereotype.Service;

@Service
public class AliasGeneratorServiceImpl implements IAliasGeneratorService {

    private final AliasGeneratorRepository aliasGeneratorRepository;

    public AliasGeneratorServiceImpl(AliasGeneratorRepository aliasGeneratorRepository) {
        this.aliasGeneratorRepository = aliasGeneratorRepository;
    }

    @Override
    public boolean exists(String alias) {
        return aliasGeneratorRepository.existsByAlias(alias);

    }

    @Override
    public String saveAlias(Alias alias) {
        aliasGeneratorRepository.save(alias);
        return alias.getAlias();
    }
}
