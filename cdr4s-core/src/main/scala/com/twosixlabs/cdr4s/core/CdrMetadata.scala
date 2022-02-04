package com.twosixlabs.cdr4s.core

import java.time.LocalDate

case class CdrMetadata( creationDate : LocalDate = null,
                        modificationDate : LocalDate = null,
                        author : String = null,
                        docType : String = null,
                        description : String = null,
                        originalLanguage : String = null,
                        classification : String = null,
                        title : String = null,
                        publisher : String = null,
                        url : String = null,
                        pages : Option[ Integer ] = None,
                        subject : String = null,
                        creator : String = null,
                        producer : String = null,
                        statedGenre : String = null,
                        predictedGenre : String = null ) {

    def makeThin : ThinCdrMetadata = ThinCdrMetadata( this )

}
