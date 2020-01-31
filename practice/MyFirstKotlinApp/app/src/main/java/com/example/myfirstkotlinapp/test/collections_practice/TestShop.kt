//package com.example.myfirstkotlinapp.test.collections_practice
//
//fun main(){
//    // Return all products this customer has ordered
//    val Customer.orderedProducts: Set<Product> get() {
//        return shop.flatMap{it.customer}.toSet()
//    }
//
//// Return all products that were ordered by at least one customer
//    val Shop.allOrderedProducts: Set<Product> get() {
//        return shop.flatMap{it.customer.length}.toSet()
//    }
//}
//
//
