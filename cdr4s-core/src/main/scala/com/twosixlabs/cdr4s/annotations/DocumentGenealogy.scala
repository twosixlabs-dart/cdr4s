package com.twosixlabs.cdr4s.annotations

case class DocumentGenealogy( similarDocuments : Map[ String, BigDecimal ], similarityMatrix : Array[ Array[ BigDecimal ] ] ) {

    // arrays are really annoying in JVM languages because they are treated like primitives, therefore, we have to do some magic in order to get equals to work right
    override def equals( obj : Any ) : Boolean = {
        if ( !obj.isInstanceOf[ DocumentGenealogy ] ) false
        else {
            val other : DocumentGenealogy = obj.asInstanceOf[ DocumentGenealogy ]
            ( other.similarityMatrix.deep == this.similarityMatrix.deep ) && ( other.similarDocuments == this.similarDocuments )
        }
    }
}
