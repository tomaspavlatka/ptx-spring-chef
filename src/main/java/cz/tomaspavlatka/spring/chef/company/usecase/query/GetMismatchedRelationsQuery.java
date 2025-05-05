package cz.tomaspavlatka.spring.chef.company.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.company.model.MismatchedRelation;
import cz.tomaspavlatka.spring.chef.company.model.Relation;
import cz.tomaspavlatka.spring.chef.easybill.usecase.query.FindCustomerByNumberQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetMismatchedRelationsQuery {
  private final FindCustomerByNumberQuery findCustomerByNumberQuery;
  private final GetCurrentRelationsQuery getCurrentRelationsQuery;

  public List<MismatchedRelation> execute(String filename) throws IOException {
    List<Relation> currentRelations = getCurrentRelationsQuery.execute(filename);

    List<MismatchedRelation> mismatchedRelations = new ArrayList<MismatchedRelation>();
    for (var i = 0; i < currentRelations.size(); i++) {
      var relation = currentRelations.get(i);
      var customer = this.findCustomerByNumberQuery.execute(relation.getEasybillNumber());

      if (customer.isPresent()) {
        if (!customer.get().id().equals(relation.getEasybillCustomerId())) {
          mismatchedRelations.add(new MismatchedRelation(
              relation.getCompanyId(),
              relation.getEasybillNumber(),
              relation.getEasybillCustomerId(),
              customer.get().id()));
        }
      } else {
        mismatchedRelations.add(new MismatchedRelation(
          relation.getCompanyId(),
          relation.getEasybillNumber(),
          relation.getEasybillCustomerId(),
          "!missing!"
        ));
      }
    }

    return mismatchedRelations;
  }
}
