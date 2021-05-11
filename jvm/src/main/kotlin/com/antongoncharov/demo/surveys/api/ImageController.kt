package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.AppProperties
import com.antongoncharov.demo.surveys.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.util.unit.DataSize
import org.springframework.util.unit.DataUnit
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class ImageController(
    private val appProperties: AppProperties
) {

    val log by logger()

    private val imageStore = File(appProperties.imageStore["path"])

    private val FILE_SIZE_LIMIT_BYTES = DataSize.ofMegabytes(5).toBytes()

    init {
        imageStore.mkdirs()
    }

    @PostMapping("/images")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        log.info("POST /images")
        // size should be < 5MB (otherwise returns HTTP 413 Payload Too Large)
        if (file.size > FILE_SIZE_LIMIT_BYTES) {
            return ResponseEntity.status(413).build()
        }

        if (file.contentType == null || file.originalFilename == null) {
            return ResponseEntity.badRequest().build()
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_")
        val filename = current.format(formatter) + file.originalFilename

        File(imageStore, filename).writeBytes(file.bytes)

        return ResponseEntity.ok().header("Location", "/api/v1/uploads/$filename").build()
    }

}