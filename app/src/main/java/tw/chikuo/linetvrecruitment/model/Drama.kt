package tw.chikuo.linetvrecruitment.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.text.DecimalFormat

@Entity
class Drama : Serializable {

    @PrimaryKey
    var drama_id: String = ""

    var name: String = ""
    var total_views: String = ""
    var created_at: String = ""
    var thumb: String = ""
    var rating: Float = 0F

    fun getRating(): String {
        val df = DecimalFormat("0.0")
        return df.format(rating).toString()
    }

    fun getCreateTime(): String {
        val parts = created_at.split("T")
        return parts[0]
    }

}
