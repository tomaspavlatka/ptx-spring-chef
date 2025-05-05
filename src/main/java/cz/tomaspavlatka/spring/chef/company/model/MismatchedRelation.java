package cz.tomaspavlatka.spring.chef.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MismatchedRelation {
  private String companyId;
  private String easybillNumber;
  private String currentCustomerId;
  private String realCustomerId;
}
