package com.mmstechnology.dmw.wallet_service.repository;

import com.mmstechnology.dmw.wallet_service.model.Cvu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CvuGeneratorRepository extends CrudRepository<Cvu, Long> {
    boolean existsByCvu(String cvu);


}

