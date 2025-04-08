package cz.tomaspavlatka.spring.chef.easybill.usecase.query;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.easybill.EasybillClient;
import cz.tomaspavlatka.spring.chef.easybill.model.Customer;

@Service
public class FindCustomerByNumberQuery {
  private final EasybillClient easybillClient;

  public FindCustomerByNumberQuery(EasybillClient easybillClient) {
    this.easybillClient = easybillClient;
  }

  public Optional<Customer> findByNumber(String number) {
    return this.easybillClient.getCustomersByNumber(number)
        .items()
        .stream()
        .findFirst();
  }
}
