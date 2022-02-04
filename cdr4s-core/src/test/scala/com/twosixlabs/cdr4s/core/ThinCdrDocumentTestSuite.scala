package com.twosixlabs.cdr4s.core

import com.twosixlabs.cdr4s.annotations.{FacetScore, OffsetTag, TranslatedFields}
import com.twosixlabs.dart.utils.{DatesAndTimes, IdGenerator}
import org.scalatest.{FlatSpecLike, Matchers}

import java.time.LocalDate

class ThinCdrDocumentTestSuite extends FlatSpecLike with Matchers {

    "Thin CdrDocument" should "generate a ThinCdrDocument object when all fields are defined" in {
        val ts = DatesAndTimes.timeStamp
        val anno = DictionaryAnnotation( "test", "1.0", Map( "a" -> "b" ) )
        val en = Map( "Key One" -> "Value One", "Key Two" -> "Value Two" )
        val dt = LocalDate.now()

        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = dt,
                                                modificationDate = dt,
                                                author = "Some Author",
                                                docType = "Article",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Some Title",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com",
                                                pages = Some( 0 ),
                                                subject = "A subject",
                                                creator = "The Creator",
                                                producer = "Jack Donaghy",
                                                statedGenre = "doctrine",
                                                predictedGenre = "think_tank" )
                               },
                               contentType = "text/html",
                               extractedNumeric = en,
                               documentId = "123abc",
                               extractedText = "Some long text",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = ts,
                               annotations = List( anno ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ],
                               labels = Set( "lorum", "ipsum" ) )


        val thinCdr = cdr.makeThin


        thinCdr.documentId shouldBe cdr.documentId
        thinCdr.contentType.get shouldBe IdGenerator.getMd5Hash( cdr.contentType.getBytes )
        thinCdr.extractedNumeric.get shouldBe IdGenerator.getMd5Hash( cdr.extractedNumeric.mkString( "," ).getBytes )
        thinCdr.extractedText.get shouldBe IdGenerator.getMd5Hash( cdr.extractedText.getBytes )
        thinCdr.uri.get shouldBe IdGenerator.getMd5Hash( cdr.uri.getBytes )
        thinCdr.sourceUri.get shouldBe IdGenerator.getMd5Hash( cdr.sourceUri.getBytes )
        thinCdr.extractedNtriples.get shouldBe IdGenerator.getMd5Hash( cdr.extractedNtriples.getBytes )
        thinCdr.timestamp.get shouldBe DatesAndTimes.toIsoOffsetDateTimeStr( cdr.timestamp )
        thinCdr.captureSource.get shouldBe IdGenerator.getMd5Hash( cdr.captureSource.getBytes )
        thinCdr.labels.get shouldBe IdGenerator.getMd5Hash( cdr.labels.mkString( "," ).getBytes )

        thinCdr.extractedMetadata.publisher.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.publisher.getBytes )
        thinCdr.extractedMetadata.url.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.url.getBytes )
        thinCdr.extractedMetadata.creationDate.get shouldBe IdGenerator.getMd5Hash( DatesAndTimes.toIsoLocalDateStr( cdr.extractedMetadata.creationDate ).getBytes )
        thinCdr.extractedMetadata.modificationDate.get shouldBe IdGenerator.getMd5Hash( DatesAndTimes.toIsoLocalDateStr( cdr.extractedMetadata.modificationDate ).getBytes )
        thinCdr.extractedMetadata.author.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.author.getBytes )
        thinCdr.extractedMetadata.docType.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.docType.getBytes )
        thinCdr.extractedMetadata.description.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.description.getBytes )
        thinCdr.extractedMetadata.language.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.originalLanguage.getBytes )
        thinCdr.extractedMetadata.classification.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.classification.getBytes )
        thinCdr.extractedMetadata.title.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.title.getBytes )
        thinCdr.extractedMetadata.pages.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.pages.get.toString.getBytes )
        thinCdr.extractedMetadata.subject.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.subject.getBytes )
        thinCdr.extractedMetadata.creator.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.creator.getBytes )
        thinCdr.extractedMetadata.producer.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.producer.getBytes )
        thinCdr.extractedMetadata.statedGenre.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.statedGenre.getBytes )
        thinCdr.extractedMetadata.predictedGenre.get shouldBe IdGenerator.getMd5Hash( cdr.extractedMetadata.predictedGenre.getBytes )

        thinCdr.annotations.head.label.get shouldBe cdr.annotations.head.label
        thinCdr.annotations.head.annotation shouldBe IdGenerator.getMd5Hash( cdr.annotations.head.toString.getBytes )
    }

    "Thin CdrDocument" should "generate a ThinCdrDocument object when CDR fields are minimally defined" in {
        val cdr = CdrDocument( extractedMetadata = CdrMetadata() )
        val thinCdr = cdr.makeThin

        thinCdr.annotations shouldBe List()
        thinCdr.extractedMetadata.publisher shouldBe None
        thinCdr.extractedMetadata.url shouldBe None
        thinCdr.extractedMetadata.creationDate shouldBe None
        thinCdr.extractedMetadata.modificationDate shouldBe None
        thinCdr.extractedMetadata.author shouldBe None
        thinCdr.extractedMetadata.docType shouldBe None
        thinCdr.extractedMetadata.description shouldBe None
        thinCdr.extractedMetadata.language shouldBe None
        thinCdr.extractedMetadata.classification shouldBe None
        thinCdr.extractedMetadata.title shouldBe None
        thinCdr.contentType shouldBe None
        thinCdr.extractedNumeric shouldBe None
        thinCdr.documentId shouldBe null
        thinCdr.extractedText shouldBe None
        thinCdr.uri shouldBe None
        thinCdr.sourceUri shouldBe None
        thinCdr.extractedNtriples shouldBe None
        thinCdr.timestamp shouldBe None
        thinCdr.captureSource shouldBe None
    }

    "CdrAnnotation.makeThin" should "generate Thin CdrAnnotations for all CdrAnnotation subclasses" in {
        val textAnnotation = TextAnnotation( "text label", "1.0", "Some content", CdrAnnotation.DERIVED )
        val dictionaryAnnotation = DictionaryAnnotation( "dict label", "1.0", Map( "something" -> "something else" ), CdrAnnotation.DERIVED )
        val offsetTagAnnotation = OffsetTagAnnotation( "offset tags label", "1.0", List( OffsetTag( 0, 10, Some( "Value " ), "Tag name", Some( BigDecimal( 0.789320001 ) ) ) ), CdrAnnotation.DERIVED )
        val facetAnnotation = FacetAnnotation( "facets label", "1.0", List( FacetScore( "facet value", Some( BigDecimal( 0.234232 ) ) ) ), CdrAnnotation.DERIVED )
        val translationAnnotation = TranslationAnnotation( "translation label", "1.0", TranslatedFields( "portuguese", Map( "extractedText" -> "obrigado saudade" ) ) )

        textAnnotation.makeThin.annotation should fullyMatch regex "[a-zA-Z0-9]{16,}"
        dictionaryAnnotation.makeThin.annotation should fullyMatch regex "[a-zA-Z0-9]{16,}"
        offsetTagAnnotation.makeThin.annotation should fullyMatch regex "[a-zA-Z0-9]{16,}"
        facetAnnotation.makeThin.annotation should fullyMatch regex "[a-zA-Z0-9]{16,}"
        translationAnnotation.makeThin.annotation should fullyMatch regex "[a-zA-Z0-9]{16,}"

    }

    "[DEFECT] - CdrAnnotation.makeThin" should "handle null labels in CdrDocument" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "Some Author",
                                                docType = "Article",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Some Title",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com",
                                                pages = Some( 0 ),
                                                subject = "A subject",
                                                creator = "The Creator",
                                                producer = "Jack Donaghy" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map(),
                               documentId = "123abc",
                               extractedText = "Some long text",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List(),
                               labels = null )


        val thinCdr : ThinCdrDocument = cdr.makeThin

        thinCdr.labels shouldBe None
    }

}
