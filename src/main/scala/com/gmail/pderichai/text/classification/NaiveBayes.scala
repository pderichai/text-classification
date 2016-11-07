package com.gmail.pderichai.text.classification
import ch.ethz.dal.tinyir.processing.{Tokenizer, XMLDocument}

import scala.collection.mutable

/*class NaiveBayes(docCollection: mutable.Set[Document], docCodes: mutable.Map[String, Code]) {
  (def probC(category: Code): Double = {
    category.documents.size / docCollection.size.toDouble
  }

  def probWC(word: String, category: Code): Double = {
    val dc = category.documents
    val numerator = dc.map(_.termFreq.getOrElse(word, 0) + 1).sum
    val vocabSize = dc.flatMap(_.termFreq.keys).distinct.size
    val denominator = dc.map(_.length).sum + vocabSize
    numerator / denominator.toDouble
  }

  def logPDC(document: Document, category: Code): Double = {
    val tf = document.termFreq
    val pwc = tf.keys.map(w => (w, probWC(w, category))).toMap
    pwc.map{ case(k, v) => tf.getOrElse(k, 0) * Math.log(v) }.sum
  }

  // TODO: change this method to return a list of top k codes
  // Returns the Category with the highest probability
  def bayesMain(document: Document): String = {
    // need the collection of docs, this is mock
    val categoryProbs = docCodes.map{case(name, code) => (name, Math.log(probC(code)) + logPDC(document, code))}
    categoryProbs.maxBy(_._2)._1
  }*/

object NaiveBayes {

  def probC(docs: Stream[XMLDocument], cat: String): Double = {
    //categories.zipWithIndex.map{case(cat, i)=>(cat, docs.filter(_.codes(cat)).length / docs.length.toDouble)}.toMap
    docs.filter(_.codes(cat)).length / docs.length.toDouble
  }

  def probOfWGivenCMany(docs: Stream[XMLDocument], cat: String): Map[String, Double] = {
    val tks = docs.filter(_.codes(cat)).flatMap(_.tokens)
    val sum = tks.length.toDouble
    val groupedTks = tks.groupBy(identity)
    groupedTks.mapValues(l=>(l.length + 1) / sum + (groupedTks.keySet.size))
  }

  def probOfDocGivenCat(docs1: Stream[XMLDocument], docs2: Stream[XMLDocument], doc: XMLDocument, cat: String): Double = {
    val probOfWGivenC = probOfWGivenCMany(docs1, cat)
    val probOfC = probC(docs2, cat)
    val tokens = Tokenizer.tokenize(doc.content)
    val termFreq = tokens.groupBy(identity).mapValues(l => l.length)
    Math.log(probOfC) + termFreq.map{case(term, i)=>(term, termFreq(term) * Math.log(probOfWGivenC(term)))}.values.sum
  }
}