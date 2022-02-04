package com.twosixlabs.cdr4s.json.dart

import com.twosixlabs.cdr4s.annotations.{DocumentGenealogy, FacetScore, OffsetTag, TranslatedFields}
import com.twosixlabs.cdr4s.core.{CdrDocument, CdrFormat, CdrMetadata, DictionaryAnnotation, DocumentGenealogyAnnotation, FacetAnnotation, OffsetTagAnnotation, TextAnnotation, ThinCdrAnnotation, TranslationAnnotation}
import com.twosixlabs.dart.test.base.StandardTestBase3x
import com.twosixlabs.dart.utils.DatesAndTimes

import java.time.{LocalDate, OffsetDateTime, ZoneOffset}

class DartJsonFormatThinCdrTestSuite extends StandardTestBase3x {

    val dartJsonFormat : CdrFormat = new DartJsonFormat

    "DART Json CDR Format" should "generate correct json of a Thin CDR Document object with no fields defined" in {
        val cdr = CdrDocument( documentId = "112233", extractedMetadata = CdrMetadata() )
        val thinCdrJson = dartJsonFormat.marshalThinCdr( cdr.makeThin ).get

        val expected = """{"extracted_metadata":{},"document_id":"112233"}"""

        thinCdrJson shouldBe expected
    }

    "DART Json CDR Format" should "be able to marshal and unmarshal a Thin CDR document" in {
        //@formatter:off
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = CdrMetadata( creationDate = LocalDate.now(),
                                                                modificationDate = LocalDate.now(),
                                                                author = "",
                                                                docType = "",
                                                                description = "Lorum Ipsum",
                                                                originalLanguage = "en",
                                                                classification = "UNCLASSIFIED",
                                                                title = "Lorum Ipsum",
                                                                publisher = "Lorum Ipsum",
                                                                url = "https://www.lorumipsum.com" ),
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( TextAnnotation( "text", "1.0", "test" ),
                                                   TranslationAnnotation("translation", "1.0", TranslatedFields( "russian", Map( "extractedText" -> "4 октября 2018 года на пресс-конференции" ) ) ),
                                                   DictionaryAnnotation( "dict", "1.0", Map( "a" -> "b" ) ),
                                                   OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( BigDecimal( 0.789320001 ) ) ) ) ),
                                                   FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( 0.5 ) ) ) ),
                                                   DocumentGenealogyAnnotation( "genealogy", "1.0", DocumentGenealogy( Map( "a" -> BigDecimal( 1.0 ), "b" -> BigDecimal( 2.0 ) ),
                                                                                                                       Array( Array( BigDecimal( 1.0 ), BigDecimal( 2.0 ) ),
                                                                                                                              Array( BigDecimal( 3.0 ), BigDecimal( 4.0 ) ) ) ) ) ),
                               labels = Set( "lorum", "ipsum" )
        )
        //@formatter:on

        val cdrThin = cdr.makeThin
        val actual = dartJsonFormat.unmarshalThinCdr( dartJsonFormat.marshalThinCdr( cdrThin ).get ).get


        actual.documentId shouldBe cdr.documentId
        actual.contentType shouldBe cdrThin.contentType
        actual.extractedNumeric shouldBe cdrThin.extractedNumeric
        actual.extractedText shouldBe cdrThin.extractedText
        actual.uri shouldBe cdrThin.uri
        actual.sourceUri shouldBe cdrThin.sourceUri
        actual.extractedNtriples shouldBe cdrThin.extractedNtriples
        actual.timestamp shouldBe cdrThin.timestamp
        actual.captureSource shouldBe cdrThin.captureSource
        actual.labels shouldBe cdrThin.labels

        actual.extractedMetadata.publisher shouldBe cdrThin.extractedMetadata.publisher
        actual.extractedMetadata.url shouldBe cdrThin.extractedMetadata.url
        actual.extractedMetadata.creationDate shouldBe cdrThin.extractedMetadata.creationDate
        actual.extractedMetadata.modificationDate shouldBe cdrThin.extractedMetadata.modificationDate
        actual.extractedMetadata.author shouldBe cdrThin.extractedMetadata.author
        actual.extractedMetadata.docType shouldBe cdrThin.extractedMetadata.docType
        actual.extractedMetadata.description shouldBe cdrThin.extractedMetadata.description
        actual.extractedMetadata.language shouldBe cdrThin.extractedMetadata.language
        actual.extractedMetadata.classification shouldBe cdrThin.extractedMetadata.classification
        actual.extractedMetadata.title shouldBe cdrThin.extractedMetadata.title
        actual.extractedMetadata.pages shouldBe cdrThin.extractedMetadata.pages
        actual.extractedMetadata.subject shouldBe cdrThin.extractedMetadata.subject
        actual.extractedMetadata.creator shouldBe cdrThin.extractedMetadata.creator
        actual.extractedMetadata.producer shouldBe cdrThin.extractedMetadata.producer

        actual.annotations should includeAll[ ThinCdrAnnotation ]( cdrThin.annotations )
    }

    "DEFECT: [DART-692] thin cdr json marshalling" should "handle genre fields correctly" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = CdrMetadata( creationDate = LocalDate.of( 1992, 6, 8 ),
                                                                modificationDate = LocalDate.of( 2010, 11, 1 ),
                                                                author = null,
                                                                docType = null,
                                                                description = "Lorum Ipsum",
                                                                originalLanguage = "en",
                                                                classification = "UNCLASSIFIED",
                                                                title = "Lorum Ipsum",
                                                                publisher = "Lorum Ipsum",
                                                                url = "https://www.lorumipsum.com",
                                                                pages = Some( 5 ),
                                                                subject = "subject",
                                                                creator = "some creator",
                                                                producer = "some producer",
                                                                statedGenre = "spec genre" ),
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "b73796720b6f469fe323bb49794a13b0",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "original source uri",
                               extractedNtriples = null,
                               timestamp = OffsetDateTime.of( 2019, 9, 17, 16, 50, 23, 0, ZoneOffset.UTC ),
                               labels = Set( "lorem ipsum", "spec label" ),
                               annotations = List() )

        val thin = cdr.makeThin
        val thinJson = dartJsonFormat.marshalThinCdr( thin ).get

        val roundTripThinCdr = dartJsonFormat.unmarshalThinCdr( thinJson ).get

        thin shouldBe roundTripThinCdr
    }

}
