package models

import java.util.Date

case class Order(id: Int,
                 orderType: String,
                 quantity: Int,
                 price: Float,
                 stockName: String,
                 status: String,
                 createdAt: Date)
