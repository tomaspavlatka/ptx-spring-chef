package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.company.model.Relation;
import cz.tomaspavlatka.spring.chef.company.usecase.query.GetCurrentRelationsQuery;
import cz.tomaspavlatka.spring.chef.lead.model.BillingsPerCompany;
import cz.tomaspavlatka.spring.chef.lead.model.Company;
import cz.tomaspavlatka.spring.chef.lead.model.MissingRelations;

@Service
public class GetMissingRelationsQuery {
  private GetCurrentRelationsQuery getCurrentRelationsQuery;
  private GetBillingsPerCompanyQuery getBillingsPerCompanyQuery;

  public GetMissingRelationsQuery(
    GetCurrentRelationsQuery getCurrentRelationsQuery,
    GetBillingsPerCompanyQuery getBillingsPerCompanyQuery
  ) {
    this.getCurrentRelationsQuery = getCurrentRelationsQuery;
    this.getBillingsPerCompanyQuery = getBillingsPerCompanyQuery;
  }

  public MissingRelations execute(Integer year, Integer month, String relationsFile) throws IOException {
    List<Relation> currentRelations = getCurrentRelationsQuery.execute(relationsFile);
    BillingsPerCompany summary = getBillingsPerCompanyQuery.execute(year, month);

    List<Company> missingCompanies = new ArrayList<Company>();
    summary.companies().forEach(comp -> {
      var found = false;

      for (var i = 0; i < currentRelations.size(); i++) {
        if (currentRelations.get(i).getCompanyId().equals(comp.auth0Id())) {
          found = true;
          break;
        }
      }

      if (!found) {
        missingCompanies.add(new Company(comp.id(), comp.auth0Id(), comp.name(), comp.dispalyName()));
      }
    });

    return new MissingRelations(year, month, missingCompanies);
  }
}
