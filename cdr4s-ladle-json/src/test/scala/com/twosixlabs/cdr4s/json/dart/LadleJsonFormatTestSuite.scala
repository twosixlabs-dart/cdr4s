package com.twosixlabs.cdr4s.json.dart

import com.twosixlabs.cdr4s.core.{CdrDocument, CdrFormat, CdrMetadata, TextAnnotation}
import com.twosixlabs.dart.test.base.StandardTestBase3x

import java.time.{LocalDate, OffsetDateTime, ZoneOffset}

class LadleJsonFormatTestSuite extends StandardTestBase3x {

    val format : CdrFormat = new LadleJsonFormat()

    val testDate : LocalDate = LocalDate.of( 2019, 6, 30 )
    val testTimeStamp : OffsetDateTime = OffsetDateTime.of( 2019, 9, 18, 9, 25, 59, 0, ZoneOffset.UTC )

    "LADLE Json CDR Format" should "marshal a document with all possible fields and unmarshal to an identical cdr" in {
        val originalCdr = CdrDocument( "ManualCuration",
                                       CdrMetadata( LocalDate.of( 1992, 6, 21 ),
                                                    LocalDate.of( 2003, 5, 1 ),
                                                    "John Doe",
                                                    "News Article",
                                                    "Lorum Ipsum",
                                                    "en",
                                                    "UNCLASSIFIED",
                                                    "Lorum Ipsum",
                                                    "Lorum Ipsum",
                                                    "https://www.lorumipsum.com",
                                                    Some( 5 ),
                                                    "Lorum Ipsum",
                                                    "creator",
                                                    "producer" ),
                                       "text/html",
                                       Map( "test" -> "test", "test2" -> "test2" ),
                                       "34jk5h34723hk",
                                       "Lorum Ipsum",
                                       "https://www.lorumipsum.com",
                                       "Lorum Ipsum",
                                       "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
                                       testTimeStamp,
                                       labels = Set( "One", "Two", "Three" ) )

        val processedCdr = format.unmarshalCdr( format.marshalCdr( originalCdr ).get ).get
        processedCdr shouldBe originalCdr
    }

    "LADLE Json CDR Format" should "marshal a document with as few fields as possible and unmarshal to an identical cdr" in {
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

    "Ladle Json CDR Format" should "return a CdrDocument when unmarshalling valid JSON" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Author": "Jane Doe"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759
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
        cdrDoc.extractedMetadata.modificationDate shouldBe null
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.annotations.isEmpty shouldBe true // Ladle has no idea what annotations are
    }

    "Ladle Json CDR Format" should "return a CdrDocument when unmarshalling valid JSON with all the metadata fields" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Author": "Jane Doe",
              |    "Pages": 2,
              |    "Subject": "Lorem Ipsum",
              |    "Creator": "Lorem Ipsum",
              |    "Producer": "Lorem Ipsum"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759
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
        cdrDoc.extractedMetadata.modificationDate shouldBe null
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.extractedMetadata.pages.get shouldBe 2
        cdrDoc.extractedMetadata.subject shouldBe "Lorem Ipsum"
        cdrDoc.extractedMetadata.creator shouldBe "Lorem Ipsum"
        cdrDoc.extractedMetadata.producer shouldBe "Lorem Ipsum"

