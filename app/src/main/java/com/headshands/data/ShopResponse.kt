package com.headshands.data

data class ShopResponse(var code: String, var message: List<Shop>)

data class Shop(
    var point_id: String,
    var shop_id: String,
    var point_address: String,
    var point_coords: String,
    var point_rank: String,
    var point_distance: String,
    var point_time_id: String,
    var shop_name: String,
    var shop_logo: String,
    var shop_rank: String,
    var currency_id: String,
    var item_is_liked: String,
    var item_id: String,
    var item_name: String,
    var item_image: String,
    var item_price: String,
    var item_bonus_amount: String,
    var item_bonus_price: String,
    var item_likes: String,
    var item_shares: String,
    var item_comments: String,
    var group_type: String
)
