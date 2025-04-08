package cz.tomaspavlatka.spring.chef.company.model;

public record IncorrectRelation(
    String companyId,
    String easybillNumber,
    String currentId,
    String correctId) {
}
