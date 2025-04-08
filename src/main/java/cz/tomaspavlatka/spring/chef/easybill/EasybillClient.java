package cz.tomaspavlatka.spring.chef.easybill;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import cz.tomaspavlatka.spring.chef.easybill.model.Customers;
import cz.tomaspavlatka.spring.chef.lead.model.OrganizationsResponse;

public interface EasybillClient {
  @GetExchange("/customers?number={number}")
  Customers getCustomersByNumber(@PathVariable String number);
}

