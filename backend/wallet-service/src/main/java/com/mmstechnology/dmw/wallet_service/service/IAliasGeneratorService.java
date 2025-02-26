package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Alias;

public interface IAliasGeneratorService {
    boolean exists(String alias);

    String saveAlias(Alias alias);
}
