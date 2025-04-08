package cz.tomaspavlatka.spring.chef.company.model;

import lombok.Getter;
import com.opencsv.bean.CsvBindByName;

@Getter
public class Relation {
  @CsvBindByName(column = "company_id")
  private String companyId;

  @CsvBindByName(column = "easybill_customer_id")
  private String easybillCustomerId;

  @CsvBindByName(column = "external_id")
  private String easybillNumber;
}
