package com.twosixlabs.cdr4s.json.dart

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}

import scala.beans.BeanProperty

@JsonInclude( Include.NON_EMPTY )
case class ThinCdrAnnotationDto( @BeanProperty @JsonProperty( "annotation" ) annotation : Option[ String ],
                                 @BeanProperty @JsonProperty( "label" ) label : Option[ String ] = None )
