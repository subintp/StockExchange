package models

import scala.collection.mutable.ListBuffer

class OrderList {
  var orders = new ListBuffer[Order]()

  def add(order: Order) = orders += order

  def remove(order: Order) = orders -= order
}
