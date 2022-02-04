package com.twosixlabs.cdr4s.json.dart

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonFormat, JsonInclude, JsonProperty, JsonSubTypes, JsonTypeInfo, JsonTypeName}

import java.time.{LocalDate, OffsetDateTime}
import scala.beans.BeanProperty

@JsonInclude( Include.NON_EMPTY )
case class DartCdrDocumentDto( @BeanProperty @JsonProperty( "capture_source" ) captureSource : String = null,
                               @BeanProperty @JsonProperty( "extracted_metadata" ) extractedMetadata : DartMetadataDto = null,
                               @BeanProperty @JsonProperty( "content_type" ) contentType : String = null,
                               @BeanProperty @JsonProperty( "extracted_numeric" ) extractedNumeric : Map[ String, String ] = Map.empty,
                               @BeanProperty @JsonProperty( "document_id" ) documentId : String = null,
                               @BeanProperty @JsonProperty( "extracted_text" ) extractedText : String = null,
                               @BeanProperty @JsonProperty( "uri" ) uri : String = null,
                               @BeanProperty @JsonProperty( "source_uri" ) sourceUri : String = null,
                               @BeanProperty @JsonProperty( "extracted_ntriples" ) extractedNtriples : String = null,
                               @BeanProperty @JsonProperty( "timestamp" ) @JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC" ) timestamp : OffsetDateTime,
                               @BeanProperty @JsonProperty( "annotations" ) @JsonInclude( Include.ALWAYS ) annotations : List[ DartAnnotationDto[ Any ] ] = List.empty,
                               @BeanProperty @JsonProperty( "labels" ) labels : Set[ String ] = Set.empty )

@JsonInclude( Include.NON_EMPTY )
case class DartMetadataDto( @BeanProperty @JsonProperty( "CreationDate" ) @JsonFormat( pattern = "yyyy-MM-dd" ) creationDate : LocalDate = null,
                            @BeanProperty @JsonProperty( "ModDate" ) @JsonFormat( pattern = "yyyy-MM-dd" ) modificationDate : LocalDate = null,
                            @BeanProperty @JsonProperty( "Author" ) author : String = null,
                            @BeanProperty @JsonProperty( "Type" ) docType : String = null,
                            @BeanProperty @JsonProperty( "Description" ) description : String = null,
                            @BeanProperty @JsonProperty( "OriginalLanguage" ) originalLanguage : String = null,
                            @BeanProperty @JsonProperty( "Classification" ) classification : String = null,
                            @BeanProperty @JsonProperty( "Title" ) title : String = null,
                            @BeanProperty @JsonProperty( "Publisher" ) publisher : String = null,
                            @BeanProperty @JsonProperty( "URL" ) url : String = null,
                            @BeanProperty @JsonProperty( "Pages" ) pages : Integer = null,
                            @BeanProperty @JsonProperty( "Subject" ) subject : String = null,
                            @BeanProperty @JsonProperty( "Creator" ) creator : String = null,
                            @BeanProperty @JsonProperty( "Producer" ) producer : String = null,
                            @BeanProperty @JsonProperty( "StatedGenre" ) statedGenre : String = null,
                            @BeanProperty @JsonProperty( "PredictedGenre" ) predictedGenre : String = null )

//@formatter:off
@JsonTypeInfo (
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes( Array(
    new Type( value = classOf[ DartDocumentGenealogyAnnotationDto ], name = "genealogy" ),
    new Type( value = classOf[ DartTextAnnotationDto ], name = "text" ),
    new Type( value = classOf[ DartDictionaryAnnotationDto ], name = "dictionary" ),
    new Type( value = classOf[ DartTagsAnnotationDto ], name = "tags" ),
    new Type( value = classOf[ DartFacetAnnotationDto ], name = "facets" ),
    new Type( value = classOf[ DartTranslationAnnotationDto ], name = "translation" )
) )
@JsonInclude( Include.NON_NULL )
abstract class DartAnnotationDto[ +T ]( @BeanProperty @JsonProperty( "label" ) val label : String,
                                        @BeanProperty @JsonProperty( "version" ) val version : String,
                                        @BeanProperty @JsonProperty( "content" ) val content : T,
                                        @BeanProperty @JsonProperty( "class" ) val classification : String )

//@formatter:on

case class DartTextAnnotationDto( override val label : String,
                                  override val version : String,
                                  override val content : String,
                                  @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ String ]( label, version, content, classification )

case class DartDictionaryAnnotationDto( override val label : String,
                                        override val version : String,
                                        override val content : Map[ String, String ],
                                        @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ Map[ String, String ] ]( label, version, content, classification )

@JsonTypeName( "tags" )
case class DartTagsAnnotationDto( override val label : String,
                                  override val version : String,
                                  override val content : List[ DartOffsetTagDto ],
                                  @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ List[ DartOffsetTagDto ] ]( label, version, content, classification )

@JsonTypeName( "facets" )
case class DartFacetAnnotationDto( override val label : String,
                                   override val version : String,
                                   override val content : List[ DartFacetScore ],
                                   @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ List[ DartFacetScore ] ]( label, version, content, classification )

@JsonTypeName( "genealogy" )
case class DartDocumentGenealogyAnnotationDto( override val label : String,
                                               override val version : String,
                                               override val content : DartDocumentGenealogyDto,
                                               @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ DartDocumentGenealogyDto ]( label, version, content, classification )

@JsonTypeName( "translation" )
case class DartTranslationAnnotationDto( override val label : String,
                                         override val version : String,
                                         override val content : DartTranslatedFields,
                                         @BeanProperty @JsonProperty( "class" ) override val classification : String ) extends DartAnnotationDto[ DartTranslatedFields ]( label, version, content, classification )

@JsonInclude( Include.NON_EMPTY )
case class DartOffsetTagDto( @BeanProperty @JsonProperty( "offset_start" ) offsetStart : Int,
                             @BeanProperty @JsonProperty( "offset_end" ) offsetEnd : Int,
                             @BeanProperty @JsonProperty( "value" ) value : String,
                             @BeanProperty @JsonProperty( "tag" ) tag : String,
                             @BeanProperty @JsonProperty( "score" ) score : BigDecimal = null )

@JsonInclude( Include.NON_EMPTY )
case class DartFacetScore( @BeanProperty @JsonProperty( "value" ) value : String,
                           @BeanProperty @JsonProperty( "score" ) score : BigDecimal = null )

@JsonInclude( Include.NON_EMPTY )
case class DartDocumentGenealogyDto( @BeanProperty @JsonProperty( "similar_documents" ) similarDocuments : Map[ String, BigDecimal ],
                                     @BeanProperty @JsonProperty( "similarity_matrix" ) similarityMatrix : Array[ Array[ BigDecimal ] ] )

@JsonInclude( Include.NON_EMPTY )
case class DartTranslatedFields( @BeanProperty @JsonProperty( "language" ) language : String,
                                 @BeanProperty @JsonProperty( "fields" ) fields : Map[ String, String ] )
