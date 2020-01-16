package de.oth.packlon.service;


import de.oth.packlon.entity.LineItem;
import de.oth.packlon.repository.LineItemRepository;
import org.springframework.stereotype.Service;

@Service
public class LineItemService implements ILineItemService {
    private final LineItemRepository lineItemRepository;
    private final PackService packService;

    public LineItemService(LineItemRepository lineItemRepository, PackService packService) {
        this.lineItemRepository = lineItemRepository;
        this.packService = packService;
    }

    @Override
    public LineItem createLineItem(LineItem lineItem){
        lineItem.setPack(packService.getPackBySize(lineItem.getPack().getSize()));
        return  lineItemRepository.save(lineItem);
    }

}
