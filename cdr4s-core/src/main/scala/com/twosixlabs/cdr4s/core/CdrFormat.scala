package com.twosixlabs.cdr4s.core

trait CdrFormat {
    def unmarshalCdr( json : String ) : Option[ CdrDocument ]

    def marshalCdr( doc : CdrDocument ) : Option[ String ]

    def marshalMetadata( metadata : CdrMetadata ) : Option[ String ]

    def unmarshalMetadata( json : String ) : Option[ CdrMetadata ]

    def marshalAnnotation( annotation : CdrAnnotation[ _ ] ) : Option[ String ]

    def unmarshalAnnotation( json : String ) : Option[ CdrAnnotation[ _ ] ]

    def unmarshalThinCdr( thinJson : String ) : Option[ ThinCdrDocument ]

    def marshalThinCdr( thinCdr : ThinCdrDocument ) : Option[ String ]

    def validate( cdr : Array[ Byte ] ) : Boolean

}
