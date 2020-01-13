package de.oth.packlon.service;


import de.oth.packlon.entity.Status;
import de.oth.packlon.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status createStatus(Status status){
       return statusRepository.save(status);
   }
}
