package de.oth.packlon.service;


import de.oth.packlon.entity.Status;
import de.oth.packlon.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService implements IStatusService {
    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status createStatus(Status status){
       return statusRepository.save(status);
   }
}
