package com.twosixlabs.cdr4s.core

import com.twosixlabs.cdr4s.annotations.{DocumentGenealogy, FacetScore, OffsetTag, TranslatedFields}

object CdrAnnotation {
    val DERIVED : String = "derived"
    val STATIC : String = "static"
}

abstract class CdrAnnotation[ +T ]( val label : String, val version : String, val content : T, val classification : String = CdrAnnotation.DERIVED ) extends Serializable {

    def markStatic( ) : CdrAnnotation[ T ]

    def markDerived( ) : CdrAnnotation[ T ]

    def makeThin : ThinCdrAnnotation = ThinCdrAnnotation( this )
}

case class TextAnnotation( override val label : String,
                           override val version : String,
                           override val content : String,
                           override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ String ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ String ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ String ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = content
}

case class DictionaryAnnotation( override val label : String,
                                 override val version : String,
                                 override val content : Map[ String, String ],
                                 override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ Map[ String, String ] ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ Map[ String, String ] ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ Map[ String, String ] ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = this.content.mkString( "," )
}

case class OffsetTagAnnotation( override val label : String,
                                override val version : String,
                                override val content : List[ OffsetTag ],
                                override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ List[ OffsetTag ] ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ List[ OffsetTag ] ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ List[ OffsetTag ] ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = {
        content.map( tag => s"${tag.tag}:${tag.value}:${tag.offsetStart}:${tag.offsetEnd}:${tag.score}" ).mkString( "&" )
    }
}

case class FacetAnnotation( override val label : String,
                            override val version : String,
                            override val content : List[ FacetScore ],
                            override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ List[ FacetScore ] ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ List[ FacetScore ] ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ List[ FacetScore ] ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = {
        content
          .map( facet => {
              val score : String = facet.score match {
                  case Some( score ) => score.toString
                  case None => "NONE"
              }
              s"${facet.value}:${score}"
          } )
          .mkString( "&" )
    }
}

case class DocumentGenealogyAnnotation( override val label : String,
                                        override val version : String,
                                        override val content : DocumentGenealogy,
                                        override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ DocumentGenealogy ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ DocumentGenealogy ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ DocumentGenealogy ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = s"${content.similarDocuments.mkString}-${content.similarityMatrix.flatten.mkString}"

}

case class TranslationAnnotation( override val label : String,
                                  override val version : String,
                                  override val content : TranslatedFields,
                                  override val classification : String = CdrAnnotation.DERIVED ) extends CdrAnnotation[ TranslatedFields ]( label, version, content, classification ) {

    override def markStatic( ) : CdrAnnotation[ TranslatedFields ] = this.copy( classification = CdrAnnotation.STATIC )

    override def markDerived( ) : CdrAnnotation[ TranslatedFields ] = this.copy( classification = CdrAnnotation.DERIVED )

    override def toString( ) : String = s"${content.language}~${content.fields.mkString( "," )}"
}
