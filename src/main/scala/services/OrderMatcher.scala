package services

import models.{Order, OrderList, Trade, TradeList}

import scala.math.Ordering.Implicits._
import util.control.Breaks._

class OrderMatcher {

  var tradeList = new TradeList()

  def perform(order: Order, orderList: OrderList) = {
    val orders = matchingOrders(order, orderList)

    breakable {

      orders.foreach(o => {

        val (sellOrder, buyOrder) = order.orderType match {
          case "sell" => (order, o)
          case "buy"  => (o, order)
        }

        val remainingQuantity = order.quantity - o.quantity

        remainingQuantity match {
          case quantity if isPrefectAllocation(quantity) => {
            createTrade(sellOrder, buyOrder, buyOrder.quantity)
            allocateOrder(buyOrder, orderList)
            allocateOrder(sellOrder, orderList)
          }
          case quantity if isPartialAllocation(quantity) => {
            createTrade(sellOrder, buyOrder, o.quantity)
            allocateOrder(o, orderList)
            changeOrderAllocation(order, quantity, orderList)
          }
          case _ => {
            createTrade(sellOrder, buyOrder, o.quantity)
            allocateOrder(order, orderList)
            changeOrderAllocation(o, remainingQuantity.abs, orderList)
          }
        }
        if (order.status == "allocated") break
      })
    }
  }

  private def matchingOrders(order: Order,
                             orderList: OrderList): List[Order] = {
    order.orderType match {
      case "buy" => {
        orderList.orders
          .filter { o =>
            o.orderType == "sell" &&
            o.price <= order.price &&
            o.stockName == order.stockName &&
            o.status == "active"
          }
          .toList
          .sortWith(_.createdAt < _.createdAt)
      }
      case "sell" => {
        orderList.orders
          .filter { o =>
            o.orderType == "buy" &&
            o.price >= order.price &&
            o.stockName == order.stockName &&
            o.status == "active"
          }
          .toList
          .sortWith(_.createdAt < _.createdAt)
      }
    }
  }

  def isPrefectAllocation(quantity: Double): Boolean = quantity == 0

  def isPartialAllocation(quantity: Double): Boolean = quantity > 0

  def allocateOrder(order: Order, orderList: OrderList) = {
    val allocatedOrder = Order(
      order.id,
      order.orderType,
      order.quantity,
      order.price,
      order.stockName,
      "allocated",
      order.createdAt
    )
    orderList.remove(order)
    orderList.add(allocatedOrder)
  }

  def changeOrderAllocation(order: Order,
                            quantity: Double,
                            orderList: OrderList) = {
    val changedOrder = Order(
      order.id,
      order.orderType,
      quantity,
      order.price,
      order.stockName,
      order.status,
      order.createdAt
    )

    orderList.remove(order)
    orderList.add(changedOrder)
  }

  def createTrade(sellOrder: Order, buyOrder: Order, quantity: Double) = {
    val trade = Trade(quantity, sellOrder.price, sellOrder.id, buyOrder.id)
    tradeList.add(trade)
  }
}
