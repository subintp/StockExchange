package models

import scala.collection.mutable.ListBuffer

class TradeList {
  var trades = new ListBuffer[Trade]()

  def add(trade: Trade) = trades += trade
}
