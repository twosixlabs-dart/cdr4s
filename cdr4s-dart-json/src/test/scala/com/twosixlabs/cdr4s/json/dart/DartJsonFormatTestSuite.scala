package com.twosixlabs.cdr4s.json.dart

import com.twosixlabs.cdr4s.annotations.{DocumentGenealogy, FacetScore, OffsetTag, TranslatedFields}
import com.twosixlabs.cdr4s.core.{CdrAnnotation, CdrDocument, CdrFormat, CdrMetadata, DictionaryAnnotation, DocumentGenealogyAnnotation, FacetAnnotation, OffsetTagAnnotation, TextAnnotation, TranslationAnnotation}
import com.twosixlabs.dart.test.base.StandardTestBase3x

import java.time.{LocalDate, OffsetDateTime, ZoneOffset}

class DartJsonFormatTestSuite extends StandardTestBase3x {

    val format : CdrFormat = new DartJsonFormat()

    val testDate : LocalDate = LocalDate.of( 2019, 6, 30 )
    val testTimeStamp : OffsetDateTime = OffsetDateTime.of( 2019, 9, 18, 9, 25, 59, 672000000, ZoneOffset.UTC )

    //@formatter:off
    "DART Json CDR Format" should "marshal a document with all possible fields and unmarshal to an identical cdr" in {
        val originalCdr = CdrDocument( captureSource = "ManualCuration",
                                       CdrMetadata( creationDate = LocalDate.of( 1992, 6, 21 ),
                                                    modificationDate = LocalDate.of( 2003, 5, 1 ),
                                                    author = "John Doe",
                                                    docType = "News Article",
                                                    description = "Lorum Ipsum",
                                                    originalLanguage = "en",
                                                    classification = "UNCLASSIFIED",
                                                    title = "Lorum Ipsum",
                                                    publisher = "Lorum Ipsum",
                                                    url = "https://www.lorumipsum.com",
                                                    pages = Some( 5 ),
                                                    subject = "Lorum Ipsum",
                                                    creator = "creator",
                                                    producer = "producer",
                                                    statedGenre = "news_article",
                                                    predictedGenre = "newswire" ),
                                       contentType = "text/html",
                                       extractedNumeric = Map( "test" -> "test", "test2" -> "test2" ),
                                       documentId = "docid1",
                                       extractedText = "Lorum Ipsum",
                                       uri = "https://www.lorumipsum.com",
                                       sourceUri = "Lorum Ipsum",
                                       extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                                       timestamp = testTimeStamp,
                                       annotations = List[ CdrAnnotation[ Any ] ]( TextAnnotation( "test", "1.0", "Lorum Ipsum" ),
                                                                     DictionaryAnnotation( "test dict", "test version", Map[ String, String ]( "test key" -> "test value" ) ),
                                                                     OffsetTagAnnotation( "test tag", "test version", List( OffsetTag( 1, 10, Some( "test value" ), "tag", Some(1)  ) ) ),
                                                                     FacetAnnotation( "test simple keyword", "test version", List( FacetScore( "test keyword", None ) ), CdrAnnotation.STATIC ),
                                                                     FacetAnnotation( "test score keyword", "test version", List( FacetScore( "test keyword 2", Some( 0.789320001 ) ) ) ),
                                                                     DocumentGenealogyAnnotation( "genealogy", "1.0", DocumentGenealogy( Map( "a" -> BigDecimal( 1.0 ) ), Array( Array( BigDecimal( 1.0 ) ) ) ) ),
                                                                     TranslationAnnotation( "translation", "1.0", TranslatedFields( "portuguese", Map( "extractedText" -> "obrigado saudade" ) ) ) ),
                                       labels = Set( "one", "two", "three" )
        )
        //@formatter:on

        val processedCdr = format.unmarshalCdr( format.marshalCdr( originalCdr ).get ).get
        processedCdr shouldBe originalCdr
    }

    "DART Json CDR Format" should "marshal a document with as few fields as possible and unmarshal to an identical cdr" in {
        val originalCdr = CdrDocument( extractedMetadata = CdrMetadata(),
                                       contentType = "text/html",
                                       documentId = "34jk5h34723hk",
                                       uri = "https://www.lorumipsum.com",
                                       sourceUri = "Lorum Ipsum",
                                       extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                                       timestamp = testTimeStamp )

        val processedCdr = format.unmarshalCdr( format.marshalCdr( originalCdr ).get ).get
        processedCdr shouldBe originalCdr
    }

