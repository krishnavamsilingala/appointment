package com.agency.appointment.service.impl;

import com.agency.appointment.entity.Operator;
import com.agency.appointment.entity.QOperator;
import com.agency.appointment.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    public Operator getOperatorById(Long id){
        return operatorRepository.findOne(QOperator.operator.id.eq(id)).orElse(null);
    }

    public List<Operator> saveAll(List<Operator> operators){
        return operatorRepository.saveAll(operators);
    }

}
