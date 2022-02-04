package com.twosixlabs.cdr4s.core

import com.twosixlabs.dart.utils.{DatesAndTimes, IdGenerator}

object ThinCdrDocument {
    def apply( cdr : CdrDocument ) : ThinCdrDocument = {
        ThinCdrDocument( if ( cdr.captureSource == null ) None else Some( IdGenerator.getMd5Hash( cdr.captureSource.getBytes ) ),
                         cdr.extractedMetadata.makeThin,
                         if ( cdr.contentType == null ) None else Some( IdGenerator.getMd5Hash( cdr.contentType.getBytes ) ),
                         if ( cdr.extractedNumeric == Map.empty || cdr.extractedNumeric == null ) None else Some( IdGenerator.getMd5Hash( cdr.extractedNumeric.mkString( "," ).getBytes ) ),
                         cdr.documentId,
                         if ( cdr.extractedText == null ) None else Some( IdGenerator.getMd5Hash( cdr.extractedText.getBytes ) ),
                         if ( cdr.uri == null ) None else Some( IdGenerator.getMd5Hash( cdr.uri.getBytes ) ),
                         if ( cdr.sourceUri == null ) None else Some( IdGenerator.getMd5Hash( cdr.sourceUri.getBytes ) ),
                         if ( cdr.extractedNtriples == null ) None else Some( IdGenerator.getMd5Hash( cdr.extractedNtriples.getBytes ) ),
                         if ( cdr.timestamp == null ) None else Some( DatesAndTimes.toIsoOffsetDateTimeStr( cdr.timestamp ) ),
                         if ( cdr.annotations == null || cdr.annotations.isEmpty ) List() else cdr.annotations.map( _.makeThin ),
                         labels = if ( cdr.labels == null || cdr.labels.isEmpty ) None else Some( IdGenerator.getMd5Hash( cdr.labels.mkString( "," ).getBytes ) ) )
    }
}

case class ThinCdrDocument( captureSource : Option[ String ] = None,
                            extractedMetadata : ThinCdrMetadata,
                            contentType : Option[ String ],
                            extractedNumeric : Option[ String ] = None,
                            documentId : String,
                            extractedText : Option[ String ] = None,
                            uri : Option[ String ],
                            sourceUri : Option[ String ],
                            extractedNtriples : Option[ String ],
                            timestamp : Option[ String ],
                            annotations : List[ ThinCdrAnnotation ] = List(),
                            labels : Option[ String ] = None )
