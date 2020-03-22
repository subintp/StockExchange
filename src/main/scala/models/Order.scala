package models

import java.util.Date

case class Order(id: Int,
                 orderType: String,
                 quantity: Double,
                 price: Float,
                 stockName: String,
                 status: String,
                 createdAt: Date)
