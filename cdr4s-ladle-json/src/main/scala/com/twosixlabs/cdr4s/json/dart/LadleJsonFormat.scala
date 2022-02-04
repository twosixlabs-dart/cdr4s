package com.twosixlabs.cdr4s.json.dart

import better.files.Resource
import com.twosixlabs.cdr4s.core.{CdrAnnotation, CdrDocument, CdrFormat, CdrMetadata, ThinCdrDocument}
import com.twosixlabs.dart.json.JsonFormat
import com.twosixlabs.dart.utils.DatesAndTimes
import org.everit.json.schema.loader.SchemaLoader
import org.everit.json.schema.{Schema, ValidationException}
import org.json.{JSONObject, JSONTokener}
import org.slf4j.{Logger, LoggerFactory}

class LadleJsonFormat extends CdrFormat with JsonFormat {

    private val LOG : Logger = LoggerFactory.getLogger( getClass )

    private val SCHEMA : Schema = {
        val schemaIs = Resource.getAsStream( "schema/cdr-v2.json" )
        val schemaJson = new JSONObject( new JSONTokener( schemaIs ) );
        SchemaLoader.load( schemaJson )
    }

    override def unmarshalCdr( json : String ) : Option[ CdrDocument ] = {
        try {
            val dto = objectMapper.readValue( json, classOf[ CdrDocumentDto ] )
            val value = dtoToCdr( dto )
            Some( value )
        }
        catch {
            case e : Exception => {
                LOG.error( s"Error unmarshalling CDR json : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.debug( s"Error root cause : ${e.getCause}" )
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
                LOG.debug( s"Error root cause : ${e.getCause}" )
                None
            }
        }
    }

    override def unmarshalMetadata( json : String ) : Option[ CdrMetadata ] = {
        try Some( dtoToMetadata( objectMapper.readValue( json, classOf[ CdrMetadataDto ] ) ) )
        catch {
            case e : Exception => {
                LOG.error( s"Error unmarshalling CDR metadata : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.debug( s"Error root cause : ${e.getCause}" )
                None
            }
        }
    }

    override def marshalMetadata( metadata : CdrMetadata ) : Option[ String ] = {
        try {
            Some( objectMapper.writeValueAsString( metadataToDto( metadata ) ) )
        } catch {
            case e : Exception => {
                LOG.error( s"Error marshalling CDR metadata : ${e.getClass.getSimpleName} : ${e.getMessage}" )
                LOG.debug( s"Error root cause : ${e.getCause}" )
                None
            }
        }
    }

    override def unmarshalAnnotation( json : String ) : Option[ CdrAnnotation[ _ ] ] = throw new UnsupportedOperationException( "Annotations are not part of the Ladle schema" )

    override def marshalAnnotation( annotation : CdrAnnotation[ _ ] ) : Option[ String ] = throw new UnsupportedOperationException( "Annotations are not part of the Ladle schema" )

    override def validate( cdr : Array[ Byte ] ) : Boolean = {
        try {
            SCHEMA.validate( new JSONObject( new String( cdr ) ) )
            return true
        }
        catch {
            case e : ValidationException => {
                LOG.debug( s"Error validating the following content : ${new String( cdr )}" )
                LOG.debug( s"${e.getMessage}" )
                LOG.debug( s"${e.getErrorMessage}" )
                LOG.debug( s"${e.getPointerToViolation}" )
                false
            }
        }
    }

    override def unmarshalThinCdr( thinJson : String ) : Option[ ThinCdrDocument ] = {
        throw new NotImplementedError( "thin cdrs are not supported in the ladle format" )
    }

    override def marshalThinCdr( thinCdr : ThinCdrDocument ) : Option[ String ] = {
        throw new NotImplementedError( "thin cdrs are not supported in the ladle format" )
    }

    //===================================================
    //
    // Implicit methods for converting back and forth from the JSON annotated classes
    //
    // Annotations are not considered part of the REST model, therefore it is not handled in marshalling and unmarshalling
    //
    //===================================================

    private def cdrToDto( doc : CdrDocument ) : CdrDocumentDto = {
        val metadataDto : CdrMetadataDto = metadataToDto( doc.extractedMetadata ) // implicit conversion
        CdrDocumentDto( doc.captureSource,
                        metadataDto,
                        doc.contentType,
                        doc.extractedNumeric,
                        doc.documentId,
                        doc.extractedText,
                        doc.uri,
                        doc.sourceUri,
                        doc.extractedNtriples,
                        {
                            if ( doc.timestamp == null ) None
                            else Some( DatesAndTimes.epochSecFromOffsetDateTime( doc.timestamp ) )
                        },
                        doc.labels )
    }

    private def dtoToCdr( dto : CdrDocumentDto ) : CdrDocument = {
        val metadata : CdrMetadata = dtoToMetadata( dto.extractedMetadata ) // implicitly converte
        CdrDocument( captureSource = dto.captureSource,
                     extractedMetadata = metadata,
                     contentType = dto.contentType,
                     if ( dto.extractedNumeric == null ) Map.empty else dto.extractedNumeric,
                     documentId = dto.documentId,
                     extractedText = dto.extractedText,
                     uri = dto.uri,
                     sourceUri = dto.sourceUri,
                     extractedNtriples = dto.extractedNtriples,
                     {
                         if ( dto.timestamp.isEmpty ) null
                         else DatesAndTimes.offsetDateTimeFromEpochSec( dto.timestamp.get )
                     },
                     labels = if ( dto.labels == null ) Set.empty else dto.labels )

    }

    private def metadataToDto( metadata : CdrMetadata ) : CdrMetadataDto = {
        CdrMetadataDto( {
                            if ( metadata.creationDate == null ) None
                            else Some( DatesAndTimes.epochSecFromLocalDate( metadata.creationDate ) )
                        },
                        {
                            if ( metadata.modificationDate == null ) None
                            else Some( DatesAndTimes.epochSecFromLocalDate( metadata.modificationDate ) )
                        },
                        metadata.author,
                        metadata.docType,
                        metadata.description,
                        metadata.originalLanguage,
                        metadata.classification,
                        metadata.title,
                        metadata.publisher,
                        metadata.url,
                        {
                            if ( metadata.pages.isDefined ) metadata.pages.get
                            else null
                        },
                        metadata.subject,
                        metadata.creator,
                        metadata.producer )
    }

    private def dtoToMetadata( dto : CdrMetadataDto ) : CdrMetadata = {
        CdrMetadata( {
                         if ( dto.creationDate.isEmpty ) null
                         else DatesAndTimes.localDateFromEpochSec( dto.creationDate.get )
                     },
                     {
                         if ( dto.modificationDate.isEmpty ) null
                         else DatesAndTimes.localDateFromEpochSec( dto.modificationDate.get )
                     },
                     dto.author,
                     dto.docType,
                     dto.description,
                     dto.language,
                     dto.classification,
                     dto.title,
                     dto.publisher,
                     dto.url,
                     Option( dto.pages ),
                     dto.subject,
                     dto.creator,
                     dto.producer )
    }
}
