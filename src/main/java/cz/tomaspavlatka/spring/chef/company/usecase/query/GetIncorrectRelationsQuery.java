package cz.tomaspavlatka.spring.chef.company.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.company.model.IncorrectRelation;
import cz.tomaspavlatka.spring.chef.company.model.Relation;
import cz.tomaspavlatka.spring.chef.easybill.usecase.query.FindCustomerByNumberQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetLegacyPayableLeadsQuery;

@Service
public class GetIncorrectRelationsQuery {
  private final FindCustomerByNumberQuery findCustomerByNumberQuery;

  public GetIncorrectRelationsQuery(
      FindCustomerByNumberQuery findCustomerByNumberQuery) {
    this.findCustomerByNumberQuery = findCustomerByNumberQuery;
  }

  public List<IncorrectRelation> getIncorrectRelation(List<Relation> current) throws IOException {
    var incorrect = new ArrayList<IncorrectRelation>();

    current.forEach(rel -> {
      var customer = this.findCustomerByNumberQuery.findByNumber(rel.getEasybillNumber());

      if (customer.isEmpty()) {
        System.err.println("Missing customer for the number: " + rel.getEasybillNumber());
      } else if (!customer.get().id().equals(rel.getEasybillCustomerId())) {
        incorrect.add(new IncorrectRelation(
            rel.getCompanyId(),
            rel.getEasybillNumber(),
            rel.getEasybillCustomerId(),
            customer.get().id()));
      }

    });

    return incorrect;
  }
}
