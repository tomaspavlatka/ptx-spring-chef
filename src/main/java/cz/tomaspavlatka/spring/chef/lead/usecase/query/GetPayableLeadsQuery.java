package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class GetPayableLeadsQuery {

  public <T> List<T> getPayableLeads(String filename, Class<T> type) throws IOException {
    ClassPathResource resource = new ClassPathResource("data/" + filename);

    Reader reader = new InputStreamReader(resource.getInputStream());
    CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
        .withType(type)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    return csvToBean.parse();
  }
}
