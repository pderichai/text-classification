
package edu.washington.cs.dericp.text.classification

import ch.ethz.dal.tinyir.processing.{Tokenizer, XMLDocument}

/**
  * Wrapper class for XMLDocument Stream generated by ReutersRCVStream
  */
class Document(val termFreq: Map[String, Int], val codes: Set[String], val tokens: List[String], val id: Int) {
  val length = termFreq.map(_._2).sum

}