        cdrDoc.annotations.isEmpty shouldBe true // Ladle has no idea what annotations are
    }

    "Ladle Json CDR Format" should "return a CdrDocument when unmarshalling valid JSON with labels" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Author": "Jane Doe",
              |    "ModDate": 100,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 2,
              |    "Subject": "Lorem Ipsum",
              |    "Creator": "Lorem Ipsum",
              |    "Producer": "Lorem Ipsum"
              |  },
              |  "content_type": "text/html",
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759,
              |  "labels": [
              |    "Lorum",
              |    "Ipsum"
              |  ]
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
        cdrDoc.labels shouldBe Set( "Lorum", "Ipsum" )

        cdrDoc.extractedMetadata.creationDate shouldBe testDate
        cdrDoc.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrDoc.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.originalLanguage shouldBe "en"
        cdrDoc.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrDoc.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrDoc.extractedMetadata.author shouldBe "Jane Doe"
        cdrDoc.annotations.isEmpty shouldBe true // Ladle has no idea what annotations are
    }

    "Ladle Json CDR Format" should "return None when unmarshalling invalid JSON" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
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
              |  "timestamp": 1568798759
              |""".stripMargin

        val cdrDoc : Option[ CdrDocument ] = format.unmarshalCdr( originalJson )

        cdrDoc should be( None )
    }

    "Ladle Json CDR Format" should "marshal a CdrDocument" in {
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
                               timestamp = testTimeStamp )
        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":1561852800,"ModDate":1561852800,"Description":"Lorum Ipsum","Language":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":1568798759}"""
        //@formatter:on

        // marshal the doc to JSON, then read it back in
        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        cdrJson.get shouldBe expected
    }

    "Ladle Json CDR Format" should "marshal a CdrDocument with all the metadata fields" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = testDate,
                                                modificationDate = testDate,
                                                author = "",
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
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":1561852800,"ModDate":1561852800,"Description":"Lorum Ipsum","Language":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com","Pages":2,"Subject":"Lorem Ipsum","Creator":"Lorem Ipsum","Producer":"Lorem Ipsum"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":1568798759}"""
        //@formatter:on

        // marshal the doc to JSON, then read it back in
        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        cdrJson.get shouldBe expected
    }

    "Ladle Json CDR Format" should "marshal a CdrDocument with labels" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = testDate,
                                                modificationDate = testDate,
                                                author = "",
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
                               timestamp = testTimeStamp,
                               labels = Set( "Lorum Ipsum" ) )
        //@formatter:off
        val expected = """{"capture_source":"ManualCuration","extracted_metadata":{"CreationDate":1561852800,"ModDate":1561852800,"Description":"Lorum Ipsum","Language":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com","Pages":2,"Subject":"Lorem Ipsum","Creator":"Lorem Ipsum","Producer":"Lorem Ipsum"},"content_type":"text/html","document_id":"123abc","extracted_text":"Lorum Ipsum","uri":"https://lorumipsum.com","source_uri":"Lorum Ipsum","extracted_ntriples":"<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .","timestamp":1568798759,"labels":["Lorum Ipsum"]}"""
        //@formatter:on

        // marshal the doc to JSON, then read it back in
        val cdrJson : Option[ String ] = format.marshalCdr( cdr )

        cdrJson.isDefined shouldBe true
        cdrJson.get shouldBe expected
    }

    //@formatter:off
    "Ladle Json CDR Format" should "be able to do two way marshalling and unmarshalling" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
            extractedMetadata = {
                CdrMetadata( creationDate = testDate,
                             modificationDate = testDate,
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
            timestamp = testTimeStamp)

        // marshal the doc to JSON, then read it back in
        val jsonSerializedCdr : String = format.marshalCdr( cdr ).get
        val cdrFromJson : CdrDocument = format.unmarshalCdr( jsonSerializedCdr ).get

        // make sure it's consistent
        cdrFromJson.captureSource shouldBe "ManualCuration"
        cdrFromJson.contentType shouldBe "text/html"
        cdrFromJson.documentId shouldBe "123abc"
        cdrFromJson.extractedText shouldBe "Lorum Ipsum"
        cdrFromJson.uri shouldBe "https://lorumipsum.com"
        cdrFromJson.sourceUri shouldBe "Lorum Ipsum"
        cdrFromJson.extractedNtriples shouldBe "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> ."
        cdrFromJson.timestamp shouldBe testTimeStamp

        cdrFromJson.extractedMetadata.creationDate shouldBe testDate
        cdrFromJson.extractedMetadata.classification shouldBe "UNCLASSIFIED"
        cdrFromJson.extractedMetadata.description shouldBe "Lorum Ipsum"
        cdrFromJson.extractedMetadata.originalLanguage shouldBe "en"
        cdrFromJson.extractedMetadata.title shouldBe "Lorum Ipsum"
        cdrFromJson.extractedMetadata.publisher shouldBe "Lorum Ipsum"
        cdrFromJson.extractedMetadata.url shouldBe "https://www.lorumipsum.com"
        cdrFromJson.annotations.isEmpty shouldBe true // ladle doesn't know about annotations
    }
    //@formatter:on

    "Ladle Json CDR Format" should "return true when validating a well-formed CdrDocument" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "ModDate": 100,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 2,
              |    "Subject": "Lorem Ipsum",
              |    "Creator": "Lorem Ipsum",
              |    "Producer": "Lorem Ipsum"
              |  },
              |  "content_type": "text/html",
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759,
              |  "labels": [
              |    "Lorum",
              |    "Ipsum"
              |  ]
              |}""".stripMargin

        format.validate( originalJson.getBytes ) shouldBe true
    }

    "Ladle Json CDR Format" should "return false when validating a CdrDocument that's missing required elements" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Author": "Jane Doe"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759
              |}""".stripMargin

        format.validate( originalJson.getBytes ) shouldBe false
    }

    "Ladle Json CDR Format" should "return true when validating a well-formed CdrDocument with unknown fields" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Author": "Jane Doe",
              |    "unknown": 1
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759
              |}""".stripMargin

        format.validate( originalJson.getBytes ) shouldBe true
    }


    "Ladle Json CDR Format" should "produce JSON that passes validation" in {
        val cdr = CdrDocument( captureSource = "ManualCuration",
                               extractedMetadata = {
                                   CdrMetadata( creationDate = testDate,
                                                modificationDate = testDate,
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
                               timestamp = testTimeStamp )

        // marshal the doc to JSON, then read it back in
        val jsonSerializedCdr : String = format.marshalCdr( cdr ).get

        format.validate( jsonSerializedCdr.getBytes ) shouldBe true
    }


    /// HOTFIX - disable unkown fields so that PDF processing occurs... will be resolved with DART-80 - Update CDR Spec
    "Ladle Json CDR Format" should "return a CdrDocument when unmarshalling valid JSON with unknown fields" in {
        val originalJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": 1561852800,
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Author": "Jane Doe",
              |    "Unknown": "Lorem Ipsum"
              |  },
              |  "content_type": "text/html",
              |  "extracted_numeric": {},
              |  "document_id": "123abc",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": 1568798759
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
        cdrDoc.annotations.isEmpty shouldBe true // Ladle has no idea what annotations are
    }

    "Ladle Json CDR Format" should "unmarshal a metadata object" in {
        val metadataJson =
            """{
              |     "CreationDate": 1561852800,
              |     "ModDate": 1561852800,
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "Language": "en",
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

    "Ladle Json CDR Format" should "marshal a metadata object" in {
        val metadata = CdrMetadata( creationDate = testDate,
                                    modificationDate = testDate,
                                    author = "Jane Doe",
                                    docType = "",
                                    description = "Lorum Ipsum",
                                    originalLanguage = "en",
                                    classification = "UNCLASSIFIED",
                                    title = "Lorum Ipsum",
                                    publisher = "Lorum Ipsum",
                                    url = "https://www.lorumipsum.com" )

        //@formatter:off
        val expected = """{"CreationDate":1561852800,"ModDate":1561852800,"Author":"Jane Doe","Description":"Lorum Ipsum","Language":"en","Classification":"UNCLASSIFIED","Title":"Lorum Ipsum","Publisher":"Lorum Ipsum","URL":"https://www.lorumipsum.com"}"""
        //@formatter:on

        val metadataJson = format.marshalMetadata( metadata ).get

        expected shouldBe metadataJson
    }

    //@formatter:off
    "Ladle Json CDR Format" should "not be able to unmarshal Annotations" in {
        val annotationJson =
            """{
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "content": [
              |        {
              |          "offset-start": 0,
              |          "offset-end": 1,
              |          "text": "michael",
              |          "tag": "noun"
              |        }
              |      ]
              |    }""".stripMargin

        assertThrows[ UnsupportedOperationException ] {
            format.unmarshalAnnotation( annotationJson )
        }
    }
    //@formatter:off

    //@formatter:off
    "Ladle Json CDR Format" should "not be able to marshal Annotations" in {
        val annotation = TextAnnotation( "text", "1.0", "text" )

        assertThrows[ UnsupportedOperationException ]{
            format.marshalAnnotation( annotation )
        }
    }
    //@formatter:on
}
