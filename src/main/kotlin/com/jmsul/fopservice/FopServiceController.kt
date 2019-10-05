package com.jmsul.fopservice

import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.springframework.http.MediaType.APPLICATION_PDF_VALUE
import org.springframework.http.MediaType.APPLICATION_XML_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.StringReader
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

@RestController
class FopServiceController {
    private final val confFile = File("fop.xconf")
    val fopFactory: FopFactory = FopFactory.newInstance(confFile)
    val tFactory: TransformerFactory = TransformerFactory.newInstance()

    @PostMapping(path = ["/process"],
            consumes = [APPLICATION_XML_VALUE],
            produces = [APPLICATION_PDF_VALUE])
    fun fopper(@RequestBody xmlfotpl: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        try {
            val fop: Fop = fopFactory.newFop(MimeConstants.MIME_PDF, outputStream)
            val transformer: Transformer = tFactory.newTransformer()
            val src: Source = StreamSource(StringReader(xmlfotpl))
            val res: Result = SAXResult(fop.defaultHandler)
            transformer.transform(src, res)
        } catch (ex: Exception) {
        }
        return outputStream.toByteArray()
    }
}
