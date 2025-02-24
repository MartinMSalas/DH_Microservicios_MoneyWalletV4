package com.mmstechnology.dmw.wallet_service.service.impl;

import com.mmstechnology.dmw.wallet_service.model.Cvu;
import com.mmstechnology.dmw.wallet_service.repository.CvuGeneratorRepository;
import com.mmstechnology.dmw.wallet_service.service.ICvuGeneratorService;

public class CvuGeneratorServiceImpl implements ICvuGeneratorService {

    private final CvuGeneratorRepository cvuGeneratorRepository;

    public CvuGeneratorServiceImpl(CvuGeneratorRepository cvuGeneratorRepository) {
        this.cvuGeneratorRepository = cvuGeneratorRepository;
    }

    @Override
    public boolean exists(String cvu) {

        return cvuGeneratorRepository.existsByCvu(cvu);


    }

    @Override
    public String saveCvu(Cvu cvu) {
        cvuGeneratorRepository.save(cvu);
        return cvu.getCvu();
    }
}
