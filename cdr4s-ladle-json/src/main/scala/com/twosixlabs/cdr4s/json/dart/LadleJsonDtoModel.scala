package com.twosixlabs.cdr4s.json.dart

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude, JsonProperty}

import scala.beans.BeanProperty

@JsonInclude( Include.NON_EMPTY )
@JsonIgnoreProperties( ignoreUnknown = true )
case class CdrDocumentDto( @BeanProperty @JsonProperty( "capture_source" ) captureSource : String = null,
                           @BeanProperty @JsonProperty( "extracted_metadata" ) extractedMetadata : CdrMetadataDto = null,
                           @BeanProperty @JsonProperty( "content_type" ) contentType : String = null,
                           @BeanProperty @JsonProperty( "extracted_numeric" ) extractedNumeric : Map[ String, String ] = Map.empty,
                           @BeanProperty @JsonProperty( "document_id" ) documentId : String = null,
                           @BeanProperty @JsonProperty( "extracted_text" ) extractedText : String = null,
                           @BeanProperty @JsonProperty( "uri" ) uri : String = null,
                           @BeanProperty @JsonProperty( "source_uri" ) sourceUri : String = null,
                           @BeanProperty @JsonProperty( "extracted_ntriples" ) extractedNtriples : String = null,
                           @BeanProperty @JsonProperty( "timestamp" ) timestamp : Option[ java.lang.Long ] = None,
                           @BeanProperty @JsonProperty( "labels" ) labels : Set[ String ] = Set.empty )

@JsonInclude( Include.NON_EMPTY )
@JsonIgnoreProperties( ignoreUnknown = true )
case class CdrMetadataDto( @BeanProperty @JsonProperty( "CreationDate" ) creationDate : Option[ java.lang.Long ] = None,
                           @BeanProperty @JsonProperty( "ModDate" ) modificationDate : Option[ java.lang.Long ] = None,
                           @BeanProperty @JsonProperty( "Author" ) author : String = null,
                           @BeanProperty @JsonProperty( "Type" ) docType : String = null,
                           @BeanProperty @JsonProperty( "Description" ) description : String = null,
                           @BeanProperty @JsonProperty( "Language" ) language : String = null,
                           @BeanProperty @JsonProperty( "Classification" ) classification : String = null,
                           @BeanProperty @JsonProperty( "Title" ) title : String = null,
                           @BeanProperty @JsonProperty( "Publisher" ) publisher : String = null,
                           @BeanProperty @JsonProperty( "URL" ) url : String = null,
                           @BeanProperty @JsonProperty( "Pages" ) pages : Integer = null, // issues unmarshalling options for primitives
                           @BeanProperty @JsonProperty( "Subject" ) subject : String = null,
                           @BeanProperty @JsonProperty( "Creator" ) creator : String = null,
                           @BeanProperty @JsonProperty( "Producer" ) producer : String = null )
