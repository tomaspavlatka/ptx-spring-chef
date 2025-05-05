package cz.tomaspavlatka.spring.chef.company.usecase.query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import cz.tomaspavlatka.spring.chef.company.model.Relation;

@Service
public class GetCurrentRelationsQuery {
  public List<Relation> execute(String filename) throws IOException {
    ClassPathResource resource = new ClassPathResource("data/" + filename);

    Reader reader = new InputStreamReader(resource.getInputStream());
    CsvToBean<Relation> csvToBean = new CsvToBeanBuilder<Relation>(reader)
        .withType(Relation.class)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    return csvToBean.parse();
  }
}
