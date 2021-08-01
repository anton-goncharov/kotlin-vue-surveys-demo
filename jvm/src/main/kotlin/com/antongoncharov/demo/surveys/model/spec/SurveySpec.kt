package com.antongoncharov.demo.surveys.model.spec

import com.antongoncharov.demo.surveys.model.Survey
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class SurveySpec: Specification<Survey> {

    val searchCriteria = mutableListOf<SearchCriteria>()

    override fun toPredicate(
        root: Root<Survey>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()
        for (criteria in searchCriteria) {
            when (criteria.operation) {
                SearchCriteria.SearchOperation.EQUAL -> {
                    predicates.add(criteriaBuilder.equal(root.get<Any>(criteria.key), criteria.value))
                }
            }
        }
        return criteriaBuilder.and(*predicates.toTypedArray());
    }

}