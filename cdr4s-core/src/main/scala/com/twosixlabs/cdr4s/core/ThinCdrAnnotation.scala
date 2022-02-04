package com.twosixlabs.cdr4s.core

import com.twosixlabs.dart.utils.IdGenerator

object ThinCdrAnnotation {
    def apply( annotation : CdrAnnotation[ _ ] ) : ThinCdrAnnotation = {
        val annotationString = IdGenerator.getMd5Hash( annotation.toString.getBytes )
        val label : Option[ String ] = Option( annotation.label )
        ThinCdrAnnotation( annotationString, label )
    }
}

case class ThinCdrAnnotation( annotation : String, label : Option[ String ] = None )
