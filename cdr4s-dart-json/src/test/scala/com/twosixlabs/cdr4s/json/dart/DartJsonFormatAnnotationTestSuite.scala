package com.twosixlabs.cdr4s.json.dart

import com.twosixlabs.cdr4s.annotations.{DocumentGenealogy, FacetScore, OffsetTag, TranslatedFields}
import com.twosixlabs.cdr4s.core.{CdrAnnotation, CdrFormat, DictionaryAnnotation, DocumentGenealogyAnnotation, FacetAnnotation, OffsetTagAnnotation, TextAnnotation, TranslationAnnotation}
import com.twosixlabs.dart.test.base.StandardTestBase3x

class DartJsonFormatAnnotationTestSuite extends StandardTestBase3x {

    val format : CdrFormat = new DartJsonFormat

    "DART Json CDR Format" should "unmarshal a Text Annotation" in {
        val annotationJson =
            """{
              |     "type": "text",
              |     "label": "text",
              |     "version": "1.0",
              |     "class" : "derived",
              |     "content": "my text"
              |}""".stripMargin

        val expected : CdrAnnotation[ _ ] = TextAnnotation( "text", "1.0", "my text" )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected
    }

    "DART Json CDR Format" should "unmarshal a Dictionary Annotation" in {
        val annotationJson =
            """{
              |     "type": "dictionary",
              |     "label": "dict",
              |     "version": "1.0",
              |     "class" : "static",
              |     "content": {
              |         "test": "my test"
              |     }
              |}""".stripMargin

        val expected = DictionaryAnnotation( "dict", "1.0", Map( "test" -> "my test" ), CdrAnnotation.STATIC )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected
    }

