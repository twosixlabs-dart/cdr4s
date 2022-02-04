package com.twosixlabs.cdr4s.json.dart

import com.twosixlabs.cdr4s.core.CdrFormat
import com.twosixlabs.dart.test.base.StandardTestBase3x

class DartJsonSchemaValidationTestSuite extends StandardTestBase3x {

    val format : CdrFormat = new DartJsonFormat()

    "DART Json Format schema validation" should "return true when validating a valid CDR Document" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "StatedGenre" : "academic_paper",
              |    "PredictedGenre" : "academic_paper"
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
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "content": "text"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "content": {
              |        "test": "1"
              |      }
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "tag": "V"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return true when validating a valid CDR Document with Offset Tags that don't have the value" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
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
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": "text"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "content": {
              |        "test": "1"
              |      }
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "value": "test",
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "value": "test",
              |          "tag": "V"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return true when validating a valid CDR Document with Offset Tags that have the value populated" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
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
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": "text"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": {
              |        "test": "1"
              |      }
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "tag": "V"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return true when validating a valid CDR Document with polymorphic annotations" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
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
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": "text"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": {
              |        "test": "1"
              |      }
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "value": "test value",
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "tag": "V"
              |        }
              |      ]
              |    },
              |    {
              |      "type": "facets",
              |      "label": "categories",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "value": "test",
              |          "score": 0.893458
              |        },
              |        {
              |          "value": "test two"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return true when validating a document with all metadata fields" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 1,
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
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "annotations": [
              |    {
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": "text"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "class": "static",
              |      "content": {
              |        "test": "1"
              |      }
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class": "derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "tag": "V"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return true when validating a document with categories field" in {
        val cdrJson =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate" : "2019-06-30",
              |    "ModDate" : "2019-06-30",
              |    "Author": "Jane Doe",
              |    "Type": "",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
              |    "Classification": "UNCLASSIFIED",
              |    "Title": "Lorum Ipsum",
              |    "Publisher": "Lorum Ipsum",
              |    "URL": "https://www.lorumipsum.com",
              |    "Pages": 1,
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
              |  "timestamp": "2019-09-18T09:25:59.672Z",
              |  "categories":[
              |     "Lorem",
              |     "Ipsum"
              |  ],
              |  "annotations": [
              |    {
              |      "type": "text",
              |      "label": "text",
              |      "version": "1.0",
              |      "content": "text",
              |      "class":"derived"
              |    },
              |    {
              |      "type": "dictionary",
              |      "label": "dict",
              |      "version": "1.0",
              |      "content": {
              |        "test": "1"
              |      },
              |     "class":"derived"
              |    },
              |    {
              |      "type": "tags",
              |      "label": "tags",
              |      "version": "1.0",
              |      "class":"derived",
              |      "content": [
              |        {
              |          "offset_start": 0,
              |          "offset_end": 1,
              |          "tag": "N"
              |        },
              |        {
              |          "offset_start": 2,
              |          "offset_end": 3,
              |          "tag": "V"
              |        }
              |      ]
              |    }
              |  ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "return false when validating a Cdr Document missing required fields" in {
        val json =
            """{
              | "capture_source": "ManualCuration",
              | "extracted_metadata": {
              |     "CreationDate" : "2019-06-30",
              |     "ModDate" : "2019-06-30",
              |     "Author": "Jane Doe",
              |     "Type": "",
              |     "Description": "Lorum Ipsum",
              |     "Language": "en",
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
              |     "content": {
              |         "test": "1"
              |     }
              |   },
              |   {
              |     "type": "text",
              |     "label": "text",
              |     "version": "1.0",
              |     "content": "1"
              |   }
              | ]
              |}""".stripMargin

        format.validate( json.getBytes() ) shouldBe true
    }

    "DART Json Format schema validation" should "validate a document w/ a Document Genealogy Annotation" in {
        val cdrJson : String =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": "1992-06-21",
              |    "ModDate": "2003-05-01",
              |    "Author": "John Doe",
              |    "Type": "News Article",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
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
              |  "document_id": "34jk5h34723hk",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://www.lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2020-05-21T18:57:10.372Z",
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

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "validate a document with a Translation Annotation" in {
        val cdrJson =
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

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "validate a document without extracted_ntriples" in {
        val cdrJson : String =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": "1992-06-21",
              |    "ModDate": "2003-05-01",
              |    "Author": "John Doe",
              |    "Type": "News Article",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
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
              |  "document_id": "34jk5h34723hk",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://www.lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "timestamp": "2020-05-21T18:57:10.372Z",
              |  "annotations": [],
              |  "labels" : [ "lorum", "ipsum" ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }

    "DART Json Format schema validation" should "validate a document with labels" in {
        val cdrJson : String =
            """{
              |  "capture_source": "ManualCuration",
              |  "extracted_metadata": {
              |    "CreationDate": "1992-06-21",
              |    "ModDate": "2003-05-01",
              |    "Author": "John Doe",
              |    "Type": "News Article",
              |    "Description": "Lorum Ipsum",
              |    "Language": "en",
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
              |  "document_id": "34jk5h34723hk",
              |  "extracted_text": "Lorum Ipsum",
              |  "uri": "https://www.lorumipsum.com",
              |  "source_uri": "Lorum Ipsum",
              |  "extracted_ntriples": "<http://one.example/subject1> <http://one.example/predicate1> <http://one.example/object1> .",
              |  "timestamp": "2020-05-21T18:57:10.372Z",
              |  "annotations": [],
              |  "labels" : [ "lorum", "ipsum" ]
              |}""".stripMargin

        format.validate( cdrJson.getBytes ) shouldBe true
    }
}
