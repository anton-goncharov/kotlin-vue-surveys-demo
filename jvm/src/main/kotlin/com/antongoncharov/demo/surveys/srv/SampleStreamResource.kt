package com.antongoncharov.demo.surveys.srv

import com.antongoncharov.demo.surveys.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration.ofSeconds
import kotlin.random.Random


@Controller
@MessageMapping("api.v1.sample")
class SampleStreamResource(val surveyResponseRxService: SurveyResponseRxService) {

    data class Sample(val name: String, val quantity: Int)

    val log by logger()

    val stream = Flux
        .interval(ofSeconds(3))
        .map { Sample(name = "test", quantity = Random(100).nextInt()) }
        .doOnSubscribe { log.info("New subscription") }
        .share()

    fun streamOfSampleData(): Flux<Sample> {
        return stream
    }

    @MessageMapping("stream")
    fun send(): Flow<Sample> = this.streamOfSampleData().asFlow()

}