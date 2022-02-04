package com.twosixlabs.cdr4s.test.base

import com.twosixlabs.cdr4s.core.{CdrDocument, CdrMetadata}
import com.twosixlabs.dart.utils.DatesAndTimes

object TestCdrData {

    val DOC_TEMPLATE : CdrDocument = CdrDocument( captureSource = "ManualCuration",
                                                  extractedMetadata = {
                                                      CdrMetadata( creationDate = DatesAndTimes.timeStamp.toLocalDate,
                                                                   modificationDate = DatesAndTimes.timeStamp.toLocalDate,
                                                                   author = "michael",
                                                                   docType = "pdf",
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
                                                  annotations = List() )

}
