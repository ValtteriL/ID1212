package se.kth.id1212.appserv.bank.presentation.acct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.id1212.appserv.bank.application.BankService;
import se.kth.id1212.appserv.bank.domain.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

import javax.validation.Valid;

/**
 * Handles all HTTP requests
 */

@Controller
@Scope("session")
public class ConversionController {
    static final String DEFAULT_PAGE_URL = "index";
    static final String CONVERT_PAGE_URL = "convert";
    private static final String CONVERT_FORM_OBJ_NAME = "convertForm";
    private static final String CONVERSION_RESULT_NAME = "conversionResult";
    private static final String CURRENCY_ALTERNATIVES = "currencyAlternatives";

    private float result = 0;
    private List<Currency> currencies;
    @Autowired
    private BankService service;

    // No page is specified, return conversion form
    @GetMapping("/")
    public String showConversionView(ConversionForm conversionForm, Model model) {
        return showConversionPage(model, conversionForm);
    }

    // Conversion form has been submitted
    @PostMapping("/" + CONVERT_PAGE_URL)
    public String convert(@ModelAttribute(CONVERT_FORM_OBJ_NAME) @Valid ConversionForm conversionForm,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return showConversionPage(model, conversionForm);
        }

        Currency fromcCurrency = service.findCurrency(conversionForm.getFrom());
        Currency toCurrency = service.findCurrency(conversionForm.getTo());

        // check that the currencies exist
        if (fromcCurrency != null && toCurrency != null) {
            result = service.convert(fromcCurrency, toCurrency, conversionForm.getAmount()); // do conversion
            service.saveConversion(conversionForm.getAmount()); // increment conversions
        } else {
            if (fromcCurrency == null) {
                bindingResult.rejectValue("from", "error.user", "No such currency.");
            }
            if (toCurrency == null) {
                bindingResult.rejectValue("to", "error.user", "No such currency.");
            }
        }

        return showConversionPage(model, conversionForm);
    }

    // bind variables and show the conversion page
    private String showConversionPage(Model model, ConversionForm conversionForm) {

        currencies = service.getCurrencies();
        model.addAttribute(CURRENCY_ALTERNATIVES, currencies);

        model.addAttribute(CONVERSION_RESULT_NAME, result);

        if (conversionForm != null) {
            model.addAttribute(CONVERT_FORM_OBJ_NAME, conversionForm);
        }
        return DEFAULT_PAGE_URL;
    }
}
