package de.oth.Packlon.service;


import de.oth.Packlon.entity.Status;
import de.oth.Packlon.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
   public Status createStatus(Status status){
       return statusRepository.save(status);
   }
}
