package se.kth.id1212.appserv.conversion.Integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.appserv.conversion.Model.*;

@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service
public class MyConversionService {

    @Autowired
    private CurrencyRepository currencyRepo;
    @Autowired
    private ConversionRepository conversionRepo;

    // get available currencies
    public List<Currency> getCurrencies() {
        return currencyRepo.findAll();
    }

    // find currency by name
    public Currency findCurrency(String name) {
        return currencyRepo.findCurrencyByName(name);
    }

    // convert
    public float convert(Currency fromCurrency, Currency toCurrency, float amount) {
        return amount*fromCurrency.getWorthInGold()/toCurrency.getWorthInGold();
    }

    // add new currency
    public void add(String name, float worthInGold) {
        currencyRepo.save(new Currency(name, worthInGold));
    }

    // modify
    public void modify(String name, float worthInGold) {
        Currency currency = currencyRepo.findCurrencyByName(name);
        currency.setWorthInGold(worthInGold);
    }

    // save conversion
    public void saveConversion(float amount) {
        conversionRepo.save(new Conversion(amount));
    }

    // get number of conversions
    public int getConversions() {
        List<Conversion> conversions = conversionRepo.findAll();
        return conversions.size();
    }
}
