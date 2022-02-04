package com.twosixlabs.cdr4s.json.dart

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

import scala.beans.BeanProperty

@JsonInclude( Include.NON_EMPTY )
case class ThinCdrDocumentDto( @BeanProperty @JsonProperty( "capture_source" ) captureSource : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "extracted_metadata" ) extractedMetadata : ThinCdrMetadataDto,
                               @BeanProperty @JsonProperty( "content_type" ) contentType : Option[ String ],
                               @BeanProperty @JsonProperty( "extracted_numeric" ) extractedNumeric : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "document_id" ) documentId : String,
                               @BeanProperty @JsonProperty( "extracted_text" ) extractedText : Option[ String ] = None,
                               @BeanProperty @JsonProperty( "uri" ) uri : Option[ String ],
                               @BeanProperty @JsonProperty( "source_uri" ) sourceUri : Option[ String ],
                               @BeanProperty @JsonProperty( "extracted_ntriples" ) extractedNtriples : Option[ String ],
                               @BeanProperty @JsonProperty( "timestamp" ) timestamp : Option[ String ],
                               @BeanProperty @JsonProperty( "annotations" ) annotations : List[ ThinCdrAnnotationDto ] = List(),
                               @BeanProperty @JsonProperty( "labels" ) labels : Option[ String ] = None )
