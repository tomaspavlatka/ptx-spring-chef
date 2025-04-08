package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import cz.tomaspavlatka.spring.chef.lead.model.PayableLead;

@Service
public class GetPayableLeadsQuery {

  public List<PayableLead> getPayableLeads(String filename) throws IOException {
    ClassPathResource resource = new ClassPathResource("data/" + filename);

    Reader reader = new InputStreamReader(resource.getInputStream());
    CsvToBean<PayableLead> csvToBean = new CsvToBeanBuilder<PayableLead>(reader)
        .withType(PayableLead.class)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    return csvToBean.parse();
  }
}
