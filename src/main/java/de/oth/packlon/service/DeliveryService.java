package de.oth.packlon.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import de.oth.packlon.entity.*;
import de.oth.packlon.repository.DeliveryRepository;
import de.oth.packlon.service.model.DeliveryRequestException;
import de.oth.packlon.service.model.TransactionDTO;
import org.apache.tomcat.util.codec.binary.Base64;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.Receiver;
import java.io.*;
import java.net.URI;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DeliveryService implements IDeliveryService {
    private final RestTemplate restServerClient;
    private final AccountService accountService;
    private final DeliveryRepository deliveryRepository;
    private final CustomerService customerService;
    private final LineItemService lineItemService;
    private final AddressService addressService;
    private final PackService packService;

    public DeliveryService(RestTemplate restServerClient, AccountService accountService, DeliveryRepository deliveryRepository, CustomerService customerService, LineItemService lineItemService, AddressService addressService, PackService packService) {
        this.restServerClient = restServerClient;
        this.accountService = accountService;
        this.deliveryRepository = deliveryRepository;
        this.customerService = customerService;
        this.lineItemService = lineItemService;
        this.addressService = addressService;
        this.packService = packService;
    }

    @Override
    public Page<Delivery> getDeliveryPageForSender(boolean paid, Customer sender, Pageable pageable) {
        return deliveryRepository.findAllByPaidAndSender(paid, sender, pageable);
    }

    @Override
    @Transactional
    public Delivery createDelivery(Delivery delivery) {
        delivery.setReceiver(customerService.getCustomerByName(delivery.getReceiver()));
        delivery.setSender(customerService.getCustomerByName(delivery.getSender()));
        List<LineItem> items = new ArrayList<LineItem>();
        for (LineItem item : delivery.getLineItemList()) {
            items.add(lineItemService.createLineItem(item));
        }
        delivery.setLineItemList(items);
        Address recAddress;
        Address sendAddress;
        if (addressService.existsAddress(delivery.getReceiverAddress())) {
            recAddress = addressService.getAddress(delivery.getReceiverAddress());
        } else {
            delivery.setReceiverAddress(addressService.createAddress(delivery.getReceiverAddress()));
        }
        if (addressService.existsAddress(delivery.getSenderAddress())) {
            sendAddress = addressService.getAddress(delivery.getSenderAddress());
        } else {
            sendAddress = addressService.createAddress(delivery.getSenderAddress());
        }
        Status status = new Status();
        status.setText("Delivery Created");

        delivery.addStatus(status);
        return deliveryRepository.save(delivery);

    }

    @Override
    public void deleteDelivery(long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    @Override
    public Delivery getDeliveryById(long deliveryId) {
        return deliveryRepository.findById(deliveryId).get();
    }

    @Override
    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public long requestDelivery(Delivery delivery ) throws DeliveryRequestException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountService.getAccountByEmail(auth.getName());
            Delivery newDelivery = new Delivery();
            if (delivery.getSenderAddress() == null) {
                newDelivery.setSenderAddress(account.getHomeAddress());
            } else {
                if (addressService.existsAddress(delivery.getSenderAddress())) {
                    newDelivery.setSenderAddress(addressService.getAddress(delivery.getSenderAddress()));
                } else {
                    newDelivery.setSenderAddress(addressService.createAddress(delivery.getSenderAddress()));
                }
            }
            if (addressService.existsAddress(delivery.getReceiverAddress())) {
                newDelivery.setReceiverAddress(addressService.getAddress(delivery.getReceiverAddress()));
            } else {
                newDelivery.setReceiverAddress(addressService.createAddress(delivery.getReceiverAddress()));
            }
            if (delivery.getSender() == null) {
                newDelivery.setSender(account.getOwner());
            } else {
                newDelivery.setSender(customerService.getCustomerByName(delivery.getSender()));
            }
            newDelivery.setReceiver(customerService.getCustomerByName(delivery.getReceiver()));
            for (LineItem item : delivery.getLineItemList()) {
                if (item.getAmount() != 0) {
                    LineItem lineItem = new LineItem(item.getAmount(), packService.getPackBySize(item.getPack().getSize()));
                    lineItem.calculatePrice();
                    newDelivery.addLineItem(lineItem);
                }
            }
            Status status = new Status();
            status.setText("Delivery Created per Request");
            if (delivery.getPaymentReference() == null){
                newDelivery.setPaymentDate(new Date());
                newDelivery.setPaid(true);
            }

            newDelivery.addStatus(status);
            return deliveryRepository.save(newDelivery).id;
        } catch (Exception e) {
            throw new DeliveryRequestException(e.getMessage());
        }

    }

    @Override
    public Delivery payDelivery(Delivery delivery, String username, String password) throws JsonProcessingException {

        TransactionDTO transactionDTO = new TransactionDTO("packlon@web.de", delivery.totalPrice(), "Reference:" + delivery.id);
        URI uri = URI.create("http://im-codd.oth-regensburg.de:8827/requestTransaction");
        HttpHeaders headers = createHeaders(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO, headers);
        restServerClient.postForObject(uri, request, TransactionDTO.class);
        delivery.setPaid(true);
        delivery.setPaymentDate(new Date());
        return deliveryRepository.save(delivery);
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }


    @Override
    public InputStream createPDFforDelivery(long deliveryId) throws IOException, DocumentException {
        Delivery delivery = deliveryRepository.findById(deliveryId).get();
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(delivery.id + "decrypted.pdf"));
        document.open();
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        addRows(table, delivery);
        document.add(table);
        document.close();
        PdfReader pdfReader = new PdfReader(delivery.id + "decrypted.pdf");
        PdfStamper pdfStamper
                = new PdfStamper(pdfReader, new FileOutputStream(delivery.id + ".pdf"));

        pdfStamper.setEncryption(
                "".getBytes(),
                "".getBytes(),
                PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY,
                PdfWriter.ENCRYPTION_AES_256
        );

        pdfStamper.close();
        InputStream in = new FileInputStream(delivery.id + ".pdf");
        Files.delete(Path.of(deliveryId+".pdf"));
        return in;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Sender", "Details", "Receiver", "Details")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Delivery delivery) {
        table.addCell("Country:");
        table.addCell(delivery.getSenderAddress().getCountry());
        table.addCell("Country:");
        table.addCell(delivery.getReceiverAddress().getCountry());
        table.completeRow();
        table.addCell("City:");
        table.addCell(delivery.getSenderAddress().getCity());
        table.addCell("City:");
        table.addCell(delivery.getReceiverAddress().getCity());
        table.completeRow();
        table.addCell("Postcode:");
        table.addCell(Integer.toString(delivery.getSenderAddress().getPostCode()));
        table.addCell("Postcode:");
        table.addCell(Integer.toString(delivery.getReceiverAddress().getPostCode()));
        table.completeRow();
        table.addCell("Street:");
        table.addCell(delivery.getSenderAddress().getStreet());
        table.addCell("Street:");
        table.addCell(delivery.getReceiverAddress().getStreet());
        table.completeRow();
        table.addCell("Addition:");
        table.addCell(delivery.getSenderAddress().getAddition());
        table.addCell("Addition:");
        table.addCell(delivery.getReceiverAddress().getAddition());
        table.completeRow();

    }

    private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    @Override
    public List<Delivery> getDeliveriesToDeliver() {
        return deliveryRepository.findAllByCashOnDeliveryOrPaidAndSubmittedIsNull(true, true);
    }

}
