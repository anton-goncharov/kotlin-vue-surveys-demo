package com.antongoncharov.demo.surveys.model

import javax.persistence.Entity

@Entity
data class Tag (

    var name: String,
    var shortName: String

): JpaPersistable()
