package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Cvu;

public interface ICvuGeneratorService {


    boolean exists(String cvu);

    String saveCvu(Cvu cvu);


}
