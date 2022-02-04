package com.twosixlabs.cdr4s.annotations

/**
  *
  * Generic container for common NLP tags
  *
  * @param offsetStart
  * @param offsetEnd
  * @param tag
  */
case class OffsetTag( offsetStart : Int, offsetEnd : Int, value : Option[ String ] = None, tag : String, score : Option[ BigDecimal ] )
