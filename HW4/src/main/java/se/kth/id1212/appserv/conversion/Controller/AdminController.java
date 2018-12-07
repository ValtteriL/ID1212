package se.kth.id1212.appserv.conversion.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.id1212.appserv.conversion.Integration.*;
import se.kth.id1212.appserv.conversion.Model.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import javax.validation.Valid;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@Scope("session")
public class AdminController {

    static final String ADMIN_PAGE_URL = "admin";
    static final String ADD_CURRENCY_URL = "add";
    static final String MODIFY_CURRENCY_URL = "modify";
    private static final String ADD_CURRENCY_FORM_OBJ_NAME = "addCurrencyForm";
    private static final String MODIFY_CURRENCY_FORM_OBJ_NAME = "modifyCurrencyForm";
    private static final String CONVERSION_OBJ_NAME = "conversions";
    private static final String CURRENCY_ALTERNATIVES = "currencyAlternatives";

    private List<Currency> currencies;
    private int conversions;

    @Autowired
    private MyConversionService service;


    // autowire this controller
    @Autowired
    public AdminController() {
    }

    // Admin page accessed
    @GetMapping("/" + ADMIN_PAGE_URL)
    public String showAdminView(CurrencyCreateForm currencyCreateForm, CurrencyModifyForm currencyModifyForm,
            Model model) {
        return showAdminPage(model, currencyCreateForm, currencyModifyForm);
    }

    // Currency add form has been submitted
    @PostMapping("/" + ADD_CURRENCY_URL)
    public String add(@ModelAttribute(ADD_CURRENCY_FORM_OBJ_NAME) @Valid CurrencyCreateForm currencyCreateForm,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showAdminPage(model, currencyCreateForm, new CurrencyModifyForm());
        }

        service.add(currencyCreateForm.getCurrency(), currencyCreateForm.getAmount()); // create new currency
        return "redirect:" + ADMIN_PAGE_URL;
    }

    // Currency modify form has been submitted
    @PostMapping("/" + MODIFY_CURRENCY_URL)
    public String modify(@ModelAttribute(MODIFY_CURRENCY_FORM_OBJ_NAME) @Valid CurrencyModifyForm currencyModifyForm,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showAdminPage(model, new CurrencyCreateForm(), currencyModifyForm);
        }

        Currency currency = service.findCurrency(currencyModifyForm.getCurrency());

        // Check that the currency exists
        if (currency == null) {
            bindingResult.rejectValue("currency", "error.user", "No such currency.");
            return showAdminPage(model, new CurrencyCreateForm(), currencyModifyForm);
        } else {
            // modify currency
            service.modify(currencyModifyForm.getCurrency(), currencyModifyForm.getAmount());
        }

        return "redirect:" + ADMIN_PAGE_URL;
    }

    // Bind variables for view and show admin page
    private String showAdminPage(Model model, CurrencyCreateForm currencyCreateForm,
            CurrencyModifyForm currencyModifyForm) {

        currencies = service.getCurrencies();
        model.addAttribute(CURRENCY_ALTERNATIVES, currencies);

        conversions = service.getConversions();
        model.addAttribute(CONVERSION_OBJ_NAME, conversions);

        if (currencyCreateForm != null) {
            model.addAttribute(ADD_CURRENCY_FORM_OBJ_NAME, currencyCreateForm);
        }
        if (currencyModifyForm != null) {
            model.addAttribute(MODIFY_CURRENCY_FORM_OBJ_NAME, currencyModifyForm);
        }
        return ADMIN_PAGE_URL;
    }
}
