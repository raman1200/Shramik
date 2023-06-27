package com.majdoor.ovr.shramik.app.DataClasses

class PostData {
    var id:String? = null
    var uploadedBy: String? = null
    var title: String? = null
    var description: String? = null
    var location: String? = null
    var category: String? = null
    var salary: String? = null
    var date: String? = null
    var image:String? = null


    constructor(
        id:String?,
        uploadedBy: String?,
        title: String?,
        description: String?,
        location: String?,
        category: String?,
        salary: String?,
        date:String?,

    ) {
        this.id = id
        this.uploadedBy = uploadedBy
        this.title = title
        this.description = description
        this.location = location
        this.category = category
        this.salary = salary
        this.date = date
    }

    constructor() {}
}
