package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Question(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
//    @JsonIgnore
    var survey: Survey? = null,

    var text: String,
    var pos: Int,
    var multiselect: Boolean = false,
    var mandatory: Boolean = false,
    var active: Boolean = true,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "question_uuid") // using @JoinColumn for children to have parent relation when saved in one shot
    var choices: MutableList<Choice> = mutableListOf()
): JpaPersistable()
