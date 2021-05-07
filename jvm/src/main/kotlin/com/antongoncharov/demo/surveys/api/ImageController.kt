package com.antongoncharov.demo.surveys.api

import com.antongoncharov.demo.surveys.AppProperties
import com.antongoncharov.demo.surveys.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
class ImageController(
    private val appProperties: AppProperties
) {

    val log by logger()

    private val imageStore = File(appProperties.imageStore["path"])

    init {
        imageStore.mkdirs()
    }

    @PostMapping("/images")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        log.info("POST /images")
        // size should be < 100kb (otherwise returns HTTP 413 Payload Too Large)
        if (file.size > 1024*100) {
            return ResponseEntity.status(413).build()
        }

        if (file.contentType == null) {
            return ResponseEntity.badRequest().build()
        }

        File(imageStore, file.originalFilename ?: "newfile").writeBytes(file.bytes)

//        TODO handle
//        file.bytes
//        file.contentType

        return ResponseEntity.ok().build()
    }

}