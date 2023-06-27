package com.majdoor.ovr.shramik.app.DataClasses

class UserData {
    var Uid:String? = null
    var Name:String? = null
    var Email:String? = null
    var Address:String? = null
    var Number:String? = null
    var Pincode:String? = null
    var State:String? = null
    var District:String? = null
    var Gender:String? = null
    var Applied_for:String? = null
    var Image:String? = null
    var Experience:String? = null

    constructor() {}
    constructor(
        Uid: String?,
        Name: String?,
        Email: String?,
        Address: String?,
        Number: String?,
        Pincode: String?,
        State: String?,
        District: String?,
        Gender: String?,
        Applied_for: String?,
        Image:String?
    ) {
        this.Uid = Uid
        this.Name = Name
        this.Email = Email
        this.Address = Address
        this.Number = Number
        this.Pincode = Pincode
        this.State = State
        this.District = District
        this.Gender = Gender
        this.Applied_for = Applied_for
        this.Image = Image
    }
}
