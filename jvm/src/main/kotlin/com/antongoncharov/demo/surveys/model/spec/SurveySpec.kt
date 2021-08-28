package com.antongoncharov.demo.surveys.model.spec

import com.antongoncharov.demo.surveys.model.Survey
import com.antongoncharov.demo.surveys.model.SurveyResponse
import com.antongoncharov.demo.surveys.model.Tag
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PAGE
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PARAM_STATUS_COMPLETED
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PARAM_STATUS_INCOMPLETE
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PARAM_TAG_UNTAGGED
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PARAM_TIME_THIS_WEEK
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.PARAM_TIME_TODAY
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.SEARCH_SURVEY_PARAM_STATUS
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.SEARCH_SURVEY_PARAM_TAG
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.SEARCH_SURVEY_PARAM_TIME
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.SIZE
import com.antongoncharov.demo.surveys.model.spec.SearchConstants.SORT
import com.antongoncharov.demo.surveys.security.RequestContext
import org.springframework.data.jpa.domain.Specification
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.criteria.*


class SurveySpec(searchParams: Map<String, String>) : Specification<Survey> {

    private val searchCriteria = mutableListOf<SearchCriteria>()

    init {
        val ignoreParams = arrayOf(PAGE, SIZE, SORT)
        searchParams.entries.filter { !ignoreParams.contains(it.key) }
                            .forEach {searchCriteria.add(SearchCriteria(it.key, it.value, SearchCriteria.SearchOperation.EQUAL)) }
    }

    override fun toPredicate(
        root: Root<Survey>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate? {
        val predicates = mutableListOf<Predicate>()
        for (criteria in searchCriteria) {
            when (criteria.operation) {
                SearchCriteria.SearchOperation.EQUAL -> {
                    val key: String
                    when (criteria.key) {
                        SEARCH_SURVEY_PARAM_TAG -> {
                            if (criteria.value == PARAM_TAG_UNTAGGED) {
                                // search untagged
                                predicates.add(cb.isEmpty(root.get("tags")));
                            } else {
                                val surveyTags: Join<Survey, Tag> = root.join("tags")
                                predicates.add(cb.equal(surveyTags.get<Any>("shortName"), criteria.value));
                            }
                        }
                        SEARCH_SURVEY_PARAM_STATUS -> {
                            when (criteria.value) {
                                PARAM_STATUS_COMPLETED -> {
                                    val responsesJoin: Join<Survey, SurveyResponse> = root.join("surveyResponses", JoinType.INNER)
                                    predicates.add(cb.and(
                                        cb.equal(responsesJoin.get<Boolean>("submitted"), true),
                                        cb.equal(responsesJoin.get<Any>("user").get<UUID>("id"), RequestContext.user?.uuid),
                                    ))
                                }
                                PARAM_STATUS_INCOMPLETE -> {
                                    val responsesJoin: Join<Survey, SurveyResponse> = root.join("surveyResponses", JoinType.LEFT)
                                    predicates.add(cb.or(
                                        cb.and(
                                            cb.isNull(responsesJoin.get<Any>("user").get<UUID>("id")),
                                            cb.isNull(responsesJoin.get<Boolean>("submitted"))
                                        ),
                                        cb.and(
                                            cb.equal(responsesJoin.get<Any>("user").get<UUID>("id"), RequestContext.user?.uuid),
                                            cb.equal(responsesJoin.get<Boolean>("submitted"), false)
                                        )
                                    ))
                                }
                            }
                        }
                        SEARCH_SURVEY_PARAM_TIME -> {
                            when (criteria.value) {
                                PARAM_TIME_TODAY -> {
                                    predicates.add(
                                        cb.between(root.get("createdDate"),
                                            Instant.now().minus(1, ChronoUnit.DAYS), Instant.now())
                                    )
                                }
                                PARAM_TIME_THIS_WEEK -> {
                                    predicates.add(
                                        cb.between(root.get("createdDate"),
                                            Instant.now().minus(7, ChronoUnit.DAYS), Instant.now())
                                    )
                                }
                            }
                        }
                        else -> {
                            key = criteria.key
                            predicates.add(cb.equal(root.get<Any>(key), criteria.value))
                        }
                    }
                }
            }
        }
        return cb.and(*predicates.toTypedArray());
    }

}
