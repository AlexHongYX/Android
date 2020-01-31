package com.example.myfirstkotlinapp.test.collections_practice

// 商场
data class Shop(val name: String, val customers: List<Customer>)

// 订单
data class Order(val products: List<Product>, val isDelevered: Boolean)

// 客户
data class Customer(val name: String,val city: City,val orders: List<Order>){
    override fun toString(): String = "$name from ${city.name}"
}

// 产品
data class Product(val name: String,val price: Double)

// 城市
data class City(val name: String){
    override fun toString(): String = name
}