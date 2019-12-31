package de.oth.Packlon.service;


import de.oth.Packlon.entity.LineItem;
import de.oth.Packlon.repository.LineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineItemService {
    @Autowired
    private LineItemRepository lineItemRepository;
    @Autowired
    private PackService packService;
    public LineItem createLineItem(LineItem lineItem){
        lineItem.setPack(packService.getPackBySize(lineItem.getPack().getSize()));
        return  lineItemRepository.save(lineItem);
    }

}
