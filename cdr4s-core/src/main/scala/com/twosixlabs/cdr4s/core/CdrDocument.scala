package com.twosixlabs.cdr4s.core

import java.time.OffsetDateTime

case class CdrDocument( captureSource : String = null,
                        extractedMetadata : CdrMetadata,
                        contentType : String = null,
                        extractedNumeric : Map[ String, String ] = Map.empty,
                        documentId : String = null,
                        extractedText : String = null,
                        uri : String = null,
                        sourceUri : String = null,
                        extractedNtriples : String = null,
                        timestamp : OffsetDateTime = null,
                        annotations : List[ CdrAnnotation[ Any ] ] = List.empty,
                        labels : Set[ String ] = Set.empty ) {

    def extractAnnotation[ Type <: CdrAnnotation[ _ ] ]( label : String ) : Option[ Type ] = {
        val found = annotations.find( _.label == label )
        if ( found.isDefined ) Some( found.get.asInstanceOf[ Type ] )
        else None
    }

    def makeThin : ThinCdrDocument = ThinCdrDocument( this )
}
