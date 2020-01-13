package de.oth.Packlon.service;


import de.oth.Packlon.entity.Status;
import de.oth.Packlon.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
