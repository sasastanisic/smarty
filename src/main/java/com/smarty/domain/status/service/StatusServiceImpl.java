package com.smarty.domain.status.service;

import com.smarty.domain.status.entity.Status;
import com.smarty.domain.status.repository.StatusRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    private static final String STATUS_NOT_EXISTS = "Status with id %d doesn't exist";

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status getStatusById(Long id) {
        Optional<Status> optionalStatus = statusRepository.findById(id);

        if (optionalStatus.isEmpty()) {
            throw new NotFoundException(STATUS_NOT_EXISTS.formatted(id));
        }

        return optionalStatus.get();
    }

    @Override
    public void existsById(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new NotFoundException(STATUS_NOT_EXISTS.formatted(id));
        }
    }

}
