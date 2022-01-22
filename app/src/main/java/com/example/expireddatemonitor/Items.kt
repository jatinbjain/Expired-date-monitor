package com.example.expireddatemonitor

class Items {
    var itemname: String? = null
        private set
    var itemcategory: String? = null
        private set
    var itemexpire: String? = null
        private set
    var itembarcode: String? = null
        private set

    constructor() {}
    constructor(itemname: String?, itemcategory: String?, itemexpire: String?, itembarcode: String?) {
        this.itemname = itemname
        this.itemcategory = itemcategory
        this.itemexpire = itemexpire
        this.itembarcode = itembarcode
    }
}