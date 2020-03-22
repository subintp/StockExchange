import java.util.Date

import models.{Order, OrderList}
import services.OrderMatcher

object StockExchange extends App {

  // TODO Use DI here
  var orderList = new OrderList()
  val matcher = new OrderMatcher()

  val order1 = Order(1, "sell", 240.12, 100, "ABB", "active", new Date())
  orderList.add(order1)
  matcher.perform(order1, orderList)

  val order2 = Order(2, "sell", 237.45, 90, "ABB", "active", new Date())
  orderList.add(order2)
  matcher.perform(order2, orderList)

  val order3 = Order(3, "buy", 238.10, 110, "ABB", "active", new Date())
  orderList.add(order3)
  matcher.perform(order3, orderList)

  val order4 = Order(4, "buy", 237.80, 10, "ABB", "active", new Date())
  orderList.add(order4)
  matcher.perform(order4, orderList)

  val order5 = Order(5, "buy", 237.80, 40, "ABB", "active", new Date())
  orderList.add(order5)
  matcher.perform(order5, orderList)

  val order6 = Order(6, "sell", 236.00, 50, "ABB", "active", new Date())
  orderList.add(order6)
  matcher.perform(order6, orderList)

  matcher.tradeList.trades.foreach(
    trade =>
      println(
        trade.buyOrderId + "- " + trade.quantity + "- " + trade.price + "- " + trade.sellOrderId
    )
  )
}
