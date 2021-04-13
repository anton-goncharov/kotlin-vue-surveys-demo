package com.antongoncharov.demo.surveys.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
data class Choice(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    var question: Question? = null,

    var text: String,
    var pos: Int,
    var active: Boolean = true
): JpaPersistable()
