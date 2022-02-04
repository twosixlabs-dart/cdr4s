package com.twosixlabs.cdr4s.core

import com.twosixlabs.cdr4s.annotations.{FacetScore, OffsetTag}
import com.twosixlabs.dart.utils.DatesAndTimes
import org.scalatest.{FlatSpec, Matchers}

import java.time.LocalDate

class ModelTestSuite extends FlatSpec with Matchers {

    "Annotation Extraction" should "successfully extract a text annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( TextAnnotation( "test", "1.0", "1" ) ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ] )

        val textAnnotation : Option[ TextAnnotation ] = cdr.extractAnnotation[ TextAnnotation ]( "test" )

        val expected = TextAnnotation( "test", "1.0", "1" )
        textAnnotation.isDefined shouldBe true
        expected shouldBe textAnnotation.get
    }

    "Annotation Extraction" should "successfully extract a dictionary annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( DictionaryAnnotation( "test", "1.0", Map( "a" -> "b" ) ) ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ] )

        val dictAnnotation : Option[ DictionaryAnnotation ] = cdr.extractAnnotation[ DictionaryAnnotation ]( "test" )

        val expected = DictionaryAnnotation( "test", "1.0", Map( "a" -> "b" ) )
        dictAnnotation.isDefined shouldBe true
        expected shouldBe dictAnnotation.get
    }

    "Annotation Extraction" should "successfully extract a tags annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( 0.789320001 ) ) ) ) ).asInstanceOf[ List[ CdrAnnotation[
                                 Any ] ] ] )

        val tagsAnnotation : Option[ OffsetTagAnnotation ] = cdr.extractAnnotation[ OffsetTagAnnotation ]( "tags" )

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( 0.789320001 ) ) ) )
        tagsAnnotation.isDefined shouldBe true
        expected shouldBe tagsAnnotation.get
    }

    "Annotation Extraction" should "successfully extract a facets annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( 0.789320001 ) ) ) ) ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ] )

        val tagsAnnotation : Option[ OffsetTagAnnotation ] = cdr.extractAnnotation[ OffsetTagAnnotation ]( "tags" )

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( 0.789320001 ) ) ) )
        tagsAnnotation.isDefined shouldBe true
        expected shouldBe tagsAnnotation.get
    }

    //@formatter:off
    "Annotation Extraction" should "successfully extract a facet annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = LocalDate.now(),
                                                modificationDate = LocalDate.now(),
                                                author = "",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = DatesAndTimes.timeStamp,
                               annotations = List( FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( BigDecimal(0.5) ) ) ) ) ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ] )

        //@formatter:on

        val classifierAnnotation : Option[ FacetAnnotation ] = cdr.extractAnnotation[ FacetAnnotation ]( "categories" )

        val expected = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( BigDecimal( 0.5 ) ) ) ) )
        classifierAnnotation.isDefined shouldBe true
        expected shouldBe classifierAnnotation.get
    }

    "Annotation Extraction" should "successfully extract annotations from a polymorphic list" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata =
                                 CdrMetadata( creationDate = LocalDate.now(),
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
                                                   DictionaryAnnotation( "dict", "1.0", Map( "a" -> "b" ) ),
                                                   OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( BigDecimal( 0.789320001 ) ) ) ) ),
                                                   FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( BigDecimal( 0.5 ) ) ) ) ) ) )

        val textAnnotation : Option[ TextAnnotation ] = cdr.extractAnnotation[ TextAnnotation ]( "text" )
        val dictAnnotation : Option[ DictionaryAnnotation ] = cdr.extractAnnotation[ DictionaryAnnotation ]( "dict" )
        val tagsAnnotation : Option[ OffsetTagAnnotation ] = cdr.extractAnnotation[ OffsetTagAnnotation ]( "tags" )
        val classifierAnnotation : Option[ OffsetTagAnnotation ] = cdr.extractAnnotation[ OffsetTagAnnotation ]( "categories" )


        val expectedText = TextAnnotation( "text", "1.0", "test" )
        val expectedDict = DictionaryAnnotation( "dict", "1.0", Map( "a" -> "b" ) )
        val expectedTags = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( BigDecimal( 0.789320001 ) ) ) ) )
        val expectedClassifier = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( BigDecimal( 0.5 ) ) ) ) )

        textAnnotation.isDefined shouldBe true
        dictAnnotation.isDefined shouldBe true
        tagsAnnotation.isDefined shouldBe true
        classifierAnnotation.isDefined shouldBe true


        expectedText shouldBe textAnnotation.get
        expectedDict shouldBe dictAnnotation.get
        expectedTags shouldBe tagsAnnotation.get
        expectedClassifier shouldBe classifierAnnotation.get
    }

}
