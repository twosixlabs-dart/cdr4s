package com.twosixlabs.cdr4s.json.dart

import better.files.Resource
import com.twosixlabs.cdr4s.annotations.{DocumentGenealogy, FacetScore, OffsetTag, TranslatedFields}
import com.twosixlabs.cdr4s.core.{CdrAnnotation, CdrDocument, CdrFormat, CdrMetadata, DictionaryAnnotation, DocumentGenealogyAnnotation, FacetAnnotation, OffsetTagAnnotation, TextAnnotation, ThinCdrAnnotation, ThinCdrDocument, ThinCdrMetadata, TranslationAnnotation}
import com.twosixlabs.dart.json.JsonFormat
import org.everit.json.schema.loader.SchemaLoader
import org.everit.json.schema.{Schema, ValidationException}
import org.json.{JSONObject, JSONTokener}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

class DartJsonFormat extends CdrFormat with JsonFormat {

    private val LOG : Logger = LoggerFactory.getLogger( getClass )

    private val SCHEMA : Schema = {
        val schemaIs = Resource.getAsStream( "schema/cdr-v5.json" )
        val schemaJson = new JSONObject( new JSONTokener( schemaIs ) );
        SchemaLoader.load( schemaJson )
    }

    override def unmarshalCdr( json : String ) : Option[ CdrDocument ] = {
        try Some( dtoToCdr( objectMapper.readValue( json, classOf[ DartCdrDocumentDto ] ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def marshalCdr( doc : CdrDocument ) : Option[ String ] = {
        try {
            Some( objectMapper.writeValueAsString( cdrToDto( doc ) ) )
        } catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def unmarshalMetadata( json : String ) : Option[ CdrMetadata ] = {
        try Some( dtoToMetadata( objectMapper.readValue( json, classOf[ DartMetadataDto ] ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def marshalMetadata( metadata : CdrMetadata ) : Option[ String ] = {
        try {
            Some( objectMapper.writeValueAsString( metadataToDto( metadata ) ) )
        } catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def unmarshalAnnotation( json : String ) : Option[ CdrAnnotation[ _ ] ] = {
        try Some( dtoToAnnotation( objectMapper.readValue( json, classOf[ DartAnnotationDto[ _ ] ] ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def marshalAnnotation( annotation : CdrAnnotation[ _ ] ) : Option[ String ] = {
        try {
            Some( objectMapper.writeValueAsString( annotationToDto( annotation ) ) )
        } catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def validate( cdr : Array[ Byte ] ) : Boolean = {
        try {
            SCHEMA.validate( new JSONObject( new String( cdr ) ) )
            true
        }
        catch {
            case e : ValidationException => {
                LOG.warn( s"Error validating the following content : ${new String( cdr )}" )
                LOG.warn( s"${e.getMessage}" )
                LOG.warn( s"${e.getErrorMessage}" )
                LOG.warn( s"${e.getPointerToViolation}" )
                e.getCausingExceptions.asScala.foreach( e => LOG.warn( e.getAllMessages.asScala.mkString( "\n" ) ) )
                false
            }
            case e : Exception => {
                e.printStackTrace
                false
            }
        }
    }

    override def unmarshalThinCdr( thinJson : String ) : Option[ ThinCdrDocument ] = {
        try Some( dtoToThin( objectMapper.readValue( thinJson, classOf[ ThinCdrDocumentDto ] ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    override def marshalThinCdr( thinCdr : ThinCdrDocument ) : Option[ String ] = {
        try Some( objectMapper.writeValueAsString( thinToDto( thinCdr ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR value : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.error( s"Error root cause : ${e.getCause}" )
                e.printStackTrace()
                None
            }
        }
    }

    private def dtoToThin( dto : ThinCdrDocumentDto ) : ThinCdrDocument = {
        ThinCdrDocument( dto.captureSource,
                         dtoMetadataToThin( dto.extractedMetadata ),
                         dto.contentType,
                         dto.extractedNumeric,
                         dto.documentId,
                         dto.extractedText,
                         dto.uri,
                         dto.sourceUri,
                         dto.extractedNtriples,
                         dto.timestamp,
                         dtoAnnotationsToThin( dto.annotations ),
                         dto.labels )
    }

    private def dtoMetadataToThin( dto : ThinCdrMetadataDto ) : ThinCdrMetadata = {
        ThinCdrMetadata( dto.creationDate,
                         dto.modificationDate,
                         dto.author,
                         dto.docType,
                         dto.description,
                         dto.originalLanguage,
                         dto.classification,
                         dto.title,
                         dto.publisher,
                         dto.url,
                         dto.pages,
                         dto.subject,
                         dto.creator,
                         dto.producer,
                         dto.statedGenre,
                         dto.predictedGenre )
    }


    private def dtoAnnotationsToThin( dtos : List[ ThinCdrAnnotationDto ] ) : List[ ThinCdrAnnotation ] = {
        if ( dtos != null && dtos.nonEmpty ) dtos.map( dtoAnnotationToThin ) else List()
    }


    private def dtoAnnotationToThin( dto : ThinCdrAnnotationDto ) : ThinCdrAnnotation = {
        val annotationString = dto.annotation match {
            case Some( value ) => value
            case None => "NONE"
        }
        ThinCdrAnnotation( annotationString, dto.label )
    }


    private def thinToDto( thin : ThinCdrDocument ) : ThinCdrDocumentDto = {
        ThinCdrDocumentDto( thin.captureSource,
                            thinMetadataToDto( thin.extractedMetadata ),
                            thin.contentType,
                            thin.extractedNumeric,
                            thin.documentId,
                            thin.extractedText,
                            thin.uri,
                            thin.sourceUri,
                            thin.extractedNtriples,
                            thin.timestamp,
                            thinAnnotationsToDto( thin.annotations ),
                            thin.labels )
    }

    private def thinMetadataToDto( thin : ThinCdrMetadata ) : ThinCdrMetadataDto = {
        ThinCdrMetadataDto( thin.creationDate,
                            thin.modificationDate,
                            thin.author,
                            thin.docType,
                            thin.description,
                            thin.language,
                            thin.classification,
                            thin.title,
                            thin.publisher,
                            thin.url,
                            thin.pages,
                            thin.subject,
                            thin.creator,
                            thin.producer,
                            thin.statedGenre,
                            thin.predictedGenre )
    }

    private def thinAnnotationsToDto( annos : List[ ThinCdrAnnotation ] ) : List[ ThinCdrAnnotationDto ] = {
        annos.map( thinAnnotationToDto )
    }

    private def thinAnnotationToDto( thin : ThinCdrAnnotation ) : ThinCdrAnnotationDto = {
        ThinCdrAnnotationDto( Option( thin.annotation ),
                              thin.label )
    }

    def cdrToDto( doc : CdrDocument ) : DartCdrDocumentDto = {
        DartCdrDocumentDto( doc.captureSource,
                            metadataToDto( doc.extractedMetadata ),
                            doc.contentType,
                            doc.extractedNumeric,
                            doc.documentId,
                            doc.extractedText,
                            doc.uri,
                            doc.sourceUri,
                            doc.extractedNtriples,
                            doc.timestamp,
                            convertList[ CdrAnnotation[ Any ], DartAnnotationDto[ Any ] ]( doc.annotations, annotationToDto ),
                            doc.labels )
    }

    def dtoToCdr( dto : DartCdrDocumentDto ) : CdrDocument = {
        CdrDocument( dto.captureSource,
                     dtoToMetadata( dto.extractedMetadata ),
                     dto.contentType,
                     if ( dto.extractedNumeric == null ) Map.empty else dto.extractedNumeric,
                     dto.documentId,
                     dto.extractedText,
                     dto.uri,
                     dto.sourceUri,
                     dto.extractedNtriples,
                     dto.timestamp,
                     convertList[ DartAnnotationDto[ Any ], CdrAnnotation[ Any ] ]( dto.annotations, dtoToAnnotation ),
                     if ( dto.labels == null ) Set.empty else dto.labels )

    }

    private def metadataToDto( metadata : CdrMetadata ) : DartMetadataDto = {
        DartMetadataDto( creationDate = metadata.creationDate,
                         modificationDate = metadata.modificationDate,
                         author = metadata.author,
                         docType = metadata.docType,
                         description = metadata.description,
                         originalLanguage = metadata.originalLanguage,
                         classification = metadata.classification,
                         title = metadata.title,
                         publisher = metadata.publisher,
                         url = metadata.url,
                         pages = {
                             if ( metadata.pages.isDefined ) metadata.pages.get
                             else null
                         },
                         subject = metadata.subject,
                         creator = metadata.creator,
                         producer = metadata.producer,
                         statedGenre = metadata.statedGenre,
                         predictedGenre = metadata.predictedGenre )
    }

    private def dtoToMetadata( dto : DartMetadataDto ) : CdrMetadata = {
        CdrMetadata( creationDate = dto.creationDate,
                     modificationDate = dto.modificationDate,
                     author = dto.author,
                     docType = dto.docType,
                     description = dto.description,
                     originalLanguage = dto.originalLanguage,
                     classification = dto.classification,
                     title = dto.title,
                     publisher = dto.publisher,
                     url = dto.url,
                     pages = Option( dto.pages ),
                     subject = dto.subject,
                     creator = dto.creator,
                     producer = dto.producer,
                     statedGenre = dto.statedGenre,
                     predictedGenre = dto.predictedGenre )
    }

    private def dtoToOffsetTag( tag : DartOffsetTagDto ) : OffsetTag = OffsetTag( tag.offsetStart, tag.offsetEnd, Option( tag.value ), tag.tag, Option( tag.score ) )

    private def offsetTagToDto( tag : OffsetTag ) : DartOffsetTagDto = DartOffsetTagDto( tag.offsetStart, tag.offsetEnd, tag.value.orNull, tag.tag,
                                                                                         if ( tag.score.isEmpty ) null else tag.score.get )

    private def dtoToKeyword( key : DartFacetScore ) : FacetScore = FacetScore( key.value, Option( key.score ) )

    private def keywordToDto( key : FacetScore ) : DartFacetScore = DartFacetScore( key.value,
                                                                                    if ( key.score.isEmpty ) null else key.score.get )

    private def dtoToDocumentGenealogy( from : DartDocumentGenealogyDto ) : DocumentGenealogy = {
        DocumentGenealogy( from.similarDocuments, from.similarityMatrix )
    }

    private def documentGenealogyToDto( from : DocumentGenealogy ) : DartDocumentGenealogyDto = {
        DartDocumentGenealogyDto( from.similarDocuments, from.similarityMatrix )
    }

    private def dtoToTranslatedFields( from : DartTranslatedFields ) : TranslatedFields = {
        TranslatedFields( from.language, from.fields )
    }

    private def translatedFieldsToDto( from : TranslatedFields ) : DartTranslatedFields = {
        DartTranslatedFields( from.language, from.fields )
    }

    private def dtoToAnnotation( annotation : DartAnnotationDto[ Any ] ) : CdrAnnotation[ Any ] = {
        annotation match {
            case text : DartTextAnnotationDto => {
                TextAnnotation( text.label, text.version, text.content, text.classification ).asInstanceOf[ CdrAnnotation[ Any ] ]
            }
            case dict : DartDictionaryAnnotationDto => {
                DictionaryAnnotation( dict.label, dict.version, dict.content, dict.classification ).asInstanceOf[ CdrAnnotation[ Any ] ]
            }
            case tags : DartTagsAnnotationDto => {
                OffsetTagAnnotation( tags.label, tags.version, convertList( tags.content, dtoToOffsetTag ), tags.classification ).asInstanceOf[ CdrAnnotation[ Any ] ]
            }
            case facets : DartFacetAnnotationDto => {
                FacetAnnotation( facets.label, facets.version, convertList( facets.content, dtoToKeyword ), facets.classification ).asInstanceOf[ CdrAnnotation[ Any ] ]
            }
            case genealogy : DartDocumentGenealogyAnnotationDto => {
                DocumentGenealogyAnnotation( genealogy.label, genealogy.version, dtoToDocumentGenealogy( genealogy.content ), genealogy.classification ).asInstanceOf[ CdrAnnotation[ Any ] ]
            }
            case translation : DartTranslationAnnotationDto => {
                TranslationAnnotation( translation.label, translation.version, dtoToTranslatedFields( translation.content ) )
            }

        }

    }

    private def annotationToDto( annotation : CdrAnnotation[ Any ] ) : DartAnnotationDto[ Any ] = {
        annotation match {
            case text : TextAnnotation => {
                DartTextAnnotationDto( text.label, text.version, text.content, text.classification ).asInstanceOf[ DartAnnotationDto[ Any ] ]
            }
            case dict : DictionaryAnnotation => {
                DartDictionaryAnnotationDto( dict.label, dict.version, dict.content, dict.classification ).asInstanceOf[ DartAnnotationDto[ Any ] ]
            }
            case tags : OffsetTagAnnotation => {
                DartTagsAnnotationDto( tags.label, tags.version, convertList( tags.content, offsetTagToDto ), tags.classification ).asInstanceOf[ DartAnnotationDto[ Any ] ]
            }
            case facets : FacetAnnotation => {
                DartFacetAnnotationDto( facets.label, facets.version, convertList( facets.content, keywordToDto ), facets.classification ).asInstanceOf[ DartAnnotationDto[ Any ] ]
            }
            case genealogy : DocumentGenealogyAnnotation => {
                DartDocumentGenealogyAnnotationDto( genealogy.label, genealogy.version, documentGenealogyToDto( genealogy.content ), genealogy.classification )
            }
            case translation : TranslationAnnotation => {
                DartTranslationAnnotationDto( translation.label, translation.version, translatedFieldsToDto( translation.content ), translation.classification )
            }
        }
    }
}
