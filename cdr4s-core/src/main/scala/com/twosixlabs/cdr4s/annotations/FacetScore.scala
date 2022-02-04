package com.twosixlabs.cdr4s.annotations

/**
  *
  * Generic container for common NLP tags
  * @param offsetStart
  * @param offsetEnd
  * @param value        - a label, term, or word that classifies some element of the document
  * @param score        - the score level we are that this characterization is accurate
  */
case class FacetScore( value : String, score : Option[ BigDecimal ] )
