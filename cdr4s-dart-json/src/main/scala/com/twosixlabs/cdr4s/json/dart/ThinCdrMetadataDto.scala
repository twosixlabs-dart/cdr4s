package com.twosixlabs.cdr4s.json.dart

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

import scala.beans.BeanProperty

@JsonInclude( Include.NON_EMPTY )
case class ThinCdrMetadataDto( @BeanProperty @JsonProperty( "CreationDate" ) creationDate : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "ModDate" ) modificationDate : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Author" ) author : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Type" ) docType : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Description" ) description : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "OriginalLanguage" ) originalLanguage : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Classification" ) classification : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Title" ) title : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Publisher" ) publisher : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "URL" ) url : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Pages" ) pages : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Subject" ) subject : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Creator" ) creator : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "Producer" ) producer : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "StatedGenre" ) statedGenre : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "PredictedGenre" ) predictedGenre : Option[ String ] = None )
