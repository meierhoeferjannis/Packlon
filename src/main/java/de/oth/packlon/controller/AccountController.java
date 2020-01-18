package de.oth.packlon.controller;

import com.itextpdf.text.DocumentException;
import de.oth.packlon.entity.Account;
import de.oth.packlon.entity.Delivery;
import de.oth.packlon.service.AccountService;
import de.oth.packlon.service.AddressService;
import de.oth.packlon.service.DeliveryService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AccountController {
    private final AccountService accountService;
    private final DeliveryService deliveryService;
    private final AddressService addressService;

    public AccountController(AccountService accountService, DeliveryService deliveryService, AddressService addressService) {
        this.accountService = accountService;
        this.deliveryService = deliveryService;
        this.addressService = addressService;
    }

    @RequestMapping(value = {"/accountDetails"},method =  RequestMethod.GET)
    public String accountDetails(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        model.addAttribute("account",account);
        return "accountDetails";
    }

    @RequestMapping(value = {"/accountPaidDeliverys"}, method = RequestMethod.GET)
    public String accountPaidDeliverys(Model model,
                                       @RequestParam("pagePaid") Optional<Integer> pagePaid
    ) {
        addPaidDeliverys(model, pagePaid);
        return "accountPaidDeliverys";
    }

    @RequestMapping(value = {"/accountUnpaidDeliverys"}, method = RequestMethod.GET)
    public String accountUnpaidDeliverys(Model model,
                                         @RequestParam("pageUnpaid") Optional<Integer> pageUnpaid
    ) {
                addUnpaidDeliverys(model, pageUnpaid);
        return "accountUnpaidDeliverys";
    }

    @RequestMapping(value = "/cancelDelivery", method = RequestMethod.GET)
    public String cancelDelivery(@RequestParam(name = "deliveryId") long deliveryId,
                                 Model model,
                                 @RequestParam("pageUnpaid") Optional<Integer> pageUnpaid
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        try {
            Delivery deliveryToDelete = deliveryService.getDeliveryById(deliveryId);
            account.removeDelivery(deliveryToDelete);
            deliveryService.deleteDelivery(deliveryId);
            model.addAttribute("success", "Delivery successfully canceled");
        } catch (Exception e) {
            model.addAttribute("error", "An Error ocurred while deleting your Delivery");
        } finally {
             addUnpaidDeliverys(model, pageUnpaid);
            return "accountUnpaidDeliverys";
        }
    }

    @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public String updateAccount(Account updatedAccount, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());

        try {

            if (addressService.existsAddress(updatedAccount.getHomeAddress())) {
                account.setHomeAddress(addressService.getAddress(updatedAccount.getHomeAddress()));
            } else {
                account.setHomeAddress(addressService.createAddress(updatedAccount.getHomeAddress()));
            }
            account.setPhone(updatedAccount.getPhone());
            model.addAttribute("account", accountService.updateAccount(account));
            model.addAttribute("success", "You Successfully updated your Account Details");
            return "accountDetails";
        } catch (Exception e) {
            model.addAttribute("error", "An Error occurred while updating your Account Details");
            model.addAttribute("account", account);
            return "accountDetails";
        }
    }

    @RequestMapping(value = "/getDeliveryLabel", method = RequestMethod.GET)
    public void getDeliveryLabel(
            @RequestParam(name = "deliveryId") long deliveryId,
            HttpServletResponse response) {
        try {

            InputStream is = deliveryService.createPDFforDelivery(deliveryId);

            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            is.close();

            Files.delete(Path.of(deliveryId+".pdf"));
            Files.delete(Path.of(deliveryId+"decrypted.pdf"));
        } catch (IOException | DocumentException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void addUnpaidDeliverys(Model model, @RequestParam("pageUnpaid") Optional<Integer> pageUnpaid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        Page<Delivery> unpaidDelivery = deliveryService.getDeliveryPageForSender(false, account.getOwner(), PageRequest.of(pageUnpaid.orElse(1) - 1, 5));
        model.addAttribute("unpaidDeliverys", unpaidDelivery);
        model.addAttribute("pageNumber",pageUnpaid);
        int totalPagesUnpaid = unpaidDelivery.getTotalPages();
        if (totalPagesUnpaid > 0) {
            List<Integer> pageNumbersUnpaid = IntStream.rangeClosed(1, totalPagesUnpaid)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbersUnpaid", pageNumbersUnpaid);
        }

    }
    private void addPaidDeliverys(Model model, @RequestParam("pagePaid") Optional<Integer> pagePaid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountService.getAccountByEmail(auth.getName());
        Page<Delivery> paidDelivery = deliveryService.getDeliveryPageForSender(true, account.getOwner(), PageRequest.of(pagePaid.orElse(1) - 1, 5));
        model.addAttribute("paidDeliverys", paidDelivery);
        model.addAttribute("pageNumber",pagePaid);

        int totalPagesPaid = paidDelivery.getTotalPages();
        if (totalPagesPaid > 0) {
            List<Integer> pageNumbersPaid = IntStream.rangeClosed(1, totalPagesPaid)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbersPaid", pageNumbersPaid);
        }
    }
}
