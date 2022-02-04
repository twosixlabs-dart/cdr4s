package com.twosixlabs.cdr4s.core

import com.twosixlabs.dart.utils.{DatesAndTimes, IdGenerator}

object ThinCdrMetadata {
    def apply( metadata : CdrMetadata ) : ThinCdrMetadata = {
        ThinCdrMetadata( if ( metadata.creationDate == null ) None else Some( IdGenerator.getMd5Hash( DatesAndTimes.toIsoLocalDateStr( metadata.creationDate ).getBytes ) ),
                         if ( metadata.modificationDate == null ) None else Some( IdGenerator.getMd5Hash( DatesAndTimes.toIsoLocalDateStr( metadata.modificationDate ).getBytes ) ),
                         if ( metadata.author == null ) None else Some( IdGenerator.getMd5Hash( metadata.author.getBytes() ) ),
                         if ( metadata.docType == null ) None else Some( IdGenerator.getMd5Hash( metadata.docType.getBytes ) ),
                         if ( metadata.description == null ) None else Some( IdGenerator.getMd5Hash( metadata.description.getBytes ) ),
                         if ( metadata.originalLanguage == null ) None else Some( IdGenerator.getMd5Hash( metadata.originalLanguage.getBytes ) ),
                         if ( metadata.classification == null ) None else Some( IdGenerator.getMd5Hash( metadata.classification.getBytes ) ),
                         if ( metadata.title == null ) None else Some( IdGenerator.getMd5Hash( metadata.title.getBytes ) ),
                         if ( metadata.publisher == null ) None else Some( IdGenerator.getMd5Hash( metadata.publisher.getBytes ) ),
                         if ( metadata.url == null ) None else Some( IdGenerator.getMd5Hash( metadata.url.getBytes ) ),
                         if ( metadata.pages.isEmpty || metadata.pages == null ) None else Some( IdGenerator.getMd5Hash( metadata.pages.get.toString.getBytes ) ),
                         if ( metadata.subject == null ) None else Some( IdGenerator.getMd5Hash( metadata.subject.getBytes ) ),
                         if ( metadata.creator == null ) None else Some( IdGenerator.getMd5Hash( metadata.creator.getBytes ) ),
                         if ( metadata.producer == null ) None else Some( IdGenerator.getMd5Hash( metadata.producer.getBytes ) ),
                         if ( metadata.statedGenre == null || metadata.statedGenre.isEmpty ) None else Some( IdGenerator.getMd5Hash( metadata.statedGenre.getBytes() ) ),
                         if ( metadata.predictedGenre == null || metadata.predictedGenre.isEmpty ) None else Some( IdGenerator.getMd5Hash( metadata.predictedGenre.getBytes() ) ) )
    }
}

case class ThinCdrMetadata( creationDate : Option[ String ] = None,
                            modificationDate : Option[ String ] = None,
                            author : Option[ String ] = None,
                            docType : Option[ String ] = None,
                            description : Option[ String ] = None,
                            language : Option[ String ] = None,
                            classification : Option[ String ] = None,
                            title : Option[ String ] = None,
                            publisher : Option[ String ] = None,
                            url : Option[ String ] = None,
                            pages : Option[ String ] = None,
                            subject : Option[ String ] = None,
                            creator : Option[ String ] = None,
                            producer : Option[ String ] = None,
                            statedGenre : Option[ String ] = None,
                            predictedGenre : Option[ String ] = None )
