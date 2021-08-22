package com.antongoncharov.demo.surveys.model.spec

import com.antongoncharov.demo.surveys.model.Survey
import com.antongoncharov.demo.surveys.model.Tag
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*


class SurveySpec(searchParams: Map<String, String>) : Specification<Survey> {

    val searchCriteria = mutableListOf<SearchCriteria>()

    init {
        val ignoreParams = arrayOf("page", "size", "sort")
        searchParams.entries.filter { !ignoreParams.contains(it.key) }
                            .forEach {searchCriteria.add(SearchCriteria(it.key, it.value, SearchCriteria.SearchOperation.EQUAL)) }
    }

    override fun toPredicate(
        root: Root<Survey>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()
        for (criteria in searchCriteria) {
            when (criteria.operation) {
                SearchCriteria.SearchOperation.EQUAL -> {
                    val key: String
                    if (criteria.key == "tag") {
                        if (criteria.value == "none") {
                            // search untagged
                            predicates.add(criteriaBuilder.isEmpty(root.get("tags")));
                        } else {
                            val surveyTags: Join<Survey, Tag> = root.join("tags")
                            predicates.add(criteriaBuilder.equal(surveyTags.get<Any>("shortName"), criteria.value));
                        }
                    } else {
                        key = criteria.key
                        predicates.add(criteriaBuilder.equal(root.get<Any>(key), criteria.value))
                    }
                }
            }
        }
        return criteriaBuilder.and(*predicates.toTypedArray());
    }

}