    "DART Json CDR Format" should "unmarshal a document with a Text Annotation" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com",
              |     "PredictedGenre" : "academic_paper"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "annotations": [{
              |     "type": "text",
              |     "label": "test",
              |     "version": "1.0",
              |     "class": "derived",
              |     "content": "1"
              | }]
              |}""".stripMargin


        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.extractedMetadata.predictedGenre shouldBe "academic_paper"
        cdrDoc.extractAnnotation[ TextAnnotation ]( "test" ).isDefined shouldBe true
        cdrDoc.extractAnnotation[ TextAnnotation ]( "test" ).get.classification shouldBe CdrAnnotation.DERIVED
    }

    "DART Json CDR Format" should "unmarshal a document with a Dictionary Annotation" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "annotations": [{
              |     "type": "dictionary",
              |     "label": "test",
              |     "version": "1.0",
              |     "class": "static",
              |     "content": {
              |         "test": "1"
              |     }
              |   }]
              |}""".stripMargin


        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.extractAnnotation[ DictionaryAnnotation ]( "test" ).isDefined shouldBe true
        cdrDoc.extractAnnotation[ DictionaryAnnotation ]( "test" ).get.classification shouldBe CdrAnnotation.STATIC
    }

    "DART Json CDR Format" should "unmarshal a document with a Tag Annotation that does not have the value populated" in {
        val source =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "tags",
              |      "label": "test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "test"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        extractedCdr.get.extractAnnotation[ OffsetTagAnnotation ]( "test" ).isDefined shouldBe true
    }


    "DART Json CDR Format" should "unmarshal a document with a Facet Annotation that has a score value" in {
        val source =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "facets",
              |      "label": "test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "value": "test keyword",
              |          "score": 0.677902
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        extractedCdr.get.extractAnnotation[ FacetAnnotation ]( "test" ).isDefined shouldBe true
    }

    "DART Json CDR Format" should "unmarshal a document with a Facet Annotation that does not have the score populated" in {
        val source =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "facets",
              |      "label": "test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "value": "test keyword"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        extractedCdr.get.extractAnnotation[ FacetAnnotation ]( "test" ).isDefined shouldBe true
    }

    "DART Json CDR Format" should "unmarshal a document with a Tag Annotation that has the value populated" in {
        val source =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "tags",
              |      "label": "test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "test",
              |          "value": "test"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        extractedCdr.get.extractAnnotation[ OffsetTagAnnotation ]( "test" ).isDefined shouldBe true
    }

    "DART Json CDR Format" should "unmarshal a document with a Genealogy Annotation" in {
        val source : String =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "News Article",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 5,
              |    "Subject": "Lorum Ipsum",
              |    "Creator": "creator",
              |    "Producer": "producer"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {
              |    "test": "test",
              |    "test2": "test2"
              |  },
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://www.lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "genealogy",
              |      "label": "qntfy-genealogy",
              |      "version": "1.0",
              |      "content": {
              |        "similar_documents": {
              |          "abc": 1.0,
              |          "def": 2.0
              |        },
              |        "similarity_matrix": [
              |          [ 1.0, 2.0, 3.0 ],
              |          [ 4.0, 5.0, 6.0 ],
              |          [ 7.0, 8.0, 9.0 ]
              |        ]
              |      },
              |      "class": "derived"
              |    }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        val annotation : Option[ DocumentGenealogyAnnotation ] = extractedCdr.get.extractAnnotation[ DocumentGenealogyAnnotation ]( "qntfy-genealogy" )
        annotation.isDefined shouldBe true
        annotation.get.content.similarDocuments.isEmpty shouldBe false
        annotation.get.content.similarityMatrix.isEmpty shouldBe false

    }

    "DART Json CDR Format" should "unmarshal a document with a Translation Annotation" in {
        val source : String =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "News Article",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 5,
              |    "Subject": "Lorum Ipsum",
              |    "Creator": "creator",
              |    "Producer": "producer"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {
              |    "test": "test",
              |    "test2": "test2"
              |  },
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://www.lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |     {
              |         "type": "translation",
              |         "label": "qntfy-translation",
              |         "version": "1.0",
              |         "content": {
              |             "language": "russian",
              |             "fields": {
              |                 "extractedText": "4 октября 2018 года на пресс-конференции"
              |              }
              |         },
              |         "class": "derived"
              |     }
              |  ]
              |}""".stripMargin

        val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( source )

        extractedCdr.isDefined shouldBe true
        extractedCdr.get.captureSource shouldBe "ManualCuration"
        extractedCdr.get.contentType shouldBe "text/html"
        extractedCdr.get.documentId shouldBe "123abc"
        extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
        extractedCdr.get.uri shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        extractedCdr.get.timestamp shouldBe testTimeStamp

        extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
        extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
        extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
        val annotation : Option[ TranslationAnnotation ] = extractedCdr.get.extractAnnotation[ TranslationAnnotation ]( "qntfy-translation" )
        annotation.isDefined shouldBe true
        annotation.get.content.language shouldBe "russian"
        annotation.get.content.fields shouldBe Map( "extractedText" -> "4 октября 2018 года на пресс-конференции" )

    }

    "DART Json CDR Format" should "should unmarshal a document with no annotations" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "annotations": []
              |}""".stripMargin

        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.annotations.isEmpty shouldBe true
    }

    "DART Json CDR Format" should "should unmarshal a document with all metadata fields" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com",
              |     "Pages": 1,
              |     "Subject": "Lorem Ipsum",
              |     "Creator": "Lorem Ipsum",
              |     "Producer": "Lorem Ipsum",
              |     "StatedGenre": "newswire"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "annotations": []
              |}""".stripMargin

        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.annotations.isEmpty shouldBe true
        cdrDoc.extractedMetadata.pages.get shouldBe 1
        cdrDoc.extractedMetadata.subject shouldBe "Lorem Ipsum"
        cdrDoc.extractedMetadata.creator shouldBe "Lorem Ipsum"
        cdrDoc.extractedMetadata.producer shouldBe "Lorem Ipsum"
        cdrDoc.extractedMetadata.statedGenre shouldBe "newswire"
    }

    "DART Json CDR Format" should "should unmarshal a document with labels" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "labels":[
              |     "Lorem",
              |     "Ipsum"
              | ],
              | "annotations": []
              |}""".stripMargin

        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp
        cdrDoc.annotations.isEmpty shouldBe true
        cdrDoc.labels shouldBe Set( "Lorem", "Ipsum" )

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
    }

    "DART Json CDR Format" should "unmarshal a document with polymorphic annotations" in {
        val originalJson =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com"
              | },
              | "content_type": "text/html",
              | "extracted_numeric": {},
              | "document_id": "123abc",
              | "extracted_text": "Lorum Ipsum",
              | "uri": "https://lorumipsum.com",
              | "source_uri": "Lorum Ipsum",
              | "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              | "timestamp": "2019-09-18T09:25:59.672Z",
              | "annotations": [
              |   {
              |     "type": "dictionary",
              |     "label": "dict",
              |     "version": "1.0",
              |     "class": "derived",
              |     "content": {
              |         "test": "1"
              |     }
              |   },
              |   {
              |     "type": "text",
              |     "label": "text",
              |     "version": "1.0",
              |     "class": "derived",
              |     "content": "1"
              |   },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "test",
              |          "score": 0.789320001
              |        }
              |      ]
              |    },
              |    {
              |      "type": "facets",
              |      "label": "simple keyword test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "value": "test keyword"
              |        }
              |      ]
              |    },
              |    {
              |      "type": "facets",
              |      "label": "score keyword test",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "value": "test keyword 2",
              |          "score": 0.00001
              |        }
              |      ]
              |    }
              | ]
              |}""".stripMargin

        val cdrDoc : CdrDocument = format.unmarshalCdr( originalJson ).get

        cdrDoc.captureSource shouldBe "ManualCuration"
        cdrDoc.contentType shouldBe "text/html"
        cdrDoc.documentId shouldBe "123abc"
        cdrDoc.extractedText shouldBe "Lorum Ipsum"
        cdrDoc.uri shouldBe "https://lorumipsum.com"
        cdrDoc.sourceUri shouldBe "Lorum Ipsum"
        cdrDoc.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrDoc.timestamp shouldBe testTimeStamp

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.extractAnnotation[ TextAnnotation ]( "text" ).isDefined shouldBe true
        cdrDoc.extractAnnotation[ TextAnnotation ]( "text" ).get.content shouldBe "1"
        cdrDoc.extractAnnotation[ DictionaryAnnotation ]( "dict" ).isDefined shouldBe true
        cdrDoc.extractAnnotation[ DictionaryAnnotation ]( "dict" ).get.content shouldBe Map( "test" -> "1" )
        cdrDoc.extractAnnotation[ OffsetTagAnnotation ]( "tags" ).isDefined shouldBe true
        cdrDoc.extractAnnotation[ OffsetTagAnnotation ]( "tags" ).get.content shouldBe List( OffsetTag( 0, 1, None, "test", Some( 0.789320001 ) ) )
        cdrDoc.extractAnnotation[ FacetAnnotation ]( "simple keyword test" ).get.content shouldBe List( FacetScore( "test keyword", None ) )
        cdrDoc.extractAnnotation[ FacetAnnotation ]( "score keyword test" ).get.content shouldBe List( FacetScore( "test keyword 2", Some( 0.00001 ) ) )
    }

    "DART Json CDR Format" should "return None when unmarshalling invalid JSON" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "Description": "Lorum Ipsum",
              |    "OriginalLanguage": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations":[
              |""".stripMargin

        val cdrDoc : Option[ CdrDocument ] = format.unmarshalCdr( originalJson )

        cdrDoc should be( None )
    }

    //@formatter:off
    "DART Json CDR Format" should "marshal a document with Text Annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
            extractedMetadata = {
                CdrMetadata( creationDate = testDate,
                             modificationDate =  testDate,
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
            testTimeStamp,
            annotations = List( TextAnnotation( "test", "1.0", "1", CdrAnnotation.STATIC ) ).asInstanceOf[ List[ CdrAnnotation[ Any ] ] ] )

        val cdrJson : String = format.marshalCdr( cdr ).get

        val expectedJson : String = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"text","label":"test","version":"1.0","content":"1","class":"static"}]}"""

        expectedJson shouldBe cdrJson

    }
    //@formatter:on

    //@formatter : off
    "DART Json CDR Format" should "marshal a document with a Dictionary Annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
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
                               timestamp = testTimeStamp,
                               annotations = List( DictionaryAnnotation( "dict", "1.0", Map( "test" -> "1" ), CdrAnnotation.STATIC ) ) )

        val cdrJson : String = format.marshalCdr( cdr ).get

        //@formatter:off
        val expectedJson : String = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"dictionary","label":"dict","version":"1.0","content":{"test":"1"},"class":"static"}]}"""
        //@formatter:on

        expectedJson shouldBe cdrJson
    }
    //@formatter:on

    "DART Json CDR Format" should "marshal a document with a Tag Annotation that does not have the value populated" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( OffsetTagAnnotation( "test", "1.0", List( OffsetTag( 0, 1, None, "test", Some( 0.789320001 ) ) ) ) ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"tags","label":"test","version":"1.0","content":[{"offset_start":0,"offset_end":1,"tag":"test","score":0.789320001}],"class":"derived"}]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with a Tag Annotation that has the value populated" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( OffsetTagAnnotation( "test", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "test", Some( 0.789320001 ) ) ) ) ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"tags","label":"test","version":"1.0","content":[{"offset_start":0,"offset_end":1,"value":"test","tag":"test","score":0.789320001}],"class":"derived"}]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with a Facet Annotation that does not have the score populated" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( FacetAnnotation( "test", "1.0", List( FacetScore( "test", None ) ) ) ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"facets","label":"test","version":"1.0","content":[{"value":"test"}],"class":"derived"}]}"""

        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with a Facet Annotation that does have the score populated" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( FacetAnnotation( "test", "1.0", List( FacetScore( "test", Some( 0.456789 ) ) ) ) ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"facets","label":"test","version":"1.0","content":[{"value":"test","score":0.456789}],"class":"derived"}]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with a Genealogy Annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( DocumentGenealogyAnnotation( "genealogy", "1.0", DocumentGenealogy( Map( "a" -> BigDecimal( 1.0 ) ), Array( Array( BigDecimal( 1.0 ) ) ) ) ) ) )


        val expected : String = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"genealogy","label":"genealogy","version":"1.0","content":{"similar_documents":{"a":1.0},"similarity_matrix":[[1.0]]},"class":"derived"}]}"""

        val actual = format.marshalCdr( cdr )
        actual.isDefined shouldBe true
        actual.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a document with a Translation Annotation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( TranslationAnnotation( "translation", "1.0", TranslatedFields( "russian", Map( "extractedText" -> "4 октября 2018 года на пресс-конференции" ) ) ) ) )

        val expected : String = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"translation","label":"translation","version":"1.0","content":{"language":"russian","fields":{"extractedText":"4 октября 2018 года на пресс-конференции"}},"class":"derived"}]}"""

        val actual = format.marshalCdr( cdr )

        actual.isDefined shouldBe true
        actual.get shouldBe expected

    }

    "DART Json CDR Format" should "marshal a document with polymorphic annotations" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               annotations = List( TextAnnotation( "text", "1.0", "text" ),
                                                   OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "test", Some( BigDecimal( 0.789320001 ) ) ) ) ),
                                                   DictionaryAnnotation( "dict", "1.0", Map( "dict" -> "test" ) ),
                                                   FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( BigDecimal( 0.789320001 ) ) ) ) ) ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[{"type":"text","label":"text","version":"1.0","content":"text","class":"derived"},{"type":"tags","label":"tags","version":"1.0","content":[{"offset_start":0,"offset_end":1,"tag":"test","score":0.789320001}],"class":"derived"},{"type":"dictionary","label":"dict","version":"1.0","content":{"dict":"test"},"class":"derived"},{"type":"facets","label":"categories","version":"1.0","content":[{"value":"test","score":0.789320001}],"class":"derived"}]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with all metadata fields" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = testDate,
                                                modificationDate = testDate,
                                                author = "Jane Doe",
                                                docType = "",
                                                description = "Lorum Ipsum",
                                                originalLanguage = "en",
                                                classification = "UNCLASSIFIED",
                                                title = "Lorum Ipsum",
                                                publisher = "Lorum Ipsum",
                                                url = "https://www.lorumipsum.com",
                                                pages = Some( 2 ),
                                                subject = "Lorem Ipsum",
                                                creator = "Lorem Ipsum",
                                                producer = "Lorem Ipsum" )
                               },
                               contentType = "text/html",
                               extractedNumeric = Map.empty,
                               documentId = "123abc",
                               extractedText = "Lorum Ipsum",
                               uri = "https://lorumipsum.com",
                               sourceUri = "Lorum Ipsum",
                               extractedNtriples = "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                               timestamp = testTimeStamp )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com","Pages":2,"Subject":"Lorem Ipsum","Creator":"Lorem Ipsum","Producer":"Lorem Ipsum"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    "DART Json CDR Format" should "marshal a document with labels" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( testDate,
                                                testDate,
                                                author = "Jane Doe",
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
                               timestamp = testTimeStamp,
                               labels = Set( "Lorem" ) )

        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":"2019-09-18T09:25:59.672Z","annotations":[],"labels":["Lorem"]}"""
        //@formatter:on

        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        expected shouldBe cdrJson.get
    }

    //@formatter:off
    "DART Json CDR Format" should "do bi-directional marshalling" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
            extractedMetadata = {
                CdrMetadata( creationDate= testDate,
                             modificationDate= testDate,
                             author = "Jane Doe",
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
            timestamp = testTimeStamp,
            annotations = List( TextAnnotation( "text", "1.0", "text" ),
                                DictionaryAnnotation( "dict", "1.0", Map( "test" -> "1" ) ),
                                OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "N", Some( BigDecimal( 0.789320001 ) ) ), OffsetTag( 2, 3, None, "V", Some( BigDecimal( 0.789320001 ) )) ) ) ) )

            val extractedJson : Option[ String ] = format.marshalCdr( cdr )
            extractedJson.isDefined shouldBe true

            val extractedCdr : Option[ CdrDocument ] = format.unmarshalCdr( extractedJson.get )
            extractedCdr.isDefined shouldBe true

            extractedCdr.get.captureSource shouldBe "ManualCuration"
            extractedCdr.get.contentType shouldBe "text/html"
            extractedCdr.get.documentId shouldBe "123abc"
            extractedCdr.get.extractedText shouldBe "Lorum Ipsum"
            extractedCdr.get.uri shouldBe "https://lorumipsum.com"
            extractedCdr.get.sourceUri shouldBe "Lorum Ipsum"
            extractedCdr.get.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
            extractedCdr.get.timestamp shouldBe testTimeStamp

            extractedCdr.get.extractedMetadata.creationDate shouldBe testDate
            extractedCdr.get.extractedMetadata.classification shouldBe "UNCLASSIFIED"
            extractedCdr.get.extractedMetadata.description shouldBe "Lorum Ipsum"
            extractedCdr.get.extractedMetadata.originalLanguage shouldBe "en"
            extractedCdr.get.extractedMetadata.title shouldBe "Lorum Ipsum"
            extractedCdr.get.extractedMetadata.publisher shouldBe "Lorum Ipsum"
            extractedCdr.get.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
            extractedCdr.get.extractedMetadata.author shouldBe "Jane Doe"
            extractedCdr.get.extractAnnotation[ TextAnnotation ]( "text" ).isDefined shouldBe true
            extractedCdr.get.extractAnnotation[ TextAnnotation ]( "text" ).get.content shouldBe "text"
            extractedCdr.get.extractAnnotation[ DictionaryAnnotation ]( "dict" ).isDefined shouldBe true
            extractedCdr.get.extractAnnotation[ DictionaryAnnotation ]( "dict" ).get.content shouldBe Map( "test" -> "1" )
            extractedCdr.get.extractAnnotation[ OffsetTagAnnotation ]( "tags" ).isDefined shouldBe true
            extractedCdr.get.extractAnnotation[ OffsetTagAnnotation ]( "tags" ).get.content.isEmpty shouldBe false
    }
    //@formatter:on

    "DART Json CDR Format" should "unmarshal a metadata object" in {
        val metadataJson =
            """{
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com"
              | }""".stripMargin


        val metadata : Option[ CdrMetadata ] = format.unmarshalMetadata( metadataJson )

        metadata.isDefined shouldBe true
        metadata.get.creationDate shouldBe testDate
        metadata.get.classification shouldBe "UNCLASSIFIED"
        metadata.get.description shouldBe "Lorum Ipsum"
        metadata.get.originalLanguage shouldBe "en"
        metadata.get.title shouldBe "Lorum Ipsum"
        metadata.get.publisher shouldBe "Lorum Ipsum"
        metadata.get.url shouldBe "https://www.lorumipsum.com"
        metadata.get.author shouldBe "Jane Doe"
    }

    "DART Json CDR Format" should "unmarshal a metadata object with all fields" in {
        val metadataJson =
            """{
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Description": "Lorum Ipsum",
              |     "OriginalLanguage": "en",
              |     "Classification": "UNCLASSIFIED",
              |     "Title": "Lorum Ipsum",
              |     "Publisher": "Lorum Ipsum",
              |     "URL": "https://www.lorumipsum.com",
              |     "Pages": 2,
              |     "Subject": "Lorem Ipsum",
              |     "Creator": "Lorem Ipsum",
              |     "Producer": "Lorem Ipsum",
              |     "StatedGenre" : "policy_paper",
              |     "PredictedGenre" : "academic_paper"
              | }""".stripMargin


        val metadata : Option[ CdrMetadata ] = format.unmarshalMetadata( metadataJson )

        metadata.isDefined shouldBe true
        metadata.get.creationDate shouldBe testDate
        metadata.get.classification shouldBe "UNCLASSIFIED"
        metadata.get.description shouldBe "Lorum Ipsum"
        metadata.get.originalLanguage shouldBe "en"
        metadata.get.title shouldBe "Lorum Ipsum"
        metadata.get.publisher shouldBe "Lorum Ipsum"
        metadata.get.url shouldBe "https://www.lorumipsum.com"
        metadata.get.author shouldBe "Jane Doe"
        metadata.get.pages.get shouldBe 2
        metadata.get.subject shouldBe "Lorem Ipsum"
        metadata.get.statedGenre shouldBe "policy_paper"
        metadata.get.predictedGenre shouldBe "academic_paper"
    }

    "DART Json CDR Format" should "marshal a metadata object" in {
        val metadata = CdrMetadata( testDate,
                                    testDate,
                                    author = "Jane Doe",
                                    docType = null,
                                    description = "Lorum Ipsum",
                                    originalLanguage = "en",
                                    classification = "UNCLASSIFIED",
                                    title = "Lorum Ipsum",
                                    publisher = "Lorum Ipsum",
                                    url = "https://www.lorumipsum.com" )

        //@formatter:off
        val expected = """{"CreationDate":"2019-06-30","ModDate":"2019-06-30","Author":"Jane Doe","Description":"Lorum Ipsum","OriginalLanguage":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"}"""
        //@formatter:on

        val metadataJson = format.marshalMetadata( metadata )

        metadataJson.isDefined shouldBe true
        expected shouldBe metadataJson.get
    }
}
