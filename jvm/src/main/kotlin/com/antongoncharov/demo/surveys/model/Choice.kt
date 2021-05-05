package com.antongoncharov.demo.surveys.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
data class Choice(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    var question: Question? = null,

    var text: String ? = null,
    var pos: Int? = null,
    var active: Boolean = true
): JpaPersistable()