    "DART Json CDR Format" should "unmarshal a Offset Tag Annotation without the value populated" in {
        val annotationJson =
            """{
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class" : "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "noun",
              |          "score": 0.789320001
              |        }
              |      ]
              |    }""".stripMargin

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "noun", Some( 0.789320001 ) ) ) )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "unmarshal a Offset Tag Annotation with the value populated" in {
        val annotationJson =
            """{
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class" : "static",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "value": "test",
              |          "tag": "noun",
              |          "score": 0.789320001
              |        }
              |      ]
              |    }""".stripMargin

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "noun", Some( 0.789320001 ) ) ), CdrAnnotation.STATIC )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "unmarshal a Offset Tag Annotation with the score populated" in {
        val annotationJson =
            """{
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class" : "static",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "noun",
              |          "score": 0.789320001
              |        }
              |      ]
              |    }""".stripMargin

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "noun", Some( 0.789320001 ) ) ), CdrAnnotation.STATIC )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "unmarshal a Offset Tag Annotation with the score not populated" in {
        val annotationJson =
            """{
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class" : "static",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "noun"
              |        }
              |      ]
              |    }""".stripMargin

        val expected = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "noun", None ) ), CdrAnnotation.STATIC )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "unmarshal a Facet Annotation with the score populated" in {
        val annotationJson =
            """{
              |      "type": "facets",
              |      "label": "categories",
              |      "version": "1.0",
              |      "class" : "derived",
              |      "content": [
              |        {
              |          "value": "test",
              |          "score": 0.0001
              |        }
              |      ]
              |    }""".stripMargin

        val expected = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( 0.0001 ) ) ) )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "unmarshal a Facet Annotation without the score populated" in {
        val annotationJson =
            """{
              |      "type": "facets",
              |      "label": "categories",
              |      "version": "1.0",
              |      "class" : "static",
              |      "content": [
              |        {
              |          "value": "test"
              |        }
              |      ]
              |    }""".stripMargin

        val expected = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", None ) ), CdrAnnotation.STATIC )

        val annotation : Option[ CdrAnnotation[ _ ] ] = format.unmarshalAnnotation( annotationJson )

        annotation.isDefined shouldBe true
        annotation.get shouldBe expected

    }

    "DART Json CDR Format" should "marshal a Text Annotation" in {
        val text : CdrAnnotation[ _ ] = TextAnnotation( "text", "1.0", "my text" )

        val json : Option[ String ] = format.marshalAnnotation( text )

        val expected = """{"type":"text","label":"text","version":"1.0","class":"derived","content":"my text"}"""

        json.isDefined shouldBe true
    }

    "DART Json CDR Format" should "marshal a Dictionary Annotation" in {
        val dict = DictionaryAnnotation( "dict", "1.0", Map( "test" -> "my test" ) )

        val expected = """{"type":"dictionary","label":"dict","version":"1.0","content":{"test":"my test"},"class":"derived"}"""

        val json : Option[ String ] = format.marshalAnnotation( dict )

        json.isDefined shouldBe true
        json.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a Offset Tag Annotation without the value populated" in {
        val tags = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, None, "noun", Some( 0.789320001 ) ) ) )

        val expected = """{"type":"tags","label":"tags","version":"1.0","content":[{"offset_start":0,"offset_end":1,"tag":"noun","score":0.789320001}],"class":"derived"}"""

        val json : Option[ String ] = format.marshalAnnotation( tags )

        json.isDefined shouldBe true
        json.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a Offset Tag Annotation with the value populated" in {
        val tags = OffsetTagAnnotation( "tags", "1.0", List( OffsetTag( 0, 1, Some( "test" ), "noun", Some( 0.789320001 ) ) ) )

        val expected = """{"type":"tags","label":"tags","version":"1.0","content":[{"offset_start":0,"offset_end":1,"value":"test","tag":"noun","score":0.789320001}],"class":"derived"}"""

        val json : Option[ String ] = format.marshalAnnotation( tags )

        json.isDefined shouldBe true
        json.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a Facet Annotation with the score populated" in {
        val tags = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", Some( 0.5 ) ) ) )

        val expected = """{"type":"facets","label":"categories","version":"1.0","content":[{"value":"test","score":0.5}],"class":"derived"}"""

        val json : Option[ String ] = format.marshalAnnotation( tags )

        json.isDefined shouldBe true
        json.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a Facet Annotation without the score populated" in {
        val tags = FacetAnnotation( "categories", "1.0", List( FacetScore( "test", None ) ) )

        val expected = """{"type":"facets","label":"categories","version":"1.0","content":[{"value":"test"}],"class":"derived"}"""

        val json : Option[ String ] = format.marshalAnnotation( tags )

        json.isDefined shouldBe true
        json.get shouldBe expected
    }

    "DART Json CDR Format" should "marshal a Document Genealogy Annotation" in {
        val genealogy : DocumentGenealogy = {
            val similarDocuments : Map[ String, BigDecimal ] = Map( "abc" -> BigDecimal( 0.5 ), "def" -> BigDecimal( 0.2 ) )
            val similarityMatrix : Array[ Array[ BigDecimal ] ] = Array( Array( BigDecimal( 0.1 ), BigDecimal( 0.2 ), BigDecimal( 0.3 ) ),
                                                                         Array( BigDecimal( 0.4 ), BigDecimal( 0.5 ), BigDecimal( 0.6 ) ),
                                                                         Array( BigDecimal( 0.7 ), BigDecimal( 0.8 ), BigDecimal( 0.9 ) ) )

            DocumentGenealogy( similarDocuments, similarityMatrix )
        }

        val annotation = DocumentGenealogyAnnotation( "genealogy", "1.0", genealogy )

        val expectedJson = """{"type":"genealogy","label":"genealogy","version":"1.0","content":{"similar_documents":{"abc":0.5,"def":0.2},"similarity_matrix":[[0.1,0.2,0.3],[0.4,0.5,0.6],[0.7,0.8,0.9]]},"class":"derived"}"""

        val actualJson = format.marshalAnnotation( annotation )

        actualJson.isDefined shouldBe true
        actualJson.get shouldBe expectedJson
    }

    "DART Json CDR Format" should "unmarshal a Document Genealogy Annotation" in {
        val genealogy : DocumentGenealogy = {
            val similarDocuments : Map[ String, BigDecimal ] = Map( "abc" -> BigDecimal( 0.5 ), "def" -> BigDecimal( 0.2 ) )
            val similarityMatrix : Array[ Array[ BigDecimal ] ] = Array( Array( BigDecimal( 0.1 ), BigDecimal( 0.2 ), BigDecimal( 0.3 ) ),
                                                                         Array( BigDecimal( 0.4 ), BigDecimal( 0.5 ), BigDecimal( 0.6 ) ),
                                                                         Array( BigDecimal( 0.7 ), BigDecimal( 0.8 ), BigDecimal( 0.9 ) ) )

            DocumentGenealogy( similarDocuments, similarityMatrix )
        }
        val expectedAnnotation = DocumentGenealogyAnnotation( "genealogy", "1.0", genealogy )


        val inputJson = """{"type":"genealogy","label":"genealogy","version":"1.0","content":{"similar_documents":{"abc":0.5,"def":0.2},"similarity_matrix":[[0.1,0.2,0.3],[0.4,0.5,0.6],[0.7,0.8,0.9]]},"class":"derived"}"""

        val actualAnnotation = format.unmarshalAnnotation( inputJson ).asInstanceOf[ Option[ DocumentGenealogyAnnotation ] ]
        actualAnnotation.isDefined shouldBe true

        actualAnnotation.get.label shouldBe expectedAnnotation.label
        actualAnnotation.get.version shouldBe expectedAnnotation.version
        actualAnnotation.get.classification shouldBe expectedAnnotation.classification
        actualAnnotation.get.content.similarDocuments shouldBe expectedAnnotation.content.similarDocuments
        actualAnnotation.get.content.similarityMatrix.deep shouldBe expectedAnnotation.content.similarityMatrix.deep
    }

    "DART Json CDR Format" should "marshal a Translation Annotation" in {
        val annotation = TranslationAnnotation( "translation", "1.0", TranslatedFields( "russian", Map( "extractedText" -> "4 октября 2018 года на пресс-конференции" ) ) )
        val expectedJson = """{"type":"translation","label":"translation","version":"1.0","content":{"language":"russian","fields":{"extractedText":"4 октября 2018 года на пресс-конференции"}},"class":"derived"}"""

        val actualJson = format.marshalAnnotation( annotation )

        actualJson.isDefined shouldBe true
        actualJson.get shouldBe expectedJson
    }

    "DART Json CDR Format" should "unmarshal a Translation Annotation" in {
        val inputJson = """{"type":"translation","label":"translation","version":"1.0","content":{"language":"russian","fields":{"extractedText":"4 октября 2018 года на пресс-конференции"}},"class":"derived"}"""
        val expectedAnnotation = TranslationAnnotation( "translation", "1.0", TranslatedFields( "russian", Map( "extractedText" -> "4 октября 2018 года на пресс-конференции" ) ) )

        val actualAnnotation = format.unmarshalAnnotation( inputJson )

        actualAnnotation.isDefined shouldBe true
        actualAnnotation.get.label shouldBe expectedAnnotation.label
        actualAnnotation.get.version shouldBe expectedAnnotation.version
        actualAnnotation.get.classification shouldBe expectedAnnotation.classification
        actualAnnotation.get.content shouldBe expectedAnnotation.content
    }
}
