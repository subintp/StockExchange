package models

case class Trade(quantity: Double,
                 price: Float,
                 sellOrderId: Int,
                 buyOrderId: Int)
